package com.senkgang.dbd;

import com.senkgang.dbd.logger.ConsoleLogger;
import com.senkgang.dbd.logger.ILogger;

public class Launcher
{
	public static ILogger logger = new ConsoleLogger();

	public static void main(String[] args)
	{
		logger.Trace("Main started");
		Game g = new Game(1280, 720);
		g.start();
	}
}