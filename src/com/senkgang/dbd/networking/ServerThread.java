package com.senkgang.dbd.networking;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerThread extends Thread
{

	private String line = null;
	private Socket s;
	private Server server;

	private final ArrayList<BufferedWriter> connectedSurvivors;

	public ServerThread(Server server, Socket s, ArrayList<BufferedWriter> connectedSurvivors)
	{
		this.s = s;
		this.server = server;
		this.connectedSurvivors = connectedSurvivors;
	}

	public void run()
	{
		try
		{
			BufferedReader readerChannel = new BufferedReader(new InputStreamReader(s.getInputStream()));

			while ((line = readerChannel.readLine()) != null)
			{
				Launcher.logger.Trace(line);
				processRequest(line);
			}

		}
		catch (IOException e)
		{
			Launcher.logger.Exception(e);
		}
		finally
		{
			try
			{
				s.close();
			}
			catch (IOException e)
			{
				Launcher.logger.Exception(e);
			}
		}
	}

	private void processRequest(String line) throws SocketException
	{
		if (line.contains("Connect request:"))
		{
			Launcher.logger.Info("New player wants to join!");
			server.addData("Connected:" + (connectedSurvivors.size()));
			String nick = line.split(":")[1];
			Game.handler.getLobby().connected(Integer.toString(connectedSurvivors.size()), nick);
			server.connectedSurvivorsNicks.add(nick);

			for (int i = 0; i < server.connectedSurvivorsNicks.size(); i++)
			{
				server.addData("Connected player:" + (i + 1) + ";" + server.connectedSurvivorsNicks.get(i));
			}
			server.send();
		}
		else if (line.contains("Position update:"))
		{
			Game.handler.getCurrentMap().updateSurvivor(line);
			server.addData(line); // inform other survivors about survivor movement
		}
		else if (line.contains("Gen repair start:"))
		{
			Game.handler.getCurrentMap().repairGen(line, true);
			server.addData(line);
		}
		else if (line.contains("Gen repair stop:"))
		{
			Game.handler.getCurrentMap().repairGen(line, false);
			server.addData(line);
		}
		else if (line.contains("Gen check:"))
		{
			Game.handler.getCurrentMap().genSkillCheck(line);
		}
		else if (line.contains("unhook start:"))
		{
			Game.handler.getCurrentMap().unhookSurv(line, true);
			server.addData(line);
		}
		else if (line.contains("unhook stop:"))
		{
			Game.handler.getCurrentMap().unhookSurv(line, false);
			server.addData(line);
		}
		else if (line.contains("heal start:"))
		{
			Game.handler.getCurrentMap().healSurv(line, true);
			server.addData(line);
		}
		else if (line.contains("heal stop:"))
		{
			Game.handler.getCurrentMap().healSurv(line, false);
			server.addData(line);
		}
		else if (line.contains("gate open start:"))
		{
			Game.handler.getCurrentMap().openGate(line, true);
			server.addData(line);
		}
		else if (line.contains("gate open stop:"))
		{
			Game.handler.getCurrentMap().openGate(line, false);
			server.addData(line);
		}
		else if (line.equals("READY!"))
		{
			server.playersReady++;
			if (server.playersReady == connectedSurvivors.size())
			{
				try
				{
					sleep(500);
				}
				catch (InterruptedException e)
				{
					Launcher.logger.Exception(e);
				}
				server.addData("START!");
				server.send();
				Game.handler.getCurrentMap().unlockPlayer();
			}
		}
	}
}