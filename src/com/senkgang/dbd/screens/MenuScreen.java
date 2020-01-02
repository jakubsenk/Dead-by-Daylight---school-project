package com.senkgang.dbd.screens;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.display.Display;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.swing.*;

public class MenuScreen extends Screen
{
	Button bk = new Button("Play as killer");
	Button bs = new Button("Play as survivor");
	TextField nick = new TextField("Choose nick");
	TextField ip = new TextField("localhost");

	public MenuScreen()
	{
		Display.addComponent(bk);
		Display.addComponent(bs);
		Display.addComponent(nick);
		Display.addComponent(ip);


		bk.relocate(Game.handler.getScreenWidth() / 4, Game.handler.getScreenHeight() / 2);
		bs.relocate(Game.handler.getScreenWidth() / 4 + Game.handler.getScreenWidth() / 2, Game.handler.getScreenHeight() / 2);
		ip.relocate(Game.handler.getScreenWidth() / 4 + Game.handler.getScreenWidth() / 2 - 25, Game.handler.getScreenHeight() / 2 + 50);
		nick.relocate(Game.handler.getScreenWidth() / 2 - 75, Game.handler.getScreenHeight() / 2);

		bk.setOnAction(actionEvent ->
		{
			if (nick.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Choose your nick first!", "Invalid input.", JOptionPane.WARNING_MESSAGE);
				return;
			}
			Game.handler.isKiller = true;
			Game.handler.playerNick = nick.getText();
			Game.handler.playerID = "0";
			Screen.setScreen(new LobbyScreen());
		});
		bs.setOnAction(actionEvent ->
		{
			if (nick.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Choose your nick first!", "Invalid input.", JOptionPane.WARNING_MESSAGE);
				return;
			}
			if (ip.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Specify IP address first!", "Invalid input.", JOptionPane.WARNING_MESSAGE);
				return;
			}
			Game.handler.isKiller = false;
			Game.handler.connectIP = ip.getText();
			Game.handler.playerNick = nick.getText();
			Screen.setScreen(new LobbyScreen());
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
