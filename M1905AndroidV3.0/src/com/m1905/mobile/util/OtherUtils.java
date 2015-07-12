package com.m1905.mobile.util;

public class OtherUtils {
	public static StackTraceElement getCallerStackTraceElement() {
		return Thread.currentThread().getStackTrace()[4];
	}
}
