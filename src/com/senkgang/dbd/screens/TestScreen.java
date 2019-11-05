package com.senkgang.dbd.screens;

import java.awt.Graphics;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Handler;
import com.senkgang.dbd.entities.Player;
import com.senkgang.dbd.entities.player.TestPlayer;;

public class TestScreen extends Screen
{
	private Player p;

	public TestScreen(Handler h)
	{
		p = new TestPlayer(h, 100, 100);
	}

	@Override
	public void update()
	{
		p.update();
	}

	@Override
	public void draw(Graphics g)
	{
		p.draw(g);
	}

}