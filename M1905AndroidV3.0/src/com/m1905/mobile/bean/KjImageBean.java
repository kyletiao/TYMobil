package com.m1905.mobile.bean;

public class KjImageBean {

	private String appid;
	private Info info;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public class Info {

		private Images images;
		private Imagelist imagelist;
		private String path;
		private String[] phoneNum;
		public int RoundTime;

		public class Images {
			private String src;
			private String start;
			private String end;

			public String getSrc() {
				return src;
			}

			public void setSrc(String src) {
				this.src = src;
			}

			public String getStart() {
				return start;
			}

			public void setStart(String start) {
				this.start = start;
			}

			public String getEnd() {
				return end;
			}

			public void setEnd(String end) {
				this.end = end;
			}

		}

		public class Imagelist {
			private String[] srclist;
			private String start;
			private String end;

			public String[] getSrclist() {
				return srclist;
			}

			public void setSrclist(String[] srclist) {
				this.srclist = srclist;
			}

			public String getStart() {
				return start;
			}

			public void setStart(String start) {
				this.start = start;
			}

			public String getEnd() {
				return end;
			}

			public void setEnd(String end) {
				this.end = end;
			}

		}

		public Images getImages() {
			return images;
		}

		public void setImages(Images images) {
			this.images = images;
		}

		public Imagelist getImagelist() {
			return imagelist;
		}

		public void setImagelist(Imagelist imagelist) {
			this.imagelist = imagelist;
		}

		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		public String[] getPhoneNum() {
			return phoneNum;
		}

		public void setPhoneNum(String[] phoneNum) {
			this.phoneNum = phoneNum;
		}

		public int getRoundTime() {
			return RoundTime;
		}

		public void setRoundTime(int roundTime) {
			RoundTime = roundTime;
		}

	}

}
