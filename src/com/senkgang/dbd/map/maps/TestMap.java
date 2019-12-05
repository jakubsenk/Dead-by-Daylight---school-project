package com.senkgang.dbd.map.maps;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.display.Display;
import com.senkgang.dbd.entities.*;
import com.senkgang.dbd.entities.player.Survivor;
import com.senkgang.dbd.map.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;
import java.net.SocketException;
import java.util.Random;

public class TestMap extends Map
{
	public TestMap(Handler h, int width, int height)
	{
		super(h, width, height);

		int wallDefinitions[][] = new int[10][];
		Random r = new Random();
		for (int i = 0; i < 10; i++)
		{
			if (r.nextBoolean())
			{
				wallDefinitions[i] = new int[]{50, 350};
			}
			else
			{
				wallDefinitions[i] = new int[]{350, 50};
			}
		}

		for (int i = 0; i < 10; i++)
		{
			Wall w = new Wall(r.nextInt(width), r.nextInt(height), wallDefinitions[i][0], wallDefinitions[i][1]);
			entities.add(w);
			sightBlockers.add(w);
		}

		for (int i = 0; i < 10; i++)
		{
			Generator gen = new Generator(r.nextInt(width), r.nextInt(height));
			entities.add(gen);
			sightBlockers.add(gen);
		}
	}

	@Override
	public void update()
	{
		for (Entity e : entities)
		{
			e.update();
		}
		for (Survivor s : survivors)
		{
			s.update();
		}
		if (newSurvivors.size() > 0) // dont iterate to prevent java.util.ConcurrentModificationException
		{
			Survivor s = newSurvivors.get(0);
			survivors.add(s);
			newSurvivors.remove(s);
		}
		killer.update();
		handler.getGameCamera().followEntity(controlledPlayer);
		if (handler.isKiller)
		{
			if (survivors.size() > 0)
			{
				try
				{
					handler.server.send();
				}
				catch (SocketException e)
				{
					survivors.clear();
					Label l = new Label("Connection with survivor lost!");
					Display.addComponentInstant(l);
					l.setTextFill(Color.RED);
					l.setFont(new Font("Segoe UI", 36));
					l.relocate(handler.getScreenWidth() / 2 - 250, handler.getScreenHeight() / 4);
					new java.util.Timer().schedule(new java.util.TimerTask()
					{
						@Override
						public void run()
						{
							Display.removeComponent(l);
						}
					}, 5000);
				}
			}
		}
		else
		{
			if (handler.client.connectFailed)
			{
				JOptionPane.showMessageDialog(null, "Unable to connect to killer.", "Connection lost.", JOptionPane.ERROR_MESSAGE);
				handler.getGame().stop();
			}
			try
			{
				handler.client.send();
			}
			catch (SocketException e)
			{
				JOptionPane.showMessageDialog(null, e, "Connection lost.", JOptionPane.ERROR_MESSAGE);
				handler.getGame().stop();
			}

		}
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.setStroke(Color.BLACK);
		g.strokeLine(0 - camX, 0 - camY, 0 - camX, height - camY);
		g.strokeLine(0 - camX, 0 - camY, width - camX, 0 - camY);
		g.strokeLine(0 - camX, height - camY, width - camX, height - camY);
		g.strokeLine(width - camX, 0 - camY, width - camX, height - camY);

		for (Entity e : entities)
		{
			e.draw(g, camX, camY);
		}
		killer.draw(g, camX, camY);
		for (Survivor s : survivors)
		{
			s.draw(g, camX, camY);
		}

		fow.draw(g, camX, camY);
	}
}
