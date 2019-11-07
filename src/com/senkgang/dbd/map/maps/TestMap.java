package com.senkgang.dbd.map.maps;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.entities.CollidableEntity;
import com.senkgang.dbd.entities.Entity;
import com.senkgang.dbd.entities.Player;
import com.senkgang.dbd.entities.Wall;
import com.senkgang.dbd.entities.player.TestPlayer;
import com.senkgang.dbd.map.Map;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class TestMap extends Map
{
	private ArrayList<CollidableEntity> entities;
	private Player player;
	private Handler handler;


	public TestMap(Handler h, int width, int height)
	{
		super(width, height);
		handler = h;
		entities = new ArrayList<CollidableEntity>();
		Random r = new Random();
		for (int i = 0; i < 100; i++)
		{
			entities.add(new Wall(r.nextInt(width), r.nextInt(height), 30, 30));
		}

		player = new TestPlayer(handler, 200, 200, entities);
	}

	@Override
	public void update()
	{
		for (Entity e : entities)
		{
			e.update();
		}
		player.update();
		handler.getGameCamera().followEntity(player);
	}

	@Override
	public void draw(Graphics g, int camX, int camY)
	{
		g.drawLine(0 - camX, 0 - camY, 0 - camX, height - camY);
		g.drawLine(0 - camX, 0 - camY, width - camX, 0 - camY);
		g.drawLine(0 - camX, height - camY, width - camX, height - camY);
		g.drawLine(width - camX, 0 - camY, width - camX, height - camY);

		for (Entity e : entities)
		{
			e.draw(g, camX, camY);
		}
		player.draw(g, camX, camY);
	}
}
