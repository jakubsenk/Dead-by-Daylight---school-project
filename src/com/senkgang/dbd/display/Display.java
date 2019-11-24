package com.senkgang.dbd.display;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowEvent;

import com.senkgang.dbd.Launcher;

public class Display
{

	private JFrame frame;
	private Canvas canvas;
	private int width;
	private int height;

	public Display(int width, int height)
	{
		this.width = width;
		this.height = height;

		createWindow();
	}

	private void createWindow()
	{
		Launcher.logger.Trace("Creating window " + width + "x" + height);
		frame = new JFrame();
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		Launcher.logger.Trace("Window created");

		Launcher.logger.Trace("Creating canvas");
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(false);

		frame.add(canvas);
		frame.pack();
		Launcher.logger.Trace("Canvas created");
	}

	public void close()
	{
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}

	public Canvas getCanvas()
	{
		return canvas;
	}

	public JFrame getJFrame()
	{
		return frame;
	}
}