package com.senkgang.dbd.resources;

import javafx.scene.image.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Assets
{
	public static Image generator, introLogo, closeGate, openGate, weapon, leftBtn, space, hook;
	public static ArrayList<Image> bleeds = new ArrayList<>();
	public static Image farm, barn, corn;

	public static void init() throws FileNotFoundException
	{
		File f = new File("res/images/generator.png");
		if (!f.exists()) throw new FileNotFoundException("Can not find file generator.png");
		generator = new Image(f.toURI().toString());
		f = new File("res/images/introLogo.jpg");
		if (!f.exists()) throw new FileNotFoundException("Can not find file introLogo.jpg");
		introLogo = new Image(f.toURI().toString());
		f = new File("res/images/closeGate.png");
		if (!f.exists()) throw new FileNotFoundException("Can not find file closeGate.png");
		closeGate = new Image(f.toURI().toString());
		f = new File("res/images/openGate.png");
		if (!f.exists()) throw new FileNotFoundException("Can not find file openGate.png");
		openGate = new Image(new File("res/images/openGate.png").toURI().toString());
		f = new File("res/images/hammer.png");
		if (!f.exists()) throw new FileNotFoundException("Can not find file hammer.png");
		weapon = new Image(f.toURI().toString());
		f = new File("res/images/leftBtn.png");
		if (!f.exists()) throw new FileNotFoundException("Can not find file leftBtn.png");
		leftBtn = new Image(f.toURI().toString());
		f = new File("res/images/hook.png");
		if (!f.exists()) throw new FileNotFoundException("Can not find file hook.png");
		hook = new Image(f.toURI().toString());
		f = new File("res/images/space.png");
		if (!f.exists()) throw new FileNotFoundException("Can not find file space.png");
		space = new Image(f.toURI().toString());

		for (int i = 0; i < 4; i++)
		{
			f = new File("res/images/bleed" + i + ".png");
			if (!f.exists()) throw new FileNotFoundException("Can not find file bleed" + i + ".png");
			bleeds.add(new Image(f.toURI().toString()));
		}

		f = new File("res/images/farm.png");
		if (!f.exists()) throw new FileNotFoundException("Can not find file farm.png");
		farm = new Image(f.toURI().toString());
		f = new File("res/images/barn.png");
		if (!f.exists()) throw new FileNotFoundException("Can not find file barn.png");
		barn = new Image(f.toURI().toString());
		f = new File("res/images/corn.png");
		if (!f.exists()) throw new FileNotFoundException("Can not find file corn.png");
		corn = new Image(f.toURI().toString());
	}
}
