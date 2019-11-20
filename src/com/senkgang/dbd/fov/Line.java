package com.senkgang.dbd.fov;

import java.awt.*;

public class Line
{
	Point start;
	Point end;

	public Line(Point start, Point end)
	{
		this.start = start;
		this.end = end;
	}

	public Point getStart()
	{
		return start;
	}

	public void setStart(Point start)
	{
		this.start = start;
	}

	public Point getEnd()
	{
		return end;
	}

	public void setEnd(Point end)
	{
		this.end = end;
	}

	public String toString()
	{
		return String.format("[%f,%f]-[%f,%f]", start.x, start.y, end.x, end.y);
	}
}
