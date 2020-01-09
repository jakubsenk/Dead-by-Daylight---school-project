package com.senkgang.dbd.entities;

import com.senkgang.dbd.enums.SkillCheckResult;
import com.senkgang.dbd.input.InputManager;
import com.senkgang.dbd.resources.Assets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;
import java.util.function.Function;


public class SkillCheck extends Entity
{

	private final int Cwidth = 150;
	private final int Cheight = 150;
	private final int Swidth = 80;
	private final int Sheight = 30;
	private double angle;
	private double i = -(Math.PI / 2);
	private boolean skill = true;
	private Function<SkillCheckResult, Object> callback;

	private SkillCheckResult SCP;

	public SkillCheck(double x, double y, Function<SkillCheckResult, Object> onDone)
	{
		super(x, y);
		callback = onDone;
	}

	@Override
	public void update()
	{
		if (skill)
		{
			Random r = new Random();
			angle = 0 + (r.nextDouble() * ((Math.PI + Math.PI / 2 - 1) - 0));
			skill = false;
		}
		if (InputManager.space)
		{
			skill = true;

			if (i < angle + 0.2 && i > angle)
			{
				SCP = SkillCheckResult.Perfect;
			}
			else if (i < angle + 0.8 && i > angle)
			{
				SCP = SkillCheckResult.Normal;
			}
			else
			{
				SCP = SkillCheckResult.Bad;
			}
			i = -(Math.PI / 2);
			callback.apply(SCP);
		}
		else
		{
			if (i > (Math.PI + Math.PI / 2) + (2 * Math.PI))
			{
				skill = true;
				i = -(Math.PI / 2);
			}
		}
		i += .0200;
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.setFill(Color.BLACK);
		g.fillOval((int) x - camX, (int) y - camY, Cwidth, Cheight);
		g.fillOval((int) x - camX + 15, (int) y - camY + 15, Cwidth - 20, Cheight - 20);
		g.setFill(Color.WHITE);
		g.fillOval((int) x - camX + 15, (int) y - camY + 15, Cwidth - 30, Cheight - 30);
		g.setFill(Color.BLACK);
		g.drawImage(Assets.space, (int) x - camX + 35, (int) y - camY + 60, Swidth, Sheight);

		for (double j = 0; j < 2 * Math.PI; j += 0.0001)
		{
			if (j < angle + 0.8 && j > angle)
			{
				g.setStroke(Color.GRAY);
				if (j < angle + 0.2 && j > angle) g.setStroke(Color.BLACK);
			}
			else
			{
				g.setStroke(Color.WHITE);
			}
			g.strokeLine((x + 75 - camX) + 65 * Math.cos(j), (y + 75 - camY) + 65 * Math.sin(j), (x + 75 - camX) + 70 * Math.cos(j), (y + 75 - camY) + 70 * Math.sin(j));
		}

		g.setStroke(Color.RED);
		g.strokeLine(x + 75 - camX, y + 75 - camY, (x + 75 - camX) + 75 * Math.cos(i), (y + 75 - camY) + 75 * Math.sin(i));
		g.setStroke(Color.BLACK);
	}

	public SkillCheckResult getResult()
	{
		return SCP;
	}
}