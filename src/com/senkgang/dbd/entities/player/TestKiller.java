package com.senkgang.dbd.entities.player;

import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.Entity;
import com.senkgang.dbd.interfaces.ISightBlocker;
import com.senkgang.dbd.resources.Assets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;

public class TestKiller extends Killer
{
	private double endX;
	private double endY;

	public TestKiller(int playerID, double x, double y, String nick, boolean playerControlled, ArrayList<Entity> entities, ArrayList<ISightBlocker> sightBlocker)
	{
		super(playerID, x, y, nick, playerControlled, entities, sightBlocker);
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

		if (attacking)
		{
			g.save();
			Rotate r = new Rotate(Math.toDegrees(getAngle() + Math.PI) * -1 + currentAttackAngle, x - camX, y - camY);
			g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
			g.drawImage(Assets.weapon, (int) x + Assets.weapon.getWidth() / 2 - camX, (int) y - Assets.weapon.getHeight() - camY);
			g.restore();

			if (Launcher.isDebug) // hit point
			{
				double xHit = x + Assets.weapon.getHeight() * Math.sin(getAngle());
				double yHit = y + Assets.weapon.getHeight() * Math.cos(getAngle());
				g.strokeOval(xHit - camX - 2, yHit - camY - 2, 4, 4);
			}
		}
	}
}
