package com.m1905.mobile.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.m1905.mobile.common.Constant;

public class StringUtils {

	/**
	 * 将字节转换成
	 * 
	 * @param bytes
	 * @return KB、M、G、Byte
	 * 
	 */
	public static String conversionBytesUnit(long bytes) {
		if (bytes < Constant.RATE_KB)
			return conversionByteToByte(bytes);
		else if (bytes >= Constant.RATE_KB && bytes < Constant.RATE_M)
			return conversionByteToKB(bytes);
		else if (bytes >= Constant.RATE_M && bytes < Constant.RATE_G)
			return conversionByteToM(bytes);
		else
			return conversionByteToG(bytes);
	}

	/**
	 * 
	 * @param bytes
	 * @return Byte
	 */
	public static String conversionByteToByte(long bytes) {
		LogUtils.i("bytes=" + bytes);
		DecimalFormat df = new DecimalFormat("0");
		return df.format(bytes) + "Byte";
	}

	/**
	 * 
	 * @param bytes
	 * @return KB
	 */
	public static String conversionByteToKB(long bytes) {
		LogUtils.i("bytes=" + bytes);
		DecimalFormat df = new DecimalFormat("0.0");
		return df.format((double) bytes / Constant.RATE_KB) + "KB";
	}

	/**
	 * 
	 * @param bytes
	 * @return M
	 */
	public static String conversionByteToM(long bytes) {
		LogUtils.i("bytes=" + bytes);
		DecimalFormat df = new DecimalFormat("0.0");
		return df.format((double) bytes / Constant.RATE_M) + "M";
	}

	/**
	 * 
	 * @param bytes
	 * @return G
	 */
	public static String conversionByteToG(long bytes) {
		LogUtils.i("bytes=" + bytes);
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format((double) bytes / Constant.RATE_G) + "G";
	}

	/**
	 * 
	 * @param minBytes
	 * @param maxBytes
	 * @return String
	 */
	public static String conversionByteToPer(long minBytes, long maxBytes) {
		// 创建一个数值格式化对象
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后1位
		numberFormat.setMaximumFractionDigits(1);
		String result = numberFormat.format((float) minBytes / (float) maxBytes
				* 100);
		return result + "%";
	}

	/**
	 * 转换成日期+时间
	 * 
	 * @param time
	 * @return yyyy-MM-dd HH:mm
	 */
	public static String formatTimeToDateTime(long time) {
		return formatTimeToFormat("yyyy-MM-dd HH:mm", time);
	}

	/**
	 * 转换成日期
	 * 
	 * @param time
	 * @return yyyy-MM-dd
	 */
	public static String formatTimeToDate(long time) {
		return formatTimeToFormat("yyyy-MM-dd", time);
	}

	/**
	 * 转换成日期
	 * 
	 * @param time
	 * @return MM-dd HH:mm
	 */
	public static String formatTimeToShortDateTime(long time) {
		return formatTimeToFormat("MM-dd HH:mm", time);
	}

	/**
	 * 时间转换
	 * 
	 * @param format
	 * @param time
	 * @return
	 */
	public static String formatTimeToFormat(String format, long time) {
		if (TextUtils.isEmpty(format))
			return String.valueOf(time);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if (String.valueOf(time).length() < 13) {
			return sdf.format(time * 1000l);
		} else {
			return sdf.format(time);
		}
	}

	public static int parseInt(String intVal) {
		return parseInt(intVal, 0);
	}

	public static int parseInt(String intVal, int defaultIntVal) {
		int val = defaultIntVal;
		if (TextUtils.isEmpty(intVal) || intVal.equalsIgnoreCase("null")
				|| intVal.equalsIgnoreCase("false")) {
			return val;
		} else {
			try {
				val = Integer.parseInt(intVal);
			} catch (NumberFormatException e) {
				LogUtils.e(e.getMessage());
			}
		}
		return val;
	}

	public static long parseLong(String longVal) {
		return parseLong(longVal, 0l);
	}

	public static long parseLong(String longVal, long defaultLongVal) {
		long val = defaultLongVal;
		if (TextUtils.isEmpty(longVal) || longVal.equalsIgnoreCase("null")) {
			return val;
		} else {
			try {
				val = Long.parseLong(longVal);
			} catch (NumberFormatException e) {
				LogUtils.e(e.getMessage());
			}
		}
		return val;
	}

	public static float parseFloat(String floatVal) {
		return parseFloat(floatVal, 0.0f);
	}

	public static float parseFloat(String floatVal, float defaultFloatVal) {
		float val = defaultFloatVal;
		if (TextUtils.isEmpty(floatVal) || floatVal.equalsIgnoreCase("null")) {
			return val;
		} else {
			try {
				val = Float.parseFloat(floatVal);
			} catch (NumberFormatException e) {
				LogUtils.e(e.getMessage());
			}
		}
		return val;
	}
	public static double parseDouble(String doubleVal) {
		return parseFloat(doubleVal, 0.0f);
	}

	public static double parseDouble(String doubleVal, double defaultDoubleVal) {
		double val = defaultDoubleVal;
		if (TextUtils.isEmpty(doubleVal) || doubleVal.equalsIgnoreCase("null")) {
			return val;
		} else {
			try {
				val = Double.parseDouble(doubleVal);
			} catch (NumberFormatException e) {
				LogUtils.e(e.getMessage());
			}
		}
		return val;
	}

	public static String formatScore(float score) {
		DecimalFormat df = new DecimalFormat("0.0");
		return df.format(score);
	}
	public static String formatPrice(float score) {
		DecimalFormat df = new DecimalFormat("0.00");
		return df.format(score);
	}

	public static boolean isEmpty(CharSequence value) {
		if (TextUtils.isEmpty(value)
				|| value.toString().equalsIgnoreCase("null"))
			return true;
		else
			return false;
	}

	public static String basicValue2String(BasicNameValuePair... queryData) {
		StringBuffer sb = new StringBuffer();
		for (BasicNameValuePair nv : queryData) {
			if (nv != null) {
				sb.append(nv.getName()).append('=').append(nv.getValue())
						.append('&');
			}
		}
		return sb.toString();
	}

	public static void showShortToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public static void showLongToast(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

	/**
	 * 获取字符串的长度，中文占一个字符,英文数字占半个字符
	 * 
	 * @param value
	 *            指定的字符串
	 * @return 字符串的长度
	 */
	public static double length(String value) {
		double valueLength = 0;
		String chinese = "[\u4e00-\u9fa5]";
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < value.length(); i++) {
			// 获取一个字符
			String temp = value.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				// 中文字符长度为1
				valueLength += 2;
			} else {
				// 其他字符长度为0.5
				valueLength += 1;
			}
		}
		// 进位取整
		return Math.ceil(valueLength);
	}
}
