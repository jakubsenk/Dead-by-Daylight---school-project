package com.senkgang.dbd.entities;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.entities.player.Survivor;
import com.senkgang.dbd.enums.SurvivorState;
import com.senkgang.dbd.fov.Line;
import com.senkgang.dbd.input.MouseManager;
import com.senkgang.dbd.interfaces.IProgressable;
import com.senkgang.dbd.interfaces.ISightBlocker;
import com.senkgang.dbd.resources.Assets;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Generator extends CollidableEntity implements ISightBlocker, IProgressable
{
	private boolean visible = false;
	private boolean repairing = false;
	private boolean repairingOther = false;
	private boolean repairAvailable = false;

	private double progress = 0;
	private boolean finished = false;

	private final int id;

	public Generator(int id, double x, double y)
	{
		super(x, y);
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setRepairing(boolean repairing)
	{
		this.repairingOther = repairing;
	}

	public boolean isRepairing()
	{
		return repairing;
	}

	public void finish()
	{
		if (Game.handler.isKiller) Game.handler.server.addData("Gen sync:" + id + ";" + 100);
		progress = 100;
		finished = true;
		Game.handler.generatorsRemaining--;
	}

	private boolean isInGeneratorRange()
	{
		return Math.abs(MouseManager.getMouseXInWorld() - x) < Assets.generator.getWidth() / 2 && Math.abs(MouseManager.getMouseYInWorld() - y) < Assets.generator.getHeight() / 2;
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle((int) x - Assets.generator.getWidth() / 2, (int) y - Assets.generator.getHeight() / 2, Assets.generator.getWidth(), Assets.generator.getHeight());
	}

	@Override
	public void update()
	{
		if (!Game.handler.isKiller && Game.handler.getCurrentMap().getPlayer() != null)
		{
			double xDiff = Game.handler.getCurrentMap().getPlayer().getX() - x;
			double yDiff = Game.handler.getCurrentMap().getPlayer().getY() - y;
			double distanceToPlayer = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
			if (distanceToPlayer < 450)
			{
				if (!visible)
				{
					visible = true;
					Game.handler.getCurrentMap().addToSurvivorVisibleEntities(this);
				}
			}
			else
			{
				if (visible)
				{
					visible = false;
					Game.handler.getCurrentMap().removeFromSurvivorVisibleEntities(this);
				}
			}

			if (Game.handler.generatorsRemaining > 0 && MouseManager.leftButtonPressed() && isInGeneratorRange() && distanceToPlayer < 80 && !finished && ((Survivor) Game.handler.getCurrentMap().getPlayer()).getState() != SurvivorState.Dying)
			{
				if (!repairing)
				{
					Game.handler.client.addData("Gen repair start:" + id);
					repairing = true;
				}
				repairAvailable = false;
			}
			else
			{
				repairAvailable = Game.handler.generatorsRemaining > 0 && distanceToPlayer < 80 && !finished && ((Survivor) Game.handler.getCurrentMap().getPlayer()).getState() != SurvivorState.Dying;
				if (repairing && !repairingOther)
				{
					Game.handler.client.addData("Gen repair stop:" + id);
					repairing = false;
				}
			}
		}

		if ((repairing || repairingOther) && !finished)
		{
			onProgress();
		}
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.drawImage(Assets.generator, x - Assets.generator.getWidth() / 2 - camX, y - Assets.generator.getHeight() / 2 - camY);

		if (repairing || repairingOther)
		{
			g.setFill(Color.YELLOWGREEN);
			g.fillRect(x - 100 - camX, y + Assets.generator.getHeight() - camY, progress * 2, 15);
			g.setStroke(Color.BLACK);
			g.strokeRect(x - 100 - camX, y + Assets.generator.getHeight() - camY, 200, 15);
		}
		if (repairAvailable)
		{
			g.drawImage(Assets.leftBtn, x - Assets.leftBtn.getWidth() / 2 - camX, y + Assets.generator.getHeight() - camY);
		}
	}

	@Override
	public ArrayList<Line> getSightBlockingLines()
	{
		ArrayList<Line> ret = new ArrayList<>();

		ret.add(new Line(new Point2D(x - Assets.generator.getWidth() / 2 + 4, y + Assets.generator.getHeight() / 2 - 12), new Point2D(x - Assets.generator.getWidth() / 2 + 4, y - Assets.generator.getHeight() / 2 + 10)));
		ret.add(new Line(new Point2D(x - Assets.generator.getWidth() / 2 + 4, y - Assets.generator.getHeight() / 2 + 10), new Point2D(x + Assets.generator.getWidth() / 2 - 28, y - Assets.generator.getHeight() / 2 + 1)));
		ret.add(new Line(new Point2D(x + Assets.generator.getWidth() / 2 - 28, y - Assets.generator.getHeight() / 2 + 1), new Point2D(x + Assets.generator.getWidth() / 2 - 4, y - Assets.generator.getHeight() / 2 + 13)));
		ret.add(new Line(new Point2D(x + Assets.generator.getWidth() / 2 - 4, y - Assets.generator.getHeight() / 2 + 13), new Point2D(x + Assets.generator.getWidth() / 2 - 4, y + Assets.generator.getHeight() / 2 - 12)));
		ret.add(new Line(new Point2D(x + Assets.generator.getWidth() / 2 - 4, y + Assets.generator.getHeight() / 2 - 12), new Point2D(x - Assets.generator.getWidth() / 2 + 25, y + Assets.generator.getHeight() / 2 - 2)));
		ret.add(new Line(new Point2D(x - Assets.generator.getWidth() / 2 + 25, y + Assets.generator.getHeight() / 2 - 2), new Point2D(x - Assets.generator.getWidth() / 2 + 4, y + Assets.generator.getHeight() / 2 - 12)));

		return ret;
	}

	@Override
	public void onProgress()
	{
		progress += 0.1;
		if (Game.handler.isKiller)
		{
			if (progress % 10 == 0)
			{
				Game.handler.server.addData("Gen sync:" + id + ";" + progress);
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
}