package com.senkgang.dbd.screens;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.display.Display;
import com.senkgang.dbd.resources.Assets;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import javax.swing.*;
import java.net.URL;

public class IntroScreen extends Screen
{
	private MediaView viewer;
	private MediaPlayer player;
	private boolean loading = true;

	public IntroScreen()
	{
		URL u = Thread.currentThread().getContextClassLoader().getResource("com/senkgang/dbd/resources/intro.mp4");
		if (u == null)
		{
			JOptionPane.showMessageDialog(null, "Can't find intro file.", "ERROR initializing assets", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Media media = new Media(u.toString());

		player = new MediaPlayer(media);
		viewer = new MediaView(player);
		Display.addComponent(viewer);
		viewer.setX(3);
		viewer.setOnMouseClicked(mouseEvent ->
		{
			disposeVideo();
			Screen.setScreen(new MenuScreen());
		});
		player.play();
		player.setOnEndOfMedia(this::disposeVideo);

	}

	private void disposeVideo()
	{
		if (!loading) return;
		loading = false;
		player.stop();
		player.dispose();
	}

	@Override
	public void update()
	{
	}

	@Override
	public void draw(GraphicsContext g)
	{
		g.setFill(Color.BLACK);
		g.fillRect(0, 0, Game.handler.getScreenWidth(), Game.handler.getScreenHeight());
		if (loading)
		{
			g.save();
			g.setFill(Color.WHITE);
			g.setFont(new Font("Segoe UI", 36));
			g.fillText("Loading...", Game.handler.getScreenWidth() / 2 - 80, Game.handler.getScreenHeight() / 2);
			g.restore();
		}
		else
		{
			g.drawImage(Assets.introLogo, 0, 0);
		}
	}

}
