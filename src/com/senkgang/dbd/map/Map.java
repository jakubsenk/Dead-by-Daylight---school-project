package com.senkgang.dbd.map;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.annotations.ClientSide;
import com.senkgang.dbd.annotations.ServerSide;
import com.senkgang.dbd.display.Display;
import com.senkgang.dbd.entities.*;
import com.senkgang.dbd.entities.player.Killer;
import com.senkgang.dbd.entities.player.Survivor;
import com.senkgang.dbd.entities.player.TestKiller;
import com.senkgang.dbd.entities.player.TestSurvivor;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.text.Font;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

public abstract class Map
{
	protected final int width;
	protected final int height;

	protected ArrayList<CollidableEntity> entities;
	protected ArrayList<BleedEffect> bleeds;
	protected ArrayList<ISightBlocker> sightBlockers;
	protected ArrayList<Entity> killerVisibleEntity;
	protected ArrayList<Entity> survivorVisibleEntity;
	protected ArrayList<Survivor> survivors;
	protected ArrayList<Survivor> newSurvivors;
	protected Killer killer;
	protected Player controlledPlayer;

	protected FogOfWar fow;

	protected Label loading = new Label("Loading...");

	public Map(int width, int height)
	{
		loading.setFont(new Font("Segoe UI", 36));
		Display.addComponent(loading);
		loading.relocate(Game.handler.getScreenWidth() / 2, Game.handler.getScreenHeight() / 2);
		this.width = width;
		this.height = height;

		entities = new ArrayList<>();
		bleeds = new ArrayList<>();
		sightBlockers = new ArrayList<>();

		killerVisibleEntity = new ArrayList<>();
		survivorVisibleEntity = new ArrayList<>();

		survivors = new ArrayList<>();
		newSurvivors = new ArrayList<>();
		killer = new TestKiller(-1, 0, 0, "unknown", false, null, null);
		fow = new FogOfWar(killer);

		if (Game.handler.isKiller)
		{
			Game.handler.server.addData(createKiller());

			for (int i = 0; i < Game.handler.server.connectedSurvivorsNicks.size(); i++)
			{
				String spawnData = addSurvivor(Game.handler.server.connectedSurvivorsNicks.get(i), i + 1);
				Game.handler.server.addData(spawnData);
			}
			try
			{
				Game.handler.server.send();
			}
			catch (SocketException e)
			{
				Launcher.logger.Exception(e);
			}
		}
	}

	public Player getPlayer()
	{
		return controlledPlayer;
	}

	public Killer getKiller()
	{
		return killer;
	}

	public ArrayList<Survivor> getSurvivors()
	{
		return survivors;
	}

	public void addToSurvivorVisibleEntities(Entity e)
	{
		if (!survivorVisibleEntity.contains(e)) survivorVisibleEntity.add(e);
	}

	public void addBleedEffect(BleedEffect e)
	{
		if (!bleeds.contains(e)) bleeds.add(e);
	}

	public void removeFromSurvivorVisibleEntities(Entity e)
	{
		survivorVisibleEntity.remove(e);
	}

	public abstract void update();

	public abstract void draw(GraphicsContext g, int camX, int camY);

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

	@ServerSide
	public String addSurvivor(String s, int id)
	{
		Random r = new Random();
		int spawnX = r.nextInt(width);
		int spawnY = r.nextInt(height);
		if (survivors.size() >= 4) return "Too many survivors";
		newSurvivors.add(new TestSurvivor(id, spawnX, spawnY, s, false, entities, sightBlockers));
		return "Spawn data:" + id + ";" + s + ";" + spawnX + "," + spawnY;
	}

	@ClientSide
	public void addSurvivor(String spawnData, boolean spawnRequested)
	{
		String data = spawnData.split(":")[1];
		String[] cropped = data.split(";");
		String nick = cropped[1];
		int id = Integer.parseInt(cropped[0]);
		int spawnX = Integer.parseInt(cropped[2].split(",")[0]);
		int spawnY = Integer.parseInt(cropped[2].split(",")[1]);
		if (id == 0)
		{
			killer = new TestKiller(0, spawnX, spawnY, nick, false, entities, sightBlockers);
			killer.setAngle(0);
			return;
		}
		Survivor spawned = new TestSurvivor(id, spawnX, spawnY, nick, false, entities, sightBlockers);
		if (spawnRequested)
		{
			controlledPlayer = spawned;
			fow = new FogOfWar(controlledPlayer);
		}
		else
		{
			spawned.setAngle(0);
		}
		newSurvivors.add(spawned);
	}

	@ServerSide
	@ClientSide
	public void updateSurvivor(String updateData)
	{
		String data = updateData.split(":")[1];
		String[] cropped = data.split(";");
		if (cropped[0].equals(Game.handler.playerID)) return;

		int id = Integer.parseInt(cropped[0]);
		double updateX = Double.parseDouble(cropped[1].split(",")[0]);
		double updateY = Double.parseDouble(cropped[1].split(",")[1]);
		double angle = Double.parseDouble(cropped[1].split(",")[2]);

		Survivor s = survivors.stream().filter(survivor -> survivor.getPlayerID() == id).findFirst().orElse(null);
		if (s == null)
		{
			Launcher.logger.Error("Some shit happend! Survivor with id " + id + " not found!");
			return;
		}
		s.setPos(updateX, updateY);
		s.setAngle(angle);
	}

	@ClientSide
	public void updateKiller(String updateData)
	{
		String data = updateData.split(";")[1];
		String[] cropped = data.split(",");
		double updateX = Double.parseDouble(cropped[0]);
		double updateY = Double.parseDouble(cropped[1]);
		double angle = Double.parseDouble(cropped[2]);

		killer.setPos(updateX, updateY);
		killer.setAngle(angle);
	}

	@ServerSide
	@ClientSide
	public void unlockPlayer()
	{
		if (Game.handler.isKiller)
		{
			killer.setControllable(true);
		}
		else
		{
			Survivor s = survivors.stream().filter(survivor -> survivor.getPlayerID() == Integer.parseInt(Game.handler.playerID)).findFirst().orElse(null);
			if (s == null)
			{
				Launcher.logger.Error("Some shit happend! Survivor with id " + Game.handler.playerID + " not found!");
				return;
			}
			s.setControllable(true);
		}
		Display.removeComponent(loading);
	}
}
