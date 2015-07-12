package com.m1905.mobile.bean;

public class ContentPath {
	private int code;
	private int orderType;
	private String msg;
	private Info info;
	
	public Productss[] Products;
	public static class Productss{
		private String productID;
		private String productName;
		private String productDesc;
		private String fee;
		private String purchaseType;
		private int secondConfirm;
		public String getProductID() {
			return productID;
		}
		public void setProductID(String productID) {
			this.productID = productID;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public String getProductDesc() {
			return productDesc;
		}
		public void setProductDesc(String productDesc) {
			this.productDesc = productDesc;
		}
		public String getFee() {
			return fee;
		}
		public void setFee(String fee) {
			this.fee = fee;
		}
		public String getPurchaseType() {
			return purchaseType;
		}
		public void setPurchaseType(String purchaseType) {
			this.purchaseType = purchaseType;
		}
		public int getSecondConfirm() {
			return secondConfirm;
		}
		public void setSecondConfirm(int secondConfirm) {
			this.secondConfirm = secondConfirm;
		}
		
	}
	
	private String ccg;
	private int isSubPgw;
	private int isGray;
	
	
	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public Productss[] getProducts() {
		return Products;
	}

	public void setProducts(Productss[] products) {
		Products = products;
	}

	public String getCcg() {
		return ccg;
	}

	public void setCcg(String ccg) {
		this.ccg = ccg;
	}

	public int getIsSubPgw() {
		return isSubPgw;
	}

	public void setIsSubPgw(int isSubPgw) {
		this.isSubPgw = isSubPgw;
	}

	public int getIsGray() {
		return isGray;
	}

	public void setIsGray(int isGray) {
		this.isGray = isGray;
	}

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
		private String title;
		private int addTime;
		private String productid;
		private String cpid;
		private String spid;
		private String comefrom;
		private Videos[] videos;
		private ViewPoints[] viewPoints;
		private boolean playLimit;
		private int payLimit;
		private int eachPrice;
		private int monthPrice;
		private String monthVipcode;
		private PlotAspects[] plotAspects;
		private int ppvpriority;
		private int  onlyppv;
		
		
		public String getComefrom() {
			return comefrom;
		}


		public void setComefrom(String comefrom) {
			this.comefrom = comefrom;
		}


		public String getTitle() {
			return title;
		}


		public void setTitle(String title) {
			this.title = title;
		}


		public int getAddTime() {
			return addTime;
		}


		public void setAddTime(int addTime) {
			this.addTime = addTime;
		}


		public String getProductid() {
			return productid;
		}


		public void setProductid(String productid) {
			this.productid = productid;
		}


		public String getCpid() {
			return cpid;
		}


		public void setCpid(String cpid) {
			this.cpid = cpid;
		}


		public String getSpid() {
			return spid;
		}


		public void setSpid(String spid) {
			this.spid = spid;
		}


		public Videos[] getVideos() {
			return videos;
		}


		public void setVideos(Videos[] videos) {
			this.videos = videos;
		}


		public ViewPoints[] getViewPoints() {
			return viewPoints;
		}


		public void setViewPoints(ViewPoints[] viewPoints) {
			this.viewPoints = viewPoints;
		}


		public boolean isPlayLimit() {
			return playLimit;
		}


		public void setPlayLimit(boolean playLimit) {
			this.playLimit = playLimit;
		}


		public int getPayLimit() {
			return payLimit;
		}


		public void setPayLimit(int payLimit) {
			this.payLimit = payLimit;
		}


		public int getEachPrice() {
			return eachPrice;
		}


		public void setEachPrice(int eachPrice) {
			this.eachPrice = eachPrice;
		}


		public int getMonthPrice() {
			return monthPrice;
		}


		public void setMonthPrice(int monthPrice) {
			this.monthPrice = monthPrice;
		}


		public String getMonthVipcode() {
			return monthVipcode;
		}


		public void setMonthVipcode(String monthVipcode) {
			this.monthVipcode = monthVipcode;
		}


		public PlotAspects[] getPlotAspects() {
			return plotAspects;
		}


		public void setPlotAspects(PlotAspects[] plotAspects) {
			this.plotAspects = plotAspects;
		}


		public int getPpvpriority() {
			return ppvpriority;
		}


		public void setPpvpriority(int ppvpriority) {
			this.ppvpriority = ppvpriority;
		}


		public int getOnlyppv() {
			return onlyppv;
		}


		public void setOnlyppv(int onlyppv) {
			this.onlyppv = onlyppv;
		}


		public static class PlotAspects{
			private int time;
			private String title;
			public int getTime() {
				return time;
			}
			public void setTime(int time) {
				this.time = time;
			}
			public String getTitle() {
				return title;
			}
			public void setTitle(String title) {
				this.title = title;
			}
			
		}
		
		
		public static class ViewPoints{
			private String name;
			private int timePoint;
			public String getName() {
				return name;
			}
			public void setName(String name) {
				this.name = name;
			}
			public int getTimePoint() {
				return timePoint;
			}
			public void setTimePoint(int timePoint) {
				this.timePoint = timePoint;
			}
			
		}
		
		
		public static class Videos{
			private String vid;
			private String quality;
			private String length;
			private String qualityid;
			private String qualityName;
			private String playUrl;
			public String getVid() {
				return vid;
			}
			public void setVid(String vid) {
				this.vid = vid;
			}
			public String getQuality() {
				return quality;
			}
			public void setQuality(String quality) {
				this.quality = quality;
			}
			public String getLength() {
				return length;
			}
			public void setLength(String length) {
				this.length = length;
			}
			public String getQualityid() {
				return qualityid;
			}
			public void setQualityid(String qualityid) {
				this.qualityid = qualityid;
			}
			public String getQualityName() {
				return qualityName;
			}
			public void setQualityName(String qualityName) {
				this.qualityName = qualityName;
			}
			public String getPlayUrl() {
				return playUrl;
			}
			public void setPlayUrl(String playUrl) {
				this.playUrl = playUrl;
			}
			
		}
		
	}
}
