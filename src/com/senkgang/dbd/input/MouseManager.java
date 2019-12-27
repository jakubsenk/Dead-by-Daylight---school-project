package com.senkgang.dbd.input;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class MouseManager
{
	private static boolean leftBtn;
	private static boolean rightBtn;

	private static int mouseX;
	private static int mouseY;

	public static void attach(Canvas c)
	{
		c.addEventFilter(MouseEvent.MOUSE_PRESSED, mousePressed);
		c.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleased);
		c.addEventFilter(MouseEvent.MOUSE_MOVED, mouseMoved);
		c.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseMoved);
	}

	public static boolean leftButtonPressed()
	{
		return leftBtn;
	}

	public static boolean rightButtonPressed()
	{
		return rightBtn;
	}

	public static int getMouseX()
	{
		return mouseX;
	}

	public static int getMouseY()
	{
		return mouseY;
	}

	private static EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>()
	{
		@Override
		public void handle(MouseEvent e)
		{
			if (e.getButton() == MouseButton.PRIMARY) leftBtn = true;
			if (e.getButton() == MouseButton.SECONDARY) rightBtn = true;
		}
	};

	private static EventHandler<MouseEvent> mouseReleased = new EventHandler<MouseEvent>()
	{
		@Override
		public void handle(MouseEvent e)
		{
			if (e.getButton() == MouseButton.PRIMARY) leftBtn = false;
			if (e.getButton() == MouseButton.SECONDARY) rightBtn = false;
		}
	};

	private static EventHandler<MouseEvent> mouseMoved = new EventHandler<MouseEvent>()
	{
		@Override
		public void handle(MouseEvent e)
		{
			mouseX = (int) e.getX();
			mouseY = (int) e.getY();
		}
	};
}
