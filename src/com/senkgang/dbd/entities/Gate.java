package com.senkgang.dbd.entities;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.player.Survivor;
import com.senkgang.dbd.enums.GateOrientation;
import com.senkgang.dbd.enums.SurvivorState;
import com.senkgang.dbd.input.MouseManager;
import com.senkgang.dbd.interfaces.IProgressable;
import com.senkgang.dbd.resources.Assets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Gate extends CollidableEntity implements IProgressable
{
	private final int width;
	private final int height;
	private GateOrientation gateOrientation;

	private double progress = 0;

	private boolean visible = false;
	private boolean opening = false;
	private boolean openingOther = false;
	private boolean openAvaiable = false;
	private boolean finished = false;

	private final int id;

	public Gate(int id, double x, double y, GateOrientation gateOrientation)
	{
		super(x, y);
		this.gateOrientation = gateOrientation;
		this.id = id;
		width = (int) Assets.closeGate.getWidth();
		height = (int) Assets.closeGate.getHeight();
	}

	public int getId()
	{
		return id;
	}

	public boolean isOpening()
	{
		return opening;
	}

	public void setOpening(boolean opening)
	{
		this.openingOther = opening;
	}

	@Override
	public Rectangle getBounds()
	{
		if (finished) return null;
		if (gateOrientation == GateOrientation.North || gateOrientation == GateOrientation.South)
		{
			return new Rectangle((int) x - width / 2, (int) y - height / 2, width, height);
		}
		else if (gateOrientation == GateOrientation.West || gateOrientation == GateOrientation.East)
		{
			return new Rectangle((int) x - height / 2, (int) y - width / 2, height, width);
		}
		else
		{
			Launcher.logger.Error("Some shit happend! GateOrientation is " + gateOrientation);
			return null;
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

			if (Game.handler.generatorsRemaining == 0 && MouseManager.leftButtonPressed() && isInRange() && distanceToPlayer < 80 && !finished && ((Survivor) Game.handler.getCurrentMap().getPlayer()).getState() != SurvivorState.Dying)
			{
				if (!opening)
				{
					Game.handler.client.addData("gate open start:" + id);
					opening = true;
				}
				openAvaiable = false;
			}
			else
			{
				openAvaiable = Game.handler.generatorsRemaining == 0 && distanceToPlayer < 80 && !finished && ((Survivor) Game.handler.getCurrentMap().getPlayer()).getState() != SurvivorState.Dying;
				if (opening && !openingOther)
				{
					Game.handler.client.addData("gate open stop:" + id);
					opening = false;
				}
			}
		}

		if ((opening || openingOther) && !finished)
		{
			onProgress();
		}
	}

	private boolean isInRange()
	{
		return Math.abs(MouseManager.getMouseXInWorld() - x) < Assets.closeGate.getHeight() && Math.abs(MouseManager.getMouseYInWorld() - y) < Assets.closeGate.getHeight();
	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		if (gateOrientation == GateOrientation.North)
		{
			g.drawImage(finished ? Assets.openGate : Assets.closeGate, x - width / 2 - camX, y - height / 2 - camY);
		}
		else
		{
			g.save();
			Rotate r;
			if (gateOrientation == GateOrientation.South)
			{
				r = new Rotate(180, x - camX, y - camY);
			}
			else if (gateOrientation == GateOrientation.West)
			{
				r = new Rotate(270, x - camX, y - camY);
			}
			else if (gateOrientation == GateOrientation.East)
			{
				r = new Rotate(90, x - camX, y - camY);
			}
			else
			{
				Launcher.logger.Error("Some shit happend! GateOrientation is " + gateOrientation);
				return;
			}
			g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
			g.drawImage(finished ? Assets.openGate : Assets.closeGate, x - width / 2 - camX, y - height / 2 - camY);
			g.restore();
		}

		if (opening || openingOther)
		{
			g.setFill(Color.YELLOWGREEN);
			g.fillRect(x - 100 - camX, y + Assets.closeGate.getWidth() - camY, progress * 2, 15);
			g.setStroke(Color.BLACK);
			g.strokeRect(x - 100 - camX, y + Assets.closeGate.getWidth() - camY, 200, 15);
		}
		if (openAvaiable)
		{
			g.drawImage(Assets.leftBtn, x - Assets.leftBtn.getWidth() / 2 - camX, y + Assets.closeGate.getWidth() - camY);
		}
	}

	public void finish()
	{
		if (Game.handler.isKiller) Game.handler.server.addData("gate sync:" + id + ";" + 100);
		progress = 100;
		finished = true;
	}

	@Override
	public void onProgress()
	{
		progress += 0.2;
		if (Game.handler.isKiller)
		{
			if (progress % 30 == 0)
			{
				Game.handler.server.addData("gate sync:" + id + ";" + progress);
			}
			if (progress >= 100) finish();
		}
	}

	@Override
	public double getProgress()
	{
		return progress;
	}

	@Override
	public void setProgress(double progress)
	{
		this.progress = progress;
	}
}
