package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;

import com.senkgang.dbd.display.Display;
import javafx.scene.canvas.GraphicsContext;

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
		setScreen(s, false);
	}

	public static void setScreen(Screen s, boolean invoked)
	{
		if (invoked)
		{
			Display.updateUIComponentsFromThread();
		}
		else
		{
			Display.updateUIComponents();
		}
		currentScreen = s;
	}

	public static Screen getScreen()
	{
		return currentScreen;
	}

	public abstract void update();

	public abstract void draw(GraphicsContext g);

}