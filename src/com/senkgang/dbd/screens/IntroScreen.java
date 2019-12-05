package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.input.MouseManager;
import com.senkgang.dbd.resources.Assets;

import javafx.scene.canvas.GraphicsContext;

public class IntroScreen extends Screen
{

	public IntroScreen(Handler h)
	{
		super(h);
	}

	@Override
	public void update()
	{
		if (MouseManager.leftButtonPressed()) Screen.setScreen(new PlayerSelectScreen(handler));
	}

	@Override
	public void draw(GraphicsContext g)
	{
		g.drawImage(Assets.introLogo, 250, 0, 1000, 1000);
	}

}
