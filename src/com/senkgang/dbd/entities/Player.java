package com.senkgang.dbd.entities;

import com.senkgang.dbd.Game;

public abstract class Player extends Entity
{

	private Game game;

	private int speed = 3;

	public Player(Game game, double x, double y)
	{
		super(x, y);
		this.game = game;
	}

	@Override
	public void update()
	{
		if (game.getInputManager().up)
		{
			y -= speed;
		}
		if (game.getInputManager().down)
		{
			y += speed;
		}
		if (game.getInputManager().left)
		{
			x -= speed;
		}
		if (game.getInputManager().right)
		{
			x += speed;
		}
	}
}