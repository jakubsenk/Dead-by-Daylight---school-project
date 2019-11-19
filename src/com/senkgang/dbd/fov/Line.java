package com.senkgang.dbd.fov;

public class Line
{
	Vector start;
	Vector end;

	public Line(Vector start, Vector end)
	{
		this.start = start;
		this.end = end;
	}

	public Vector getStart()
	{
		return start;
	}

	public void setStart(Vector start)
	{
		this.start = start;
	}

	public Vector getEnd()
	{
		return end;
	}

	public void setEnd(Vector end)
	{
		this.end = end;
	}

	public String toString()
	{
		return String.format("[%f,%f]-[%f,%f]", start.x, start.y, end.x, end.y);
	}
}
