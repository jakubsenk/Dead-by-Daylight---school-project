package com.senkgang.dbd.entities.player;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.entities.Player;

import java.awt.Color;
import java.awt.Graphics;

public class TestPlayer extends Player
{
	private Color c;

	public TestPlayer(Game game, double x, double y)
	{
		super(game, x, y);
		c = Color.gray;
	}

	public void update()
	{
		super.update();
		if (x > 500)
		{
			c = Color.red;
		}
		else if (x > 300)
		{
			c = Color.blue;
		}
		else if (x > 100) c = Color.black;
	}

	public void draw(Graphics g)
	{
		g.setColor(c);
		g.fillRect((int) x, (int) y, 50, 50);
	}
}