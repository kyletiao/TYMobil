package com.m1905.mobile.dao;

import java.io.Serializable;

/**
 * VIP包月数据信息
 * 
 * @author forcetech
 * 
 */
public class VIPInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 包月开始时间
	private long startTime;
	// 包月结束时间
	private long endTime;
	// 包月状态
	private int m1905Vip;

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public int getM1905Vip() {
		return m1905Vip;
	}

	public void setM1905Vip(int m1905Vip) {
		this.m1905Vip = m1905Vip;
	}

	@Override
	public String toString() {
		return "VIPInfo [startTime=" + startTime + ", endTime=" + endTime
				+ ", m1905Vip=" + m1905Vip + "]";
	}

	public VIPInfo(long startTime, long endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.m1905Vip = System.currentTimeMillis() > endTime * 1000l ? 0 : 1;
	}

	public VIPInfo(){}
	public VIPInfo(long startTime, long endTime, int m1905Vip) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.m1905Vip = m1905Vip;
	}

}
