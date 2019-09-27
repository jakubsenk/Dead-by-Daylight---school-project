package com.senkgang.dbd.logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleLogger implements ILogger {

  private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSSS");

  private String curTime() {
    return "[" + dateFormat.format(LocalDateTime.now()) + "]: ";
  }

  private String getStackTrace() {
    String ret = "\n";
    StackTraceElement[] elements = Thread.currentThread().getStackTrace();
    for (int i = 3; i < elements.length; i++) {
      StackTraceElement s = elements[i];
      ret += "\tat " + s.getClassName() + "." + s.getMethodName() + "(" + s.getFileName() + ":" + s.getLineNumber()
          + "):\n";
    }
    return ret;
  }

  private String getClassName() {
    String ret = "";
    StackTraceElement[] elements = Thread.currentThread().getStackTrace();
    if (elements.length >= 4) {
      ret += "in " + elements[3].getClassName() + "(" + elements[3].getFileName() + ":" + elements[3].getLineNumber()
          + "): ";
    }
    return ret;
  }

  @Override
  public void Exception(Exception e) {
    System.out.println(this.curTime() + "EXCEPTION OCCURED");
    if (e != null)
      e.printStackTrace();
    else
      System.out.println("Exception was null: " + this.getStackTrace());
  }

  @Override
  public void Exception(Exception e, String msg) {
    System.out.println(this.curTime() + "EXCEPTION OCCURED");
    if (e != null)
      e.printStackTrace();
    else
      System.out.println("Exception was null: " + this.getStackTrace());
      System.out.println(msg);
  }

  @Override
  public void Error(String msg) {
    System.out.println(this.curTime() + "ERROR " + this.getStackTrace() + msg);

  }

  @Override
  public void Warning(String msg) {
    System.out.println(this.curTime() + "WARNING " + this.getClassName() + msg);

  }

  @Override
  public void Info(String msg) {
    System.out.println(this.curTime() + "INFO " + this.getClassName() + msg);
  }

  @Override
  public void Debug(String msg) {
    System.out.println(this.curTime() + "DEBUG " + this.getClassName() + msg);
  }

  @Override
  public void Trace(String msg) {
    System.out.println(this.curTime() + "TRACE " + this.getClassName() + msg);
  }

}