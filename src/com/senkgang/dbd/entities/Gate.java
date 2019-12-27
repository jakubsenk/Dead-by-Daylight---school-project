package com.senkgang.dbd.entities;

import com.senkgang.dbd.resources.Assets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class Gate extends CollidableEntity {

	private final int width = 150;
	private final int height = 50;

	public Gate(double x, double y)
	{
		super(x, y);
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle((int) x - width / 2, (int) y - height / 2, width, height);
	}

	@Override
	public void update()
	{

	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.drawImage(Assets.closegate, (int) x - width / 2 - camX, (int) y - height / 2 - camY, width, height);
	}
}
