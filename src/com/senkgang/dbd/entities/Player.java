package com.senkgang.dbd.entities;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.enums.MovementRestriction;
import com.senkgang.dbd.input.InputManager;
import com.senkgang.dbd.input.MouseManager;
import com.senkgang.dbd.interfaces.ISightBlocker;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public abstract class Player extends CollidableEntity
{
	private ArrayList<CollidableEntity> entities;
	protected ArrayList<ISightBlocker> sightBlockers;
	protected boolean canControl;

	private double customAngle = -100;

	protected double speed = 3;

	private double lastPosX;
	private double lastPosY;
	private double lastAngle;

	private int playerID;
	private String nick;

	public Player(int playerID, double x, double y, String nick, boolean playerControlled, ArrayList<CollidableEntity> entities, ArrayList<ISightBlocker> sightBlocker)
	{
		super(x, y);
		lastPosX = x;
		lastPosY = y;
		lastAngle = getAngle();
		this.entities = entities;
		this.sightBlockers = sightBlocker;
		canControl = playerControlled;
		this.playerID = playerID;
		this.nick = nick;
	}

	public double getAngle()
	{
		if (customAngle != -100) return customAngle;
		return getAngle(MouseManager.getMouseX() + Game.handler.getGameCamera().getxOffset(), MouseManager.getMouseY() + Game.handler.getGameCamera().getyOffset());
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

	public void setControllable(boolean canControl)
	{
		this.canControl = canControl;
	}

	@Override
	public void update()
	{
		if (!canControl) return;

		double deltaX = 0;
		double deltaY = 0;
		if (InputManager.up)
		{
			deltaX += speed * Math.sin(getAngle());
			deltaY += speed * Math.cos(getAngle());
		}
		if (InputManager.down)
		{
			deltaX -= speed * Math.sin(getAngle());
			deltaY -= speed * Math.cos(getAngle());
		}
		if (InputManager.left)
		{
			deltaX += speed * Math.sin(getAngle() + Math.PI / 2);
			deltaY += speed * Math.cos(getAngle() + Math.PI / 2);
		}
		if (InputManager.right)
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
			if (Game.handler.isKiller)
			{
				Game.handler.server.addData("Position update:0;" + x + "," + y + "," + curAngle);
			}
			else
			{
				Game.handler.client.addData("Position update:" + playerID + ";" + x + "," + y + "," + curAngle);
			}
		}
		lastPosX = x;
		lastPosY = y;
		lastAngle = curAngle;
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.strokeText(nick, x - camX, y - 75 - camY);
		if (Launcher.isDebug)
		{
			g.setStroke(Color.CYAN);
			Rectangle r = getBounds();
			g.strokeRect(r.getX() - camX, r.getY() - camY, r.getWidth(), r.getHeight());
			double endX = x + 350 * Math.sin(getAngle());
			double endY = y + 350 * Math.cos(getAngle());
			double endLeftX = x + 350 * Math.sin(getAngle() + Math.PI / 4);
			double endLeftY = y + 350 * Math.cos(getAngle() + Math.PI / 4);
			double endRightX = x + 350 * Math.sin(getAngle() - Math.PI / 4);
			double endRightY = y + 350 * Math.cos(getAngle() - Math.PI / 4);

			g.strokeLine((int) x - camX, (int) y - camY, (int) endX - camX, (int) endY - camY);
			g.strokeLine((int) x - camX, (int) y - camY, (int) endLeftX - camX, (int) endLeftY - camY);
			g.strokeLine((int) x - camX, (int) y - camY, (int) endRightX - camX, (int) endRightY - camY);
		}
	}

	private ArrayList<MovementRestriction> detectCollisions(double deltaX, double deltaY)
	{
		ArrayList<MovementRestriction> result = new ArrayList<>();
		Rectangle r = getBounds();
		Rectangle movedPosition = new Rectangle((int) (r.getX() + deltaX), (int) (r.getY() + deltaY), r.getWidth(), r.getHeight());
		if (entities != null)
		{
			for (CollidableEntity e : entities)
			{
				Rectangle otherRect = e.getBounds();
				if (otherRect.intersects(movedPosition.getBoundsInLocal()))
				{
					if (movedPosition.getX() + movedPosition.getWidth() > otherRect.getX())
					{
						Launcher.logger.Info("Collision with object on right");
						result.add(MovementRestriction.XPositive);
					}
					if (otherRect.getX() + otherRect.getWidth() > movedPosition.getX())
					{
						Launcher.logger.Info("Collision with object on left");
						result.add(MovementRestriction.XNegative);
					}

					if (movedPosition.getY() + movedPosition.getHeight() > otherRect.getY())
					{
						Launcher.logger.Info("Collision with object on bot");
						result.add(MovementRestriction.YPositive);
					}
					if (otherRect.getY() + otherRect.getHeight() > movedPosition.getY())
					{
						Launcher.logger.Info("Collision with object on top");
						result.add(MovementRestriction.YNegative);
					}
				}
			}
		}
		return result;
	}

	public abstract double[] getViewPolygonX();

	public abstract double[] getViewPolygonY();
}