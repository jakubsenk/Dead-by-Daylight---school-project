package com.senkgang.dbd.entities.FarmMap;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.entities.Entity;
import com.senkgang.dbd.fov.Line;
import com.senkgang.dbd.interfaces.ISightBlocker;
import com.senkgang.dbd.resources.Assets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class Corn extends Entity implements ISightBlocker
{
	private final int width;
	private final int height;

	public Corn(double x, double y)
	{
		super(x, y);
		this.width = (int) Assets.corn.getWidth();
		this.height = (int) Assets.corn.getHeight();
	}

	@Override
	public void update()
	{
		if (Game.handler.getCurrentMap().getPlayer() == null) return;

		double xDiff = Game.handler.getCurrentMap().getPlayer().getX() - x;
		double yDiff = Game.handler.getCurrentMap().getPlayer().getY() - y;
		double distanceToPlayer = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
		if (Game.handler.isKiller ? distanceToPlayer < 600 : distanceToPlayer < 450)
		{
			Game.handler.getCurrentMap().addToSurvivorVisibleEntities(this);
			Game.handler.getCurrentMap().addToKillerVisibleEntities(this);
		}
		else
		{
			Game.handler.getCurrentMap().removeFromSurvivorVisibleEntities(this);
			Game.handler.getCurrentMap().removeFromKillerVisibleEntities(this);
		}
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.drawImage(Assets.corn, x - width / 2 - camX, y - height / 2 - camY);
	}

	@Override
	public ArrayList<Line> getSightBlockingLines()
	{
		ArrayList<Line> ret = new ArrayList<>();
		ret.add(new Line(new Point2D(x - 5, y + height / 2 - 5), new Point2D(x - 5, y - height / 2 + 5)));
		return ret;
	}
}
