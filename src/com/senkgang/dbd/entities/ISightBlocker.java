package com.senkgang.dbd.entities;

import com.senkgang.dbd.fov.Line;

import java.util.ArrayList;

public interface ISightBlocker
{
	ArrayList<Line> getSightBlockingLines();
}
