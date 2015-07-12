package com.m1905.mobile.dao;


/**
 * 用户信息类
 * 
 * @author leepan
 * 
 */
public class Register extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 用户名称
	private String userName;
	// 用户密码
	private String userEmail;
	// 用户平台
	private String userSite;
	//token
	private String token;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserSite() {
		return userSite;
	}
	public void setUserSite(String userSite) {
		this.userSite = userSite;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	

}
