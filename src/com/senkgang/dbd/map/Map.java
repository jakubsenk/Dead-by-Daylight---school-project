package com.senkgang.dbd.map;

import java.awt.*;

public abstract class Map
{
	protected final int width;
	protected final int height;

	public Map(int width, int height)
	{
		this.width = width;
		this.height = height;
	}

	public abstract void update();

	public abstract void draw(Graphics g, int camX, int camY);
}
