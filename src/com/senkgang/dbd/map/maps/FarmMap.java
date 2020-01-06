package com.senkgang.dbd.map.maps;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.*;
import com.senkgang.dbd.entities.FarmMap.Barn;
import com.senkgang.dbd.entities.FarmMap.Corn;
import com.senkgang.dbd.entities.player.TestKiller;
import com.senkgang.dbd.enums.GateOrientation;
import com.senkgang.dbd.input.MouseManager;
import com.senkgang.dbd.map.Map;
import com.senkgang.dbd.resources.Assets;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.Random;

public class FarmMap extends Map
{

	private final int[][] genLocations = {{735, 490}, {1160, 490}, {1400, 400}, {1400, 830}, {2440, 920}, {2850, 920}, {2600, 920}, {2100, 1550}, {1850, 1550}, {1600, 1550}, {625, 1450}, {275, 1850}, {2487, 1670}, {2910, 1670}, {200, 150}, {3375, 1900}, {3375, 150}};
	private final int[][] wallLocations = {{450, 1850, 300, 50}, {600, 1650, 300, 50}, {450, 1450, 300, 50}, {450, 1650, 50, 350}, {1900, 675, 350, 50}, {2050, 500, 50, 350}, {1350, 1150, 300, 25}, {1850, 1150, 300, 25}};
	private final int[][] hookLocations = {{775, 1615}, {2050, 260}, {1400, 675}, {450, 900}, {3200, 1650}, {1515, 1370}};

	public FarmMap()
	{
		super((int) Assets.farm.getWidth(), (int) Assets.farm.getHeight());

		if (Game.handler.isKiller)
		{
			ArrayList<String> spawnObjects = new ArrayList<>();
			Random r = new Random();
			for (int i = r.nextInt(wallLocations.length / 4); i < wallLocations.length; i++)
			{
				int x = wallLocations[i][0];
				int y = wallLocations[i][1];
				Wall w = new Wall(x, y, wallLocations[i][2], wallLocations[i][3]);
				entities.add(w);
				sightBlockers.add(w);
				survivorVisibleEntity.add(w);
				killerVisibleEntity.add(w);
				spawnObjects.add("Spawn object;" + Wall.class.getSimpleName() + ":" + x + ":" + y + ":" + wallLocations[i][2] + ":" + wallLocations[i][3]);
				if (r.nextBoolean()) i++;
			}

			for (int i = 0, offset = r.nextInt(1); i < 7; i++, offset += r.nextInt(2) + 1)
			{
				int x = genLocations[offset][0];
				int y = genLocations[offset][1];
				Generator gen = new Generator(i + 1, x, y);
				entities.add(gen);
				sightBlockers.add(gen);
				killerVisibleEntity.add(gen);
				spawnObjects.add("Spawn object;" + Generator.class.getSimpleName() + ":" + (i + 1) + ":" + x + ":" + y);
			}

			for (int i = 0; i < hookLocations.length; i++)
			{
				int x = hookLocations[i][0];
				int y = hookLocations[i][1];
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

		spawnStaticObjects();
	}

	private void spawnStaticObjects()
	{
		Barn b = new Barn(950, 425);
		entities.add(b);
		survivorVisibleEntity.add(b);
		killerVisibleEntity.add(b);
		sightBlockers.add(b);

		b = new Barn(2700, 1600);
		entities.add(b);
		survivorVisibleEntity.add(b);
		killerVisibleEntity.add(b);
		sightBlockers.add(b);

		for (int j = 0; j < 4; j++)
		{
			for (int i = 0; i < 5; i++)
			{
				Corn c = new Corn(1275 + j * (Assets.corn.getWidth() + 20), 240 + i * (Assets.corn.getHeight() + 20));
				entities.add(c);
				sightBlockers.add(c);
			}
		}

		for (int j = 0; j < 8; j++)
		{
			for (int i = 0; i < 3; i++)
			{
				Corn c = new Corn(1565 + j * (Assets.corn.getWidth() + 20), 1400 + i * (Assets.corn.getHeight() + 20));
				entities.add(c);
				sightBlockers.add(c);
			}
		}

		for (int j = 0; j < 8; j++)
		{
			for (int i = 0; i < 3; i++)
			{
				Corn c = new Corn(2320 + j * (Assets.corn.getWidth() + 20), 775 + i * (Assets.corn.getHeight() + 20));
				entities.add(c);
				sightBlockers.add(c);
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
		g.drawImage(Assets.farm, 0 - camX, 0 - camY);
		super.draw(g, camX, camY);

		g.strokeText("Generators remaining: " + Game.handler.generatorsRemaining, 0, 25);
		g.strokeText(MouseManager.getMouseXInWorld() + ":" + MouseManager.getMouseYInWorld(), 0, 45);
	}

	@Override
	public String createKiller()
	{
		int spawnX = 200;
		int spawnY = 400;
		killer = new TestKiller(0, spawnX, spawnY, Game.handler.playerNick, false, entities, sightBlockers);
		controlledPlayer = killer;
		fow = new FogOfWar(controlledPlayer);
		return "Spawn data:0;" + Game.handler.playerNick + ";" + spawnX + "," + spawnY;
	}
}
