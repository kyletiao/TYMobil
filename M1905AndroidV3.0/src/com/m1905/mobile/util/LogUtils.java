package com.m1905.mobile.util;

import android.text.TextUtils;
import android.util.Log;

public class LogUtils {
	private static String customTagPrefix = "";
	private final static boolean allowD = true;
	private final static boolean allowE = true;
	private final static boolean allowI = true;
	private final static boolean allowV = true;
	private final static boolean allowW = true;

	private static String generateTag(StackTraceElement caller) {
		String tag = "%s.%s(Line:%d)";
		String callerClazzName = caller.getClassName();
		callerClazzName = callerClazzName.substring(callerClazzName
				.lastIndexOf(".") + 1);
		tag = String.format(tag, callerClazzName, caller.getMethodName(),
				caller.getLineNumber());
		tag = TextUtils.isEmpty(customTagPrefix) ? tag : customTagPrefix + ":"
				+ tag;
		return tag;
	}

	public static void d(Object msgLog) {
		if (!allowD)
			return;
		StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
		String tag = generateTag(caller);
		Log.d(tag, msgLog == null ? "" : msgLog.toString());
	}

	public static void e(Object msgLog) {
		if (!allowE)
			return;
		StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
		String tag = generateTag(caller);
		Log.e(tag, msgLog == null ? "" : msgLog.toString());
	}

	public static void i(Object msgLog) {
		if (!allowI)
			return;
		StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
		String tag = generateTag(caller);
		Log.i(tag, msgLog == null ? "" : msgLog.toString());
	}

	public static void v(Object msgLog) {
		if (!allowV)
			return;
		StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
		String tag = generateTag(caller);
		Log.v(tag, msgLog == null ? "" : msgLog.toString());
	}

	public static void w(Object msgLog) {
		if (!allowW)
			return;
		StackTraceElement caller = OtherUtils.getCallerStackTraceElement();
		String tag = generateTag(caller);
		Log.w(tag, msgLog == null ? "" : msgLog.toString());
	}

}
