package com.senkgang.dbd.entities;

import com.senkgang.dbd.Launcher;
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
	private BufferedImage img;


	public Generator(double x, double y)
	{
		super(x, y);
		this.width = 50;
		this.height = 50;
		try
		{
			img = ImageIO.read(new File("res/images/generator.png"));
		}
		catch (IOException e)
		{
			Launcher.logger.Exception(e);
		}
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

		g.drawImage(img, (int) x - width / 2 - camX, (int) y - height / 2 - camY, width, height, null);

	}

	@Override
	public ArrayList<Line> getSightBlockingLines()
	{
		ArrayList<Line> ret = new ArrayList<>();

		ret.add(new Line(new Point((int) (x - width / 2 + 4), (int) (y + height / 2 - 12)), new Point((int) (x - width / 2 + 4), (int) (y - height / 2 + 10))));
		ret.add(new Line(new Point((int) (x - width / 2 + 4), (int) (y - height / 2 + 10)), new Point((int) (x + width / 2 - 28), (int) (y - height / 2 + 1))));
		ret.add(new Line(new Point((int) (x + width / 2 - 28), (int) (y - height / 2 + 1)), new Point((int) (x + width / 2 - 4), (int) (y - height / 2 + 13))));
		ret.add(new Line(new Point((int) (x + width / 2 - 4), (int) (y - height / 2 + 13)), new Point((int) (x + width / 2 - 4), (int) (y + height / 2 - 12))));
		ret.add(new Line(new Point((int) (x + width / 2 - 4), (int) (y + height / 2 - 12)), new Point((int) (x - width / 2 + 25), (int) (y + height / 2 - 2))));
		ret.add(new Line(new Point((int) (x - width / 2 + 25), (int) (y + height / 2 - 2)), new Point((int) (x - width / 2 + 4), (int) (y + height / 2 - 12))));

		return ret;
	}
}