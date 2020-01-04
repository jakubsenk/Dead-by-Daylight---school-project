package com.senkgang.dbd;

import com.senkgang.dbd.display.GameCamera;
import com.senkgang.dbd.map.Map;
import com.senkgang.dbd.networking.Client;
import com.senkgang.dbd.networking.Server;
import com.senkgang.dbd.screens.GameScreen;
import com.senkgang.dbd.screens.LobbyScreen;
import com.senkgang.dbd.screens.Screen;

public class Handler
{
	private Game g;

	public Handler(Game g)
	{
		this.g = g;
	}

	public Game getGame()
	{
		return g;
	}

	public int getScreenWidth()
	{
		return g.getWidth();
	}

	public int getScreenHeight()
	{
		return g.getHeight();
	}

	public GameCamera getGameCamera()
	{
		return g.getGameCamera();
	}

	public Map getCurrentMap()
	{
		if (Screen.getScreen() instanceof GameScreen)
		{
			return ((GameScreen) Screen.getScreen()).getMap();
		}
		return null;
	}

	public LobbyScreen getLobby()
	{
		if (Screen.getScreen() instanceof LobbyScreen)
		{
			return (LobbyScreen) Screen.getScreen();
		}
		return null;
	}

	public boolean isKiller = false;

	public Server server = new Server();
	public Client client = new Client();
	public String playerNick;
	public String playerID;
	public String connectIP;

	public int generatorsRemaining;
}
