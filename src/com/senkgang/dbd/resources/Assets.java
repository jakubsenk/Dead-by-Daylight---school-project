package com.senkgang.dbd.resources;

import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;

public class Assets
{
	public static Image generator, introLogo, closeGate, openGate, weapon, leftBtn, space, hook;
	public static ArrayList<Image> bleeds = new ArrayList<>();
	public static Image farm, barn, corn;

	public static void init()
	{
		generator = new Image(new File("res/images/generator.png").toURI().toString());
		introLogo = new Image(new File("res/images/introLogo.jpg").toURI().toString());
		closeGate = new Image(new File("res/images/closeGate.png").toURI().toString());
		openGate = new Image(new File("res/images/openGate.png").toURI().toString());
		weapon = new Image(new File("res/images/hammer.png").toURI().toString());
		leftBtn = new Image(new File("res/images/leftBtn.png").toURI().toString());
		hook = new Image(new File("res/images/hook.png").toURI().toString());
		space = new Image(new File("res/images/space.png").toURI().toString());

		bleeds.add(new Image(new File("res/images/bleed0.png").toURI().toString()));
		bleeds.add(new Image(new File("res/images/bleed1.png").toURI().toString()));
		bleeds.add(new Image(new File("res/images/bleed2.png").toURI().toString()));
		bleeds.add(new Image(new File("res/images/bleed3.png").toURI().toString()));

		farm = new Image(new File("res/images/farm.png").toURI().toString());
		barn = new Image(new File("res/images/barn.png").toURI().toString());
		corn = new Image(new File("res/images/corn.png").toURI().toString());
	}
}
