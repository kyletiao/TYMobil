package com.m1905.mobile.common;

/**
 * 应用程序版本信息
 * 
 * @author forcetech
 * 
 */
public class Version {
	// 渠道id
	private int pid;
	private int versionId;
	private int versionCode;
	private String versionName;
	private long versionMini;
	private long lastUpdate;

	/**
	 * 获得渠道id
	 * 
	 * @return
	 */
	public int getPid() {
		return pid;
	}

	/**
	 * 设置渠道id
	 * 
	 * @param pid
	 */
	public void setPid(int pid) {
		this.pid = pid;
	}

	/**
	 * 获得android应用程序id
	 * 
	 * @return
	 */
	public int getVersionId() {
		return versionId;
	}

	/**
	 * 设置android应用程序id
	 * 
	 * @return
	 */
	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	/**
	 * 获得应用程序版本号
	 * 
	 * @return
	 */
	public int getVersionCode() {
		return versionCode;
	}

	/**
	 * 设置应用程序版本号
	 * 
	 * @return
	 */
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	/**
	 * 获得应用程序版本名称
	 * 
	 * @return
	 */
	public String getVersionName() {
		return versionName;
	}

	/**
	 * 设置应用程序版本名称
	 * 
	 * @return
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	/**
	 * 获得应用程序小版本号
	 * 
	 * @return
	 */
	public long getVersionMini() {
		return versionMini;
	}

	/**
	 * 设置应用程序小版本号
	 * 
	 * @return
	 */
	public void setVersionMini(long versionMini) {
		this.versionMini = versionMini;
	}

	/**
	 * 获得应用程序最后一次更新时间
	 * 
	 * @return
	 */
	public long getLastUpdate() {
		return lastUpdate;
	}

	/**
	 * 设置应用程序最后一次更新时间
	 * 
	 * @return
	 */
	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	/**
	 * 获得应用程序 versionId + "/" + versionCode + "/" + versionMini
	 * 
	 * @return
	 */
	public String getVer() {
		return versionId + "/" + versionCode + "/" + versionMini;
	}

	@Override
	public String toString() {
		return "Version [pid=" + pid + ", versionId=" + versionId
				+ ", versionCode=" + versionCode + ", versionName="
				+ versionName + ", versionMini=" + versionMini
				+ ", lastUpdate=" + lastUpdate + "]";
	}

	public Version(int pid, int versionId, int versionCode, String versionName,
			long versionMini, long lastUpdate) {
		this.pid = pid;
		this.versionId = versionId;
		this.versionCode = versionCode;
		this.versionName = versionName;
		this.versionMini = versionMini;
		this.lastUpdate = lastUpdate;
	}

}
