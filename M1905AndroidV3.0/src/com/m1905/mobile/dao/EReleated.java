package com.m1905.mobile.dao;

import java.util.ArrayList;
import java.util.List;

public class EReleated extends Entity {

	/**
	 * 相关List
	 */
	private static final long serialVersionUID = 1L;
	private List<Releated> releatedList;
	public List<Releated> getReleatedList() {
		return releatedList==null?new ArrayList<Releated>():releatedList;
	}
	public void setReleatedList(List<Releated> releatedList) {
		this.releatedList = releatedList;
	}



}
