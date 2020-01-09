package com.senkgang.dbd.entities.player;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.display.Display;
import com.senkgang.dbd.entities.BleedEffect;
import com.senkgang.dbd.entities.Entity;
import com.senkgang.dbd.entities.Gate;
import com.senkgang.dbd.input.MouseManager;
import com.senkgang.dbd.interfaces.IProgressable;
import com.senkgang.dbd.interfaces.ISightBlocker;
import com.senkgang.dbd.entities.Player;
import com.senkgang.dbd.enums.SurvivorState;
import com.senkgang.dbd.fov.Algorithm;
import com.senkgang.dbd.fov.Line;
import com.senkgang.dbd.resources.Assets;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Survivor extends Player implements IProgressable
{
	private final int DyingSpeedMultiplier = 10;
	private final int BoostSpeedMultiplier = 2;

	private Algorithm algorithm = new Algorithm(450);
	private ArrayList<Point2D> points = new ArrayList<>();
	private ArrayList<Line> sceneLines = new ArrayList<>();
	private ArrayList<Line> scanLines = new ArrayList<>();
	private double[] viewPolygonX;
	private double[] viewPolygonY;
	private Timer boostTimer = new Timer();
	private Timer bleedTimer = new Timer();

	private boolean beingHealed = false;
	private boolean healAvaiable = false;

	protected SurvivorState state = SurvivorState.Normal;
	protected double progress = 0;

	private int lives = 3;
	private boolean escaped = false;

	public Survivor(int playerID, double x, double y, String nick, boolean playerControlled, ArrayList<Entity> entities, ArrayList<ISightBlocker> sightBlockers)
	{
		super(playerID, x, y, nick, playerControlled, entities, sightBlockers);
	}

	public SurvivorState getState()
	{
		return state;
	}

	public void setState(SurvivorState state)
	{
		this.state = state;
		if (state == SurvivorState.Hooked) lives--;
		if (lives == 0 && getPlayerID() == Integer.parseInt(Game.handler.playerID))
		{
			Label l = new Label("And this is the end of your journey...");
			l.setFont(new Font("Segoe UI", 36));
			l.setStyle("-fx-font-weight: bold");
			l.setTextFill(Color.RED);
			Display.addComponentInstant(l);
			l.relocate(Game.handler.getScreenWidth() / 2 - 270, Game.handler.getScreenHeight() / 2 + 100);
		}
	}

	public boolean isAlive()
	{
		return lives > 0;
	}

	public int getLives()
	{
		return lives;
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

	public boolean canBeHealed()
	{
		return state == SurvivorState.Bleeding;
	}

	public void setHealAvaiable(boolean healAvaiable)
	{
		this.healAvaiable = healAvaiable;
	}

	public void setHealing(boolean heal)
	{
		this.beingHealed = heal;
	}

	public boolean isBeingHealed()
	{
		return beingHealed;
	}

	public void unhook()
	{
		speed *= DyingSpeedMultiplier;
		state = SurvivorState.Bleeding;
		if (getPlayerID() == Integer.parseInt(Game.handler.playerID)) setControllable(true);
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
		speed *= BoostSpeedMultiplier;
		Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x, y, 1000));
		boostTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				speed /= BoostSpeedMultiplier;
				scheduleBleed();
			}
		}, 2500);
	}

	private void hitKO()
	{
		state = SurvivorState.Dying;
		speed /= DyingSpeedMultiplier;
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

	private void scheduleBleed()
	{
		if (!Game.handler.isKiller) return;
		Random r = new Random();
		double lastX = x;
		double lastY = y;
		bleedTimer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				if (Game.handler.getCurrentMap() == null || escaped) return;
				if (state == SurvivorState.Bleeding || state == SurvivorState.Dying)
				{
					if ((lastX != x && lastY != y && state == SurvivorState.Dying) || state == SurvivorState.Bleeding)
					{
						Game.handler.server.addData("bleed:" + x + ";" + y);
						Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x, y, 1000));
					}
					scheduleBleed();
				}
				else if (state == SurvivorState.Hooked)
				{
					double xOffset = x + r.nextInt(35) - 15;
					Game.handler.server.addData("bleed:" + xOffset + ";" + (y + 30));
					Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(xOffset, y + 30, 1000));
					scheduleBleed();
				}
			}
		}, r.nextInt(5500) + 1500);
	}

	public void finish()
	{
		if (Game.handler.isKiller) Game.handler.server.addData("heal sync:" + getPlayerID() + ";" + 100);
		progress = 0;
		state = SurvivorState.Normal;
		healAvaiable = false;
		beingHealed = false;
	}

	@Override
	public void onProgress()
	{
		progress += 0.175;
		if (Game.handler.isKiller)
		{
			if (progress % 30 == 0)
			{
				Game.handler.server.addData("heal sync:" + getPlayerID() + ";" + progress);
			}
			if (progress >= 100) finish();
		}
	}

	@Override
	public double getProgress()
	{
		return progress;
	}

	@Override
	public void setProgress(double progress)
	{
		this.progress = progress;
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

		if (!Game.handler.isKiller && canControl)
		{
			for (int i = 0; i < Game.handler.getCurrentMap().getSurvivors().size(); i++)
			{
				Survivor s = Game.handler.getCurrentMap().getSurvivors().get(i);
				if (s.getPlayerID() == Integer.parseInt(Game.handler.playerID)) continue;
				double xDiff = x - s.getX();
				double yDiff = y - s.getY();
				double distanceToPlayer = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
				if (distanceToPlayer < 60)
				{
					if (s.canBeHealed())
					{
						if (MouseManager.leftButtonPressed() && isInSurvivorRange(s.getX(), s.getY()))
						{
							s.setHealAvaiable(false);
							if (!s.isBeingHealed())
							{
								Game.handler.client.addData("heal start:" + s.getPlayerID());
								s.setHealing(true);
							}
						}
						else
						{
							if (s.isBeingHealed())
							{
								Game.handler.client.addData("heal stop:" + s.getPlayerID());
								s.setHealing(false);
							}
							s.setHealAvaiable(true);
						}
					}
					else
					{
						s.setHealAvaiable(false);
					}
				}
				else
				{
					if (s.isBeingHealed())
					{
						Game.handler.client.addData("heal stop:" + s.getPlayerID());
						s.setHealing(false);
					}
					s.setHealAvaiable(false);
				}
			}
		}

		if (Game.handler.generatorsRemaining == 0 && !escaped)
		{
			ArrayList<Gate> gates = Game.handler.getCurrentMap().getGates();
			for (Gate g : gates)
			{
				if (g.isOpened())
				{
					switch (g.getOrientation())
					{
						case North:
							if (g.getY() + 2 >= y && Math.abs(g.getX() - x) < Assets.openGate.getWidth() / 2)
							{
								setControllable(false);
								setPos(x, -150);
								setAngle(0);
								lives = -1;
								escaped = true;
								addLabel();
							}
							break;
						case West:
							if (g.getX() + 2 >= x && Math.abs(g.getY() - y) < Assets.openGate.getWidth() / 2)
							{
								setControllable(false);
								setPos(-150, y);
								setAngle(Math.PI / 2);
								lives = -1;
								escaped = true;
								addLabel();
							}
							break;
						case East:
							if (g.getX() - 2 <= x && Math.abs(g.getY() - y) < Assets.openGate.getWidth() / 2)
							{
								setControllable(false);
								setPos(Game.handler.getCurrentMap().width + 150, y);
								setAngle(-Math.PI / 2);
								lives = -1;
								escaped = true;
								addLabel();
							}
							break;
						case South:
							if (g.getY() - 2 <= y && Math.abs(g.getX() - x) < Assets.openGate.getWidth() / 2)
							{
								setControllable(false);
								setPos(x, Game.handler.getCurrentMap().height + 150);
								setAngle(Math.PI);
								lives = -1;
								escaped = true;
								addLabel();
							}
							break;
					}
				}
			}
		}

		if (beingHealed) onProgress();
	}

	private void addLabel()
	{
		if (Game.handler.isKiller || getPlayerID() != Integer.parseInt(Game.handler.playerID)) return;
		Label l = new Label("You have successfully escaped death!");
		l.setFont(new Font("Segoe UI", 36));
		l.setStyle("-fx-font-weight: bold");
		l.setTextFill(Color.RED);
		Display.addComponentInstant(l);
		l.relocate(Game.handler.getScreenWidth() / 2 - 270, Game.handler.getScreenHeight() / 2 + 100);
	}

	private boolean isInSurvivorRange(double x, double y)
	{
		return Math.abs(MouseManager.getMouseXInWorld() - x) < 25 && Math.abs(MouseManager.getMouseYInWorld() - y) < 25;
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		super.draw(g, camX, camY);
		if (beingHealed)
		{
			g.setFill(Color.YELLOWGREEN);
			g.fillRect(x - 100 - camX, y + Assets.generator.getHeight() - camY, progress * 2, 15);
			g.setStroke(Color.BLACK);
			g.strokeRect(x - 100 - camX, y + Assets.generator.getHeight() - camY, 200, 15);
		}
		if (healAvaiable)
		{
			g.drawImage(Assets.leftBtn, x - Assets.leftBtn.getWidth() / 2 - camX, y + Assets.generator.getHeight() - camY);
		}
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
