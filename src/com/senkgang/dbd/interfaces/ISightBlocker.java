package com.senkgang.dbd.interfaces;

import com.senkgang.dbd.fov.Line;

import java.util.ArrayList;

public interface ISightBlocker
{
	ArrayList<Line> getSightBlockingLines();
}
