package com.aisino.fpqz.util;

import com.aisino.fpqz.exception.CustomException;
import java.util.ArrayList;
import java.util.List;

public class StringUtil
{
  public static String[] substringToArry(String text, int length)
    throws CustomException
  {
    if (text == null) {
      return null;
    }
    List strList = new ArrayList();
    try {
      StringBuilder sb = new StringBuilder();
      double currentLength = 0.0D;
      double totalLength = 0.0D;
      for (char c : text.toCharArray()) {
        totalLength += 1.0D;
        currentLength += getLength(c);
        if (currentLength <= length) {
          sb.append(c);
          if ((currentLength == getLength(text)) || (totalLength == text.length()))
            strList.add(sb.toString());
        }
        else {
          strList.add(sb.toString());
          if (currentLength == length) {
            currentLength = 0.0D;
          } else {
            sb = new StringBuilder();
            sb.append(c);
            if ((currentLength == getLength(text)) || (totalLength == text.length())) {
              strList.add(sb.toString());
            }
            currentLength = getLength(c);
          }
        }
      }
    } catch (Exception e) {
      throw new CustomException(1004, new StringBuilder().append("截取字符串失败：").append(e.getMessage()).append("(").append(text).append(")").toString(), e);
    }
    String[] strs = new String[strList.size()];
    return (String[])strList.toArray(strs);
  }

  public static double getLength(String s) {
    double i = 0.0D;
    for (char c : s.toCharArray())
      i += getLength(c);
    return i;
  }

  public static double getLength(char c) {
    double i = 0.0D;
    String match = "";
    if (String.valueOf(c).matches(match)) {
      if (String.valueOf(c).matches("^[0-9]+$"))
        i += 1.0D;
      else
        i += 1.11D;
    }
    else {
      i += 2.0D;
    }
    return i;
  }

  public static String substring(String str, int length) throws CustomException {
    if (null == str)
      return "";
    StringBuilder sb = new StringBuilder();
    try {
      if (str.getBytes("GBK").length <= length)
        return str;
      double currentLength = 0.0D;
      char[] cs = str.toCharArray();
      int i = 0;
      while ((currentLength += String.valueOf(cs[i]).getBytes("GBK").length) <= length)
        sb.append(cs[(i++)]);
    }
    catch (Exception e) {
      throw new CustomException(1148, new StringBuilder().append("截取字符串失败：").append(e.getMessage()).append("(").append(str).append(")").toString(), e);
    }
    return sb.toString();
  }

  public static String addZero(String str, int strLength) {
    int strLen = null == str ? 0 : str.length();
    if ((strLen > 0) && (strLen < strLength)) {
      while (strLen < strLength) {
        StringBuffer sb = new StringBuffer();
        sb.append("0").append(str);
        str = sb.toString();
        strLen = str.length();
      }
    }
    return str;
  }
}