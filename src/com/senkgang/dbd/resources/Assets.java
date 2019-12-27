package com.senkgang.dbd.resources;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;

public class Assets
{
	public static Image generator, introLogo,closegate,opengate;

	public static void init() throws IOException
	{
		generator = new Image(new File("res/images/generator.png").toURI().toString());
		introLogo = new Image(new File("res/images/introLogo.jpg").toURI().toString());
		closegate = new Image(new File("res/images/closegate.jpg").toURI().toString());
		opengate = new Image(new File("res/images/opengate.jpg").toURI().toString());
	}
}
