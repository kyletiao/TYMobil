package com.m1905.mobile.bean;

public class NavigationBean {
	private int areaCode;
	private Tabs[] tabs;
	
	public int getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(int areaCode) {
		this.areaCode = areaCode;
	}

	public Tabs[] getTabs() {
		return tabs;
	}

	public void setTabs(Tabs[] tabs) {
		this.tabs = tabs;
	}

	public static class Tabs{
		private String name;
		private String path;
		private String areaType;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPath() {
			return path;
		}
		public void setPath(String path) {
			this.path = path;
		}
		public String getAreaType() {
			return areaType;
		}
		public void setAreaType(String areaType) {
			this.areaType = areaType;
		}
		
	}
}
