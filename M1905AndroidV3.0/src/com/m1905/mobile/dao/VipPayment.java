package com.m1905.mobile.dao;

public class VipPayment extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sn;// 订单号码
	private String paymentUrl;// 支付地址
	private String userCode;// 用户编码
	private String userName; // 用户名
	private float amount;// 总额

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getPaymentUrl() {
		return paymentUrl;
	}

	public void setPaymentUrl(String paymentUrl) {
		this.paymentUrl = paymentUrl;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
}
