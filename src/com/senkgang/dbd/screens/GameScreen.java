package com.senkgang.dbd.screens;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.display.Display;
import com.senkgang.dbd.entities.player.Survivor;
import com.senkgang.dbd.map.Map;
import com.senkgang.dbd.map.maps.FarmMap;
import com.senkgang.dbd.map.maps.TestMap;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameScreen extends Screen
{
	private Map map;

	private boolean ended = false;
	private boolean returnToMenu = false;

	public GameScreen()
	{
		map = new FarmMap();
		//map = new TestMap(2500, 1500);
	}

	@Override
	public void update()
	{
		if (returnToMenu)
		{
			Game.handler.client.stop();
			Game.handler.server.stop();
			Game.handler.generatorsRemaining = -1;
			Game.handler.playerID = null;
			Game.handler.playerNick = null;
			Screen.setScreen(new MenuScreen());
			return;
		}

		map.update();

		ArrayList<Survivor> survs = map.getSurvivors();
		if (survs.size() == 0) return;
		int finished = 0;
		int score = 0;
		for (Survivor s : survs)
		{
			if (!s.isAlive())
			{
				finished++;
				score -= s.getLives();
			}
		}
		if (finished == survs.size() && !ended)
		{
			ended = true;
			new Timer().schedule(new TimerTask()
			{
				@Override
				public void run()
				{
					returnToMenu = true;
				}
			}, 5000);
			Label l = new Label();
			if (Game.handler.isKiller)
			{
				switch (score)
				{
					case 0:
						l.setText("Your'e such a badass... kill'em all!");
						break;
					case 1:
						l.setText("Not that bad... could be worse.");
						break;
					case 2:
						l.setText("Well... half of success.");
						break;
					case 3:
						l.setText("Shame on you!");
						break;
					case 4:
						l.setText("Your'e such a noob, go play Minecraft!");
						break;
				}
				l.setFont(new Font("Segoe UI", 36));
				l.setStyle("-fx-font-weight: bold");
				l.setTextFill(Color.RED);
				Display.addComponentInstant(l);
				l.relocate(Game.handler.getScreenWidth() / 2 - 270, Game.handler.getScreenHeight() / 2 + 100);
			}
		}
	}

	@Override
	public void draw(GraphicsContext g)
	{
		map.draw(g, Game.handler.getGameCamera().getxOffset(), Game.handler.getGameCamera().getyOffset());
	}

	public Map getMap()
	{
		return map;
	}
}