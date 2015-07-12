package com.m1905.mobile.common;


public class Constant {

	/**
	 * APP 版本信息
	 */
	/*public final static Version APP_VERSION = new Version(1,// 渠道号
			xxxx,// 版本ID，1xx表示android，不同的值表示一个唯一应用程序
			xxx,// versionCode，不需修改，使用manifest文件中android:versionCode
			"3.0.0",// 显示版本号，不需修改，使用manifest文件中android:versionName
			2013120601,// 小版本号，
			2013120601// 最近一次更新时间，时间戳
	);*/
	
	public final static Version APP_VERSION = new Version(1,// 渠道号
			3,// 版本ID，1xx表示android，不同的值表示一个唯一应用程序
			30,// versionCode，不需修改，使用manifest文件中android:versionCode
			"3.0.0",// 显示版本号，不需修改，使用manifest文件中android:versionName
			2013120601,// 小版本号，
			2013120601// 最近一次更新时间，时间戳
	);

	// 转换常量
	public final static float RATE_KB = 1024;
	public final static float RATE_M = 1024 * 1024;
	public final static float RATE_G = 1024 * 1024 * 1024;
	// 分隔符
	public final static char SPLITER = ',';
	// 新浪第三方登录地址
	public final static String SERVER_URL_SINA = "";
	// 腾讯第三方登录地址
	public final static String SERVER_URL_QQ = "";
	/**
	 * 下载删除完成Handler消息类型
	 */
	public final static int HANDLER_INT_DELETE = 1;
	public final static int HANDLER_INT_NODATA = 2;
	public final static int HANDLER_INT_SOMEDATA = 3;

	// 刷新时间数据
	public final static long REFRESH_TIME = 500;
	public final static long CTR_SHOW_TIME = 5000;
	/**
	 * 播放器中所用Handler消息类型
	 */
	public final static int HANDLER_INIT_PLAY = 1;// 初始播放
	public final static int HANDLER_BUFFER_SHOW = 2;// 显示控制
	public final static int HANDLER_BUFFER_HIDE = 3;// 隐藏控制
	public final static int HANDLER_WHAT_PLAY = 4;// 播放
	public final static int HANDLER_WHAT_PAUSE = 5;// 暂停
	public final static int HANDLER_WHAT_FULL = 6;// 全屏播放
	public final static int HANDLER_WHAT_MINI = 7;// mini播放器
	public final static int HANDLER_CTR_SHOW = 8;// 控制窗口显示
	public final static int HANDLER_CTR_HIDE = 9;// 控制窗口隐藏
	public final static int HANDLER_WHAT_VOLUME = 10;// 更新声音
	public final static int HANDLER_WHAT_REFRESH = 11;// 更新缓冲
	public final static int HANDLER_PROGRESS_CHANGED = 12;//进度条改变
	public final static int HANDLER_IN_BUY = 13;
	public final static int HANDLER_IN_LOGIN = 14;
	public final static int HANDLER_BUY_CONTENT = 15;
}
