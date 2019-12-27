package com.senkgang.dbd.entities;

import javafx.scene.shape.Rectangle;

public abstract class CollidableEntity extends Entity
{

	public CollidableEntity(double x, double y)
	{
		super(x, y);
	}

	public abstract Rectangle getBounds();

}
