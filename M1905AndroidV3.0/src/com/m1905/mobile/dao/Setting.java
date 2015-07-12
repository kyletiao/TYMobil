package com.m1905.mobile.dao;

/**
 * 设置对象
 * @author forcetech
 *
 */
public class Setting {
	private int funcNoticeId;
	private int funcDescId;
	private boolean isOn;

	public int getFuncNoticeId() {
		return funcNoticeId;
	}

	public void setFuncNoticeId(int funcNoticeId) {
		this.funcNoticeId = funcNoticeId;
	}

	public int getFuncDescId() {
		return funcDescId;
	}

	public void setFuncDescId(int funcDescId) {
		this.funcDescId = funcDescId;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}

	/**
	 * 
	 * @param funcNoticeId
	 * @param funcDescId
	 *            getString(setting.getFuncDescId())描述信息为空时，不显示描述信息
	 * @param isOn
	 */
	public Setting(int funcNoticeId, int funcDescId, boolean isOn) {
		this.funcNoticeId = funcNoticeId;
		this.funcDescId = funcDescId;
		this.isOn = isOn;
	}

}
