package com.m1905.mobile.bean;

public class PayContentBean {
	private String code;
	private String msg;
	private Info[] info;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	

	public Info[] getInfo() {
		return info;
	}

	public void setInfo(Info[] info) {
		this.info = info;
	}



	public static class Info{
		private String productName;
		private String productId;
		private String pstart;
		private String pend;
		private String subId;
		private String fee;
		private int unsubscribed;
		private int purchaseType;
		private int payMode;
		private String payModeName;
		private String contentID;
		private String unsubscribedTime;
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public String getProductId() {
			return productId;
		}
		public void setProductId(String productId) {
			this.productId = productId;
		}
		public String getPstart() {
			return pstart;
		}
		public void setPstart(String pstart) {
			this.pstart = pstart;
		}
		public String getPend() {
			return pend;
		}
		public void setPend(String pend) {
			this.pend = pend;
		}
		public String getSubId() {
			return subId;
		}
		public void setSubId(String subId) {
			this.subId = subId;
		}
		public String getFee() {
			return fee;
		}
		public void setFee(String fee) {
			this.fee = fee;
		}
		public int getUnsubscribed() {
			return unsubscribed;
		}
		public void setUnsubscribed(int unsubscribed) {
			this.unsubscribed = unsubscribed;
		}
		public int getPurchaseType() {
			return purchaseType;
		}
		public void setPurchaseType(int purchaseType) {
			this.purchaseType = purchaseType;
		}
		public int getPayMode() {
			return payMode;
		}
		public void setPayMode(int payMode) {
			this.payMode = payMode;
		}
		public String getPayModeName() {
			return payModeName;
		}
		public void setPayModeName(String payModeName) {
			this.payModeName = payModeName;
		}
		public String getContentID() {
			return contentID;
		}
		public void setContentID(String contentID) {
			this.contentID = contentID;
		}
		public String getUnsubscribedTime() {
			return unsubscribedTime;
		}
		public void setUnsubscribedTime(String unsubscribedTime) {
			this.unsubscribedTime = unsubscribedTime;
		}
	}
}
