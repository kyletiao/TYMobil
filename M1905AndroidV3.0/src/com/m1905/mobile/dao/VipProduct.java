package com.m1905.mobile.dao;

import java.io.Serializable;

public class VipProduct implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String proCode;// 产品编码
	private String title;// 产品名称
	private float oprice;// 原价
	private float price;// 价格
	private String note; // 备注
	private String thumb;// 图
	private String description;// 描述

	public String getProCode() {
		return proCode;
	}

	public void setProCode(String proCode) {
		this.proCode = proCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public float getOprice() {
		return oprice;
	}

	public void setOprice(float oprice) {
		this.oprice = oprice;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
