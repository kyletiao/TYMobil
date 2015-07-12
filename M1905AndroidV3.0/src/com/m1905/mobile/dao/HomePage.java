package com.m1905.mobile.dao;

public class HomePage extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*
	 * 头图
	 */
	private ETop topData;
	private ETop freeData;
	/*
	 * 强片推荐
	 */
	private EContent todayData;
	/*
	 * 新片预告
	 */
	private EContent latestData;
	/*
	 * 好莱坞
	 */
	private EContent hotData;
	/*
	 * 华语强档
	 */
	private EContent cctv6Data;
	/*
	 * 日韩专区
	 */
	private EContent rhData;
	/*
	 * 异色欧美
	 */
	private EContent ysData;
	/*
	 * 微片
	 */
	private EContent wpData;
	/*
	 * 免费专区
	 */
	private EContent mfData;
	
	public EContent getRhData() {
		return rhData;
	}

	public void setRhData(EContent rhData) {
		this.rhData = rhData;
	}

	public EContent getYsData() {
		return ysData;
	}

	public void setYsData(EContent ysData) {
		this.ysData = ysData;
	}

	public EContent getWpData() {
		return wpData;
	}

	public void setWpData(EContent wpData) {
		this.wpData = wpData;
	}

	public EContent getMfData() {
		return mfData;
	}

	public void setMfData(EContent mfData) {
		this.mfData = mfData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ETop getTopData() {
		return topData;
	}

	public void setTopData(ETop topData) {
		this.topData = topData;
	}

	public ETop getFreeData() {
		return freeData;
	}

	public void setFreeData(ETop freeData) {
		this.freeData = freeData;
	}

	public EContent getTodayData() {
		return todayData;
	}

	public void setTodayData(EContent todayData) {
		this.todayData = todayData;
	}

	public EContent getLatestData() {
		return latestData;
	}

	public void setLatestData(EContent latestData) {
		this.latestData = latestData;
	}

	public EContent getHotData() {
		return hotData;
	}

	public void setHotData(EContent hotData) {
		this.hotData = hotData;
	}

	public EContent getCctv6Data() {
		return cctv6Data;
	}

	public void setCctv6Data(EContent cctv6Data) {
		this.cctv6Data = cctv6Data;
	}

	/**
	 * 获得 Content大小
	 * 
	 * @return
	 */
	public int getContentSize() {
		return getTodayDataSize() + getLatestDataSize() + getHotDataSize()
				+ getCctv6DataSize() + getFreeDataSize() + getRhDataSize()+getYsDataSize()+getWpDataSize()+getMfDataSize();
	}

	/**
	 * 获得所有数据大小
	 * 
	 * @return
	 */
	public int getAllSize() {
		return getContentSize() + getTopDataSize();
	}

	/**
	 * 获得头图数据大小
	 * 
	 * @return
	 */
	public int getTopDataSize() {
		return getTopData() != null ? getTopData().getTopList().size() : 0;
	}

	public int getTodayDataSize() {
		return getTodayData() != null ? getTodayData().getContentList().size()
				: 0;
	}

	public int getLatestDataSize() {
		return getLatestData() != null ? getLatestData().getContentList()
				.size() : 0;
	}

	public int getHotDataSize() {
		return getHotData() != null ? getHotData().getContentList().size() : 0;
	}

	public int getCctv6DataSize() {
		return getCctv6Data() != null ? getCctv6Data().getContentList().size()
				: 0;
	}
	public int getRhDataSize() {
		return getRhData() != null ? getRhData().getContentList().size()
				: 0;
	}
	public int getYsDataSize() {
		return getYsData() != null ? getYsData().getContentList().size()
				: 0;
	}
	public int getWpDataSize() {
		return getWpData() != null ? getWpData().getContentList().size()
				: 0;
	}
	public int getMfDataSize() {
		return getMfData() != null ? getMfData().getContentList().size()
				: 0;
	}
	public int getFreeDataSize() {
		return getFreeData() != null ? getFreeData().getTopList().size() : 0;
	}

}
