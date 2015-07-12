package com.m1905.mobile.bean;

public class LoginBean {
	private int code;
	private String msg;
	private Info info;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public static class Info{
		private String uid;
		private int bind;
		private int phoneStatus;
		private String nickName;
		private int isSubPgw;
		private String provinceId;
		private Proxy proxy;
		
		public String getUid() {
			return uid;
		}

		public void setUid(String uid) {
			this.uid = uid;
		}

		public int getBind() {
			return bind;
		}

		public void setBind(int bind) {
			this.bind = bind;
		}

		public int getPhoneStatus() {
			return phoneStatus;
		}

		public void setPhoneStatus(int phoneStatus) {
			this.phoneStatus = phoneStatus;
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public int getIsSubPgw() {
			return isSubPgw;
		}

		public void setIsSubPgw(int isSubPgw) {
			this.isSubPgw = isSubPgw;
		}

		public String getProvinceId() {
			return provinceId;
		}

		public void setProvinceId(String provinceId) {
			this.provinceId = provinceId;
		}

		public Proxy getProxy() {
			return proxy;
		}

		public void setProxy(Proxy proxy) {
			this.proxy = proxy;
		}

		public static class Proxy{
			private String apihost;
			private String pushhost;
			private String imghost;
			private String omshost;
			public String getApihost() {
				return apihost;
			}
			public void setApihost(String apihost) {
				this.apihost = apihost;
			}
			public String getPushhost() {
				return pushhost;
			}
			public void setPushhost(String pushhost) {
				this.pushhost = pushhost;
			}
			public String getImghost() {
				return imghost;
			}
			public void setImghost(String imghost) {
				this.imghost = imghost;
			}
			public String getOmshost() {
				return omshost;
			}
			public void setOmshost(String omshost) {
				this.omshost = omshost;
			}
			
		}
	}
}
