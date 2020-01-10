package com.senkgang.dbd.resources;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;

public class Assets
{
	public static Image generator, introLogo, closeGate, openGate, weapon, leftBtn, space, hook;
	public static ArrayList<Image> bleeds = new ArrayList<>();
	public static Image farm, barn, corn;

	public static void init() throws FileNotFoundException
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL u = classLoader.getResource("com/senkgang/dbd/resources/generator.png");
		if (u == null) throw new FileNotFoundException("Can not find resource generator.png");
		generator = new Image(u.toString());
		u = classLoader.getResource("com/senkgang/dbd/resources/introLogo.png");
		if (u == null) throw new FileNotFoundException("Can not find resource introLogo.png");
		introLogo = new Image(u.toString());
		u = classLoader.getResource("com/senkgang/dbd/resources/closeGate.png");
		if (u == null) throw new FileNotFoundException("Can not find resource closeGate.png");
		closeGate = new Image(u.toString());
		u = classLoader.getResource("com/senkgang/dbd/resources/openGate.png");
		if (u == null) throw new FileNotFoundException("Can not find resource openGate.png");
		openGate = new Image(u.toString());
		u = classLoader.getResource("com/senkgang/dbd/resources/hammer.png");
		if (u == null) throw new FileNotFoundException("Can not find resource hammer.png");
		weapon = new Image(u.toString());
		u = classLoader.getResource("com/senkgang/dbd/resources/leftBtn.png");
		if (u == null) throw new FileNotFoundException("Can not find resource leftBtn.png");
		leftBtn = new Image(u.toString());
		u = classLoader.getResource("com/senkgang/dbd/resources/hook.png");
		if (u == null) throw new FileNotFoundException("Can not find resource hook.png");
		hook = new Image(u.toString());
		u = classLoader.getResource("com/senkgang/dbd/resources/space.png");
		if (u == null) throw new FileNotFoundException("Can not find resource space.png");
		space = new Image(u.toString());

		for (int i = 0; i < 4; i++)
		{
			u = classLoader.getResource("com/senkgang/dbd/resources/bleed" + i + ".png");
			if (u == null) throw new FileNotFoundException("Can not find resource bleed" + i + ".png");
			bleeds.add(new Image(u.toString()));
		}

		u = classLoader.getResource("com/senkgang/dbd/resources/farm.png");
		if (u == null) throw new FileNotFoundException("Can not find resource farm.png");
		farm = new Image(u.toString());
		u = classLoader.getResource("com/senkgang/dbd/resources/barn.png");
		if (u == null) throw new FileNotFoundException("Can not find resource barn.png");
		barn = new Image(u.toString());
		u = classLoader.getResource("com/senkgang/dbd/resources/corn.png");
		if (u == null) throw new FileNotFoundException("Can not find resource corn.png");
		corn = new Image(u.toString());
	}
}
