package com.senkgang.dbd;

import com.senkgang.dbd.display.Display;
import com.senkgang.dbd.logger.ConsoleLogger;
import com.senkgang.dbd.logger.ILogger;

import javafx.application.Application;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;


public class Launcher extends Application
{
	public static ILogger logger = new ConsoleLogger();
	public static final boolean isDebug = true;

	public static void main(String[] args)
	{
		logger.Trace("Main started");
		launch(args);
	}

	@Override
	public void start(Stage stage)
	{
		int width = 1600;
		int height = 900;
		Game g = new Game(width, height);
		GraphicsContext gr = Display.createWindow(stage, width, height, g);
		g.start(gr);
	}
}