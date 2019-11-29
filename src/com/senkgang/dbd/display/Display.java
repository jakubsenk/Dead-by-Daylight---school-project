package com.senkgang.dbd.display;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;

public class Display
{

	private JFrame frame;
	private Canvas canvas;
	private int width;
	private int height;

	private boolean noStop = false;

	public Display(int width, int height, Game g)
	{
		this.width = width;
		this.height = height;

		createWindow(g);
	}

	private void createWindow(Game g)
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
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				super.windowClosing(e);
				if (!noStop) g.stop();
			}
		});
		Launcher.logger.Trace("Canvas created");
	}

	public void close()
	{
		noStop = true;
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