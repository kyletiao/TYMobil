package com.m1905.mobile.bean;

public class KvBean {
	private int areaCode;
	private Data[] data;
	
	public int getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(int areaCode) {
		this.areaCode = areaCode;
	}

	public Data[] getData() {
		return data;
	}

	public void setData(Data[] data) {
		this.data = data;
	}

	public static class Data{
		private String title;
		private String contentId;
		private String productId;
		private String clickType;
		private String clickParam;
		private String subscript;
		private String cover;
		private String recommendid;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
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
		public String getClickType() {
			return clickType;
		}
		public void setClickType(String clickType) {
			this.clickType = clickType;
		}
		public String getClickParam() {
			return clickParam;
		}
		public void setClickParam(String clickParam) {
			this.clickParam = clickParam;
		}
		public String getSubscript() {
			return subscript;
		}
		public void setSubscript(String subscript) {
			this.subscript = subscript;
		}
		public String getCover() {
			return cover;
		}
		public void setCover(String cover) {
			this.cover = cover;
		}
		public String getRecommendid() {
			return recommendid;
		}
		public void setRecommendid(String recommendid) {
			this.recommendid = recommendid;
		}
		
	}
}
