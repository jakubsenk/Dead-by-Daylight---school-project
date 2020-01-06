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
import com.senkgang.dbd.enums.GateOrientation;
import com.senkgang.dbd.interfaces.ISightBlocker;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

public abstract class Map
{
	public final int width;
	public final int height;

	private int objCount = 0;
	private int maxObjCount = -1;

	protected ArrayList<Entity> entities;
	protected ArrayList<BleedEffect> bleeds;
	protected ArrayList<ISightBlocker> sightBlockers;
	protected ArrayList<Entity> killerVisibleEntity;
	protected ArrayList<Entity> survivorVisibleEntity;
	protected ArrayList<Survivor> survivors;
	protected Killer killer;
	protected Player controlledPlayer;

	protected FogOfWar fow;

	private Label loading = new Label("Loading...");

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
		killer = new TestKiller(-1, 0, 0, "unknown", false, null, null);
		fow = new FogOfWar(killer);

		if (Game.handler.isKiller)
		{
			Game.handler.generatorsRemaining = 2 * Game.handler.server.connectedSurvivorsNicks.size();
			Game.handler.server.addData("Gens to repair:" + Game.handler.generatorsRemaining);
			Game.handler.server.addData(createKiller());

			for (int i = 0; i < Game.handler.server.connectedSurvivorsNicks.size(); i++)
			{
				String spawnData = addSurvivor(Game.handler.server.connectedSurvivorsNicks.get(i), i + 1);
				Game.handler.server.addData(spawnData);
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

	public void addToKillerVisibleEntities(Entity e)
	{
		if (!killerVisibleEntity.contains(e)) killerVisibleEntity.add(e);
	}

	public void addBleedEffect(BleedEffect e)
	{
		if (!bleeds.contains(e)) bleeds.add(e);
	}

	public void removeBleedEffect(BleedEffect e)
	{
		bleeds.remove(e);
	}

	public void removeFromSurvivorVisibleEntities(Entity e)
	{
		survivorVisibleEntity.remove(e);
	}

	public void removeFromKillerVisibleEntities(Entity e)
	{
		killerVisibleEntity.remove(e);
	}

	public void update()
	{
		for (int i = 0; i < entities.size(); i++)
		{
			entities.get(i).update();
		}
		for (int i = 0; i < bleeds.size(); i++)
		{
			bleeds.get(i).update();
		}
		for (int i = 0; i < survivors.size(); i++)
		{
			survivors.get(i).update();
		}

		killer.update();
		Game.handler.getGameCamera().followEntity(controlledPlayer);
		if (Game.handler.isKiller)
		{
			if (survivors.size() > 0)
			{
				try
				{
					Game.handler.server.send();
				}
				catch (SocketException e)
				{
					survivors.clear();
					Label l = new Label("Connection with survivor lost!");
					Display.addComponentInstant(l);
					l.setTextFill(Color.RED);
					l.setFont(new Font("Segoe UI", 36));
					l.relocate(Game.handler.getScreenWidth() / 2 - 250, Game.handler.getScreenHeight() / 4);
					new java.util.Timer().schedule(new java.util.TimerTask()
					{
						@Override
						public void run()
						{
							Display.removeComponent(l);
						}
					}, 5000);
				}
			}
		}
		else
		{
			if (Game.handler.client.connectFailed)
			{
				JOptionPane.showMessageDialog(null, "Unable to connect to killer.", "Connection lost.", JOptionPane.ERROR_MESSAGE);
				Game.handler.getGame().stop();
			}
			try
			{
				Game.handler.client.send();
			}
			catch (SocketException e)
			{
				JOptionPane.showMessageDialog(null, e, "Connection lost.", JOptionPane.ERROR_MESSAGE);
				Game.handler.getGame().stop();
			}
		}
	}

	public void draw(GraphicsContext g, int camX, int camY)
	{
		for (int i = 0; i < bleeds.size(); i++)
		{
			bleeds.get(i).draw(g, camX, camY);
		}

		if (Game.handler.isKiller)
		{
			for (int i = 0; i < survivors.size(); i++)
			{
				survivors.get(i).draw(g, camX, camY);
			}
		}
		else
		{
			killer.draw(g, camX, camY);
		}
		fow.draw(g, camX, camY);

		if (!Game.handler.isKiller)
		{
			for (int i = 0; i < survivors.size(); i++)
			{
				survivors.get(i).draw(g, camX, camY);
			}
		}
		else
		{
			killer.draw(g, camX, camY);
		}
		for (int i = 0; i < (Game.handler.isKiller ? killerVisibleEntity : survivorVisibleEntity).size(); i++)
		{
			(Game.handler.isKiller ? killerVisibleEntity : survivorVisibleEntity).get(i).draw(g, camX, camY);
		}
	}

	public abstract String createKiller();

	@ServerSide
	public String addSurvivor(String s, int id)
	{
		Random r = new Random();
		int spawnX = r.nextInt(width);
		int spawnY = r.nextInt(height);
		if (survivors.size() >= 4) return "Too many survivors";
		survivors.add(new TestSurvivor(id, spawnX, spawnY, s, false, entities, sightBlockers));
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
		survivors.add(spawned);
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

	@ServerSide
	@ClientSide
	public void repairGen(String line, boolean repair)
	{
		int id = Integer.parseInt(line.split(":")[1]);
		Generator g = (Generator) entities.stream().filter(x -> x instanceof Generator && ((Generator) x).getId() == id).findFirst().orElse(null);
		if (g == null)
		{
			Launcher.logger.Error("Some shit happend! Generator with id " + id + " not found.");
			return;
		}
		if (!g.isRepairing()) g.setRepairing(repair);
	}

	@ServerSide
	@ClientSide
	public void openGate(String line, boolean open)
	{
		int id = Integer.parseInt(line.split(":")[1]);
		Gate g = (Gate) entities.stream().filter(x -> x instanceof Gate && ((Gate) x).getId() == id).findFirst().orElse(null);
		if (g == null)
		{
			Launcher.logger.Error("Some shit happend! Gate with id " + id + " not found.");
			return;
		}
		if (!g.isOpening()) g.setOpening(open);
	}

	@ServerSide
	@ClientSide
	public void unhookSurv(String line, boolean rescue)
	{
		int id = Integer.parseInt(line.split(":")[1]);
		Hook h = (Hook) entities.stream().filter(x -> x instanceof Hook && ((Hook) x).getId() == id).findFirst().orElse(null);
		if (h == null)
		{
			Launcher.logger.Error("Some shit happend! Hook with id " + id + " not found.");
			return;
		}
		if (!h.isRescuing()) h.setRescuing(rescue);
	}

	@ServerSide
	@ClientSide
	public void healSurv(String line, boolean heal)
	{
		int id = Integer.parseInt(line.split(":")[1]);
		Survivor s = survivors.stream().filter(x -> x.getPlayerID() == id).findFirst().orElse(null);
		if (s == null)
		{
			Launcher.logger.Error("Some shit happend! Survivor with id " + id + " not found.");
			return;
		}
		s.setHealing(heal);
	}

	@ClientSide
	public void syncGen(String line)
	{
		int id = Integer.parseInt(line.split(":")[1].split(";")[0]);
		double progress = Double.parseDouble(line.split(";")[1]);
		Generator g = (Generator) entities.stream().filter(x -> x instanceof Generator && ((Generator) x).getId() == id).findFirst().orElse(null);
		if (g == null)
		{
			Launcher.logger.Error("Some shit happend! Generator with id " + id + " not found.");
			return;
		}
		g.setProgress(progress);
		if (progress == 100) g.finish();
	}

	@ClientSide
	public void syncGate(String line)
	{
		int id = Integer.parseInt(line.split(":")[1].split(";")[0]);
		double progress = Double.parseDouble(line.split(";")[1]);
		Gate g = (Gate) entities.stream().filter(x -> x instanceof Gate && ((Gate) x).getId() == id).findFirst().orElse(null);
		if (g == null)
		{
			Launcher.logger.Error("Some shit happend! Gate with id " + id + " not found.");
			return;
		}
		g.setProgress(progress);
		if (progress == 100) g.finish();
	}

	@ClientSide
	public void syncUnhooking(String line)
	{
		int id = Integer.parseInt(line.split(":")[1].split(";")[0]);
		double progress = Double.parseDouble(line.split(";")[1]);
		Hook h = (Hook) entities.stream().filter(x -> x instanceof Hook && ((Hook) x).getId() == id).findFirst().orElse(null);
		if (h == null)
		{
			Launcher.logger.Error("Some shit happend! Hook with id " + id + " not found.");
			return;
		}
		h.setProgress(progress);
		if (progress == 100) h.finish();
	}

	@ClientSide
	public void syncHealing(String line)
	{
		int id = Integer.parseInt(line.split(":")[1].split(";")[0]);
		double progress = Double.parseDouble(line.split(";")[1]);
		Survivor s = survivors.stream().filter(x -> x.getPlayerID() == id).findFirst().orElse(null);
		if (s == null)
		{
			Launcher.logger.Error("Some shit happend! Survivor with id " + id + " not found.");
			return;
		}
		s.setProgress(progress);
		if (progress == 100) s.finish();
	}

	@ClientSide
	public void pickSurv(String line)
	{
		int id = Integer.parseInt(line.split(":")[1]);
		Survivor s = survivors.stream().filter(x -> x.getPlayerID() == id).findFirst().orElse(null);
		if (s == null)
		{
			Launcher.logger.Error("Some shit happend! Survivor with id " + id + " not found.");
			return;
		}
		killer.pick(s);
	}

	@ClientSide
	public void putSurv(String line)
	{
		int id = Integer.parseInt(line.split(":")[1]);
		Hook h = (Hook) entities.stream().filter(x -> x instanceof Hook && ((Hook) x).getId() == id).findFirst().orElse(null);
		if (h == null)
		{
			Launcher.logger.Error("Some shit happend! Hook with id " + id + " not found.");
			return;
		}
		h.put(killer);
	}

	@ClientSide
	public void spawnObject(String line)
	{
		String t = line.split(";")[1];
		String[] obj = t.split(":");

		if (obj[0].equals(Wall.class.getSimpleName()))
		{
			Wall w = new Wall(Double.parseDouble(obj[1]), Double.parseDouble(obj[2]), Integer.parseInt(obj[3]), Integer.parseInt(obj[4]));
			entities.add(w);
			sightBlockers.add(w);
			survivorVisibleEntity.add(w);
			killerVisibleEntity.add(w);
			objCount++;
		}
		else if (obj[0].equals(Generator.class.getSimpleName()))
		{
			Generator gen = new Generator(Integer.parseInt(obj[1]), Double.parseDouble(obj[2]), Double.parseDouble(obj[3]));
			entities.add(gen);
			sightBlockers.add(gen);
			killerVisibleEntity.add(gen);
			objCount++;
		}
		else if (obj[0].equals(Hook.class.getSimpleName()))
		{
			Hook h = new Hook(Integer.parseInt(obj[1]), Double.parseDouble(obj[2]), Double.parseDouble(obj[3]));
			entities.add(h);
			sightBlockers.add(h);
			killerVisibleEntity.add(h);
			survivorVisibleEntity.add(h);
			objCount++;
		}
		else if (obj[0].equals(Gate.class.getSimpleName()))
		{
			Gate gate = new Gate(Integer.parseInt(obj[1]), Double.parseDouble(obj[2]), Double.parseDouble(obj[3]), GateOrientation.valueOf(obj[4]));
			entities.add(gate);
			killerVisibleEntity.add(gate);
			objCount++;
		}

		if (objCount == maxObjCount)
		{
			Launcher.logger.Info("Sending ready");
			Game.handler.client.addData("READY!");
		}
	}

	@ClientSide
	public void setMaxObjCount(int cnt)
	{
		maxObjCount = cnt;
	}
}
