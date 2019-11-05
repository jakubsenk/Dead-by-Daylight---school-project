package com.senkgang.dbd.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.senkgang.dbd.Launcher;

public class InputManager implements KeyListener
{
	public boolean up, down, left, right;

	public InputManager()
	{

	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		Launcher.logger.Info("key pressed");
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_A:
				left = true;
				break;
			case KeyEvent.VK_D:
				right = true;
				break;
			case KeyEvent.VK_W:
				up = true;
				break;
			case KeyEvent.VK_S:
				down = true;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_A:
				left = false;
				break;
			case KeyEvent.VK_D:
				right = false;
				break;
			case KeyEvent.VK_W:
				up = false;
				break;
			case KeyEvent.VK_S:
				down = false;
				break;
		}
	}
}