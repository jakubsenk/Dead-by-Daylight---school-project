package com.senkgang.dbd.logger;

public interface ILogger
{
	void Exception(Exception e);

	void Exception(Exception e, String msg);

	void Error(String msg);

	void Warning(String msg);

	void Info(String msg);

	void Debug(String msg);

	void Trace(String msg);
}