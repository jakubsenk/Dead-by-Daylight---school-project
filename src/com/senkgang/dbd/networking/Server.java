package com.senkgang.dbd.networking;

import com.senkgang.dbd.Handler;
import com.senkgang.dbd.Launcher;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server implements Runnable
{

	private Thread thread;
	private Handler h;

	private ArrayList<Object> dataToSend = new ArrayList<>();

	private BufferedWriter writerChannel;

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
						if (line.equals("Spawn survivor")) // yeah waste 10 hours figuring why cant compare stupid strings... java is real shit
						{
							Launcher.logger.Info("New player wants to join!");
							String playerInit = h.getCurrentMap().addSurvivor();
							addData("Spawn response:" + playerInit);
							send();
						}
						if (line.contains("Position update:"))
						{
							h.getCurrentMap().updateSurvivor(line);
						}
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

	public void send()
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
		catch (IOException e)
		{
			Launcher.logger.Exception(e);
		}
	}
}