package com.senkgang.dbd.logger;

public interface ILogger
{
	public void Exception(Exception e);

	public void Exception(Exception e, String msg);

	public void Error(String msg);

	public void Warning(String msg);

	public void Info(String msg);

	public void Debug(String msg);

	public void Trace(String msg);
}