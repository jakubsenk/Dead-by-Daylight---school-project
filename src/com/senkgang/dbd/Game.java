package com.senkgang.dbd;

import com.senkgang.dbd.display.Display;
import com.senkgang.dbd.display.GameCamera;
import com.senkgang.dbd.input.InputManager;
import com.senkgang.dbd.input.MouseManager;
import com.senkgang.dbd.resources.Assets;
import com.senkgang.dbd.screens.IntroScreen;
import com.senkgang.dbd.screens.Screen;

import javax.swing.*;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.Color;
import java.io.IOException;

public class Game implements Runnable
{
	private final int width;
	private final int height;

	private int fps;

	private Display display;
	private Thread thread;
	private boolean running = false;

	private BufferStrategy bs;
	private Graphics gr;

	private Screen screen;

	private InputManager inputMgr;
	private MouseManager mouseMgr;

	private Handler handler;

	private GameCamera camera;

	public Game(int width, int height)
	{
		this.width = width;
		this.height = height;
		inputMgr = new InputManager();
		mouseMgr = new MouseManager();
	}

	private void init() throws IOException
	{
		display = new Display(width, height, this);
		display.getJFrame().addKeyListener(inputMgr);
		display.getJFrame().addMouseListener(mouseMgr);
		display.getJFrame().addMouseMotionListener(mouseMgr);
		display.getCanvas().addMouseListener(mouseMgr);
		display.getCanvas().addMouseMotionListener(mouseMgr);

		handler = new Handler(this);

		Assets.init();

		camera = new GameCamera(handler, 0, 0);

		screen = new IntroScreen(handler);
		Screen.setScreen(screen);
	}

	public InputManager getInputManager()
	{
		return inputMgr;
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
		if (inputMgr.quit)
		{
			stop();
		}
	}

	private void draw()
	{
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null)
		{
			display.getCanvas().createBufferStrategy(2);
			return;
		}
		gr = bs.getDrawGraphics();

		gr.clearRect(0, 0, width, height); // Clear screen

		// #region drawings

		gr.setColor(Color.black);
		gr.drawString("FPS: " + fps, 0, 10);

		if (Screen.getScreen() != null)
		{
			Screen.getScreen().draw(gr);
		}

		// #endregion

		bs.show();
		gr.dispose();
	}

	@Override
	public void run()
	{
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

		int targetFps = 60;
		double timePerTick = 1000000000 / targetFps;
		double delta = 0;
		long current;
		long last = System.nanoTime();
		long timer = 0;
		int ticks = 0;

		while (running)
		{
			current = System.nanoTime();
			delta += (current - last) / timePerTick;
			timer += current - last;
			last = current;

			if (delta >= 1)
			{
				update();
				draw();
				ticks++;
				delta = 0;
			}

			if (timer >= 1000000000)
			{
				if (ticks < 55)
				{
					Launcher.logger.Warning("Frames per second droped to: " + ticks);
				}
				else if (ticks < 59) Launcher.logger.Trace("Frames per second droped to: " + ticks);
				fps = ticks;
				ticks = 0;
				timer = 0;
			}
		}
	}

	public synchronized void start()
	{
		if (running)
		{
			Launcher.logger.Error("Trying to run game while game already running!");
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop()
	{
		if (!running)
		{
			Launcher.logger.Error("Trying to stop game while game is not running!");
			return;
		}
		running = false;
		try
		{
			handler.server.stop();
			Launcher.logger.Info("Stopping game...");
			display.close();
			thread.join(3000);
			Launcher.logger.Info("Game stopped.");
		}
		catch (InterruptedException e)
		{
			Launcher.logger.Exception(e);
		}
	}
}