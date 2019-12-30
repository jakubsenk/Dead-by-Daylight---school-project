package com.senkgang.dbd.entities;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.enums.GateOrientation;
import com.senkgang.dbd.resources.Assets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Gate extends CollidableEntity
{
	private final int width = 150;
	private final int height = 50;
	private GateOrientation gateOrientation;

	private boolean visible = false;

	public Gate(double x, double y, GateOrientation gateOrientation)
	{
		super(x, y);
		this.gateOrientation = gateOrientation;
	}

	@Override
	public Rectangle getBounds()
	{
		if (gateOrientation == GateOrientation.North || gateOrientation == GateOrientation.South)
		{
			return new Rectangle((int) x - width / 2, (int) y - height / 2, width, height);
		}
		else
		{
			if (gateOrientation == GateOrientation.West || gateOrientation == GateOrientation.East)
			{
				return new Rectangle((int) x - height / 2, (int) y - width / 2, height, width);
			}
			else
			{
				Launcher.logger.Error("Some shit happend! GateOrientation is " + gateOrientation);
				return null;
			}
		}
	}


	@Override
	public void update()
	{
		if (!Game.handler.isKiller && Game.handler.getCurrentMap().getPlayer() != null)
		{
			double xDiff = Game.handler.getCurrentMap().getPlayer().getX() - x;
			double yDiff = Game.handler.getCurrentMap().getPlayer().getY() - y;
			double distanceToPlayer = Math.sqrt(xDiff * xDiff + yDiff * yDiff);
			if (distanceToPlayer < 450)
			{
				if (!visible)
				{
					visible = true;
					Game.handler.getCurrentMap().addToSurvivorVisibleEntities(this);
				}
			}
			else
			{
				if (visible)
				{
					visible = false;
					Game.handler.getCurrentMap().removeFromSurvivorVisibleEntities(this);
				}
			}
		}
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		if (gateOrientation == GateOrientation.North)
		{
			g.drawImage(Assets.closeGate, (int) x - width / 2 - camX, (int) y - height / 2 - camY, width, height);
		}
		else
		{
			if (gateOrientation == GateOrientation.South)
			{
				g.save();
				Rotate r = new Rotate(180, x - camX, y - camY);
				g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
				g.drawImage(Assets.closeGate, (int) x - width / 2 - camX, (int) y - height / 2 - camY, width, height);
				g.restore();
			}
			else
			{
				if (gateOrientation == GateOrientation.West)
				{
					g.save();
					Rotate r = new Rotate(270, x - camX, y - camY);
					g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
					g.drawImage(Assets.closeGate, (int) x - width / 2 - camX, (int) y - height / 2 - camY, width, height);
					g.restore();
				}
				else
				{
					if (gateOrientation == GateOrientation.East)
					{
						g.save();
						Rotate r = new Rotate(90, x - camX, y - camY);
						g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
						g.drawImage(Assets.closeGate, (int) x - width / 2 - camX, (int) y - height / 2 - camY, width, height);
						g.restore();
					}
					else
					{
						Launcher.logger.Error("Some shit happend! GateOrientation is " + gateOrientation);
						return;
					}
				}
			}
		}
	}
}
