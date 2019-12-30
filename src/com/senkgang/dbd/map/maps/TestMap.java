package com.senkgang.dbd.map.maps;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.*;
import com.senkgang.dbd.entities.player.Survivor;
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

			for (int i = 0; i < 20; i++)
			{
				int x = r.nextInt(width);
				int y = r.nextInt(height);
				Generator gen = new Generator(x, y);
				entities.add(gen);
				sightBlockers.add(gen);
				killerVisibleEntity.add(gen);
				spawnObjects.add("Spawn object;" + Generator.class.getSimpleName() + ":" + x + ":" + y);
			}

			for (int i = 0; i < 2; i++)
			{
				int a = r.nextInt(4);
				int x, y;
				GateOrientation gateOrientation;
				switch (a)
				{
					case 0:
						x = r.nextInt(width);
						y = 0;
						gateOrientation = GateOrientation.North;
						break;
					case 1:
						x = r.nextInt(width);
						y = height;
						gateOrientation = GateOrientation.South;
						break;
					case 2:
						x = 0;
						y = r.nextInt(height);
						gateOrientation = GateOrientation.West;
						break;
					case 3:
						x = width;
						y = r.nextInt(height);
						gateOrientation = GateOrientation.East;
						break;
					default:
						Launcher.logger.Error("Some shit happend! Gate got wrong number in switch" + a);
						return;
				}
				Gate gate = new Gate(x, y, gateOrientation);
				entities.add(gate);
				killerVisibleEntity.add(gate);

				spawnObjects.add("Spawn object;" + Gate.class.getSimpleName() + ":" + x + ":" + y + ":" + gateOrientation);
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
