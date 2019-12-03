package com.senkgang.dbd;

import com.senkgang.dbd.input.InputManager;
import com.senkgang.dbd.logger.ConsoleLogger;
import com.senkgang.dbd.logger.ILogger;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Launcher extends Application
{
	public static ILogger logger = new ConsoleLogger();
	public static final boolean isDebug = true;
	public static Pane pane;

	public static void main(String[] args)
	{
		logger.Trace("Main started");
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception
	{
		stage.setTitle("Dead by Daylight");

		Canvas c = new Canvas();

		pane = new Pane();
		pane.getChildren().add(c);
		Scene s = new Scene(pane, 1600, 900);
		s.setOnKeyPressed(InputManager.keyPressed);
		s.setOnKeyReleased(InputManager.keyReleased);

		stage.setScene(s);
		c.setWidth(1600);
		c.setHeight(900);

		GraphicsContext gr = c.getGraphicsContext2D();
		Game g = new Game(c);
		new AnimationTimer()
		{
			public void handle(long currentNanoTime)
			{
				g.tick(gr, currentNanoTime);
			}
		}.start();

		s.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent windowEvent)
			{
				g.stop();
			}
		});
		stage.show();
	}
}