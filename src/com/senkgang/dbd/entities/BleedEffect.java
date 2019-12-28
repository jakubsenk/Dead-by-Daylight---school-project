package com.senkgang.dbd.entities;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.resources.Assets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;

import java.util.Random;

public class BleedEffect extends Entity
{
	private double ticks;
	private double ticksMax;
	private Image bl;
	private double opacity = 1;
	private BoxBlur effect = null;

	public BleedEffect(double x, double y, int tickDuration)
	{
		super(x, y);
		ticks = tickDuration;
		ticksMax = tickDuration;
		Random r = new Random();
		bl = Assets.bleeds.get(r.nextInt(Assets.bleeds.size()));
	}

	private double map(double x, double in_min, double in_max, double out_min, double out_max)
	{
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

	@Override
	public void update()
	{
		opacity = map(ticks, 0, ticksMax, 0, 1);
		if (ticks < ticksMax / 2)
		{
			effect = new BoxBlur(2, 2, (int)map(ticks, 0, ticksMax / 2, 15, 1));
		}
		if (ticks > 0)
		{
			ticks--;
		}
		else if (ticks == 0)
		{
			Game.handler.getCurrentMap().removeBleedEffect(this);
		}
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.setGlobalAlpha(opacity);
		g.setEffect(effect);
		g.drawImage(bl, x - bl.getWidth() / 2 - camX, y - bl.getHeight() / 2 - camY);
		g.setEffect(null);
		g.setGlobalAlpha(1);
	}
}
