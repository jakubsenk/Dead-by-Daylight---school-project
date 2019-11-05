package com.senkgang.dbd.entities;

import java.awt.*;

public class TestObject extends Entity
{
	public TestObject(double x, double y)
	{
		super(x, y);
	}

	@Override
	public void update()
	{

	}

	@Override
	public void draw(Graphics g, int camX, int camY)
	{
		g.fillRect((int)x - 5 - camX, (int)y - 5 - camY, 10, 10);
	}
}
