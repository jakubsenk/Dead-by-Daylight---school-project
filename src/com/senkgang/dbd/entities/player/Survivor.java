package com.senkgang.dbd.entities.player;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.BleedEffect;
import com.senkgang.dbd.entities.CollidableEntity;
import com.senkgang.dbd.interfaces.ISightBlocker;
import com.senkgang.dbd.entities.Player;
import com.senkgang.dbd.enums.SurvivorState;
import com.senkgang.dbd.fov.Algorithm;
import com.senkgang.dbd.fov.Line;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Survivor extends Player
{
	private Algorithm algorithm = new Algorithm(450);
	private ArrayList<Point2D> points = new ArrayList<>();
	private ArrayList<Line> sceneLines = new ArrayList<>();
	private ArrayList<Line> scanLines = new ArrayList<>();
	private double[] viewPolygonX;
	private double[] viewPolygonY;
	private Timer boostTimer = new Timer();
	private Timer bleedTimer = new Timer();

	protected SurvivorState state = SurvivorState.Normal;

	public Survivor(int playerID, double x, double y, String nick, boolean playerControlled, ArrayList<CollidableEntity> entities, ArrayList<ISightBlocker> sightBlockers)
	{
		super(playerID, x, y, nick, playerControlled, entities, sightBlockers);
	}

	@Override
	public double[] getViewPolygonX()
	{
		return viewPolygonX;
	}

	@Override
	public double[] getViewPolygonY()
	{
		return viewPolygonY;
	}

	public void hit()
	{
		if (Game.handler.isKiller)
		{
			Game.handler.server.addData("hit:" + getPlayerID());
		}
		if (state == SurvivorState.Normal)
		{
			hitBleed();
		}
		else if (state == SurvivorState.Bleeding)
		{
			hitKO();
		}
	}

	private void hitBleed()
	{
		state = SurvivorState.Bleeding;
		speed *= 2;
		Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x, y, 1000));
		boostTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				speed /= 2;
				scheduleBleed();
			}
		}, 2500);
	}

	private void hitKO()
	{
		state = SurvivorState.Dying;
		speed /= 10;
		if (Game.handler.isKiller)
		{
			Game.handler.server.addData("bleed:" + x + ";" + y);
			Game.handler.server.addData("bleed:" + (x + 20) + ";" + y);
			Game.handler.server.addData("bleed:" + (x - 20) + ";" + y);
			Game.handler.server.addData("bleed:" + x + ";" + (y + 20));
			Game.handler.server.addData("bleed:" + x + ";" + (y - 20));
			Game.handler.server.addData("bleed:" + (x + 20) + ";" + (y + 20));
			Game.handler.server.addData("bleed:" + (x - 20) + ";" + (y - 20));
			Game.handler.server.addData("bleed:" + (x + 20) + ";" + (y - 20));
			Game.handler.server.addData("bleed:" + (x - 20) + ";" + (y + 20));
			Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x, y, 1000));
			Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x + 20, y, 1000));
			Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x - 20, y, 1000));
			Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x, y + 20, 1000));
			Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x, y - 20, 1000));
			Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x + 20, y + 20, 1000));
			Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x - 20, y - 20, 1000));
			Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x + 20, y - 20, 1000));
			Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x - 20, y + 20, 1000));
		}
		scheduleBleed();
	}

	private void scheduleBleed()
	{
		Random r = new Random();
		double lastX = x;
		double lastY = y;
		bleedTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				if (state == SurvivorState.Bleeding)
				{
					if (Game.handler.isKiller)
					{
						Game.handler.server.addData("bleed:" + x + ";" + y);
						Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x, y, 1000));
					}
					scheduleBleed();
				}
				if (state == SurvivorState.Dying)
				{
					if (lastX != x && lastY != y && Game.handler.isKiller)
					{
						Game.handler.server.addData("bleed:" + x + ";" + y);
						Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x, y, 1000));
					}
					scheduleBleed();
				}
			}
		}, r.nextInt(5500) + 1500);
	}

	@Override
	public void update()
	{
		super.update();

		scanLines = algorithm.createScanLines(getX(), getY());

		sceneLines = new ArrayList<>();
		if (sightBlockers != null)
		{
			for (int i = 0; i < sightBlockers.size(); i++)
			{
				sceneLines.addAll(sightBlockers.get(i).getSightBlockingLines());
			}
		}

		points = algorithm.getIntersectionPoints(scanLines, sceneLines);

		viewPolygonX = new double[points.size()];
		viewPolygonY = new double[points.size()];
		for (int i = 0; i < points.size(); i++)
		{
			viewPolygonX[i] = points.get(i).getX();
			viewPolygonY[i] = points.get(i).getY();
		}
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		super.draw(g, camX, camY);
		if (Launcher.isDebug)
		{
			g.setStroke(Color.GREEN);

			double[] xPol = new double[points.size()];
			double[] yPol = new double[points.size()];
			for (int i = 0; i < points.size(); i++)
			{
				xPol[i] = points.get(i).getX() - camX;
				yPol[i] = points.get(i).getY() - camY;
			}
			g.setLineWidth(3);
			g.strokePolygon(xPol, yPol, points.size());
			g.setLineWidth(1);
		}
	}
}
