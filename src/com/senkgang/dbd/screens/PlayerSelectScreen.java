package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.display.Display;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;

public class PlayerSelectScreen extends Screen
{
	Button bk = new Button("Play as killer");
	Button bs = new Button("Play as survivor");

	public PlayerSelectScreen(Handler h)
	{
		super(h);
		Display.addComponent(bk);
		Display.addComponent(bs);

		bk.relocate(300, 400);
		bs.relocate(1200, 400);

		bk.setOnAction(actionEvent ->
		{
			handler.isKiller = true;
			Screen.setScreen(new GameScreen(handler));
		});
		bs.setOnAction(actionEvent ->
		{
			handler.isKiller = false;
			Screen.setScreen(new GameScreen(handler));
		});
	}

	@Override
	public void update()
	{

	}

	@Override
	public void draw(GraphicsContext g)
	{

	}
}
