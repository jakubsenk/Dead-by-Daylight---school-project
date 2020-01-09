package com.senkgang.dbd.entities;

import com.senkgang.dbd.enums.SkillCheckResult;
import com.senkgang.dbd.input.InputManager;
import com.senkgang.dbd.resources.Assets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

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
			if (i > (Math.PI + Math.PI / 2))
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


		g.setFill(Color.WHITE);
		g.fillOval((int) x - Cwidth / 2 + 11 - camX, (int) y - Cheight / 2 + 11 - camY, Cwidth - 22, Cheight - 22);
		g.fillOval((int) x - Cwidth / 2 + 1 - camX, (int) y - Cheight / 2 + 1 - camY, Cwidth - 2, Cheight - 2);
		g.strokeOval((int) x - Cwidth / 2 + 10 - camX, (int) y - Cheight / 2 + 10 - camY, Cwidth - 20, Cheight - 20);
		g.strokeOval((int) x - Cwidth / 2 - camX, (int) y - Cheight / 2 - camY, Cwidth, Cheight);
		g.setFill(Color.BLACK);
		g.drawImage(Assets.space, (int) x - camX - Cwidth / 2 + 35, (int) y - camY - Cheight / 2 + 60, Swidth, Sheight);

		for (double j = angle; j < 2 * Math.PI; j += 0.0001)
		{
			if (j < angle + 0.8 && j > angle)
			{
				g.setStroke(Color.GRAY);
				if (j < angle + 0.2 && j > angle) g.setStroke(Color.BLACK);
				g.strokeLine((x - camX) + (Cwidth / 2 - 10) * Math.cos(j), (y - camY) + (Cheight / 2 - 10) * Math.sin(j), (x - camX) + Cwidth / 2 * Math.cos(j), (y - camY) + Cheight / 2 * Math.sin(j));
			}
		}
		g.setStroke(Color.RED);
		g.strokeLine(x - camX, y - camY, (x - camX) + Cwidth / 2 * Math.cos(i), (y - camY) + Cheight / 2 * Math.sin(i));
		g.setStroke(Color.BLACK);

	}


	public SkillCheckResult getResult()
	{
		return SCP;
	}
}