package com.m1905.mobile.dao;

/**
 * 意见反馈
 * 
 * @author Victor.Yang
 * 
 */
public class Feedback extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int STATUS_OK = 1;
	public static final int STATUS_FAIL = 2;
	public static final int STATUS_ERROR = 0;
	private int status;// 1：成功 2：失败 0：信息不全
	private String submitMessage;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getSubmitMessage() {
		return submitMessage;
	}

	public void setSubmitMessage(String submitMessage) {
		this.submitMessage = submitMessage;
	}

}
