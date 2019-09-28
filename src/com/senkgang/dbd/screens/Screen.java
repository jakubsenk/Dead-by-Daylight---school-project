package com.senkgang.dbd.screens;

import java.awt.Graphics;

public abstract class Screen {

  private static Screen currentScreen = null;

  public static void setScreen(Screen s)
  {
    currentScreen = s;
  }

  public static Screen getScreen()
  {
    return currentScreen;
  }

  public abstract void update();
  
  public abstract void draw(Graphics g);
  
}