package com.senkgang.dbd.map.maps;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.*;
import com.senkgang.dbd.entities.player.TestKiller;
import com.senkgang.dbd.entities.player.TestSurvivor;
import com.senkgang.dbd.enums.GateOrientation;
import com.senkgang.dbd.map.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class TestMap extends Map
{
	public TestMap(int width, int height)
	{
		super(width, height);

		if (Game.handler.isKiller)
		{
			ArrayList<String> spawnObjects = new ArrayList<>();
			int wallDefinitions[][] = new int[5][];
			Random r = new Random();
			for (int i = 0; i < 5; i++)
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

			for (int i = 0; i < 5; i++)
			{
				int x = r.nextInt(width);
				int y = r.nextInt(height);
				Wall w = new Wall(x, y, wallDefinitions[i][0], wallDefinitions[i][1]);
				entities.add(w);
				sightBlockers.add(w);
				survivorVisibleEntity.add(w);
				killerVisibleEntity.add(w);
				spawnObjects.add("Spawn object;" + Wall.class.getSimpleName() + ":" + x + ":" + y + ":" + wallDefinitions[i][0] + ":" + wallDefinitions[i][1]);
			}

			for (int i = 0; i < 7; i++)
			{
				int x = r.nextInt(width);
				int y = r.nextInt(height);
				Generator gen = new Generator(i + 1, x, y);
				entities.add(gen);
				sightBlockers.add(gen);
				killerVisibleEntity.add(gen);
				spawnObjects.add("Spawn object;" + Generator.class.getSimpleName() + ":" + (i + 1) + ":" + x + ":" + y);
			}

			for (int i = 0; i < 5; i++)
			{
				int x = r.nextInt(width);
				int y = r.nextInt(height);
				Hook h = new Hook(i + 1, x, y);
				entities.add(h);
				sightBlockers.add(h);
				killerVisibleEntity.add(h);
				survivorVisibleEntity.add(h);
				spawnObjects.add("Spawn object;" + Hook.class.getSimpleName() + ":" + (i + 1) + ":" + x + ":" + y);
			}

			for (int i = 0; i < 2; i++)
			{
				int x, y;
				GateOrientation gateOrientation = GateOrientation.values()[r.nextInt(4)];
				switch (gateOrientation)
				{
					case North:
						x = r.nextInt(width);
						y = 0;
						break;
					case South:
						x = r.nextInt(width);
						y = height;
						break;
					case West:
						x = 0;
						y = r.nextInt(height);
						break;
					case East:
						x = width;
						y = r.nextInt(height);
						break;
					default:
						Launcher.logger.Error("Some shit happend! Gate got wrong number in switch" + gateOrientation);
						return;
				}
				Gate gate = new Gate(i + 1, x, y, gateOrientation);
				entities.add(gate);
				killerVisibleEntity.add(gate);

				spawnObjects.add("Spawn object;" + Gate.class.getSimpleName() + ":" + (i + 1) + ":" + x + ":" + y + ":" + gateOrientation);
			}
			Game.handler.server.addData("Spawn object count:" + entities.size());
			for (String s : spawnObjects)
			{
				Game.handler.server.addData(s);
			}
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

		super.draw(g, camX, camY);
	}

	@Override
	public String createKiller()
	{
		Random r = new Random();
		int spawnX = r.nextInt(width);
		int spawnY = r.nextInt(height);
		killer = new TestKiller(0, spawnX, spawnY, Game.handler.playerNick, false, entities, sightBlockers);
		controlledPlayer = killer;
		fow = new FogOfWar(controlledPlayer);
		return "Spawn data:0;" + Game.handler.playerNick + ";" + spawnX + "," + spawnY;
	}

	@Override
	public String addSurvivor(String s, int id)
	{
		Random r = new Random();
		int spawnX = r.nextInt(width);
		int spawnY = r.nextInt(height);
		if (survivors.size() >= 4) return "Too many survivors";
		survivors.add(new TestSurvivor(id, spawnX, spawnY, s, false, entities, sightBlockers));
		return "Spawn data:" + id + ";" + s + ";" + spawnX + "," + spawnY;
	}
}
