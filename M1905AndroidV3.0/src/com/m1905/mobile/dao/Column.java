package com.m1905.mobile.dao;

import java.io.Serializable;

public class Column implements Serializable {

	/**
	 * 栏目实体类
	 */
	private static final long serialVersionUID = 1L;
	
	private int id;//栏目id
	private int type;//类别
	private String title;//标题
	private String img;//图片地址
	private String url;//跳转地址
	private String description;//描述
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
