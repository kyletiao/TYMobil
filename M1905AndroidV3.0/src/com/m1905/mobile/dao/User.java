package com.m1905.mobile.dao;

import java.io.Serializable;

import com.m1905.mobile.util.EncryptUtils;

/**
 * 用户信息类
 * 
 * @author forcetech
 * 
 */
public class User extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 用户id
	private long userCode;
	// 用户名称
	private String userName;
	// 用户密码
	private String userPass;
	// 用户邮箱
	private String userEmail;
	// 用户平台
	private String userSite;
	// vip验证标识
	private String userToken;
	// sp应用区分
	private String userSp;
	// 用户包月信息
	private VIPInfo vipInfo;

	public long getUserCode() {
		return userCode;
	}

	public void setUserCode(long userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPass() {
		return EncryptUtils.encryptOrUncrypt(userPass);
	}

	public void setUserPass(String userPass) {
		this.userPass = EncryptUtils.encryptOrUncrypt(userPass);
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

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getUserSp() {
		return userSp;
	}

	public void setUserSp(String userSp) {
		this.userSp = userSp;
	}

	public VIPInfo getVipInfo() {
		return vipInfo;
	}

	public void setVipInfo(VIPInfo vipInfo) {
		this.vipInfo = vipInfo;
	}

	/**
	 * 是否是会员
	 * 
	 * @return
	 */
	public boolean isM1905VIP() {
		return (vipInfo != null && vipInfo.getM1905Vip() == 1 && System
				.currentTimeMillis() <= vipInfo.getEndTime() * 1000l) ? true
				: false;
	}

	@Override
	public String toString() {
		return "User [userCode=" + userCode + ", userName=" + userName
				+ ", userPass=" + userPass + ", userEmail=" + userEmail
				+ ", userSite=" + userSite + ", userToken=" + userToken
				+ ", userSp=" + userSp + ", vipInfo=" + vipInfo + "]";
	}
	
	public User(){}

	public User(String userName, String userPass) {
		this.userName = userName;
		this.userPass = EncryptUtils.encryptOrUncrypt(userPass);
	}

	public User(long userCode, String userName, String userPass,
			String userEmail, String userSite, String userToken, String userSp,
			VIPInfo vipInfo) {
		this.userCode = userCode;
		this.userName = userName;
		this.userPass = EncryptUtils.encryptOrUncrypt(userPass);
		this.userEmail = userEmail;
		this.userSite = userSite;
		this.userToken = userToken;
		this.userSp = userSp;
		this.vipInfo = vipInfo;
	}

}
