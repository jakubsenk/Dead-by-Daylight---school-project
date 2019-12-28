package com.senkgang.dbd.networking;

import com.senkgang.dbd.Launcher;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Server
{
	private final Server selfRef;

	private ArrayList<ServerThread> threads = new ArrayList<>();

	private ArrayList<Object> dataToSend = new ArrayList<>();

	public ArrayList<BufferedWriter> connectedSurvivors = new ArrayList<>();
	public ArrayList<String> connectedSurvivorsNicks = new ArrayList<>();
	public int playersReady = 0;


	private Runnable work = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				ServerSocket listener = new ServerSocket(4000);
				try
				{
					while (threads.size() < 4)
					{
						Socket socket = listener.accept();
						BufferedWriter writerChannel = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
						connectedSurvivors.add(writerChannel);
						ServerThread st = new ServerThread(selfRef, socket, connectedSurvivors);
						threads.add(st);
						st.start();
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
	};

	public Server()
	{
		selfRef = this;
	}

	public void start()
	{
		Thread thread = new Thread(work);
		thread.start();
	}

	public void stop()
	{
		Launcher.logger.Info("Stopping server...");
		if (threads.size() > 0)
		{
			threads.forEach((t) ->
			{
				try
				{
					t.join(1000);
				}
				catch (InterruptedException e)
				{
					Launcher.logger.Exception(e);
				}
			});
		}
		else
		{
			Launcher.logger.Info("Server is not running!");
		}
		Launcher.logger.Info("Server stopped.");
	}

	public void addData(Object data)
	{
		dataToSend.add(data);
	}

	public void send() throws SocketException
	{
		if (connectedSurvivors == null || connectedSurvivors.size() == 0)
		{
			Launcher.logger.Error("Cant send data becouse there are no clients!");
			return;
		}
		if (dataToSend.size() == 0) return;
		try
		{
			while (!dataToSend.isEmpty()) // iterate this way to prevent java.util.ConcurrentModificationException
			{
				Object o = dataToSend.get(0);
				for (BufferedWriter wc : connectedSurvivors)
				{
					wc.write(o.toString() + "\n\r");
				}
				dataToSend.remove(0);
			}
			for (BufferedWriter wc : connectedSurvivors)
			{
				wc.flush();
			}
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
}