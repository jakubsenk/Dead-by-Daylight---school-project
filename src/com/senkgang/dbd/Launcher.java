package com.senkgang.dbd;

import com.senkgang.dbd.logger.ConsoleLogger;
import com.senkgang.dbd.interfaces.ILogger;

import javafx.application.Application;
import javafx.stage.Stage;


public class Launcher extends Application
{
	public static ILogger logger = new ConsoleLogger(1);
	public static final boolean isDebug = false;

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
		Game g = new Game(width, height, stage);
		g.start();
	}
}