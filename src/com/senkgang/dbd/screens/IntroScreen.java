package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.Launcher;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IntroScreen extends Screen
{
	private BufferedImage img;

	public IntroScreen(Handler h)
	{
		super(h);
		try
		{
			img = ImageIO.read(new File("res/images/killer.jpg"));
		}
		catch (IOException e)
		{
			Launcher.logger.Exception(e);
		}

	}

	@Override
	public void update()
	{
		if (handler.getMouseManager().leftButtonPressed()) Screen.setScreen(new PlayerSelectScreen(handler));
	}

	@Override
	public void draw(Graphics g)
	{

		g.drawImage(img, 250, 0, 1000, 1000, null);

	}

}
