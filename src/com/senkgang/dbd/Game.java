package com.senkgang.dbd;

import com.senkgang.dbd.display.Display;
import com.senkgang.dbd.player.Player;

import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.awt.Color;

public class Game implements Runnable {

  private int width;
  private int height;

  private int fps;

  private Display display;
  private Thread thread;
  private boolean running = false;

  private BufferStrategy bs;
  private Graphics gr;

  private Player p;

  int x = 0;

  public Game(int width, int height) {
    this.width = width;
    this.height = height;
  }

  private void init() {
    display = new Display(width, height);
    p = new Player();
    p.setPos(width / 2, height / 2);
  }

  private void update() {
    x++;
    if (x > 600)
    {
      x = 5;
    }
    p.setPos(x, x);
    p.update();
  }

  private void draw() {
    bs = display.canvas().getBufferStrategy();
    if (bs == null) {
      display.canvas().createBufferStrategy(2);
      return;
    }
    gr = bs.getDrawGraphics();

    gr.clearRect(0, 0, width, height); // Clear screen

    // #region drawings

    gr.setColor(Color.black);
    gr.drawString("FPS: " + fps, 0, 10);
    gr.fillRect(x, 10, 10, 10);
    p.draw(gr);

    // #endregion

    bs.show();
    gr.dispose();
  }

  @Override
  public void run() {
    init();

    int targetFps = 60;
    double timePerTick = 1000000000 / targetFps;
    double delta = 0;
    long current;
    long last = System.nanoTime();
    long timer = 0;
    int ticks = 0;

    while (running) {
      current = System.nanoTime();
      delta += (current - last) / timePerTick;
      timer += current - last;
      last = current;

      if (delta >= 1) {
        update();
        draw();
        ticks++;
        delta = 0;
      }

      if (timer >= 1000000000) {
        Launcher.logger.Trace("Frames per second: " + ticks);
        fps = ticks;
        ticks = 0;
        timer = 0;
      }
    }

    stop();
  }

  public synchronized void start() {
    if (running) {
      Launcher.logger.Error("Trying to run game while game already running!");
      return;
    }
    running = true;
    thread = new Thread(this);
    thread.start();
  }

  public synchronized void stop() {
    if (!running) {
      Launcher.logger.Error("Trying to stop game while game is not running!");
      return;
    }
    running = false;
    try {
      thread.join();
    } catch (InterruptedException e) {
      Launcher.logger.Exception(e);
    }
  }
}