package com.senkgang.dbd.entities.player;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.entities.Player;

import java.awt.Color;
import java.awt.Graphics;

public class TestPlayer extends Player
{
	private Color c;
	private double endX;
	private double endY;
	private Handler handler;

	public TestPlayer(Handler h, double x, double y)
	{
		super(h, x, y);
		c = Color.gray;
		handler = h;
	}

	private double getAngle()
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
}