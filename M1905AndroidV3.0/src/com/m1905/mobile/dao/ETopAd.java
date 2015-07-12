package com.m1905.mobile.dao;

import java.util.ArrayList;
import java.util.List;

public class ETopAd extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private List<TopAd> lstTopAd;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<TopAd> getLstTopAd() {
		return lstTopAd == null ? lstTopAd = new ArrayList<TopAd>() : lstTopAd;
	}

	public void setLstTopAd(List<TopAd> lstTopAd) {
		this.lstTopAd = lstTopAd;
	}

}
