package com.senkgang.dbd.fov;


import javafx.geometry.Point2D;

public class Line
{
	Point2D start;
	Point2D end;

	public Line(Point2D start, Point2D end)
	{
		this.start = start;
		this.end = end;
	}

	public Point2D getStart()
	{
		return start;
	}

	public void setStart(Point2D start)
	{
		this.start = start;
	}

	public Point2D getEnd()
	{
		return end;
	}

	public void setEnd(Point2D end)
	{
		this.end = end;
	}

	public String toString()
	{
		return String.format("[%f,%f]-[%f,%f]", start.getX(), start.getY(), end.getX(), end.getY());
	}
}
