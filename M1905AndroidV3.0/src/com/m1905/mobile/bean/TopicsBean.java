package com.m1905.mobile.bean;

public class TopicsBean {
	private int areaCode;
	private Label label;
	private Data[] data;
	
	
	public int getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(int areaCode) {
		this.areaCode = areaCode;
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public Data[] getData() {
		return data;
	}

	public void setData(Data[] data) {
		this.data = data;
	}

	public static class Label{
		private String name;
		private int clickType;
		private String clickParam;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getClickType() {
			return clickType;
		}
		public void setClickType(int clickType) {
			this.clickType = clickType;
		}
		public String getClickParam() {
			return clickParam;
		}
		public void setClickParam(String clickParam) {
			this.clickParam = clickParam;
		}
		
	}
	
	public static class Data{
		private String description;
		private String title;
		private String cover;
		private int clickType;
		private String clickParam;
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getCover() {
			return cover;
		}
		public void setCover(String cover) {
			this.cover = cover;
		}
		public int getClickType() {
			return clickType;
		}
		public void setClickType(int clickType) {
			this.clickType = clickType;
		}
		public String getClickParam() {
			return clickParam;
		}
		public void setClickParam(String clickParam) {
			this.clickParam = clickParam;
		}
		
	}
}
