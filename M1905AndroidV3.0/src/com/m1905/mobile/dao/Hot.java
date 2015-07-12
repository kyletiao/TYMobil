package com.m1905.mobile.dao;

import java.io.Serializable;

/**
 * 热词
 * 
 * @author forcetech
 * 
 */
public class Hot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title; // 标题
	private int num; // 共被搜索次数

	/**
	 * 关键字
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 共被搜索次数
	 * 
	 * @return
	 */
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
