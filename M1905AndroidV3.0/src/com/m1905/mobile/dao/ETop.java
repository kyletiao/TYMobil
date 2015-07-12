package com.m1905.mobile.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ETop implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private List<Top> topList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Top> getTopList() {
		return topList == null ? topList = new ArrayList<Top>() : topList;
	}

	public void setTopList(List<Top> topList) {
		this.topList = topList;
	}

}
