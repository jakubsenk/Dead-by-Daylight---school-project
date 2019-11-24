package com.senkgang.dbd.entities.player;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.CollidableEntity;
import com.senkgang.dbd.entities.ISightBlocker;
import com.senkgang.dbd.entities.Player;
import com.senkgang.dbd.fov.Algorithm;
import com.senkgang.dbd.fov.Line;

import java.awt.*;
import java.util.ArrayList;

public abstract class Survivor extends Player
{
	private Algorithm algorithm = new Algorithm(450);
	private ArrayList<Point> points = new ArrayList<>();
	private ArrayList<Line> sceneLines = new ArrayList<>();
	private ArrayList<Line> scanLines = new ArrayList<>();
	private Polygon viewPolygon;

	public Survivor(int playerID, Handler h, double x, double y, boolean playerControlled, ArrayList<CollidableEntity> entities, ArrayList<ISightBlocker> sightBlockers)
	{
		super(playerID, h, x, y, playerControlled, entities, sightBlockers);
	}

	@Override
	public Polygon getViewPolygon()
	{
		return viewPolygon;
	}

	@Override
	public void update()
	{
		super.update();

		scanLines = algorithm.createScanLines(getX(), getY());

		sceneLines = new ArrayList<>();
		for (ISightBlocker sb : sightBlockers)
		{
			sceneLines.addAll(sb.getSightBlockingLines());
		}

		points = algorithm.getIntersectionPoints(scanLines, sceneLines);

		viewPolygon = new Polygon();
		for (Point point : points)
		{
			viewPolygon.addPoint(point.x, point.y);
		}
	}

	@Override
	public void draw(Graphics g, int camX, int camY)
	{
		super.draw(g, camX, camY);
		if (Launcher.isDebug)
		{
			g.setColor(Color.GREEN);

			Polygon p = new Polygon();
			for (Point point : points)
			{
				p.addPoint(point.x - camX, point.y - camY);
			}
			((Graphics2D) g).setStroke(new BasicStroke(5));
			g.drawPolygon(p);
			((Graphics2D) g).setStroke(new BasicStroke());
		}
	}
}
