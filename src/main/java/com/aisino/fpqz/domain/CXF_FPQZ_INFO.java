package com.aisino.fpqz.domain;

public class CXF_FPQZ_INFO
{
  private int code;
  private String msg;
  private byte[] pdfFile;

  public int getCode()
  {
    return this.code;
  }
  public void setCode(int code) {
    this.code = code;
  }
  public String getMsg() {
    return this.msg;
  }
  public void setMsg(String msg) {
    this.msg = msg;
  }
  public byte[] getPdfFile() {
    return this.pdfFile;
  }
  public void setPdfFile(byte[] pdfFile) {
    this.pdfFile = pdfFile;
  }
}