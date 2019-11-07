package com.senkgang.dbd.entities;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.enums.MovementRestriction;

import java.awt.*;
import java.util.ArrayList;

public abstract class Player extends CollidableEntity
{

	private Handler handler;
	private ArrayList<CollidableEntity> entities;

	private int speed = 3;

	public Player(Handler h, double x, double y, ArrayList<CollidableEntity> entities)
	{
		super(x, y);
		handler = h;
		this.entities = entities;
	}

	protected double getAngle()
	{
		int mX = handler.getMouseManager().getMouseX() + handler.getGameCamera().getxOffset();
		int mY = handler.getMouseManager().getMouseY() + handler.getGameCamera().getyOffset();
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

	@Override
	public void update()
	{
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
	}

	@Override
	public void draw(Graphics g, int camX, int camY)
	{
		if (Launcher.isDebug)
		{
			g.setColor(Color.CYAN);
			Rectangle r = getBounds();
			g.drawRect(r.x - camX, r.y - camY, r.width, r.height);
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
}