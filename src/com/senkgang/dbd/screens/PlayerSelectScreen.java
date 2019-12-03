package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.Launcher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;


public class PlayerSelectScreen extends GUIScreen
{
	Button bk = new Button("Play as killer");
	Button bs = new Button("Play as survivor");

	public PlayerSelectScreen(Handler h)
	{
		super(h);
		Launcher.pane.getChildren().add(bk);
		Launcher.pane.getChildren().add(bs);

		bk.relocate(300, 400);
		bs.relocate(1200, 400);

		bk.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				handler.isKiller = true;
				Screen.setScreen(new GameScreen(handler));
			}
		});
		bs.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent actionEvent)
			{
				handler.isKiller = false;
				Screen.setScreen(new GameScreen(handler));
			}
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

	@Override
	public void disposeUIComponents()
	{
		Launcher.pane.getChildren().remove(bk);
		Launcher.pane.getChildren().remove(bs);
	}
}
