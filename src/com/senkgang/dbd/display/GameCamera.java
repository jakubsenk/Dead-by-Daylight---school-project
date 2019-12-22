package com.senkgang.dbd.display;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.entities.Entity;

public class GameCamera
{
	private int xOffset;
	private int yOffset;

	public GameCamera(int xOffset, int yOffset)
	{
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	public void followEntity(Entity entity)
	{
		if (entity == null) return;
		xOffset = (int) entity.getX() - Game.handler.getScreenWidth() / 2;
		yOffset = (int) entity.getY() - Game.handler.getScreenHeight() / 2;
	}

	public void moveCamera(int x, int y)
	{
		xOffset += x;
		yOffset += y;
	}

	public void setxOffset(int xOffset)
	{
		this.xOffset = xOffset;
	}

	public void setyOffset(int yOffset)
	{
		this.yOffset = yOffset;
	}

	public int getxOffset()
	{
		return xOffset;
	}

	public int getyOffset()
	{
		return yOffset;
	}
}
