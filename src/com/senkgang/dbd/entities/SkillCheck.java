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
	private Function<SkillCheckResult, Object> callback;

	public SkillCheck(double x, double y, Function<SkillCheckResult, Object> onDone)
	{
		super(x, y);
		callback = onDone;
		Random r = new Random();
		angle = r.nextDouble() * (Math.PI + Math.PI / 2 - 1);
	}

	@Override
	public void update()
	{
		if (InputManager.space)
		{
			if (Math.toDegrees(i) < Math.toDegrees(angle) + 10 && i > angle)
			{
				callback.apply(SkillCheckResult.Perfect);
			}
			else if (Math.toDegrees(i) < Math.toDegrees(angle) + 50 && i > angle)
			{
				callback.apply(SkillCheckResult.Normal);
			}
			else
			{
				callback.apply(SkillCheckResult.Bad);
			}
		}
		else if (Math.toDegrees(i) > 270)
		{
			callback.apply(SkillCheckResult.Bad);
		}
		i += 0.0750;
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		g.setFill(Color.WHITE);
		g.fillOval(x - Cwidth / 2 + 1 - camX, y - Cheight / 2 + 1 - camY, Cwidth - 2, Cheight - 2);
		g.setStroke(Color.BLACK);
		g.strokeOval(x - Cwidth / 2 + 10 - camX, y - Cheight / 2 + 10 - camY, Cwidth - 20, Cheight - 20);
		g.strokeOval(x - Cwidth / 2 - camX, y - Cheight / 2 - camY, Cwidth, Cheight);

		g.setFill(Color.BLACK);
		g.fillArc(x - Cwidth / 2 - camX, y - Cheight / 2 - camY, Cwidth, Cheight, -Math.toDegrees(angle) - 10, 10, ArcType.ROUND);
		g.setFill(Color.GREY);
		g.fillArc(x - Cwidth / 2 - camX, y - Cheight / 2 - camY, Cwidth, Cheight, -Math.toDegrees(angle) - 50, 40, ArcType.ROUND);

		g.setFill(Color.WHITE);
		g.fillOval(x - Cwidth / 2 + 11 - camX, y - Cheight / 2 + 11 - camY, Cwidth - 22, Cheight - 22);
		g.drawImage(Assets.space, x - camX - Cwidth / 2 + 35, y - camY - Cheight / 2 + 60, Swidth, Sheight);

		g.setStroke(Color.RED);
		g.strokeLine(x - camX, y - camY, (x - camX) + Cwidth / 2 * Math.cos(i), (y - camY) + Cheight / 2 * Math.sin(i));
	}
}