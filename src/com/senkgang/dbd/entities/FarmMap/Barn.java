package com.senkgang.dbd.entities.FarmMap;

import com.senkgang.dbd.entities.CollidableEntity;
import com.senkgang.dbd.fov.Line;
import com.senkgang.dbd.interfaces.ISightBlocker;
import com.senkgang.dbd.resources.Assets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Barn extends CollidableEntity implements ISightBlocker
{
	private final int width;
	private final int height;

	public Barn(double x, double y)
	{
		super(x, y);
		this.width = (int) Assets.barn.getWidth();
		this.height = (int) Assets.barn.getHeight();
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle((int) x - width / 2 + 15, (int) y - height / 2 + 60, width - 30, height - 60);
	}

	@Override
	public void update()
	{

	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.drawImage(Assets.barn, x - width / 2 - camX, y - height / 2 - camY);
	}

	@Override
	public ArrayList<Line> getSightBlockingLines()
	{
		ArrayList<Line> ret = new ArrayList<>();
		ret.add(new Line(new Point2D(x - width / 2 + 10, y + height / 2), new Point2D(x + width / 2 - 19, y + height / 2)));
		ret.add(new Line(new Point2D(x - width / 2 + 10, y + height / 2), new Point2D(x - width / 2 + 10, y)));
		ret.add(new Line(new Point2D(x + width / 2 - 19, y + height / 2), new Point2D(x + width / 2 - 19, y)));
		return ret;
	}
}
