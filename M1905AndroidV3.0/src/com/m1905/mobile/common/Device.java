package com.m1905.mobile.common;

import android.util.DisplayMetrics;

/**
 * 设备信息
 * 
 * @author forcetech
 * 
 */
public class Device {
	// 设备型号
	private String model;
	// 设备系统
	private String os;
	// 设备系统语言
	private String language;
	// 网络类型
	private String networkTypeName;
	// 设备标识号
	private String did;
	

	/**
	 * 获得设备型号
	 * 
	 * @return
	 */
	public String getModel() {
		return model;
	}

	/**
	 * 设置设备型号
	 * 
	 * @param model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * 获得设备系统
	 * 
	 * @return
	 */
	public String getOs() {
		return os;
	}

	/**
	 * 设置设备系统
	 * 
	 * @param os
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * 获得设备语言
	 * 
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * 设置设备语言
	 * 
	 * @param language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * 获得网络类型
	 * 
	 * @return
	 */
	public String getNetworkTypeName() {
		return networkTypeName;
	}

	/**
	 * 设置网络类型
	 * 
	 * @param networkTypeName
	 */
	public void setNetworkTypeName(String networkTypeName) {
		this.networkTypeName = networkTypeName;
	}

	/**
	 * 获得设备标识号
	 * 
	 * @return
	 */
	public String getDid() {
		return did;
	}

	/**
	 * 设置设备标识号
	 * 
	 * @param did
	 */
	public void setDid(String did) {
		this.did = did;
	}

	/**
	 * 设备信息初始化
	 * 
	 * @param model
	 * @param os
	 * @param language
	 * @param networkType
	 * @param did
	 */
	public Device(String model, String os, String language,
			String networkTypeName, String did) {
		this.model = model;
		this.os = os;
		this.language = language;
		this.networkTypeName = networkTypeName;
		this.did = did;
	}

	@Override
	public String toString() {
		return "Device [model=" + model + ", os=" + os + ", language="
				+ language + ", networkType=" + networkTypeName + ", did="
				+ did + "]";
	}

}
