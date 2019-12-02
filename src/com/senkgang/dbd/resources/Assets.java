package com.senkgang.dbd.resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Assets
{
	public static BufferedImage generator, introLogo;

	public static void init() throws IOException
	{
		generator = ImageIO.read(new File("res/images/generator.png"));
		introLogo = ImageIO.read(new File("res/images/introLogo.jpg"));
	}
}
