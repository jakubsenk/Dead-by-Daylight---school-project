package com.senkgang.dbd.entities;

import com.senkgang.dbd.fov.Line;
import com.senkgang.dbd.resources.Assets;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

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
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.drawImage(Assets.generator, (int) x - width / 2 - camX, (int) y - height / 2 - camY, width, height);
	}

	@Override
	public ArrayList<Line> getSightBlockingLines()
	{
		ArrayList<Line> ret = new ArrayList<>();

		ret.add(new Line(new Point2D(x - width / 2 + 4, y + height / 2 - 12), new Point2D(x - width / 2 + 4, y - height / 2 + 10)));
		ret.add(new Line(new Point2D(x - width / 2 + 4, y - height / 2 + 10), new Point2D(x + width / 2 - 28, y - height / 2 + 1)));
		ret.add(new Line(new Point2D(x + width / 2 - 28, y - height / 2 + 1), new Point2D(x + width / 2 - 4, y - height / 2 + 13)));
		ret.add(new Line(new Point2D(x + width / 2 - 4, y - height / 2 + 13), new Point2D(x + width / 2 - 4, y + height / 2 - 12)));
		ret.add(new Line(new Point2D(x + width / 2 - 4, y + height / 2 - 12), new Point2D(x - width / 2 + 25, y + height / 2 - 2)));
		ret.add(new Line(new Point2D(x - width / 2 + 25, y + height / 2 - 2), new Point2D(x - width / 2 + 4, y + height / 2 - 12)));

		return ret;
	}
}