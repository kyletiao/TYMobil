package com.m1905.mobile.dao;

/**
 * push client info
 * 
 * @author Eric
 * 
 */
public class PushUser {
	private String appId;
	private String channelId;
	private String userId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
