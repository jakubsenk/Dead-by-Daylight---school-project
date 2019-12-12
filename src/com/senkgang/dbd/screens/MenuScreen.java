package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;
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

	public MenuScreen(Handler h)
	{
		super(h);
		Display.addComponent(bk);
		Display.addComponent(bs);
		Display.addComponent(nick);
		Display.addComponent(ip);


		bk.relocate(handler.getScreenWidth() / 4, handler.getScreenHeight() / 2);
		bs.relocate(handler.getScreenWidth() / 4 + handler.getScreenWidth() / 2, handler.getScreenHeight() / 2);
		ip.relocate(handler.getScreenWidth() / 4 + handler.getScreenWidth() / 2 - 25, handler.getScreenHeight() / 2 + 50);
		nick.relocate(handler.getScreenWidth() / 2 - 75, handler.getScreenHeight() / 2);

		bk.setOnAction(actionEvent ->
		{
			if (nick.getText().isEmpty())
			{
				JOptionPane.showMessageDialog(null, "Choose your nick first!", "Invalid input.", JOptionPane.WARNING_MESSAGE);
				return;
			}
			handler.isKiller = true;
			handler.playerNick = nick.getText();
			Screen.setScreen(new LobbyScreen(handler));
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
			handler.isKiller = false;
			handler.connectIP = ip.getText();
			handler.playerNick = nick.getText();
			Screen.setScreen(new LobbyScreen(handler));
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
