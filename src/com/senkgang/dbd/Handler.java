package com.senkgang.dbd;

import com.senkgang.dbd.input.InputManager;
import com.senkgang.dbd.input.MouseManager;

public class Handler
{
	private Game g;

	public Handler(Game g)
	{
		this.g = g;
	}

	public Game getGame()
	{
		return g;
	}

	public int getScreenWidth()
	{
		return g.width;
	}

	public int getScreenHeight()
	{
		return g.height;
	}

	public InputManager getInputManager()
	{
		return g.getInputManager();
	}

	public MouseManager getMouseManager()
	{
		return g.getMouseManager();
	}
}
