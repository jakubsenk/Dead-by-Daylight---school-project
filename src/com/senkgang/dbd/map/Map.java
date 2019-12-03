package com.senkgang.dbd.map;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.CollidableEntity;
import com.senkgang.dbd.entities.FogOfWar;
import com.senkgang.dbd.entities.ISightBlocker;
import com.senkgang.dbd.entities.Player;
import com.senkgang.dbd.entities.player.Killer;
import com.senkgang.dbd.entities.player.Survivor;
import com.senkgang.dbd.entities.player.TestKiller;
import com.senkgang.dbd.entities.player.TestSurvivor;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

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

	public Map(Handler h, int width, int height)
	{
		this.width = width;
		this.height = height;
		handler = h;

		entities = new ArrayList<CollidableEntity>();
		sightBlockers = new ArrayList<ISightBlocker>();

		survivors = new ArrayList<>();
		newSurvivors = new ArrayList<>();
		killer = new TestKiller(0, handler, 300, 300, handler.isKiller, entities, sightBlockers);

		if (handler.isKiller)
		{
			handler.server.start(h);
		}
		else
		{
			handler.client.start(h);
		}
		controlledPlayer = handler.isKiller ? killer : null;
		fow = new FogOfWar(controlledPlayer, handler);
	}

	public abstract void update();

	public abstract void draw(GraphicsContext g, int camX, int camY);

	public String addSurvivor()
	{
		if (survivors.size() >= 4) return "Too many survivors";
		int id = survivors.size() + 1;
		newSurvivors.add(new TestSurvivor(id, handler, 350, 350, false, entities, sightBlockers));
		return id + ";350,350";
	}

	public void addSurvivor(String spawnData, boolean spawnRequested)
	{
		String data = spawnData.split(":")[1];
		String[] cropped = data.split(";");
		int id = Integer.parseInt(cropped[0]);
		int spawnX = Integer.parseInt(cropped[1].split(",")[0]);
		int spawnY = Integer.parseInt(cropped[1].split(",")[1]);
		Survivor spawned = new TestSurvivor(id, handler, spawnX, spawnY, spawnRequested, entities, sightBlockers);
		if (spawnRequested)
		{
			controlledPlayer = spawned;
			fow = new FogOfWar(controlledPlayer, handler);
		}
		newSurvivors.add(spawned);
	}

	public void updateSurvivor(String updateData)
	{
		String data = updateData.split(":")[1];
		String[] cropped = data.split(";");
		int id = Integer.parseInt(cropped[0]);
		double updateX = Double.parseDouble(cropped[1].split(",")[0]);
		double updateY = Double.parseDouble(cropped[1].split(",")[1]);
		double angle = Double.parseDouble(cropped[1].split(",")[2]);

		Survivor s = survivors.stream().filter(surv -> surv.getPlayerID() == id).findFirst().orElse(null);
		if (s == null)
		{
			Launcher.logger.Error("Some shit happend! Survivor with id " + id + " not found!");
			return;
		}
		s.setPos(updateX, updateY);
		s.setAngle(angle);
	}

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
}
