package com.senkgang.dbd.entities.player;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.entities.CollidableEntity;
import com.senkgang.dbd.entities.ISightBlocker;

import java.awt.*;
import java.util.ArrayList;

public class TestKiller extends Killer
{
	private double endX;
	private double endY;

	public TestKiller(Handler h, double x, double y, ArrayList<CollidableEntity> entities, ArrayList<ISightBlocker> sightBlocker)
	{
		super(h, x, y, entities, sightBlocker);
		fov = 130;
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle((int)x - 25, (int)y - 25, 50, 50);
	}

	@Override
	public void update()
	{
		super.update();

		endX = x + 25 * Math.sin(getAngle());
		endY = y + 25 * Math.cos(getAngle());
	}

	@Override
	public void draw(Graphics g, int camX, int camY)
	{
		super.draw(g, camX, camY);
		g.setColor(Color.DARK_GRAY);
		g.fillOval((int) x - 25 - camX, (int) y - 25 - camY, 50, 50);
		g.setColor(Color.black);
		g.drawLine((int) x - camX, (int) y - camY, (int) endX - camX, (int) endY - camY);
	}
}
