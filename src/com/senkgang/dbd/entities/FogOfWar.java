package com.senkgang.dbd.entities;

import com.senkgang.dbd.entities.player.Survivor;

import java.awt.*;

public class FogOfWar extends Entity
{
	private Survivor s;
	private boolean isHidden;

	public FogOfWar(double x, double y, Survivor s)
	{
		super(x, y);
		this.s = s;
	}

	@Override
	public void update()
	{
		isHidden = s.getViewPolygon().contains(x, y) || s.getViewPolygonEx().contains(x, y);
	}

	@Override
	public void draw(Graphics g, int camX, int camY)
	{
		if (!isHidden)
		{
			g.setColor(new Color(50, 50, 50, 100));
			g.fillRect((int) x - 5 - camX, (int) y - 5 - camY, 10, 10);
		}
	}
}
