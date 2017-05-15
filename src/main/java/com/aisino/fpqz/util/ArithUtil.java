package com.aisino.fpqz.util;

import com.aisino.fpqz.exception.CustomException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 数学工具类
 * @author yaoxj
 * @time 2017年4月15日上午10:55:22
 */
public class ArithUtil {
	/**
	 * 格式化
	 * @param value
	 * @param scale
	 * @param precision
	 * @return
	 * @throws CustomException
	 */
	public static String fdd(String value, int scale, int precision)
			throws CustomException {
		BigDecimal b = new BigDecimal(value);
		return fdd(b, scale, precision);
	}
	/**
	 * 数字格式化
	 * @param value
	 * @param scale
	 * @param precision
	 * @return
	 * @throws CustomException
	 */
	public static String fdd(BigDecimal value, int scale, int precision)throws CustomException {
		if ((scale < 0) || (precision < 0) || (precision > scale)) {
			throw new CustomException(1010, "The scale is " + scale
					+ " and the precision is " + precision);
		}
		String format = "#0";
		if (scale > 0) {
			format = format + ".";
			for (int i = 0; i < precision; i++) {
				format = format + "0";
			}
			for (int i = 0; i < scale - precision; i++) {
				format = format + "#";
			}
		}
		DecimalFormat df = new DecimalFormat(format);
		df.setRoundingMode(RoundingMode.HALF_UP);
		return df.format(value);
	}

	public static BigDecimal fdd(BigDecimal value, int scale)
			throws CustomException {
		return value.setScale(scale, 4);
	}

	public static BigDecimal fdd(String value, int scale)
			throws CustomException {
		return new BigDecimal(value).setScale(scale, 4);
	}

	public static int compareTo(BigDecimal a, BigDecimal b, BigDecimal c) {
		return a.subtract(b).abs().compareTo(c);
	}

	public static int getNumericPrecision(String value) {
		return value.contains(".") ? delZero(value.split("\\.")[1]).length()
				: 0;
	}

	public static String delZero(String src) {
		if (src.endsWith("0")) {
			return delZero(src.substring(0, src.length() - 1));
		}
		return src;
	}
}