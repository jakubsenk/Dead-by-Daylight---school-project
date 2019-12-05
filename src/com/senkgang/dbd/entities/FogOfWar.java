package com.senkgang.dbd.entities;

import com.senkgang.dbd.Handler;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class FogOfWar
{
	private Player pl;
	private Polygon p = new Polygon();
	private Handler h;

	public FogOfWar(Player p, Handler h)
	{
		this.pl = p;
		this.h = h;
	}

	public void draw(GraphicsContext g, int camX, int camY)
	{
		if (pl == null) return;
		g.setFill(new Color(0.5, 0.5, 0.5, 0.5));

		double[] xPl = pl.getViewPolygonX();
		double[] yPl = pl.getViewPolygonY();

		if (xPl == null) return;

		double[] xPol = new double[xPl.length + 6];
		double[] yPol = new double[xPl.length + 6];

		p = new Polygon();
		xPol[0] = 0;
		yPol[0] = 0;
		xPol[1] = 0;
		yPol[1] = h.getGame().getHeight();
		xPol[2] = h.getGame().getWidth();
		yPol[2] = h.getGame().getHeight();
		xPol[3] = h.getGame().getWidth();
		yPol[3] = 0;
		int i = 0;
		for (; i < xPl.length; i++)
		{
			xPol[i + 4] = xPl[i] - camX;
			yPol[i + 4] = yPl[i] - camY;
		}
		xPol[i + 4] = xPl[0] - camX;
		yPol[i + 4] = yPl[0] - camY;
		xPol[i + 5] = h.getGame().getWidth();
		yPol[i + 5] = 0;
		g.fillPolygon(xPol, yPol, xPol.length);

	}
}
