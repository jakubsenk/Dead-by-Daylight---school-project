package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.annotations.ClientSide;
import com.senkgang.dbd.annotations.ServerSide;
import com.senkgang.dbd.display.Display;
import com.senkgang.dbd.entities.player.Survivor;
import com.senkgang.dbd.entities.player.TestSurvivor;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import javax.swing.*;
import java.net.SocketException;

public class LobbyScreen extends Screen
{
	Button startBtn = new Button("Start");
	Button backBtn = new Button("Back");
	Label connecting = new Label("Connecting...");

	Survivor s1;
	Survivor s2;
	Survivor s3;
	Survivor s4;

	public LobbyScreen(Handler h)
	{
		super(h);
		Display.addComponent(backBtn);
		if (!handler.isKiller)
		{
			Display.addComponent(connecting);
			connecting.setFont(new Font("Segoe UI", 36));
			connecting.relocate(handler.getScreenWidth() / 2 - 100, handler.getScreenHeight() / 2);
		}
		else
		{
			Display.addComponent(startBtn);
			startBtn.relocate(handler.getScreenWidth() - 100, handler.getScreenHeight() - 50);
			startBtn.setOnAction(actionEvent ->
			{
				handler.server.addData("Load game.");
				try
				{
					handler.server.send();
				}
				catch (SocketException e)
				{
					Launcher.logger.Exception(e);
				}
				Screen.setScreen(new GameScreen(handler));
			});
			startBtn.setVisible(false);
		}

		backBtn.relocate(100, handler.getScreenHeight() - 50);

		backBtn.setOnAction(actionEvent ->
		{
			handler.server.stop();
			handler.client.stop();
			handler.client.connectFailed = false;
			Screen.setScreen(new MenuScreen(handler));
		});

		if (handler.isKiller)
		{
			handler.server.start(h);
		}
		else
		{
			handler.client.start(h);
		}
	}

	@Override
	public void update()
	{
		if (handler.client.connectFailed)
		{
			JOptionPane.showMessageDialog(null, "Unable to connect to killer.", "Connection lost.", JOptionPane.ERROR_MESSAGE);
			handler.client.connectFailed = false;
			Screen.setScreen(new MenuScreen(handler));
		}

		if (s1 != null) s1.update();
		if (s2 != null) s2.update();
		if (s3 != null) s3.update();
		if (s4 != null) s4.update();
	}

	@Override
	public void draw(GraphicsContext g)
	{
		if (s1 != null) s1.draw(g, 0, 0);
		if (s2 != null) s2.draw(g, 0, 0);
		if (s3 != null) s3.draw(g, 0, 0);
		if (s4 != null) s4.draw(g, 0, 0);
	}

	@ClientSide
	public void connected(String id)
	{
		connected(id, handler.playerNick);
	}

	@ClientSide
	public void connectedExisting(String id, String nick)
	{
		connected(id, nick);
	}

	@ClientSide
	public void startGame()
	{
		Screen.setScreen(new GameScreen(handler), true);
	}

	@ServerSide
	@ClientSide
	public void connected(String id, String nick)
	{
		startBtn.setVisible(true);
		Display.removeComponent(connecting);
		if (id.equals("1"))
		{
			s1 = new TestSurvivor(Integer.parseInt(id), handler, 100, 200, nick, false, null, null);
			s1.setAngle(Math.PI / 4);
		}
		else if (id.equals("2"))
		{
			s2 = new TestSurvivor(Integer.parseInt(id), handler, 300, 200, nick, false, null, null);
			s2.setAngle(Math.PI / 6);
		}
		else if (id.equals("3"))
		{
			s3 = new TestSurvivor(Integer.parseInt(id), handler, 500, 200, nick, false, null, null);
			s3.setAngle(-Math.PI / 6);
		}
		else if (id.equals("4"))
		{
			s4 = new TestSurvivor(Integer.parseInt(id), handler, 700, 200, nick, false, null, null);
			s4.setAngle(-Math.PI / 4);
		}
	}
}
