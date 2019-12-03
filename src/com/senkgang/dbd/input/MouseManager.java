package com.senkgang.dbd.input;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;


public class MouseManager
{
	private boolean leftBtn;
	private boolean rightBtn;

	private int mouseX;
	private int mouseY;

	public MouseManager(Canvas c)
	{
		c.addEventFilter(MouseEvent.MOUSE_CLICKED, mousePressed);
		c.addEventFilter(MouseEvent.MOUSE_RELEASED, mouseReleased);
		c.addEventFilter(MouseEvent.MOUSE_MOVED, mouseMoved);
		c.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseMoved);
	}

	public boolean leftButtonPressed()
	{
		return leftBtn;
	}

	public boolean rightButtonPressed()
	{
		return rightBtn;
	}

	public int getMouseX()
	{
		return mouseX;
	}

	public int getMouseY()
	{
		return mouseY;
	}

	private EventHandler<MouseEvent> mousePressed = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			if (e.getButton() == MouseButton.PRIMARY) leftBtn = true;
			if (e.getButton() == MouseButton.SECONDARY) rightBtn = true;
		}
	};

	private EventHandler<MouseEvent> mouseReleased = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			if (e.getButton() == MouseButton.PRIMARY) leftBtn = false;
			if (e.getButton() == MouseButton.SECONDARY) rightBtn = false;
		}
	};

	private EventHandler<MouseEvent> mouseMoved = new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent e) {
			mouseX = (int)e.getX();
			mouseY = (int)e.getY();
		}
	};
}
