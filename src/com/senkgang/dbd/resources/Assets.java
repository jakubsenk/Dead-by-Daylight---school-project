package com.senkgang.dbd.resources;

import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;

public class Assets
{
	public static Image generator, introLogo, weapon;
	public static ArrayList<Image> bleeds = new ArrayList<>();

	public static void init()
	{
		generator = new Image(new File("res/images/generator.png").toURI().toString());
		introLogo = new Image(new File("res/images/introLogo.jpg").toURI().toString());
		weapon = new Image(new File("res/images/hammer.png").toURI().toString());
		bleeds.add(new Image(new File("res/images/bleed0.png").toURI().toString()));
		bleeds.add(new Image(new File("res/images/bleed1.png").toURI().toString()));
		bleeds.add(new Image(new File("res/images/bleed2.png").toURI().toString()));
		bleeds.add(new Image(new File("res/images/bleed3.png").toURI().toString()));
	}
}
