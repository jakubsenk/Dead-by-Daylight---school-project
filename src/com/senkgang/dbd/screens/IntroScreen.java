package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;

import java.awt.*;

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
		g.drawString("Fancy intro", this.handler.getScreenWidth() / 2, this.handler.getScreenHeight() / 2);
	}
}
