package com.senkgang.dbd.player;

import java.awt.Graphics;
import java.awt.Color;

public class Player {

  private int x;
  private int y;
  private Color c;

  public Player() {
    c = Color.gray;
  }

  public int posX() {
    return x;
  }

  public int posY() {
    return x;
  }

  public void setPos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void update() {
    if (x > 500)
      c = Color.red;
    else if (x > 300)
      c = Color.blue;
    else if (x > 100)
      c = Color.black;
  }

  public void draw(Graphics g) {
    g.setColor(c);
    g.fillRect(x, y, 50, 50);
  }

}