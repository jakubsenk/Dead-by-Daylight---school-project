package com.senkgang.dbd.entities;

import com.senkgang.dbd.fov.Line;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Wall extends CollidableEntity implements ISightBlocker
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
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.setFill(Color.BLACK);
		g.fillRect((int) x - width / 2 - camX, (int) y - height / 2 - camY, width, height);
	}

	@Override
	public ArrayList<Line> getSightBlockingLines()
	{
		ArrayList<Line> ret = new ArrayList<>();
		ret.add(new Line(new Point2D(x - width / 2, y - height / 2), new Point2D(x - width / 2, y + height / 2)));
		ret.add(new Line(new Point2D(x - width / 2, y - height / 2), new Point2D(x + width / 2, y - height / 2)));
		ret.add(new Line(new Point2D(x + width / 2, y - height / 2), new Point2D(x + width / 2, y + height / 2)));
		ret.add(new Line(new Point2D(x - width / 2, y + height / 2), new Point2D(x + width / 2, y + height / 2)));
		return ret;
	}
}
