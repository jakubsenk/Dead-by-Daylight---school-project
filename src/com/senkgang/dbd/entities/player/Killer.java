package com.senkgang.dbd.entities.player;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.Entity;
import com.senkgang.dbd.enums.SurvivorState;
import com.senkgang.dbd.input.InputManager;
import com.senkgang.dbd.interfaces.ISightBlocker;
import com.senkgang.dbd.entities.Player;
import com.senkgang.dbd.fov.Algorithm;
import com.senkgang.dbd.fov.Line;
import com.senkgang.dbd.input.MouseManager;
import com.senkgang.dbd.resources.Assets;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Killer extends Player
{
	private Algorithm algorithm = new Algorithm(600);
	private ArrayList<Point2D> points = new ArrayList<>();
	private ArrayList<Line> sceneLines = new ArrayList<>();
	private ArrayList<Line> scanLines = new ArrayList<>();
	private double[] viewPolygonX;
	private double[] viewPolygonY;
	private boolean preAttack = false;
	private boolean postAttack = false;
	private boolean canPick = false;
	private boolean carrying = false;
	private Survivor carrySurv = null;
	private Timer attackTimer = new Timer();

	protected boolean attacking = false;
	protected double currentAttackAngle = 0;
	protected int fov = 90;

	public Killer(int playerID, double x, double y, String nick, boolean playerControlled, ArrayList<Entity> entities, ArrayList<ISightBlocker> sightBlocker)
	{
		super(playerID, x, y, nick, playerControlled, entities, sightBlocker);
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

	@Override
	public void update()
	{
		super.update();

		scanLines = algorithm.createScanLines(getX(), getY());

		sceneLines = new ArrayList<>();
		if (sightBlockers != null)
		{
			for (ISightBlocker sb : sightBlockers)
			{
				sceneLines.addAll(sb.getSightBlockingLines());
			}
		}

		double endLeftX = getX() + 500 * Math.sin(getAngle() + Math.toRadians(fov / 2));
		double endLeftY = getY() + 500 * Math.cos(getAngle() + Math.toRadians(fov / 2));
		double endRightX = getX() + 500 * Math.sin(getAngle() - Math.toRadians(fov / 2));
		double endRightY = getY() + 500 * Math.cos(getAngle() - Math.toRadians(fov / 2));

		sceneLines.add(new Line(new Point2D((int) (getX() - 5 * Math.sin(getAngle())), (int) (getY() - 5 * Math.cos(getAngle()))), new Point2D((int) endLeftX, (int) endLeftY)));
		sceneLines.add(new Line(new Point2D((int) (getX() - 5 * Math.sin(getAngle())), (int) (getY() - 5 * Math.cos(getAngle()))), new Point2D((int) endRightX, (int) endRightY)));

		points = algorithm.getIntersectionPoints(scanLines, sceneLines);

		viewPolygonX = new double[points.size()];
		viewPolygonY = new double[points.size()];
		for (int i = 0; i < points.size(); i++)
		{
			viewPolygonX[i] = points.get(i).getX();
			viewPolygonY[i] = points.get(i).getY();
		}

		if (canControl && Game.handler.isKiller && MouseManager.leftButtonPressed())
		{
			if (attack()) Game.handler.server.addData("attack");
		}

		if (preAttack && !attacking)
		{
			attackTimer.schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					preAttack = false;
				}
			}, 250);
			attacking = true;
			currentAttackAngle = 80;
		}
		else if (!preAttack && attacking)
		{
			currentAttackAngle -= 2.5;
			if (currentAttackAngle <= -15)
			{
				if (Game.handler.isKiller)
				{
					double xHit = x + Assets.weapon.getHeight() * Math.sin(getAngle());
					double yHit = y + Assets.weapon.getHeight() * Math.cos(getAngle());
					for (Survivor s : Game.handler.getCurrentMap().getSurvivors())
					{
						double xCenter = s.getX();
						double yCenter = s.getY();
						double xDist = xHit - xCenter < 0 ? xCenter - xHit : xHit - xCenter;
						double yDist = yHit - yCenter < 0 ? yCenter - yHit : yHit - yCenter;
						double distance = Math.sqrt(xDist * xDist + yDist * yDist);

						if (distance < 25)
						{
							Launcher.logger.Info("HIT");
							s.hit();
						}
					}
				}
				postAttack = true;
				attacking = false;
				speed /= 4;
				attackTimer.schedule(new TimerTask()
				{
					@Override
					public void run()
					{
						speed *= 4;
						postAttack = false;
					}
				}, 1500);
			}
		}

		if (canControl && !carrying && Game.handler.getCurrentMap().getSurvivors().stream().anyMatch(x -> x.state == SurvivorState.Dying))
		{
			canPick = false;
			for (int i = 0; i < Game.handler.getCurrentMap().getSurvivors().size(); i++)
			{
				Survivor s = Game.handler.getCurrentMap().getSurvivors().get(i);
				if (s.state != SurvivorState.Dying) continue;
				if (Math.abs(x - s.getX()) < 50 && Math.abs(y - s.getY()) < 50)
				{
					canPick = true;
					if (InputManager.space)
					{
						pick(s);
						break;
					}
				}
			}
		}
		else
		{
			canPick = false;
			if (carrySurv != null)
			{
				carrySurv.setPos(x + 25 * Math.sin(getAngle() + Math.PI / 2), y + 25 * Math.cos(getAngle() + Math.PI / 2));
			}
		}
	}

	public void pick(Survivor s)
	{
		if (Game.handler.isKiller) Game.handler.server.addData("pick:" + s.getPlayerID());
		carrying = true;
		s.setControllable(false);
		carrySurv = s;
	}

	public void put()
	{
		carrySurv = null;
		carrying = false;
	}

	public boolean isCarrying()
	{
		return carrying;
	}

	public Survivor getCarryingSurvivor()
	{
		return carrySurv;
	}

	private boolean isAttacking()
	{
		return preAttack || attacking || postAttack;
	}

	public boolean attack()
	{
		if (isAttacking()) return false;
		preAttack = true;
		return true;
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		super.draw(g, camX, camY);
		if (canPick)
		{
			g.drawImage(Assets.space, x - Assets.space.getWidth() / 2 - camX, y + getBounds().getHeight() - camY);
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
