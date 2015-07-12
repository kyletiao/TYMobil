package com.m1905.mobile.dao;

import java.util.ArrayList;
import java.util.List;

public class EVideo extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int pi;// 页码
	private int ps;// 页容量
	private int count;// 内容总数
	private List<Video> videoList;

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

	public List<Video> getVideoList() {
		return videoList==null? videoList=new ArrayList<Video>():videoList;
	}

	public void setVideoList(List<Video> videoList) {
		this.videoList = videoList;
	}

}
