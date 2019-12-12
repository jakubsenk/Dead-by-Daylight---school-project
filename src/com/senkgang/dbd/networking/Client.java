package com.senkgang.dbd.networking;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.Launcher;

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

	private Handler h;

	private ArrayList<Object> dataToSend = new ArrayList<>();

	public boolean connectFailed = false;

	public void start(Handler h)
	{
		this.h = h;
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
			for (Object o : dataToSend)
			{
				writerChannel.write(o.toString() + "\n\r");
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
			socket = new Socket(h.connectIP, 4000);
			writerChannel = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			readerChannel = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			addData("Connect request:" + h.playerNick);
			send();
			String line;

			while ((line = readerChannel.readLine()) != null)
			{
				Launcher.logger.Info(line);
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
		if (line.startsWith("Connected:"))
		{
			String id = line.split(":")[1];
			Launcher.logger.Info("Connected to killer.");
			h.getLobby().connected(id);
			h.playerID = id;
		}
		if (line.startsWith("Position update:0"))
		{
			h.getCurrentMap().updateKiller(line);
		}
		if (line.equals("Game start!"))
		{
			h.getLobby().startGame();
		}
		if (line.startsWith("Spawn data:"))
		{
			String id = line.split(":")[1].split(";")[0];
			h.getCurrentMap().addSurvivor(line, h.playerID.equals(id));
		}
	}
}
