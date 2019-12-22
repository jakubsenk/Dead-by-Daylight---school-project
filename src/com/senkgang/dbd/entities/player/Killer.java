package com.senkgang.dbd.entities.player;

import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.CollidableEntity;
import com.senkgang.dbd.entities.ISightBlocker;
import com.senkgang.dbd.entities.Player;
import com.senkgang.dbd.fov.Algorithm;
import com.senkgang.dbd.fov.Line;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public abstract class Killer extends Player
{
	private Algorithm algorithm = new Algorithm(600);
	private ArrayList<Point2D> points = new ArrayList<>();
	private ArrayList<Line> sceneLines = new ArrayList<>();
	private ArrayList<Line> scanLines = new ArrayList<>();
	private double[] viewPolygonX;
	private double[] viewPolygonY;

	protected int fov = 90;

	public Killer(int playerID, double x, double y, String nick, boolean playerControlled, ArrayList<CollidableEntity> entities, ArrayList<ISightBlocker> sightBlocker)
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
