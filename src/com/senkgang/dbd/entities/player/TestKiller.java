package com.senkgang.dbd.entities.player;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.entities.CollidableEntity;
import com.senkgang.dbd.entities.ISightBlocker;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class TestKiller extends Killer
{
	private double endX;
	private double endY;

	public TestKiller(int playerID, Handler h, double x, double y, boolean playerControlled, ArrayList<CollidableEntity> entities, ArrayList<ISightBlocker> sightBlocker)
	{
		super(playerID, h, x, y, playerControlled, entities, sightBlocker);
		fov = 130;
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle((int) x - 25, (int) y - 25, 50, 50);
	}

	@Override
	public void update()
	{
		super.update();

		endX = x + 25 * Math.sin(getAngle());
		endY = y + 25 * Math.cos(getAngle());
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		super.draw(g, camX, camY);
		g.setFill(Color.DARKGRAY);
		g.fillOval((int) x - 25 - camX, (int) y - 25 - camY, 50, 50);
		g.setStroke(Color.BLACK);
		g.strokeLine((int) x - camX, (int) y - camY, (int) endX - camX, (int) endY - camY);
	}
}
