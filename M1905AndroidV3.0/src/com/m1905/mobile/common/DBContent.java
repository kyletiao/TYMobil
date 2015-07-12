package com.m1905.mobile.common;

/**
 * 数据库对象
 * 
 * @author forcetech
 * 
 */
public class DBContent {
	/**
	 * 视频id
	 */
	private String id;
	/**
	 * 视频类别
	 */
	private int type;
	/**
	 * 视频名称
	 */
	private String title;

	/**
	 * 获得视频id
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置视频id
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获得视频类别
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * 设置视频类别
	 * 
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 获取视频名称
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置视频名称
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 对象初始化
	 * 
	 * @param id
	 * @param type
	 * @param title
	 */
	public DBContent(String id, int type, String title) {
		this.id = id;
		this.type = type;
		this.title = title;
	}

	@Override
	public String toString() {
		return "DBContent [id=" + id + ", type=" + type + ", title=" + title
				+ "]";
	}

}
