package com.senkgang.dbd.entities;

import com.senkgang.dbd.resources.Assets;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class Gate extends CollidableEntity
{
	private int id;
	private int mawidth;
	private int maheight;
	private final int width = 150;
	private final int height = 50;

	public Gate(double x, double y, int id, int mawidth, int maheight)
	{
		super(x, y);
		this.id = id;
		this.maheight = maheight;
		this.mawidth = mawidth;
	}

	@Override
	public Rectangle getBounds()
	{
		if (x >= 0 && y == 0)
		{
			return new Rectangle((int) x - width / 2, (int) y - height / 2 - 22, width, height);
		}
		else
		{
			if (x >= 0 && y == maheight)
			{
				return new Rectangle((int) x - width / 2, (int) y - height / 2 + 22, width, height);
			}
			else
			{
				if (x == 0 && y >= 0)
				{
					return new Rectangle((int) x - width / 2 + 25, (int) y - height / 2 - 44, height, width);
				}
				else
				{
					return new Rectangle((int) x - width / 2 + 74, (int) y - height / 2 - 44, height, width);
				}
			}
		}
	}

	@Override
	public void update()
	{

	}

	@Override
	public void draw(GraphicsContext g, int camX, int camY)
	{
		if (x >= 0 && y == 0)
		{
			g.drawImage(Assets.closeGate, (int) x - width / 2 - camX, (int) y - height / 2 - camY - 22, width, height);
		}
		else
		{
			if (x >= 0 && y == maheight)
			{
				g.save();
				Rotate r = new Rotate(180, x - camX, y - camY);
				g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
				g.drawImage(Assets.closeGate, (int) x - width / 2 - camX, (int) y - height / 2 - camY - 22, width, height);
				g.restore();
			}
			else
			{
				if (x == 0 && y >= 0)
				{
					g.save();
					Rotate r = new Rotate(270, x - camX, y - camY);
					g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
					g.drawImage(Assets.closeGate, (int) x - width / 2 - camX, (int) y - height / 2 - camY - 22, width, height);
					g.restore();
				}
				else
				{
					g.save();
					Rotate r = new Rotate(90, x - camX, y - camY);
					g.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
					g.drawImage(Assets.closeGate, (int) x - width / 2 - camX, (int) y - height / 2 - camY - 22, width, height);
					g.restore();
				}
			}
		}
	}
}
