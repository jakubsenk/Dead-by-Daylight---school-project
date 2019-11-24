package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;

import java.awt.*;

public class PlayerSelectScreen extends Screen
{
	private boolean leftHovered = false;
	private boolean rightHovered = false;

	private boolean clickHold = true;

	public PlayerSelectScreen(Handler h)
	{
		super(h);
	}

	@Override
	public void update()
	{
		leftHovered = handler.getMouseManager().getMouseX() < handler.getScreenWidth() / 2;
		rightHovered = handler.getMouseManager().getMouseX() > handler.getScreenWidth() / 2;

		if (handler.getMouseManager().leftButtonPressed() && !clickHold)
		{
			if (leftHovered)
			{
				handler.isKiller = true;
			}
			else
			{
				handler.isKiller = false;
			}
			Screen.setScreen(new GameScreen(handler));
		}
		else if (!handler.getMouseManager().leftButtonPressed() && clickHold)
		{
			clickHold = false;
		}
	}

	@Override
	public void draw(Graphics g)
	{
		if (leftHovered)
		{
			g.setColor(Color.yellow);
			g.fillRect(0, 0, handler.getScreenWidth() / 2, handler.getScreenHeight());
		}
		if (rightHovered)
		{
			g.setColor(Color.yellow);
			g.fillRect(handler.getScreenWidth() / 2, 0, handler.getScreenWidth() / 2, handler.getScreenHeight());
		}
		g.setColor(Color.black);
		g.drawString("Play as Killer", handler.getScreenWidth() / 3, handler.getScreenHeight() / 2);
		g.drawString("Play as survivor", handler.getScreenWidth() / 3 * 2, handler.getScreenHeight() / 2);
	}
}
