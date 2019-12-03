package com.senkgang.dbd;

import com.senkgang.dbd.display.GameCamera;
import com.senkgang.dbd.input.InputManager;
import com.senkgang.dbd.input.MouseManager;
import com.senkgang.dbd.resources.Assets;
import com.senkgang.dbd.screens.IntroScreen;
import com.senkgang.dbd.screens.Screen;

import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import javax.swing.*;
import java.io.IOException;

public class Game
{
	private final int width = 1600;
	private final int height = 900;

	private int fps;

	private Screen screen;

	private MouseManager mouseMgr;

	private Handler handler;

	private GameCamera camera;


	public Game(Canvas c)
	{
		mouseMgr = new MouseManager(c);

		try
		{
			init();
		}
		catch (IOException e)
		{
			Launcher.logger.Exception(e);
			JOptionPane.showMessageDialog(null, e, "ERROR initializing assets", JOptionPane.ERROR_MESSAGE);
			stop();
			return;
		}
	}

	private void init() throws IOException
	{
		handler = new Handler(this);

		Assets.init();

		camera = new GameCamera(handler, 0, 0);

		screen = new IntroScreen(handler);
		Screen.setScreen(screen);
	}

	public MouseManager getMouseManager()
	{
		return mouseMgr;
	}

	public GameCamera getGameCamera()
	{
		return camera;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	private void update()
	{
		if (Screen.getScreen() != null)
		{
			Screen.getScreen().update();
		}
		if (InputManager.quit)
		{
			stop();
		}
	}

	private void draw(GraphicsContext gr)
	{
		gr.clearRect(0, 0, width, height); // Clear screen

		// #region drawings

		gr.setFill(Color.BLACK);
		gr.strokeText("FPS: " + fps, 0, 10);

		if (Screen.getScreen() != null)
		{
			Screen.getScreen().draw(gr);
		}

		// #endregion

	}

	private long last;
	private long ticks = 0;
	private long timer = 0;
	private long current;

	public void tick(GraphicsContext gr, long currentNanoTime)
	{
		current = currentNanoTime;
		timer += current - last;
		last = current;

		update();
		draw(gr);
		ticks++;


		if (timer >= 1000000000)
		{
			if (ticks < 55)
			{
				Launcher.logger.Warning("Frames per second dropped to: " + ticks);
			}
			else if (ticks < 59) Launcher.logger.Trace("Frames per second dropped to: " + ticks);
			fps = (int) ticks;
			ticks = 0;
			timer = 0;
		}
	}

	public void stop()
	{
		handler.server.stop();
		handler.client.stop();
		Launcher.logger.Info("Stopping game...");
		Platform.exit();
		Launcher.logger.Info("Game stopped.");
		System.exit(0);
	}

}