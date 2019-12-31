package com.senkgang.dbd.interfaces;

public interface IProgressable
{
	void onProgress();

	double getProgress();

	void setProgress(double progress);
}
