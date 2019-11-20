package com.senkgang.dbd.entities;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.entities.player.Killer;
import com.senkgang.dbd.entities.player.Survivor;

import java.awt.*;

public class FogOfWar
{
	private Survivor s;
	private Killer k;
	private Polygon p = new Polygon();
	private Handler h;

	public FogOfWar(Survivor s, Handler h)
	{
		this.s = s;
		this.h = h;
	}

	public FogOfWar(Killer k, Handler h)
	{
		this.k = k;
		this.h = h;
	}

	public void draw(Graphics g, int camX, int camY)
	{

		g.setColor(new Color(50, 50, 50, 100));
		Polygon pol;
		if (s != null)
		{
			pol = s.getViewPolygon();
		}
		else
		{
			pol = k.getViewPolygon();
		}
		if (pol == null) return;

		p = new Polygon();
		p.addPoint(0, 0);
		p.addPoint(0, h.getGame().getHeight());
		p.addPoint(h.getGame().getWidth(), h.getGame().getHeight());
		p.addPoint(h.getGame().getWidth(), 0);
		for (int i = 0; i < pol.npoints; i++)
		{
			p.addPoint(pol.xpoints[i] - camX, pol.ypoints[i] - camY);
		}
		p.addPoint(pol.xpoints[0] - camX, pol.ypoints[0] - camY);
		p.addPoint(h.getGame().getWidth(), 0);
		g.fillPolygon(p);

	}
}
