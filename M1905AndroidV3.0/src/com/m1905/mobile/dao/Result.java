package com.m1905.mobile.dao;

import java.io.Serializable;

/**
 * 请求返回信息
 * 
 * @author forcetech
 * 
 */
public class Result implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	private int result;
	private String did = "";
	private String sid = "";
	private String uid = "";
}
