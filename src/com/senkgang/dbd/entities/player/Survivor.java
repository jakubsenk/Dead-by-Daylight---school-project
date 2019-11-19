package com.senkgang.dbd.entities.player;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.CollidableEntity;
import com.senkgang.dbd.entities.ISightBlocker;
import com.senkgang.dbd.entities.Player;
import com.senkgang.dbd.fov.Algorithm;
import com.senkgang.dbd.fov.Line;
import com.senkgang.dbd.fov.Vector;

import java.awt.*;
import java.util.ArrayList;

public abstract class Survivor extends Player
{
	private Algorithm algorithm = new Algorithm();
	private Algorithm algorithmEx = new Algorithm(500);
	private ArrayList<Vector> points = new ArrayList<>();
	private ArrayList<Vector> pointsEx = new ArrayList<>();
	private ArrayList<Line> sceneLines = new ArrayList<>();
	private ArrayList<Line> scanLines = new ArrayList<>();
	private ArrayList<Line> scanLinesEx = new ArrayList<>();
	private Polygon viewPolygon;
	private Polygon viewPolygonEx;

	public Survivor(Handler h, double x, double y, ArrayList<CollidableEntity> entities, ArrayList<ISightBlocker> sightBlockers)
	{
		super(h, x, y, entities, sightBlockers);
	}

	public Polygon getViewPolygon()
	{
		return viewPolygon;
	}

	public Polygon getViewPolygonEx()
	{
		return viewPolygonEx;
	}

	@Override
	public void update()
	{
		super.update();

		scanLines = algorithm.createScanLines(getX(), getY());
		scanLinesEx = algorithmEx.createScanLines(getX(), getY());

		sceneLines = new ArrayList<>();
		for (ISightBlocker sb : sightBlockers)
		{
			sceneLines.addAll(sb.getSightBlockingLines());
		}

		double endLeftX = getX() + 500 * Math.sin(getAngle() + Math.PI / 4);
		double endLeftY = getY() + 500 * Math.cos(getAngle() + Math.PI / 4);
		double endRightX = getX() + 500 * Math.sin(getAngle() - Math.PI / 4);
		double endRightY = getY() + 500 * Math.cos(getAngle() - Math.PI / 4);

		points = algorithm.getIntersectionPoints(scanLines, sceneLines);

		sceneLines.add(new Line(new Vector(getX() - 5 * Math.sin(getAngle()), getY() - 5 * Math.cos(getAngle())), new Vector(endLeftX, endLeftY)));
		sceneLines.add(new Line(new Vector(getX() - 5 * Math.sin(getAngle()), getY() - 5 * Math.cos(getAngle())), new Vector(endRightX, endRightY)));

		pointsEx = algorithmEx.getIntersectionPoints(scanLinesEx, sceneLines);

		viewPolygon = new Polygon();
		for (Vector point : points)
		{
			viewPolygon.addPoint((int) point.x, (int) point.y);
		}
		viewPolygonEx = new Polygon();
		for (Vector point : pointsEx)
		{
			viewPolygonEx.addPoint((int) point.x, (int) point.y);
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
			for (Vector point : points)
			{
				p.addPoint((int) point.x - camX, (int) point.y - camY);
			}
			((Graphics2D) g).setStroke(new BasicStroke(5));
			g.drawPolygon(p);

			Polygon p2 = new Polygon();
			for (Vector point : pointsEx)
			{
				p2.addPoint((int) point.x - camX, (int) point.y - camY);
			}

			g.drawPolygon(p2);
		}
	}
}
