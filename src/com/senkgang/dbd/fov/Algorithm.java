package com.senkgang.dbd.fov;

import java.awt.*;
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

		Point scanLine = new Point((int) startX, (int) startY);

		for (double angle = angleStart; angle < angleEnd; angle += step)
		{
			double x = scanLine.x + Math.cos(angle) * scanLineLength;
			double y = scanLine.y + Math.sin(angle) * scanLineLength;

			Line line = new Line(scanLine, new Point((int) x, (int) y));

			scanLines.add(line);

		}

		return scanLines;
	}

	public ArrayList<Point> getIntersectionPoints(ArrayList<Line> scanLines, ArrayList<Line> sceneLines)
	{
		ArrayList<Point> points = new ArrayList<>();

		for (Line scanLine : scanLines)
		{
			ArrayList<Point> intersections = getIntersections(scanLine, sceneLines);

			double x = 0;
			double y = 0;
			double dist = Double.MAX_VALUE;

			// find the intersection that is closest to the scanline
			if (intersections.size() > 0)
			{
				for (Point item : intersections)
				{
					double currDist = dist(scanLine.getStart(), item);

					if (currDist < dist)
					{
						x = item.x;
						y = item.y;

						dist = currDist;
					}
				}

				points.add(new Point((int) x, (int) y));
			}
		}

		return points;
	}

	public ArrayList<Point> getIntersections(Line scanLine, ArrayList<Line> sceneLines)
	{
		ArrayList<Point> list = new ArrayList<>();

		Point intersection;

		for (Line line : sceneLines)
		{
			// check if 2 lines intersect
			intersection = getLineIntersection(scanLine, line);

			// lines intersect => we have an end point
			Point end = null;
			if (intersection != null)
			{
				end = new Point(intersection.x, intersection.y);
			}

			Point start = scanLine.getStart();

			// no intersection found => full scan line length
			if (end == null)
			{
				end = new Point(scanLine.getEnd().x, scanLine.getEnd().y);
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

	private Point getLineIntersection(Line lineA, Line lineB)
	{
		double x1 = lineA.getStart().x;
		double y1 = lineA.getStart().y;
		double x2 = lineA.getEnd().x;
		double y2 = lineA.getEnd().y;

		double x3 = lineB.getStart().x;
		double y3 = lineB.getStart().y;
		double x4 = lineB.getEnd().x;
		double y4 = lineB.getEnd().y;

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

		return new Point((int) (x1 + t * ax), (int) (y1 + t * ay));
	}

	private double dist(Point v1, Point v2)
	{
		double dx = v1.x - v2.x;
		double dy = v1.y - v2.y;
		return Math.sqrt(dx * dx + dy * dy);
	}

	private Point mult(Point v, double n)
	{
		return new Point((int) (v.x * n), (int) (v.y * n));
	}

	public Point normalize(Point p)
	{
		double m = mag(p);
		if (m != 0 && m != 1)
		{
			return div(p, m);
		}
		return p;
	}

	private double mag(Point p)
	{
		return (double) Math.sqrt(p.x * p.x + p.y * p.y);
	}

	private Point div(Point v, double n)
	{
		return new Point((int) (v.x / n), (int) (v.y / n));
	}
}
