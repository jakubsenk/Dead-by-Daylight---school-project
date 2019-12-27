package com.senkgang.dbd.entities;

import com.senkgang.dbd.resources.Assets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.Random;

public class BleedEffect extends Entity
{
	private int ticks;
	private Image bl;

	public BleedEffect(double x, double y, int tickDuration)
	{
		super(x, y);
		ticks = tickDuration;
		Random r = new Random();
		bl = Assets.bleeds.get(r.nextInt(Assets.bleeds.size()));
	}

	@Override
	public void update()
	{

	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.drawImage(bl, x - bl.getWidth() / 2 - camX, y - bl.getHeight() / 2 - camY);
	}
}
