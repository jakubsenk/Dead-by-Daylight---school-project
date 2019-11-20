package com.senkgang.dbd.map.maps;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.entities.*;
import com.senkgang.dbd.entities.player.Killer;
import com.senkgang.dbd.entities.player.Survivor;
import com.senkgang.dbd.entities.player.TestKiller;
import com.senkgang.dbd.entities.player.TestSurvivor;
import com.senkgang.dbd.map.Map;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class TestMap extends Map
{
	private ArrayList<CollidableEntity> entities;
	private ArrayList<ISightBlocker> sightBlockers;
	private Survivor survivor;
	private Killer killer;
	private Handler handler;

	private FogOfWar fow;

	public TestMap(Handler h, int width, int height)
	{
		super(width, height);
		handler = h;
		entities = new ArrayList<CollidableEntity>();
		sightBlockers = new ArrayList<ISightBlocker>();
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

		survivor = new TestSurvivor(handler, 200, 200, entities, sightBlockers);
		killer = new TestKiller(handler, 300, 300, entities, sightBlockers);
		fow = new FogOfWar(killer, handler);
	}

	@Override
	public void update()
	{
		for (Entity e : entities)
		{
			e.update();
		}
		survivor.update();
		killer.update();
		handler.getGameCamera().followEntity(killer);
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
		fow.draw(g, camX, camY);
		survivor.draw(g, camX, camY);
	}
}
