package com.m1905.mobile.dao;

/**
 * 软件升级
 * 
 * @author forcetech
 * 
 */
public class Update extends Entity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int UPDATE_STATE_NO = 0;
	public static final int UPDATE_STATE_PART = 1;
	public static final int UPDATE_STATE_ALL = 2;
	private String versionName;// 版本名称
	private int versionCode;// 版本号
	private long versionMini;// 小版本
	private int needUpdate;// 是否需要更新
	private String url;// 升级地址
	private String info; // 升级版本信息
	private String srcdate;// 小版本升级源文件
	private String dstdate;// 小版本升级目标文件
	private String versionpicx;// 启动加载图

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public long getVersionMini() {
		return versionMini;
	}

	public void setVersionMini(long versionMini) {
		this.versionMini = versionMini;
	}

	public int getNeedUpdate() {
		return needUpdate;
	}

	public void setNeedUpdate(int needUpdate) {
		this.needUpdate = needUpdate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getSrcdate() {
		return srcdate;
	}

	public void setSrcdate(String srcdate) {
		this.srcdate = srcdate;
	}

	public String getDstdate() {
		return dstdate;
	}

	public void setDstdate(String dstdate) {
		this.dstdate = dstdate;
	}

	public String getVersionpicx() {
		return versionpicx;
	}

	public void setVersionpicx(String versionpicx) {
		this.versionpicx = versionpicx;
	}

}
