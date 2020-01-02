package com.senkgang.dbd.networking;

import com.senkgang.dbd.Game;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.BleedEffect;
import com.senkgang.dbd.entities.player.Survivor;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;


public class Client implements Runnable
{
	private Thread thread;
	private Socket socket;
	private BufferedWriter writerChannel;
	private BufferedReader readerChannel;

	private ArrayList<Object> dataToSend = new ArrayList<>();

	public boolean connectFailed = false;

	public void start()
	{
		thread = new Thread(this);
		thread.start();
	}

	public void addData(Object data)
	{
		dataToSend.add(data);
	}

	public void send() throws SocketException
	{
		if (dataToSend.size() == 0) return;
		try
		{
			while (!dataToSend.isEmpty())
			{
				Object o = dataToSend.get(0);
				writerChannel.write(o.toString() + "\n\r");
				dataToSend.remove(0);
			}
			writerChannel.flush();
			dataToSend.clear();
		}
		catch (SocketException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			Launcher.logger.Exception(e);
		}
	}

	@Override
	public void run()
	{
		try
		{
			socket = new Socket(Game.handler.connectIP, 4000);
			writerChannel = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			readerChannel = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			addData("Connect request:" + Game.handler.playerNick);
			send();
			String line;

			while ((line = readerChannel.readLine()) != null)
			{
				Launcher.logger.Trace(line);
				processRequest(line);
			}
		}
		catch (IOException e)
		{
			Launcher.logger.Exception(e);
			connectFailed = true;
		}
	}

	public void stop()
	{
		try
		{
			Launcher.logger.Info("Stopping client...");
			if (thread != null)
			{
				thread.join(3000);
			}
			else
			{
				Launcher.logger.Info("Client is not running!");
			}
			Launcher.logger.Info("Client stopped.");
		}
		catch (InterruptedException e)
		{
			Launcher.logger.Exception(e);
		}
	}

	private void processRequest(String line)
	{
		if (line.startsWith("Connected:") && Game.handler.playerID == null)
		{
			String id = line.split(":")[1];
			Launcher.logger.Info("Connected to killer.");
			Game.handler.getLobby().connected(id);
			Game.handler.playerID = id;
		}
		else if (line.startsWith("Connected player:"))
		{
			String id = line.split(":")[1].split(";")[0];
			String nick = line.split(";")[1];
			Launcher.logger.Info("Got info about existing player.");
			if (Game.handler.playerID.equals(id))
			{
				Launcher.logger.Trace("Got info about myself, do nothing.");
				return;
			}
			Game.handler.getLobby().connectedExisting(id, nick);
		}
		else if (line.startsWith("Position update:0"))
		{
			Game.handler.getCurrentMap().updateKiller(line);
		}
		else if (line.startsWith("Position update:"))
		{
			Game.handler.getCurrentMap().updateSurvivor(line);
		}
		else if (line.startsWith("attack"))
		{
			Game.handler.getCurrentMap().getKiller().attack();
		}
		else if (line.startsWith("hit:"))
		{
			int id = Integer.parseInt(line.split(":")[1]);
			for (Survivor s : Game.handler.getCurrentMap().getSurvivors())
			{
				if (s.getPlayerID() == id) s.hit();
			}
		}
		else if (line.contains("bleed:"))
		{
			double x = Double.parseDouble(line.split(";")[0].split(":")[1]);
			double y = Double.parseDouble(line.split(";")[1]);
			Game.handler.getCurrentMap().addBleedEffect(new BleedEffect(x, y, 1000));
		}
		else if (line.equals("Load game."))
		{
			Game.handler.getLobby().startGame();
		}
		else if (line.equals("START!"))
		{
			Game.handler.getCurrentMap().unlockPlayer();
		}
		else if (line.startsWith("Spawn data:"))
		{
			String id = line.split(":")[1].split(";")[0];
			Game.handler.getCurrentMap().addSurvivor(line, Game.handler.playerID.equals(id));
		}
		else if (line.startsWith("Spawn object;"))
		{
			Game.handler.getCurrentMap().spawnObject(line);
		}
		else if (line.startsWith("Spawn object count"))
		{
			Game.handler.getCurrentMap().setMaxObjCount(Integer.parseInt(line.split(":")[1]));
		}
		else if (line.contains("Gen repair start:"))
		{
			Game.handler.getCurrentMap().repairGen(line, true);
		}
		else if (line.contains("Gen repair stop:"))
		{
			Game.handler.getCurrentMap().repairGen(line, false);
		}
		else if (line.contains("unhook start:"))
		{
			Game.handler.getCurrentMap().unhookSurv(line, true);
		}
		else if (line.contains("unhook stop:"))
		{
			Game.handler.getCurrentMap().unhookSurv(line, false);
		}
		else if (line.contains("heal start:"))
		{
			Game.handler.getCurrentMap().healSurv(line, true);
		}
		else if (line.contains("heal stop:"))
		{
			Game.handler.getCurrentMap().healSurv(line, false);
		}
		else if (line.contains("Gen sync:"))
		{
			Game.handler.getCurrentMap().syncGen(line);
		}
		else if (line.contains("unhook sync:"))
		{
			Game.handler.getCurrentMap().syncUnhooking(line);
		}
		else if (line.contains("heal sync:"))
		{
			Game.handler.getCurrentMap().syncHealing(line);
		}
		else if (line.contains("pick:"))
		{
			Game.handler.getCurrentMap().pickSurv(line);
		}
		else if (line.contains("hook:"))
		{
			Game.handler.getCurrentMap().putSurv(line);
		}
	}
}
