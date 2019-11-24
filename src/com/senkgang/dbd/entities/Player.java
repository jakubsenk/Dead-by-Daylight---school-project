package com.senkgang.dbd.entities;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.enums.MovementRestriction;

import java.awt.*;
import java.util.ArrayList;

public abstract class Player extends CollidableEntity
{

	protected Handler handler;
	private ArrayList<CollidableEntity> entities;
	protected ArrayList<ISightBlocker> sightBlockers;
	protected boolean canControl;

	private double customAngle = -100;

	private int speed = 3;

	private double lastPosX;
	private double lastPosY;
	private double lastAngle;

	private int playerID;

	public Player(int playerID, Handler h, double x, double y, boolean playerControlled, ArrayList<CollidableEntity> entities, ArrayList<ISightBlocker> sightBlocker)
	{
		super(x, y);
		handler = h;
		lastPosX = x;
		lastPosY = y;
		lastAngle = getAngle();
		this.entities = entities;
		this.sightBlockers = sightBlocker;
		canControl = playerControlled;
		this.playerID = playerID;
	}

	public double getAngle()
	{
		if (customAngle != -100) return customAngle;
		return getAngle(handler.getMouseManager().getMouseX() + handler.getGameCamera().getxOffset(), handler.getMouseManager().getMouseY() + handler.getGameCamera().getyOffset());
	}

	public void setAngle(double angle)
	{
		customAngle = angle;
	}

	public double getAngle(int mX, int mY)
	{
		double dx = mX - x;
		// Minus to correct for coord re-mapping
		double dy = mY - y;

		double inRads = Math.atan2(dy, dx);

		// We need to map to coord system when 0 degree is at 3 O'clock, 270 at 12 O'clock
		if (inRads < 0)
		{
			inRads = Math.abs(inRads);
		}
		else
		{
			inRads = 2 * Math.PI - inRads;
		}
		return inRads + Math.PI / 2;
	}

	public int getPlayerID()
	{
		return playerID;
	}

	@Override
	public void update()
	{
		if (!canControl) return;

		double deltaX = 0;
		double deltaY = 0;
		if (handler.getInputManager().up)
		{
			deltaX += speed * Math.sin(getAngle());
			deltaY += speed * Math.cos(getAngle());
		}
		if (handler.getInputManager().down)
		{
			deltaX -= speed * Math.sin(getAngle());
			deltaY -= speed * Math.cos(getAngle());
		}
		if (handler.getInputManager().left)
		{
			deltaX += speed * Math.sin(getAngle() + Math.PI / 2);
			deltaY += speed * Math.cos(getAngle() + Math.PI / 2);
		}
		if (handler.getInputManager().right)
		{
			deltaX += speed * Math.sin(getAngle() - Math.PI / 2);
			deltaY += speed * Math.cos(getAngle() - Math.PI / 2);
		}
		ArrayList<MovementRestriction> rest = detectCollisions(deltaX, deltaY);
		if (!rest.contains(MovementRestriction.XPositive) && deltaX > 0) x += deltaX;
		if (!rest.contains(MovementRestriction.XNegative) && deltaX < 0) x += deltaX;
		if (!rest.contains(MovementRestriction.YPositive) && deltaY > 0) y += deltaY;
		if (!rest.contains(MovementRestriction.YNegative) && deltaY < 0) y += deltaY;

		double curAngle = getAngle();
		if (lastPosX != x || lastPosY != y || lastAngle != curAngle && canControl)
		{
			if (handler.isKiller)
			{
				handler.server.addData("Position update:0;" + x + "," + y + "," + curAngle);
			}
			else
			{
				handler.client.addData("Position update:" + playerID + ";" + x + "," + y + "," + curAngle);
			}
		}
		lastPosX = x;
		lastPosY = y;
		lastAngle = curAngle;
	}

	@Override
	public void draw(Graphics g, int camX, int camY)
	{
		if (Launcher.isDebug)
		{
			g.setColor(Color.CYAN);
			Rectangle r = getBounds();
			g.drawRect(r.x - camX, r.y - camY, r.width, r.height);
			double endX = x + 350 * Math.sin(getAngle());
			double endY = y + 350 * Math.cos(getAngle());
			double endLeftX = x + 350 * Math.sin(getAngle() + Math.PI / 4);
			double endLeftY = y + 350 * Math.cos(getAngle() + Math.PI / 4);
			double endRightX = x + 350 * Math.sin(getAngle() - Math.PI / 4);
			double endRightY = y + 350 * Math.cos(getAngle() - Math.PI / 4);

			g.drawLine((int) x - camX, (int) y - camY, (int) endX - camX, (int) endY - camY);
			g.drawLine((int) x - camX, (int) y - camY, (int) endLeftX - camX, (int) endLeftY - camY);
			g.drawLine((int) x - camX, (int) y - camY, (int) endRightX - camX, (int) endRightY - camY);
		}
	}

	private ArrayList<MovementRestriction> detectCollisions(double deltaX, double deltaY)
	{
		ArrayList<MovementRestriction> result = new ArrayList<>();
		Rectangle r = getBounds();
		Rectangle movedPosition = new Rectangle((int) (r.x + deltaX), (int) (r.y + deltaY), r.width, r.height);
		for (CollidableEntity e : entities)
		{
			Rectangle otherRect = e.getBounds();
			if (otherRect.intersects(movedPosition))
			{
				if (movedPosition.x + movedPosition.width > otherRect.x)
				{
					Launcher.logger.Info("Collision with object on right");
					result.add(MovementRestriction.XPositive);
				}
				if (otherRect.x + otherRect.width > movedPosition.x)
				{
					Launcher.logger.Info("Collision with object on left");
					result.add(MovementRestriction.XNegative);
				}

				if (movedPosition.y + movedPosition.height > otherRect.y)
				{
					Launcher.logger.Info("Collision with object on bot");
					result.add(MovementRestriction.YPositive);
				}
				if (otherRect.y + otherRect.height > movedPosition.y)
				{
					Launcher.logger.Info("Collision with object on top");
					result.add(MovementRestriction.YNegative);
				}
			}
		}
		return result;
	}

	public abstract Polygon getViewPolygon();
}