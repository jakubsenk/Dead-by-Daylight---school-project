package com.senkgang.dbd.entities;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.entities.player.Killer;
import com.senkgang.dbd.entities.player.Survivor;
import com.senkgang.dbd.enums.SurvivorState;
import com.senkgang.dbd.fov.Line;
import com.senkgang.dbd.input.InputManager;
import com.senkgang.dbd.interfaces.IProgressable;
import com.senkgang.dbd.interfaces.ISightBlocker;
import com.senkgang.dbd.resources.Assets;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Hook extends CollidableEntity implements ISightBlocker, IProgressable
{
	private boolean canPut = false;
	private boolean canRescue = false;
	private boolean rescuing = false;
	private boolean rescuingOther = false;
	private final int id;
	private Survivor hookedSurv = null;

	private double progress = 0;

	public Hook(int id, double x, double y)
	{
		super(x, y);
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public boolean isRescuing()
	{
		return rescuing;
	}

	public void setRescuing(boolean rescuing)
	{
		this.rescuingOther = rescuing;
	}

	public void put(Killer k)
	{
		if (Game.handler.isKiller) Game.handler.server.addData("hook:" + id);
		canPut = false;
		hookedSurv = k.getCarryingSurvivor();
		k.put();
		hookedSurv.setPos(x + 15, y);
		hookedSurv.setState(SurvivorState.Hooked);
		Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x + 15, y, 1000));
		Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x + 20 + 15, y, 1000));
		Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x - 20 + 15, y, 1000));
		Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x + 15, y + 20, 1000));
		Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x + 15, y - 20, 1000));
		Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x + 20 + 15, y + 20, 1000));
		Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x - 20 + 15, y - 20, 1000));
		Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x + 20 + 15, y - 20, 1000));
		Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x - 20 + 15, y + 20, 1000));
	}

	public void finish()
	{
		if (Game.handler.isKiller)
		{
			Game.handler.server.addData("unhook sync:" + id + ";" + 100);
		}
		hookedSurv.unhook();
		hookedSurv = null;
		progress = 0;
		rescuing = false;
		rescuingOther = false;
		canRescue = false;
		canPut = false;
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle(x - Assets.hook.getWidth() / 2, y + Assets.hook.getHeight() / 2 - 10, Assets.hook.getWidth() / 2, 10);
	}

	@Override
	public void update()
	{
		if (Game.handler.isKiller && Game.handler.getCurrentMap().getKiller().isCarrying())
		{
			if (distanceToPlayer() < 60 && hookedSurv == null)
			{
				canPut = true;
				if (InputManager.space)
				{
					put(Game.handler.getCurrentMap().getKiller());
				}
			}
			else
			{
				canPut = false;
			}
		}
		else if (!Game.handler.isKiller && hookedSurv != null)
		{
			Survivor s = (Survivor) Game.handler.getCurrentMap().getPlayer();
			if (s.getState() != SurvivorState.Hooked && s.getState() != SurvivorState.Dying)
			{
				if (distanceToPlayer() < 60)
				{
					canRescue = true;
					if (InputManager.space)
					{
						if (!rescuing) Game.handler.client.addData("unhook start:" + id);
						rescuing = true;
					}
					else
					{
						if (rescuing) Game.handler.client.addData("unhook stop:" + id);
						rescuing = false;
					}
				}
				else
				{
					if (rescuing && !rescuingOther) Game.handler.client.addData("unhook stop:" + id);
					rescuing = false;
					canRescue = false;
				}
			}
		}
		if (rescuing || rescuingOther) onProgress();
	}

	private double distanceToPlayer()
	{
		double xDiff = Game.handler.getCurrentMap().getPlayer().getX() - x - 15;
		double yDiff = Game.handler.getCurrentMap().getPlayer().getY() - y + 15;
		return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.drawImage(Assets.hook, x - Assets.hook.getWidth() / 2 - camX, y - Assets.hook.getHeight() / 2 - camY);
		if (rescuing || rescuingOther)
		{
			g.setFill(Color.YELLOWGREEN);
			g.fillRect(x - 100 - camX, y + Assets.hook.getHeight() - camY, progress * 2, 15);
			g.setStroke(Color.BLACK);
			g.strokeRect(x - 100 - camX, y + Assets.hook.getHeight() - camY, 200, 15);
		}
		if (canPut || canRescue)
		{
			g.drawImage(Assets.space, x - Assets.space.getWidth() / 2 - camX, getBounds().getY() - camY + 15);
		}
	}

	@Override
	public ArrayList<Line> getSightBlockingLines()
	{
		ArrayList<Line> ret = new ArrayList<>();
		ret.add(new Line(new Point2D(x - Assets.hook.getWidth() / 2 + 5, y + Assets.hook.getHeight() / 2), new Point2D(x - Assets.hook.getWidth() / 2 + 5, y + Assets.hook.getHeight() / 2 - 40)));
		ret.add(new Line(new Point2D(x, y + Assets.hook.getHeight() / 2), new Point2D(x, y + Assets.hook.getHeight() / 2 - 40)));
		ret.add(new Line(new Point2D(x - Assets.hook.getWidth() / 2 + 5, y + Assets.hook.getHeight() / 2), new Point2D(x, y + Assets.hook.getHeight() / 2)));
		return ret;
	}

	@Override
	public void onProgress()
	{
		progress += 0.75;
		if (Game.handler.isKiller)
		{
			if (progress % 30 == 0)
			{
				Game.handler.server.addData("unhook sync:" + id + ";" + progress);
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
