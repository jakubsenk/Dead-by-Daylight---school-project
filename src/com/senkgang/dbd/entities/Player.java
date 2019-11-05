package com.senkgang.dbd.entities;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Handler;

import java.awt.*;

public abstract class Player extends Entity
{

	private Handler handler;

	private int speed = 3;

	public Player(Handler h, double x, double y)
	{
		super(x, y);
		handler = h;
	}

	@Override
	public void update()
	{
		if (handler.getInputManager().up)
		{
			y -= speed;
		}
		if (handler.getInputManager().down)
		{
			y += speed;
		}
		if (handler.getInputManager().left)
		{
			x -= speed;
		}
		if (handler.getInputManager().right)
		{
			x += speed;
		}
	}

	@Override
	public void draw(Graphics g)
	{

	}
}