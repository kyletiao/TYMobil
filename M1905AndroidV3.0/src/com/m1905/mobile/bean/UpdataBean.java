package com.m1905.mobile.bean;

public class UpdataBean {
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
		private Lists[] list;
		
		public static class Lists{
			private String appId;
			private String appName;
			private String description;
			private int isupdate;
			private String md5;
			private String name;
			private String path;
			private String updateImg;
			private int updatemodel;
			private String version;
			public String getAppId() {
				return appId;
			}
			public void setAppId(String appId) {
				this.appId = appId;
			}
			public String getAppName() {
				return appName;
			}
			public void setAppName(String appName) {
				this.appName = appName;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}
			public int getIsupdate() {
				return isupdate;
			}
			public void setIsupdate(int isupdate) {
				this.isupdate = isupdate;
			}
			public String getMd5() {
				return md5;
			}
			public void setMd5(String md5) {
				this.md5 = md5;
			}
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
			public String getUpdateImg() {
				return updateImg;
			}
			public void setUpdateImg(String updateImg) {
				this.updateImg = updateImg;
			}
			public int getUpdatemodel() {
				return updatemodel;
			}
			public void setUpdatemodel(int updatemodel) {
				this.updatemodel = updatemodel;
			}
			public String getVersion() {
				return version;
			}
			public void setVersion(String version) {
				this.version = version;
			}
			
		}

		public Lists[] getList() {
			return list;
		}

		public void setList(Lists[] list) {
			this.list = list;
		}
		
	}
}
