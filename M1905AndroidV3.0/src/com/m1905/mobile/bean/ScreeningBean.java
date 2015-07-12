package com.m1905.mobile.bean;

public class ScreeningBean {
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
		private int total;
		private Data[] data;
		
		public int getTotal() {
			return total;
		}

		public void setTotal(int total) {
			this.total = total;
		}

		public Data[] getData() {
			return data;
		}

		public void setData(Data[] data) {
			this.data = data;
		}

		public static class Data{
			private String cover;
			private String countryName;
			private String title;
			private int ppvStyle;
			private int seriescount;
			private int contentMode;
			private int plats;
			private String contentType;
			private int nowseriescount;
			private int seriesId;
			private String contentId;
			private String description;
			private String cast;
			private String director;
			private float averageScoreValue;
			
			public float getAverageScoreValue() {
				return averageScoreValue;
			}
			public void setAverageScoreValue(float averageScoreValue) {
				this.averageScoreValue = averageScoreValue;
			}
			public String getCast() {
				return cast;
			}
			public void setCast(String cast) {
				this.cast = cast;
			}
			public String getDirector() {
				return director;
			}
			public void setDirector(String director) {
				this.director = director;
			}
			public String getDescription() {
				return description;
			}
			public void setDescription(String description) {
				this.description = description;
			}
			public int getContentMode() {
				return contentMode;
			}
			public void setContentMode(int contentMode) {
				this.contentMode = contentMode;
			}
			public String getCover() {
				return cover;
			}
			public void setCover(String cover) {
				this.cover = cover;
			}
			public String getCountryName() {
				return countryName;
			}
			public void setCountryName(String countryName) {
				this.countryName = countryName;
			}
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			public int getPpvStyle() {
				return ppvStyle;
			}
			public void setPpvStyle(int ppvStyle) {
				this.ppvStyle = ppvStyle;
			}
			public int getSeriescount() {
				return seriescount;
			}
			public void setSeriescount(int seriescount) {
				this.seriescount = seriescount;
			}
			public int getPlats() {
				return plats;
			}
			public void setPlats(int plats) {
				this.plats = plats;
			}
			public String getContentType() {
				return contentType;
			}
			public void setContentType(String contentType) {
				this.contentType = contentType;
			}
			public int getNowseriescount() {
				return nowseriescount;
			}
			public void setNowseriescount(int nowseriescount) {
				this.nowseriescount = nowseriescount;
			}
			public int getSeriesId() {
				return seriesId;
			}
			public void setSeriesId(int seriesId) {
				this.seriesId = seriesId;
			}
			public String getContentId() {
				return contentId;
			}
			public void setContentId(String contentId) {
				this.contentId = contentId;
			}
			
		}
	} 
}
