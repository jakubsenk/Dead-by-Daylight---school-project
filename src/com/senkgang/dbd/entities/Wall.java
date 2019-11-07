package com.senkgang.dbd.entities;

import java.awt.*;

public class Wall extends CollidableEntity
{
	private final int width;
	private final int height;

	public Wall(double x, double y, int width, int height)
	{
		super(x, y);
		this.width = width;
		this.height = height;
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle((int) x - width / 2, (int) y - height / 2, width, height);
	}

	@Override
	public void update()
	{

	}

	@Override
	public void draw(Graphics g, int camX, int camY)
	{
		g.fillRect((int) x - width / 2 - camX, (int) y - height / 2 - camY, width, height);
	}
}
