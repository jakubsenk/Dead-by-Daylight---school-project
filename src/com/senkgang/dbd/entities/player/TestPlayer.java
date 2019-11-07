package com.senkgang.dbd.entities.player;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.entities.CollidableEntity;
import com.senkgang.dbd.entities.Player;

import java.awt.*;
import java.util.ArrayList;

public class TestPlayer extends Player
{
	private Color c;
	private double endX;
	private double endY;
	private Handler handler;

	public TestPlayer(Handler h, double x, double y, ArrayList<CollidableEntity> entities)
	{
		super(h, x, y, entities);
		c = Color.gray;
		handler = h;
	}

	@Override
	public void update()
	{
		super.update();
		if (x > 500)
		{
			c = Color.red;
		}
		else if (x > 300)
		{
			c = Color.blue;
		}
		else if (x > 100) c = Color.pink;

		endX = x + 25 * Math.sin(getAngle());
		endY = y + 25 * Math.cos(getAngle());
	}

	@Override
	public void draw(Graphics g, int camX, int camY)
	{
		super.draw(g, camX, camY);
		g.setColor(c);
		g.fillOval((int) x - 25 - camX, (int) y - 25 - camY, 50, 50);
		g.setColor(Color.black);
		g.drawLine((int) x - camX, (int) y - camY, (int) endX - camX, (int) endY - camY);
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle((int)x - 25, (int)y - 25, 50, 50);
	}
}