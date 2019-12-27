package com.senkgang.dbd.screens;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.map.Map;
import com.senkgang.dbd.map.maps.TestMap;

import javafx.scene.canvas.GraphicsContext;

public class GameScreen extends Screen
{
	private Map map;

	public GameScreen()
	{
		map = new TestMap(2500, 1500);
	}

	@Override
	public void update()
	{
		map.update();
	}

	@Override
	public void draw(GraphicsContext g)
	{
		map.draw(g, Game.handler.getGameCamera().getxOffset(), Game.handler.getGameCamera().getyOffset());
	}

	public Map getMap()
	{
		return map;
	}
}