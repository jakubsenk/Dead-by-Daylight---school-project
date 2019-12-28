package com.senkgang.dbd.map.maps;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.entities.*;
import com.senkgang.dbd.entities.player.Survivor;
import com.senkgang.dbd.map.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

public class TestMap extends Map
{
	public TestMap(int width, int height)
	{
		super(width, height);

		int wallDefinitions[][] = new int[10][];
		Random r = new Random();
		for (int i = 0; i < 2; i++)
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

		for (int i = 0; i < 2; i++)
		{
			Wall w = new Wall(r.nextInt(width), r.nextInt(height), wallDefinitions[i][0], wallDefinitions[i][1]);
			entities.add(w);
			sightBlockers.add(w);
			survivorVisibleEntity.add(w);
			killerVisibleEntity.add(w);
		}

		for (int i = 0; i < 10; i++)
		{
			Generator gen = new Generator(r.nextInt(width), r.nextInt(height));
			entities.add(gen);
			sightBlockers.add(gen);
			killerVisibleEntity.add(gen);
		}

		for (int i = 0; i < 2; i++)
		{
			if (i == 0)
			{
				Gate gate = new Gate(r.nextInt(width), 0);
				entities.add(gate);
				killerVisibleEntity.add(gate);
			}
			else
			{
				Gate gate = new Gate(r.nextInt(width), height);
				entities.add(gate);
				killerVisibleEntity.add(gate);
			}
		}

		if (!Game.handler.isKiller)
		{
			Game.handler.client.addData("READY!");
		}
	}

	@Override
	public void update()
	{
		super.update();
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.setStroke(Color.BLACK);
		g.strokeLine(0 - camX, 0 - camY, 0 - camX, height - camY);
		g.strokeLine(0 - camX, 0 - camY, width - camX, 0 - camY);
		g.strokeLine(0 - camX, height - camY, width - camX, height - camY);
		g.strokeLine(width - camX, 0 - camY, width - camX, height - camY);

		for (BleedEffect b : bleeds)
		{
			b.draw(g, camX, camY);
		}

		if (Game.handler.isKiller)
		{
			for (Survivor s : survivors)
			{
				s.draw(g, camX, camY);
			}
		}
		else
		{
			killer.draw(g, camX, camY);
		}
		fow.draw(g, camX, camY);

		if (!Game.handler.isKiller)
		{
			for (Survivor s : survivors)
			{
				s.draw(g, camX, camY);
			}
		}
		else
		{
			killer.draw(g, camX, camY);
		}
		for (Entity e : Game.handler.isKiller ? killerVisibleEntity : survivorVisibleEntity)
		{
			e.draw(g, camX, camY);
		}

	}
}
