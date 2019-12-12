package com.senkgang.dbd.networking;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.Launcher;
import com.senkgang.dbd.entities.player.Survivor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server implements Runnable
{
	private Thread thread;
	private Handler h;

	private ArrayList<Object> dataToSend = new ArrayList<>();

	private BufferedWriter writerChannel;

	public ArrayList<String> connectedSurvivors = new ArrayList<>();

	public void start(Handler h)
	{
		this.h = h;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run()
	{
		try
		{
			ServerSocket listener = new ServerSocket(4000);
			String line;
			try
			{
				Socket socket = listener.accept();
				BufferedReader readerChannel = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writerChannel = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				try
				{
					while ((line = readerChannel.readLine()) != null)
					{
						Launcher.logger.Info(line);
						processRequest(line);
					}
				}
				finally
				{
					socket.close();
				}
			}
			finally
			{
				listener.close();
			}
		}
		catch (IOException e)
		{
			Launcher.logger.Exception(e);
		}
	}

	public void stop()
	{
		try
		{
			Launcher.logger.Info("Stopping server...");
			if (thread != null)
			{
				thread.join(3000);
			}
			else
			{
				Launcher.logger.Info("Server is not running!");
			}
			Launcher.logger.Info("Server stopped.");

		}
		catch (InterruptedException e)
		{
			Launcher.logger.Exception(e);
		}
	}

	public void addData(Object data)
	{
		dataToSend.add(data);
	}

	public void send() throws SocketException
	{
		if (writerChannel == null)
		{
			Launcher.logger.Error("Cant send data becouse writerChannel is null!");
			return;
		}
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

	private void processRequest(String line) throws SocketException
	{
		if (line.contains("Connect request:"))
		{
			Launcher.logger.Info("New player wants to join!");
			addData("Connected:" + (connectedSurvivors.size() + 1));
			String nick = line.split(":")[1];
			h.getLobby().connected(Integer.toString(connectedSurvivors.size() + 1), nick);
			connectedSurvivors.add(nick);
			send();
		}
		if (line.contains("Position update:"))
		{
			h.getCurrentMap().updateSurvivor(line);
		}
	}
}