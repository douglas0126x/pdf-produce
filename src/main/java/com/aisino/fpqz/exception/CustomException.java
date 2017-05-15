package com.aisino.fpqz.exception;

public class CustomException extends Exception
{
  private static final long serialVersionUID = 1L;
  private int code;

  public CustomException(int code, String message)
  {
    super(message);
    this.code = code;
  }

  public CustomException(int code, String message, Exception e) {
    super(message, e);
    this.code = code;
  }

  public int getCode() {
    return this.code;
  }
}