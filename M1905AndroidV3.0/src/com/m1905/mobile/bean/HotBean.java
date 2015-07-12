package com.m1905.mobile.bean;

public class HotBean {
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
		private String keyword;
		private String productId;
		private String ppvid;
		public String getKeyword() {
			return keyword;
		}
		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}
		public String getProductId() {
			return productId;
		}
		public void setProductId(String productId) {
			this.productId = productId;
		}
		public String getPpvid() {
			return ppvid;
		}
		public void setPpvid(String ppvid) {
			this.ppvid = ppvid;
		}
		
	}
}
