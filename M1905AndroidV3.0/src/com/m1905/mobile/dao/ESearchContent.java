package com.m1905.mobile.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索数据
 * 
 * @author forcetech
 * 
 */
public class ESearchContent extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pi;// 页码
	private int ps;// 页容量
	private int count;// 内容总数
	private List<SearchContent> lstSearchContents;

	public int getPi() {
		return pi;
	}

	public void setPi(int pi) {
		this.pi = pi;
	}

	public int getPs() {
		return ps;
	}

	public void setPs(int ps) {
		this.ps = ps;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<SearchContent> getLstSearchContents() {
		return lstSearchContents == null ? lstSearchContents = new ArrayList<SearchContent>()
				: lstSearchContents;
	}

	public void setLstSearchContents(List<SearchContent> lstSearchContents) {
		this.lstSearchContents = lstSearchContents;
	}

}
