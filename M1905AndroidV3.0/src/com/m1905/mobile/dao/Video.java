package com.m1905.mobile.dao;

import java.io.Serializable;

public class Video implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;//内容ID
	private String title;//类别ID
	private String img;//图片URL
	private int type;//内容类型
	private String url;//跳转或接口地址
	private String description;//简介
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
