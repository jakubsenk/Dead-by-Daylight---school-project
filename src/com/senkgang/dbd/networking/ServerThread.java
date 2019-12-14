package com.senkgang.dbd.networking;

import com.senkgang.dbd.Handler;
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

	private Handler h;
	private final ArrayList<BufferedWriter> connectedSurvivors;

	public ServerThread(Server server, Socket s, Handler h, ArrayList<BufferedWriter> connectedSurvivors)
	{
		this.s = s;
		this.server = server;
		this.h = h;
		this.connectedSurvivors = connectedSurvivors;
	}

	public void run()
	{
		try
		{
			BufferedReader readerChannel = new BufferedReader(new InputStreamReader(s.getInputStream()));
			//writerChannel = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

			while ((line = readerChannel.readLine()) != null)
			{
				Launcher.logger.Info(line);
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
			h.getLobby().connected(Integer.toString(connectedSurvivors.size()), nick);
			server.connectedSurvivorsNicks.add(nick);

			for (int i = 0; i < server.connectedSurvivorsNicks.size(); i++)
			{
				server.addData("Connected player:" + (i + 1) + ";" + nick);
			}
			server.send();
		}
		else if (line.contains("Position update:"))
		{
			h.getCurrentMap().updateSurvivor(line);
			server.addData(line); // inform other survivors about survivor movement
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
			}
			server.send();
			h.getCurrentMap().unlockPlayer();
		}
	}
}