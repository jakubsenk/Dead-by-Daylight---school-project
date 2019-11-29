package com.senkgang.dbd.entities;

import com.senkgang.dbd.fov.Line;
import com.senkgang.dbd.resources.Assets;

import java.awt.*;
import java.util.ArrayList;

public class Generator extends CollidableEntity implements ISightBlocker
{
	private final int width = 50;
	private final int height = 50;

	public Generator(double x, double y)
	{
		super(x, y);
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

		g.drawImage(Assets.generator, (int) x - width / 2 - camX, (int) y - height / 2 - camY, width, height, null);

	}

	@Override
	public ArrayList<Line> getSightBlockingLines()
	{
		ArrayList<Line> ret = new ArrayList<>();
		ret.add(new Line(new Point((int) (x - width / 2), (int) (y - height / 2)), new Point((int) (x - width / 2), (int) (y + height / 2))));
		ret.add(new Line(new Point((int) (x - width / 2), (int) (y - height / 2)), new Point((int) (x + width / 2), (int) (y - height / 2))));
		ret.add(new Line(new Point((int) (x + width / 2), (int) (y - height / 2)), new Point((int) (x + width / 2), (int) (y + height / 2))));
		ret.add(new Line(new Point((int) (x - width / 2), (int) (y + height / 2)), new Point((int) (x + width / 2), (int) (y + height / 2))));
		return ret;
	}
}