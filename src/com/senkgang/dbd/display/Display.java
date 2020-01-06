package com.senkgang.dbd.display;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.input.InputManager;
import com.senkgang.dbd.input.MouseManager;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;

public class Display
{

	private static Pane pane;
	private static Canvas canvas;

	private static ArrayList<Node> currentSceneControls = new ArrayList<>();
	private static ArrayList<Node> nextSceneControls = new ArrayList<>();

	public static GraphicsContext createWindow(Stage stage, int width, int height, Game g)
	{
		pane = new Pane();
		stage.setTitle("Dead by Daylight");

		canvas = new Canvas();

		pane.getChildren().add(canvas);
		Scene s = new Scene(pane, width, height);
		s.setOnKeyPressed(InputManager.keyPressed);
		s.setOnKeyReleased(InputManager.keyReleased);
		MouseManager.attach(canvas);

		stage.setScene(s);
		canvas.setWidth(width);
		canvas.setHeight(height);
		stage.show();

		s.getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent windowEvent)
			{
				g.stop();
			}
		});

		return canvas.getGraphicsContext2D();
	}

	public static void addComponent(Node c)
	{
		nextSceneControls.add(c);
	}

	public static void addComponentInstant(Node c)
	{
		currentSceneControls.add(c);
		Platform.runLater(() ->
		{
			pane.getChildren().add(c);
		});
	}

	public static void removeComponent(Node c)
	{
		if (currentSceneControls.contains(c))
		{
			Platform.runLater(() ->
			{
				pane.getChildren().remove(c);
				currentSceneControls.remove(c);
			});
		}
	}

	public static void updateUIComponents()
	{
		currentSceneControls.forEach(x -> pane.getChildren().remove(x));
		currentSceneControls.clear();
		currentSceneControls = new ArrayList<>(nextSceneControls);
		currentSceneControls.forEach(x -> pane.getChildren().add(x));
		nextSceneControls.clear();
	}

	public static void updateUIComponentsFromThread()
	{
		Platform.runLater(() ->
		{
			currentSceneControls.forEach(x -> pane.getChildren().remove(x));
			currentSceneControls.clear();
			currentSceneControls = new ArrayList<>(nextSceneControls);
			currentSceneControls.forEach(x -> pane.getChildren().add(x));
			nextSceneControls.clear();
		});
	}
}
