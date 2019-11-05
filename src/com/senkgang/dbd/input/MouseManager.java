package com.senkgang.dbd.input;

import javafx.scene.input.MouseButton;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseManager implements MouseListener, MouseMotionListener
{
	private boolean leftBtn;
	private boolean rightBtn;

	private int mouseX;
	private int mouseY;

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

	@Override
	public void mouseClicked(MouseEvent mouseEvent)
	{

	}

	@Override
	public void mousePressed(MouseEvent mouseEvent)
	{
		if (mouseEvent.getButton() == MouseEvent.BUTTON1) leftBtn = true;
		if (mouseEvent.getButton() == MouseEvent.BUTTON3) rightBtn = true;
	}

	@Override
	public void mouseReleased(MouseEvent mouseEvent)
	{
		if (mouseEvent.getButton() == MouseEvent.BUTTON1) leftBtn = false;
		if (mouseEvent.getButton() == MouseEvent.BUTTON3) rightBtn = false;
	}

	@Override
	public void mouseEntered(MouseEvent mouseEvent)
	{

	}

	@Override
	public void mouseExited(MouseEvent mouseEvent)
	{

	}

	@Override
	public void mouseDragged(MouseEvent mouseEvent)
	{

	}

	@Override
	public void mouseMoved(MouseEvent mouseEvent)
	{
		mouseX = mouseEvent.getX();
		mouseY = mouseEvent.getY();
	}
}
