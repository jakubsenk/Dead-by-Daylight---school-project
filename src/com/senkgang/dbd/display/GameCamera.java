package com.senkgang.dbd.display;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.entities.Entity;

public class GameCamera
{
	private Handler handler;
	private int xOffset;
	private int yOffset;

	public GameCamera(Handler h, int xOffset, int yOffset)
	{
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		handler = h;
	}

	public void followEntity(Entity entity)
	{
		xOffset = (int)entity.getX() - handler.getScreenWidth() / 2;
		yOffset = (int)entity.getY() - handler.getScreenHeight() / 2;
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
