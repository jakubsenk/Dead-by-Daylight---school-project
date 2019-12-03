package com.senkgang.dbd.screens;

import com.senkgang.dbd.Handler;

public abstract class GUIScreen extends Screen
{
	public GUIScreen(Handler h)
	{
		super(h);
	}

	public abstract void disposeUIComponents();
}
