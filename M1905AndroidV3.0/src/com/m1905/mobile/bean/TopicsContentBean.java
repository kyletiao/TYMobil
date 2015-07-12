package com.m1905.mobile.bean;

public class TopicsContentBean {
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

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
	}
	
	public static class Data{
		private String title;
		private String aspect;
		private String contentId;
		private String productId;
		private int clickType;
		private String clickParam;
		private String cover;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getAspect() {
			return aspect;
		}
		public void setAspect(String aspect) {
			this.aspect = aspect;
		}
		public String getContentId() {
			return contentId;
		}
		public void setContentId(String contentId) {
			this.contentId = contentId;
		}
		public String getProductId() {
			return productId;
		}
		public void setProductId(String productId) {
			this.productId = productId;
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
		public String getCover() {
			return cover;
		}
		public void setCover(String cover) {
			this.cover = cover;
		}
		
	}
	
}
