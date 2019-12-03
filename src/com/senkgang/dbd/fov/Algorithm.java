package com.senkgang.dbd.fov;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Algorithm
{
	private final int scanLineLength;
	private final int scanLineCount;

	public Algorithm()
	{
		this(200, 500);
	}

	public Algorithm(int scanLineLength)
	{
		this(scanLineLength, 500);
	}

	public Algorithm(int scanLineLength, int scanLineCount)
	{
		this.scanLineLength = scanLineLength;
		this.scanLineCount = scanLineCount;
	}

	public ArrayList<Line> createScanLines(double startX, double startY)
	{
		ArrayList<Line> scanLines;

		double angleStart = 0;
		double angleEnd = Math.PI * 2;
		double step = Math.PI / scanLineCount;

		scanLines = new ArrayList<>();

		Point2D scanLine = new Point2D(startX, startY);

		for (double angle = angleStart; angle < angleEnd; angle += step)
		{
			double x = scanLine.getX() + Math.cos(angle) * scanLineLength;
			double y = scanLine.getY() + Math.sin(angle) * scanLineLength;

			Line line = new Line(scanLine, new Point2D(x, y));

			scanLines.add(line);

		}

		return scanLines;
	}

	public ArrayList<Point2D> getIntersectionPoints(ArrayList<Line> scanLines, ArrayList<Line> sceneLines)
	{
		ArrayList<Point2D> Point2Ds = new ArrayList<>();

		for (Line scanLine : scanLines)
		{
			ArrayList<Point2D> intersections = getIntersections(scanLine, sceneLines);

			double x = 0;
			double y = 0;
			double dist = Double.MAX_VALUE;

			// find the intersection that is closest to the scanline
			if (intersections.size() > 0)
			{
				for (Point2D item : intersections)
				{
					double currDist = dist(scanLine.getStart(), item);

					if (currDist < dist)
					{
						x = item.getX();
						y = item.getY();

						dist = currDist;
					}
				}

				Point2Ds.add(new Point2D((int) x, (int) y));
			}
		}

		return Point2Ds;
	}

	public ArrayList<Point2D> getIntersections(Line scanLine, ArrayList<Line> sceneLines)
	{
		ArrayList<Point2D> list = new ArrayList<>();

		Point2D intersection;

		for (Line line : sceneLines)
		{
			// check if 2 lines intersect
			intersection = getLineIntersection(scanLine, line);

			// lines intersect => we have an end Point2D
			Point2D end = null;
			if (intersection != null)
			{
				end = new Point2D(intersection.getX(), intersection.getY());
			}

			Point2D start = scanLine.getStart();

			// no intersection found => full scan line length
			if (end == null)
			{
				end = new Point2D(scanLine.getEnd().getX(), scanLine.getEnd().getY());
			}
			// intersection found => limit to scan line length
			else if (dist(start, end) > scanLineLength)
			{
				end = normalize(end);
				end = mult(end, scanLineLength);
			}


			// we have a valid line end, either an intersection with another line or we have the scan line limit
			if (end != null)
			{
				list.add(end);
			}
		}
		return list;
	}

	private Point2D getLineIntersection(Line lineA, Line lineB)
	{
		double x1 = lineA.getStart().getX();
		double y1 = lineA.getStart().getY();
		double x2 = lineA.getEnd().getX();
		double y2 = lineA.getEnd().getY();

		double x3 = lineB.getStart().getX();
		double y3 = lineB.getStart().getY();
		double x4 = lineB.getEnd().getX();
		double y4 = lineB.getEnd().getY();

		double ax = x2 - x1;
		double ay = y2 - y1;
		double bx = x4 - x3;
		double by = y4 - y3;

		double denominator = ax * by - ay * bx;

		if (denominator == 0) return null;

		double cx = x3 - x1;
		double cy = y3 - y1;

		double t = (cx * by - cy * bx) / denominator;
		if (t < 0 || t > 1) return null;

		double u = (cx * ay - cy * ax) / denominator;
		if (u < 0 || u > 1) return null;

		return new Point2D((int) (x1 + t * ax), (int) (y1 + t * ay));
	}

	private double dist(Point2D v1, Point2D v2)
	{
		double dx = v1.getX() - v2.getX();
		double dy = v1.getY() - v2.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	private Point2D mult(Point2D v, double n)
	{
		return new Point2D((int) (v.getX() * n), (int) (v.getY() * n));
	}

	public Point2D normalize(Point2D p)
	{
		double m = mag(p);
		if (m != 0 && m != 1)
		{
			return div(p, m);
		}
		return p;
	}

	private double mag(Point2D p)
	{
		return (double) Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
	}

	private Point2D div(Point2D v, double n)
	{
		return new Point2D((int) (v.getX() / n), (int) (v.getY() / n));
	}
}
