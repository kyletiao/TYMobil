package com.m1905.mobile.dao;

/**
 * 实体类
 * 
 * @author leepan
 * @version 3.0
 * @created 2013-12-10
 */
public abstract class Entity extends Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	protected String cacheKey;

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
}
