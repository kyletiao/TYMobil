package com.m1905.mobile.dao;

import java.io.Serializable;

/**
 * 搜索
 * 
 * @author forcetech
 * 
 */
public class SearchContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;// 内容ID
	private String title;// 标题
	private String img;// 图片URL
	private String dircotor;// 导演
	private String actor; // 主演
	private String mtype; // 影片类型
	private String clime; // 影片地区
	private float sorce; // 评分
	private String years; // 年份
	private String description;// 描述

	private int bmonth;// 包月类型
	private float price; // 价格
	private int type;// 内容类型
	private String url;// 需要加载的URL

	/**
	 * 内容ID
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * 内容ID
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 内容类型
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * 内容类型
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 标题
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 图片URL
	 * 
	 * @return
	 */
	public String getImg() {
		return img;
	}

	/**
	 * 图片URL
	 * 
	 * @param img
	 */
	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * 主演
	 * 
	 * @return
	 */
	public String getActor() {
		return actor;
	}

	/**
	 * 主演
	 * 
	 * @param actor
	 */
	public void setActor(String actor) {
		this.actor = actor;
	}

	/**
	 * 导演
	 * 
	 * @return
	 */
	public String getDircotor() {
		return dircotor;
	}

	/**
	 * 导演
	 * 
	 * @param dircotor
	 */
	public void setDircotor(String dircotor) {
		this.dircotor = dircotor;
	}

	/**
	 * 评分
	 * 
	 * @return
	 */
	public float getSorce() {
		return sorce;
	}

	/**
	 * 评分
	 * 
	 * @param sorce
	 */
	public void setSorce(float sorce) {
		this.sorce = sorce;
	}

	/**
	 * 需要加载的URL
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 需要加载的URL
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 描述
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 描述
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public String getMtype() {
		return mtype;
	}

	public void setMtype(String mtype) {
		this.mtype = mtype;
	}

	public String getClime() {
		return clime;
	}

	public void setClime(String clime) {
		this.clime = clime;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public int getBmonth() {
		return bmonth;
	}

	public void setBmonth(int bmonth) {
		this.bmonth = bmonth;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}
