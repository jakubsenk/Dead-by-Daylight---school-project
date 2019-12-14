package com.senkgang.dbd.map;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.annotations.ClientSide;
import com.senkgang.dbd.annotations.ServerSide;
import com.senkgang.dbd.display.Display;
import com.senkgang.dbd.entities.CollidableEntity;
import com.senkgang.dbd.entities.FogOfWar;
import com.senkgang.dbd.entities.ISightBlocker;
import com.senkgang.dbd.entities.Player;
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
	protected ArrayList<ISightBlocker> sightBlockers;
	protected ArrayList<Survivor> survivors;
	protected ArrayList<Survivor> newSurvivors;
	protected Killer killer;
	protected Player controlledPlayer;
	protected Handler handler;

	protected FogOfWar fow;

	protected Label loading = new Label("Loading...");

	public Map(Handler h, int width, int height)
	{
		handler = h;
		loading.setFont(new Font("Segoe UI", 36));
		Display.addComponent(loading);
		loading.relocate(handler.getScreenWidth() / 2, handler.getScreenHeight() / 2);
		this.width = width;
		this.height = height;

		entities = new ArrayList<CollidableEntity>();
		sightBlockers = new ArrayList<ISightBlocker>();

		survivors = new ArrayList<>();
		newSurvivors = new ArrayList<>();
		killer = new TestKiller(0, handler, 300, 300, handler.playerNick, false, entities, sightBlockers);

		controlledPlayer = handler.isKiller ? killer : null;
		fow = new FogOfWar(controlledPlayer, handler);

		if (handler.isKiller)
		{
			for (int i = 0; i < handler.server.connectedSurvivorsNicks.size(); i++)
			{
				String spawnData = addSurvivor(handler.server.connectedSurvivorsNicks.get(i), i + 1);
				handler.server.addData(spawnData);
			}
			try
			{
				handler.server.send();
			}
			catch (SocketException e)
			{
				Launcher.logger.Exception(e);
			}
		}
	}

	public abstract void update();

	public abstract void draw(GraphicsContext g, int camX, int camY);

	@ServerSide
	public String addSurvivor(String s, int id)
	{
		Random r = new Random();
		int spawnX = r.nextInt(width);
		int spawnY = r.nextInt(height);
		if (survivors.size() >= 4) return "Too many survivors";
		newSurvivors.add(new TestSurvivor(id, handler, spawnX, spawnY, s, false, entities, sightBlockers));
		return "Spawn data:" + id + ";" + spawnX + "," + spawnY;
	}

	@ClientSide
	public void addSurvivor(String spawnData, boolean spawnRequested)
	{
		String data = spawnData.split(":")[1];
		String[] cropped = data.split(";");
		int id = Integer.parseInt(cropped[0]);
		int spawnX = Integer.parseInt(cropped[1].split(",")[0]);
		int spawnY = Integer.parseInt(cropped[1].split(",")[1]);
		Survivor spawned = new TestSurvivor(id, handler, spawnX, spawnY, handler.playerNick, false, entities, sightBlockers);
		if (spawnRequested)
		{
			controlledPlayer = spawned;
			fow = new FogOfWar(controlledPlayer, handler);
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
		if (cropped[0].equals(handler.playerID)) return;

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
		if (handler.isKiller)
		{
			killer.setControllable(true);
		}
		else
		{
			Survivor s = survivors.stream().filter(survivor -> survivor.getPlayerID() == Integer.parseInt(handler.playerID)).findFirst().orElse(null);
			if (s == null)
			{
				Launcher.logger.Error("Some shit happend! Survivor with id " + handler.playerID + " not found!");
				return;
			}
			s.setControllable(true);
		}
		Display.removeComponent(loading);
	}
}
