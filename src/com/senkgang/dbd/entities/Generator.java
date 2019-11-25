package com.senkgang.dbd.entities;

import com.senkgang.dbd.fov.Line;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Generator extends CollidableEntity implements ISightBlocker
{
	private final int width;
	private final int height;

	public Generator(double x, double y, int width, int height)
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
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("res/images/generator.png"));

			g.drawImage(img,(int) x - width / 2 - camX, (int) y - height / 2 - camY, width, height,null);
		} catch (IOException e) {

		}

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