package com.m1905.mobile.common;

/**
 * 历史记录
 * 
 * @author forcetech
 * 
 */
public class HistoryContent extends DBContent {
	/**
	 * 视频观看时间
	 */
	private int watchTime;
	/**
	 * 视频源路径(本地视频)
	 */
	private String localPath;
    /**
     * 选中状态
     */
    private boolean isChecked;

	/**
	 * 获得视频的观看时间
	 * 
	 * @return
	 */
	public int getWatchTime() {
		return watchTime;
	}

	/**
	 * 设置观看时间
	 * 
	 * @param watchTime
	 */
	public void setWatchTime(int watchTime) {
		this.watchTime = watchTime;
	}

	/**
	 * 获得本地视频的路径
	 * 
	 * @return
	 */
	public String getLocalPath() {
		return localPath;
	}

	/**
	 * 设置视频本地播放地址
	 * 
	 * @param localPath
	 */
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    /**
	 * 初始化数据信息
	 * 
	 * @param id
	 * @param type
	 * @param title
	 * @param watchTime
	 * @param localPath
	 *            视频源本地地址
	 */
	public HistoryContent(String id, int type, String title, int watchTime,
			String localPath) {
		super(id, type, title);
		this.watchTime = watchTime;
		this.localPath = localPath;
	}

	public HistoryContent(String id, int type, String title) {
		super(id, type, title);
	}

	@Override
	public String toString() {
		return "HistoryContent [watchTime=" + watchTime + ", localPath="
				+ localPath + "]";
	}

}
