package com.senkgang.dbd.input;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class InputManager
{
	public static boolean up, down, left, right;
	public static boolean quit;

	public static EventHandler<KeyEvent> keyPressed = new EventHandler<KeyEvent>()
	{
		@Override
		public void handle(KeyEvent e)
		{
			switch (e.getCode())
			{
				case A:
					left = true;
					break;
				case D:
					right = true;
					break;
				case W:
					up = true;
					break;
				case S:
					down = true;
					break;
				case ESCAPE:
					quit = true;
					break;
			}
		}
	};

	public static EventHandler<KeyEvent> keyReleased = new EventHandler<KeyEvent>()
	{
		@Override
		public void handle(KeyEvent e)
		{
			switch (e.getCode())
			{
				case A:
					left = false;
					break;
				case D:
					right = false;
					break;
				case W:
					up = false;
					break;
				case S:
					down = false;
					break;
				case ESCAPE:
					quit = false;
					break;
			}
		}
	};
}