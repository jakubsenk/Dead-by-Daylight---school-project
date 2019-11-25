package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IntroScreen extends Screen
{
	public IntroScreen(Handler h)
	{
		super(h);
	}

	@Override
	public void update()
	{
		if (handler.getMouseManager().leftButtonPressed())
			Screen.setScreen(new PlayerSelectScreen(handler));
	}

	@Override
	public void draw(Graphics g)
	{

		drawimg(g,"res/images/killer.jpg",250,0,1000,1000);

	}

	public void pause(int s)
	{
		try
		{
			Thread.sleep(s);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	public void drawimg(Graphics g,String path,int x1, int y1, int x2, int y2)
	{
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path));

			g.drawImage(img,x1,y1,x2,y2,null);
		} catch (IOException e) {

		}

	}
}
