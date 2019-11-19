package com.senkgang.dbd.map.maps;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.entities.*;
import com.senkgang.dbd.entities.player.Survivor;
import com.senkgang.dbd.entities.player.TestSurvivor;
import com.senkgang.dbd.map.Map;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class TestMap extends Map
{
	private ArrayList<CollidableEntity> entities;
	private ArrayList<ISightBlocker> sightBlockers;
	private Survivor player;
	private Handler handler;

	private ArrayList<ArrayList<FogOfWar>> fows;

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

		player = new TestSurvivor(handler, 200, 200, entities, sightBlockers);

		fows = new ArrayList<>();
		for (int i = 0; i < width / 10; i++)
		{
			ArrayList<FogOfWar> row = new ArrayList<>();
			for (int j = 0; j < height / 10; j++)
			{
				row.add(new FogOfWar(i * 10, j * 10, player));
			}
			fows.add(row);
		}
	}

	@Override
	public void update()
	{
		for (Entity e : entities)
		{
			e.update();
		}
		player.update();
		handler.getGameCamera().followEntity(player);
		for (int i = 0; i < width / 10; i++)
		{
			for (int j = 0; j < height / 10; j++)
			{
				fows.get(i).get(j).update();
			}
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
		player.draw(g, camX, camY);
		for (int i = 0; i < width / 10; i++)
		{
			for (int j = 0; j < height / 10; j++)
			{
				fows.get(i).get(j).draw(g, camX, camY);
			}
		}
	}
}
