package com.m1905.mobile.dao;

import java.io.Serializable;

public class Content implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;//内容ID
	private int type;//内容类型
	private String img;//图片URL
	private String title;//标题
	private String url;//目标url
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
