package com.m1905.mobile.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EContent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private List<Content> contentList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Content> getContentList() {
		return contentList == null ? contentList = new ArrayList<Content>()
				: contentList;
	}

	public void setContentList(List<Content> contentList) {
		this.contentList = contentList;
	}

}
