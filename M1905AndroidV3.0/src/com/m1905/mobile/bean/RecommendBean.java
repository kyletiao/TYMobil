package com.m1905.mobile.bean;

public class RecommendBean {
	private int areaCode;
	private Label label;
	private More more;
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

	public More getMore() {
		return more;
	}

	public void setMore(More more) {
		this.more = more;
	}

	public Data[] getData() {
		return data;
	}

	public void setData(Data[] data) {
		this.data = data;
	}

	public static class Label{
		private String name;
		private String title;
		private int clickType;
		private int imgtype;
		private String clickParam;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public int getClickType() {
			return clickType;
		}
		public void setClickType(int clickType) {
			this.clickType = clickType;
		}
		public int getImgtype() {
			return imgtype;
		}
		public void setImgtype(int imgtype) {
			this.imgtype = imgtype;
		}
		public String getClickParam() {
			return clickParam;
		}
		public void setClickParam(String clickParam) {
			this.clickParam = clickParam;
		}
		
	}
	
	public static class More{
		private String title;
		private String name;
		private int imgtype;
		private int clickType;
		private String clickParam;
		private String recommendid;
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getImgtype() {
			return imgtype;
		}
		public void setImgtype(int imgtype) {
			this.imgtype = imgtype;
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
		public String getRecommendid() {
			return recommendid;
		}
		public void setRecommendid(String recommendid) {
			this.recommendid = recommendid;
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
		private String recommendid;
		private String subscript;
		
		
		public String getSubscript() {
			return subscript;
		}
		public void setSubscript(String subscript) {
			this.subscript = subscript;
		}
		public String getRecommendid() {
			return recommendid;
		}
		public void setRecommendid(String recommendid) {
			this.recommendid = recommendid;
		}
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
