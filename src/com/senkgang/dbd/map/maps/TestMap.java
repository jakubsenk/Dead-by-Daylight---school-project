package com.senkgang.dbd.map.maps;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.entities.*;
import com.senkgang.dbd.entities.player.Survivor;
import com.senkgang.dbd.map.Map;

import java.awt.*;
import java.util.Random;

public class TestMap extends Map
{
	public TestMap(Handler h, int width, int height)
	{
		super(h, width, height);

		int wallDefinitions[][] = new int[10][];
		int genDefinitions[][] = new int[2][];
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
		for (int i = 0; i < 2; i++)
		{

				genDefinitions[i] = new int[]{50, 50};

		}
		for (int i = 0; i < 2; i++)
		{
			Generator gen= new Generator(r.nextInt(width), r.nextInt(height), genDefinitions[i][0], genDefinitions[i][1]);
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
			if (survivors.size() > 0) handler.server.send();
		}
		else
		{
			handler.client.send();
		}
	}

	@Override
	public void draw(Graphics g, int camX, int camY)
	{
		g.drawLine(0 - camX, 0 - camY, 0 - camX, height - camY);
		g.drawLine(0 - camX, 0 - camY, width - camX, 0 - camY);
		g.drawLine(0 - camX, height - camY, width - camX, height - camY);
		g.drawLine(width - camX, 0 - camY, width - camX, height - camY);

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
