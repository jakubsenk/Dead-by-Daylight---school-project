package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;

import java.awt.*;

public class IntroScreen extends Screen
{
	private Handler handler;

	public IntroScreen(Handler h)
	{
		this.handler = h;
	}

	@Override
	public void update()
	{
		if (handler.getMouseManager().leftButtonPressed())
			Screen.setScreen(new TestScreen(handler));
	}

	@Override
	public void draw(Graphics g)
	{
		g.drawString("Fancy intro", this.handler.getScreenWidth() / 2, this.handler.getScreenHeight() / 2);
	}
}
