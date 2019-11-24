package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;

import java.awt.Graphics;

public abstract class Screen
{
	protected Handler handler;

	public Screen(Handler h)
	{
		this.handler = h;
	}

	private static Screen currentScreen = null;

	public static void setScreen(Screen s)
	{
		currentScreen = s;
	}

	public static Screen getScreen()
	{
		return currentScreen;
	}

	public abstract void update();

	public abstract void draw(Graphics g);

}