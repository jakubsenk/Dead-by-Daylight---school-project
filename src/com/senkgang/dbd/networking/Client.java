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
			socket = new Socket("127.0.0.1", 4000);
			writerChannel = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			readerChannel = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			addData("Spawn survivor");
			send();
			String line;
			boolean spawnRequested = true;

			while ((line = readerChannel.readLine()) != null)
			{
				Launcher.logger.Info(line);
				if (line.startsWith("Spawn response:"))
				{
					Launcher.logger.Info("Got spawn response. Creating survivor...");
					h.getCurrentMap().addSurvivor(line, spawnRequested);
					spawnRequested = false;
				}
				if (line.startsWith("Position update:0"))
				{
					h.getCurrentMap().updateKiller(line);
				}
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
}
