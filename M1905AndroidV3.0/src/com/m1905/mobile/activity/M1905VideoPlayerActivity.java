package com.m1905.mobile.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dianxin.mobilefree.R;
import com.lidroid.xutils.exception.DbException;
import com.m1905.mobile.adapter.MainAdapter;
import com.m1905.mobile.bean.ContentPath;
import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.common.Constant;
import com.m1905.mobile.common.FavouriteContent;
import com.m1905.mobile.common.HistoryContent;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.content.ReleatedContent;
import com.m1905.mobile.content.VideoBuyContent;
import com.m1905.mobile.content.VideoInfoContent;
import com.m1905.mobile.download.DownloadManager;
import com.m1905.mobile.download.DownloadService;
import com.m1905.mobile.service.FavouriteService;
import com.m1905.mobile.service.HistoryService;
import com.m1905.mobile.ui.SyncHorizontalScrollView;
import com.m1905.mobile.ui.VideoView;
import com.m1905.mobile.util.LogUtils;
import com.m1905.mobile.util.NetHttpConnection;
import com.m1905.mobile.util.Result;
import com.m1905.mobile.util.StringUtils;
import com.m1905.mobile.window.BuyWindow;
import com.m1905.mobile.window.LoginWindow;
import com.m1905.mobile.window.ZhiBuyWindow;
import com.telecomsdk.phpso.TysxOA;
import com.umeng.analytics.MobclickAgent;

public class M1905VideoPlayerActivity extends Activity implements
		OnClickListener {

	// UI
	private SyncHorizontalScrollView mhsv;
	private RelativeLayout rl_scroll;
	private RadioGroup tab_content;
	private ImageView cursor;
	private ViewPager vPager;
	private VideoView videoPlayer;
	// 播放器
	private GestureDetector mGestureDetector = null;
	private AudioManager mAudioManager = null;
	/**
	 * 播放器进度条
	 */
	private View mSoundView = null;
	private PopupWindow mSoundWindow = null;
	private int maxVolume = 0;
	private int currentVolume = 0;
	private KeyguardManager mKeyguardManager = null;
	private KeyguardLock mKeyguardLock = null;
	// 视频控制器
	private RelativeLayout rltController, rltVideoView;

	private Button btnBack, btnDownload, btnFavorite, btnPhoto,
			btnMicFullScreen;
	/**
	 * 播放按钮
	 */
	private Button btnMicPlay;
	private ImageButton volumeBtn;
	// 时间进度条和声音进度条
	private com.m1905.mobile.ui.SeekBar seekbar_sound;
	private boolean isPlaying = false;
	private boolean isFull = false;
	private Animation fullAnim = null;
	private ProgressBar pbrBuffer = null;
	private int screenWidth = 0, screenHeight = 0;
	/**
	 * 记录静音前的声音
	 */
	private int saveCurrentVolume = 5;
	/**
	 * 保存播放状态
	 */
	private boolean bSaveIsPlayingStatus = false;
	private View fastforwardView;
	protected PopupWindow fastforwardWindow;
	// 影片总时间
	private static int totalTime;
	private SeekBar seekBar_time = null;// mini播放器进度条
	// 是否缓冲
	private static boolean CAN_BUFFER = true;
	private int CHANGE_URL_TIME = 0;
	private boolean CHANGE_URL = false;
	// 初始化 拖放时间、是否滑动视频
	private boolean isSeekTo = false;
	private int seekTime = 0;
	private boolean isShowingPromptVipMonth;
	// DATA
	private MainAdapter mainAdapter;
	private List<RadioButton> rb_pages = new ArrayList<RadioButton>();
	private int mCurrentCheckedRadioLeft;// 当前的Tab距离左侧的距离
	private List<View> listViews = new ArrayList<View>();
	private LayoutInflater mInflater;
	private int cursorWidth;// 宽度
	// private String[] rb_pageStr = { "详情", "相关", "购买" };
	private String[] rb_pageStr = { "详情", "相关" };
	private VideoInfoContent videoInfoContent; 
	private ReleatedContent releatedContent;
	// private VideoBuyContent videoBuyContent;
	private int id;// 类容id
	private int type;// 类容类型
	private DownloadManager downloadManager;
	private View vs;

	@Override
	protected void onResume() {
		super.onResume();
		CAN_BUFFER = true;
		System.out.println("运行onResume" + isFull);
		MobclickAgent.onResume(M1905VideoPlayerActivity.this);
		int checkId = 0;
		tab_content.removeView(rb_pages.get(rb_pageStr.length - 1));
		tab_content.addView(rb_pages.get(rb_pageStr.length - 1));
		if (tab_content != null && tab_content.getChildAt(checkId) != null) {
			((RadioButton) tab_content.getChildAt(checkId)).setChecked(true);
		}

		mSoundWindow = new PopupWindow(mSoundView, screenWidth / 14,
				(int) (screenHeight * (3.0 / 5.0)));

		mSoundWindow.setFocusable(false); 
		mSoundWindow.setOutsideTouchable(true);  
	}

	@Override
	protected void onPause() {
		System.out.println("运行onRause" + isFull);
		videoPlayerPause();
		super.onPause();
		System.out.println("关闭视频播放器onpause");
		if (videoPlayer.myisPlaying()) {
			System.out.println("添加播放记录");
			HistoryService historyService = new HistoryService(this);

			System.out.println("播放时长：" + videoPlayer.getmSeekWhenPrepared());
			historyService.addHistory(new HistoryContent(id + "", type,
					videoInfoContent.videoName, videoPlayer
							.getmSeekWhenPrepared(), "lalala"));
			System.out.println("添加播放记录成功");
		} else {
			System.out.println("没有播放视频");
		}
		MobclickAgent.onPause(M1905VideoPlayerActivity.this);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.HANDLER_INIT_PLAY:
				if (!TextUtils.isEmpty(videoInfoContent.playurl)) {
					System.out.println(videoInfoContent.playurl);
					ileLoading.setVisibility(View.GONE);
					if (videoInfoContent.playurl.equals("136")) {
						// 弹出订购框
						/*StringUtils.showLongToast(
								M1905VideoPlayerActivity.this, "订购之后再观看视频");*/
						bt_vip_img.setVisibility(View.VISIBLE);
					} else if (TianyiContent.user.equals("")) {
						// 弹出登陆框
						/*StringUtils.showLongToast(
								M1905VideoPlayerActivity.this, "登陆账号之后再观看视频");*/
						bt_vip_img.setVisibility(View.VISIBLE);
					} else {
						bt_vip_img.setVisibility(View.GONE);
						Bundle bundle = getIntent().getExtras();
						if (String.valueOf(bundle.getInt("time")) != null) {
							pbrBuffer.setVisibility(View.VISIBLE);
							seekTime = bundle.getInt("time");
							// seekBar_time.setProgress(bundle.getInt("time"));
							videoPlayer.seekTo(seekTime);
							videoPlayer.setVideoPath(videoInfoContent.playurl,
									seekTime);
							micVideoPlayers();
							System.out.println("当前播放时间为：：：" + seekTime);
						} else {
							videoPlayer.setVideoPath(videoInfoContent.playurl,
									0);
						}
						// videoPlayer.setVideoPath(videoInfoContent.playurl,);
					}
				} else {
					ileLoading.setVisibility(View.GONE);
					Toast.makeText(M1905VideoPlayerActivity.this, "数据加载失败",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case Constant.HANDLER_WHAT_PLAY:
				btnMicPlay.setBackgroundResource(R.drawable.ctr_play_selector);
				break;
			case Constant.HANDLER_WHAT_PAUSE:
				btnMicPlay.setBackgroundResource(R.drawable.ctr_pause_selector);
				break;
			case Constant.HANDLER_CTR_SHOW:
				showController();
				break;
			case Constant.HANDLER_CTR_HIDE:
				hideController();
				break;
			case Constant.HANDLER_WHAT_FULL:
				showFullScreenController();
				break;
			case Constant.HANDLER_WHAT_MINI:
				showMicScreenController();
				break;
			case Constant.HANDLER_WHAT_VOLUME:
				// 调整声音
				updateVolume(msg.arg1);
				break;
			case Constant.HANDLER_BUFFER_SHOW:
				pbrBuffer.setVisibility(View.VISIBLE);
				break;
			case Constant.HANDLER_BUFFER_HIDE:
				pbrBuffer.setVisibility(View.GONE);
				break;
			case Constant.HANDLER_PROGRESS_CHANGED:
				int i = videoPlayer.getCurrentPosition();
				seekBar_time.setProgress(i);
				sendEmptyMessageDelayed(Constant.HANDLER_PROGRESS_CHANGED, 100);
				break;
			case Constant.HANDLER_WHAT_REFRESH:
				// 关闭缓冲
				int cTime = videoPlayer.getCurrentPosition();
				if (cTime > 0) {
					if (pbrBuffer.getVisibility() != View.GONE) {
						mHandler.sendEmptyMessage(Constant.HANDLER_BUFFER_HIDE);
					}
					if (mHandler.hasMessages(Constant.HANDLER_WHAT_REFRESH)) {
						mHandler.removeMessages(Constant.HANDLER_WHAT_REFRESH);
						return;
					}
				}
				if (isPlaying) {
					if (pbrBuffer.getVisibility() != View.VISIBLE && cTime <= 0) {
						mHandler.sendEmptyMessage(Constant.HANDLER_BUFFER_SHOW);
					}
				} else {
					if (pbrBuffer.getVisibility() != View.GONE) {
						mHandler.sendEmptyMessage(Constant.HANDLER_BUFFER_HIDE);
					}
					if (mHandler.hasMessages(Constant.HANDLER_WHAT_REFRESH)) {
						mHandler.removeMessages(Constant.HANDLER_WHAT_REFRESH);
						return;
					}
				}
				mHandler.sendEmptyMessageDelayed(Constant.HANDLER_WHAT_REFRESH,
						Constant.REFRESH_TIME);
				break;
			case Constant.HANDLER_IN_BUY:
				buyContent(fastforwardView);
				break;
			case Constant.HANDLER_IN_LOGIN:
				toggleDealCtrBtnPlay(vs);
				break;
			case Constant.HANDLER_BUY_CONTENT:
				Result result = new Result((String) msg.obj);
				StringUtils.showLongToast(M1905VideoPlayerActivity.this,
						result.getResult());
				if (result.getResult().equals("支付成功")) {
					StringUtils.showLongToast(M1905VideoPlayerActivity.this,
							"订购成功");
				} else {
				}
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 小屏界面显示
	 */
	private void showFullScreenController() {
		System.out.println("竖屏显示1" + isFull);
		hideController();
		mSoundView.setVisibility(View.GONE);
		btnMicFullScreen
				.setBackgroundResource(R.drawable.ctr_full_screen_selector);
		if (mHandler.hasMessages(Constant.HANDLER_CTR_HIDE))
			mHandler.removeMessages(Constant.HANDLER_CTR_HIDE);
		mHandler.sendEmptyMessage(Constant.HANDLER_CTR_SHOW);
		mHandler.sendEmptyMessageDelayed(Constant.HANDLER_CTR_HIDE,
				Constant.CTR_SHOW_TIME);
		System.out.println("竖屏显示2" + isFull);
	}

	/**
	 * 大屏界面显示
	 */
	private void showMicScreenController() {
		System.out.println("横屏显示5" + isFull);
		showController(); 
		mSoundView.setVisibility(View.VISIBLE);
		btnMicFullScreen
				.setBackgroundResource(R.drawable.ctr_mic_screen_selector);
		if (mHandler.hasMessages(Constant.HANDLER_CTR_HIDE))
			mHandler.removeMessages(Constant.HANDLER_CTR_HIDE);
		mHandler.sendEmptyMessage(Constant.HANDLER_CTR_SHOW);
		mHandler.sendEmptyMessageDelayed(Constant.HANDLER_CTR_HIDE,
				Constant.CTR_SHOW_TIME);
		System.out.println("横屏显示6" + isFull);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_player); 
		LogUtils.i("===========================");
		mGestureDetector = new GestureDetector(new SimpleOnGestureListener() { 
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) { 
				toggleShowVideoController();
				return super.onSingleTapConfirmed(e);
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				return true;
			}

			@Override
			public boolean onDown(MotionEvent e) {
				// 初始化 拖放时间、是否滑动视频
				seekTime = seekBar_time.getProgress();
				isSeekTo = false;
				return true;
			}

			// 拖动手势
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				videoPlayer.setFocusable(true);
				videoPlayer.setFocusableInTouchMode(true);
				videoPlayer.requestFocus();
				videoPlayer.requestFocusFromTouch();
				// 左右位移
				float Xchange = e1.getX() - e2.getX();
				float absXchange = Math.abs(Xchange);
				// 上下位移
				float Ychange = e1.getY() - e2.getY();
				float absYchange = Math.abs(Ychange);
				LogUtils.i("X1===" + e1.getX() + "-----Y1===" + e1.getY());
				LogUtils.i("X===" + absXchange + "-----Y===" + absYchange);
				// 播放时间改变

				if (isSeekTo
						|| (absXchange > 50 && absXchange / absYchange > 1.0)) {

					mHandler.removeMessages(Constant.HANDLER_CTR_HIDE);
					int currentTime = seekBar_time.getProgress();
					int maxTime = seekBar_time.getMax();
					int newTime = currentTime
							- (int) ((absXchange > 50 && absXchange
									/ absYchange > 1.0) ? Xchange : Ychange)
							* 100;
					newTime = newTime > maxTime ? maxTime : newTime;
					newTime = newTime < 0 ? 0 : newTime;
					// ys 记录拖放时间、修改是否可滑动视频
					seekTime = newTime;
					isSeekTo = true;
					// end
					int i = newTime;
					i /= 1000;
					int minute = i / 60;
					int hour = minute / 60;
					int second = i % 60;
					minute %= 60;
					fastforwardView.setVisibility(View.VISIBLE);
					// 手势滑动后 快进画面
					if (distanceX < 0) {
						((ImageView) fastforwardView
								.findViewById(R.id.playfast_image))
								.setImageDrawable(getResources().getDrawable(
										R.drawable.playfast));
					}
					if (distanceX > 0) {
						((ImageView) fastforwardView
								.findViewById(R.id.playfast_image))
								.setImageDrawable(getResources().getDrawable(
										R.drawable.playrewind));
					}
					((TextView) fastforwardView.findViewById(R.id.playtime))
							.setText(String.format("%02d:%02d:%02d", hour,
									minute, second));
					if (fastforwardWindow.isShowing()) {
						fastforwardWindow.update();
					} else {
						fastforwardWindow.showAtLocation(
								findViewById(R.id.rltVideoView),
								Gravity.CENTER, 0, 0);
						fastforwardWindow.update();
					}
				}
				mHandler.sendEmptyMessageDelayed(Constant.HANDLER_CTR_HIDE,
						Constant.CTR_SHOW_TIME);
				if (Math.abs(distanceX) < 40)
					mHandler.sendEmptyMessageDelayed(Constant.HANDLER_CTR_HIDE,
							Constant.CTR_SHOW_TIME);

				return true;
			}

		});
		/**
		 * 禁止锁屏回来出现手动解锁
		 */
		mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
		mKeyguardLock = mKeyguardManager.newKeyguardLock("");
		mKeyguardLock.disableKeyguard();

		videoInfoContent = new VideoInfoContent(M1905VideoPlayerActivity.this,
				mHandler, R.layout.player_video_info);
		releatedContent = new ReleatedContent(M1905VideoPlayerActivity.this,
				R.layout.player_releated);
		/*
		 * videoBuyContent = new VideoBuyContent(M1905VideoPlayerActivity.this,
		 * R.layout.player_buy);
		 */
		init();
		setListener();
		downloadManager = DownloadService
				.getDownloadManager(M1905VideoPlayerActivity.this);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && String.valueOf(bundle.getInt("id")) != null
				&& String.valueOf(bundle.getInt("type")) != null) {
			id = bundle.getInt("id");
			type = bundle.getInt("type");
		} else {
			StringUtils.showLongToast(M1905VideoPlayerActivity.this,
					"不合法的用户ID:)");
		}
		if (bundle.getString("path") != null) {
			voidpath = bundle.getString("path");
			videoInfoContent.playurl = bundle.getString("path");
			videoPlayer.setVideoPath(bundle.getString("path"), 0);
			System.out.println("播放");
			videoPlayerStart();
			videoPlayer.startAnimation(fullAnim);
			ileLoading.setVisibility(View.GONE);
			System.out.println("播放了么？" + bundle.getString("path"));
		}

	}
	private String voidpath="";
	private void init() {

		btnBack = (Button) findViewById(R.id.btn_back);
		btnDownload = (Button) findViewById(R.id.btn_download);
		btnFavorite = (Button) findViewById(R.id.btn_favorite);
		btnPhoto = (Button) findViewById(R.id.btn_photo);
		bt_vip_img = (Button) findViewById(R.id.bt_vip_img);
		ileLoading = (RelativeLayout) findViewById(R.id.ileLoading);
		ileLoading.setVisibility(View.VISIBLE);
		bt_vip_img.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnDownload.setOnClickListener(this);
		btnFavorite.setOnClickListener(this);
		btnPhoto.setOnClickListener(this);
		mInflater = LayoutInflater.from(M1905VideoPlayerActivity.this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		cursorWidth = dm.widthPixels / 2;

		mhsv = (SyncHorizontalScrollView) findViewById(R.id.mhsv);
		rl_scroll = (RelativeLayout) findViewById(R.id.rl_scroll);
		tab_content = (RadioGroup) findViewById(R.id.tab_content);
		videoPlayer = (VideoView) findViewById(R.id.fthVideoView);
		cursor = (ImageView) findViewById(R.id.cursor);
		vPager = (ViewPager) findViewById(R.id.vPager);
		LayoutParams cursor_Params = cursor.getLayoutParams();
		cursor_Params.width = cursorWidth;// 初始化滑动下标的宽
		cursor.setLayoutParams(cursor_Params); 

		mhsv.setSomeParam(rl_scroll, M1905VideoPlayerActivity.this);
		listViews.add(videoInfoContent.getView());
		listViews.add(releatedContent.getView());
		// listViews.add(videoBuyContent.getView());

		mainAdapter = new MainAdapter(listViews);
		vPager.setAdapter(mainAdapter);
		vPager.setCurrentItem(0);
		initTabContent();
		initTabValue();
		setSelector(0);
		mainAdapter.notifyDataSetChanged();
		initMiniPlayer();
		initVoice();
		initAnimation();
		// 时间快进后退
		fastforwardView = View.inflate(this, R.layout.play_time_fastforward,
				null);
		fastforwardWindow = new PopupWindow(fastforwardView,
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	}

	/**
	 * 
	 */
	private void initMiniPlayer() {
		// mini VideoPlayer
		// 时间进度条
		seekBar_time = (SeekBar) findViewById(R.id.videoplay_seekbar_time);
		seekBar_time.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekbar, int progress,
					boolean fromUser) {
				if (fromUser) {
					videoPlayer.seekTo(progress);
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				mHandler.removeMessages(Constant.HANDLER_CTR_HIDE);
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mHandler.sendEmptyMessageDelayed(Constant.HANDLER_CTR_HIDE,
						Constant.CTR_SHOW_TIME);
			}

		});
		pbrBuffer = (ProgressBar) findViewById(R.id.pbrBuffer);
		rltController = (RelativeLayout) findViewById(R.id.miniPlayerController);
		rltVideoView = (RelativeLayout) findViewById(R.id.rltVideoView);
		//videoPlayer = (VideoView) findViewById(R.id.fthVideoView);
		videoPlayer.setKeepScreenOn(true);
		videoPlayer.setFocusable(true);
		videoPlayer.setFocusableInTouchMode(true);
		videoPlayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				StringUtils.showLongToast(M1905VideoPlayerActivity.this,
						"播放失败!");
				if (pbrBuffer.getVisibility() != View.GONE)
					mHandler.sendEmptyMessage(Constant.HANDLER_BUFFER_HIDE);
				videoPlayerPause();
				return true;
			}
		});
		/**
		 * 播放器触摸
		 */
		videoPlayer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean result = false;
				if (event.getAction() == MotionEvent.ACTION_UP) {
					videoPlayer.requestFocus();
					result = mGestureDetector.onTouchEvent(event);
				}
				return result;
			}
		});
		/**
		 * 播放器准备状态
		 */
		videoPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				setVideoScale(mp.getVideoWidth(), mp.getVideoHeight());
				totalTime = videoPlayer.getDuration();
				seekBar_time.setMax(totalTime);
				seekBar_time.setProgress(videoPlayer.getCurrentPosition());
				if (isShowingPromptVipMonth || isPlaying) {
					videoPlayerPause();
				} else {
					videoPlayerStart();
					
					if (CHANGE_URL) {
						if (CHANGE_URL_TIME != 0) {
							videoPlayer.seekTo(CHANGE_URL_TIME);
						}
						CHANGE_URL = false;
					}
				}
				if (rltController.getVisibility() != View.VISIBLE) {
					showController();
				}
				mHandler.sendEmptyMessageDelayed(Constant.HANDLER_CTR_HIDE,
						Constant.CTR_SHOW_TIME);
				mHandler.sendEmptyMessage(Constant.HANDLER_PROGRESS_CHANGED);
			}
		});
		/**
		 * 播放完成状态
		 */
		videoPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				videoPlayerPause();
			}
		});

		// 缓冲进度条，暂停时也加载
		Executors.newFixedThreadPool(5).execute(new Runnable() {
			@Override
			public void run() {
				while (CAN_BUFFER) {
					int bufferTime = (int) (((float) videoPlayer
							.getBufferPercentage() / 100.0) * totalTime);
					if (videoPlayer.getBufferPercentage() <= 100) {
						seekBar_time.setSecondaryProgress(bufferTime);
					} else {
						CAN_BUFFER = false;
					}
				}
			}
		});
	}

	private void initAnimation() {

		btnMicPlay = (Button) findViewById(R.id.ctrBtnPlay);
		btnMicFullScreen = (Button) findViewById(R.id.ctrBtnMicFullScreen);
		btnMicPlay.setOnClickListener(this);
		btnMicFullScreen.setOnClickListener(this);
		fullAnim = AnimationUtils.loadAnimation(M1905VideoPlayerActivity.this,
				R.anim.anim_video_player_rotate);
		fullAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				System.out.println("屏幕转换操作开始");
				bSaveIsPlayingStatus = isPlaying;
				changeVideoPlayerSize(isFull);
				videoPlayerPause();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// 屏幕旋转后暂停、弹出媒体控制条
				// 根据当前视频状态
				if (bSaveIsPlayingStatus)
					videoPlayerStart();
				else
					videoPlayerPause();
			}
		});
	}

	private void initVoice() {
		// 音量调节
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		System.out.println("当前音量currentVolume" + currentVolume);
		// btn_sound.setAlpha(findAlphaFromSound());

		mSoundView = View.inflate(this,
				R.layout.videoplayer_layout_sound_change, null);
		LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) mSoundView
				.findViewById(R.id.videoplay_layout_sound).getLayoutParams();
		layoutParams2.height = (int) (screenHeight * (3.0 / 5.0));
		mSoundView.setLayoutParams(layoutParams2);

		seekbar_sound = (com.m1905.mobile.ui.SeekBar) mSoundView
				.findViewById(R.id.videoplay_layout_sound_change_seekbar);
		seekbar_sound.setMax(maxVolume);
		seekbar_sound.setProgress(currentVolume);
		volumeBtn = (ImageButton) mSoundView.findViewById(R.id.volumebtn);

		// 当前音量为0时
		if (currentVolume == 0) {
			volumeBtn.setImageDrawable(getResources().getDrawable(
					R.drawable.ctr_no_voice));
		}
		volumeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (currentVolume != 0) {
					saveCurrentVolume = currentVolume;
					updateVolume(0);
					seekbar_sound.setProgress(0);
				} else {
					updateVolume(saveCurrentVolume);
					seekbar_sound.setProgress(saveCurrentVolume);
				}
			}
		});
		mSoundWindow = new PopupWindow(mSoundView, screenWidth / 14,
				(int) (screenHeight * (3.0 / 5.0)));

		mSoundWindow.setFocusable(false);
		mSoundWindow.setOutsideTouchable(true);

		seekbar_sound
				.setOnSeekBarChangeListener(new com.m1905.mobile.ui.SeekBar.OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(
							com.m1905.mobile.ui.SeekBar VerticalSeekBar) {
						mHandler.sendEmptyMessageDelayed(
								Constant.HANDLER_CTR_HIDE,
								Constant.CTR_SHOW_TIME);
					}

					@Override
					public void onStartTrackingTouch(
							com.m1905.mobile.ui.SeekBar VerticalSeekBar) {
						// 记录触球前的音量
						saveCurrentVolume = VerticalSeekBar.getProgress();
						mHandler.removeMessages(Constant.HANDLER_CTR_HIDE);
					}

					@Override
					public void onProgressChanged(
							com.m1905.mobile.ui.SeekBar VerticalSeekBar,
							int progress, boolean fromUser) {
						// cancelDelayHide();
						// updateVolume(progress);
						// hideControllerDelay();
						Message msgVolume = new Message();
						msgVolume.what = Constant.HANDLER_WHAT_VOLUME;
						msgVolume.arg1 = progress;
						mHandler.sendMessage(msgVolume);
					}
				});
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(!voidpath.equals("")){
				mHandler.removeMessages(Constant.HANDLER_PROGRESS_CHANGED);
				mHandler.removeMessages(Constant.HANDLER_CTR_HIDE);
				finish();
			}else{
				if (isFull) {
					videoPlayer.startAnimation(fullAnim);
					return false;
				} else {
					mHandler.removeMessages(Constant.HANDLER_PROGRESS_CHANGED);
					mHandler.removeMessages(Constant.HANDLER_CTR_HIDE);
					finish();
				}
			}
			break;
		case KeyEvent.KEYCODE_MENU:
			toggleShowVideoController();
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			updateVolumeView();
			break;
		case KeyEvent.KEYCODE_VOLUME_UP:
			updateVolumeView();
			break;
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * 更新当前音量视图
	 */
	private void updateVolumeView() {
		currentVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		if (currentVolume == 0)
			volumeBtn.setBackgroundResource(R.drawable.ctr_no_voice);
		else
			volumeBtn.setBackgroundResource(R.drawable.ctr_has_voice);
		seekbar_sound.setProgress(currentVolume);
	}

	private void setListener() {
		tab_content.setOnCheckedChangeListener(tab_onCheckedChangeListener);
		vPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	// 页卡切换监听
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
			if (rb_pages != null && rb_pages.size() > position) {

				((RadioButton) rb_pages.get(position)).performClick();
				setSelector(position);
				if (position == 1) {
					if (!TextUtils.isEmpty(videoInfoContent.playurl)) {
						releatedContent.updatacontnt(videoInfoContent);
					}
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	// RadioGroup点击CheckedChanged监听
	private OnCheckedChangeListener tab_onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			try {
				if (tab_content != null && tab_content.getChildCount() > 0
						&& tab_content.getChildAt(checkedId) != null) {
					TranslateAnimation _TranslateAnimation = new TranslateAnimation(
							mCurrentCheckedRadioLeft,
							((RadioButton) tab_content.getChildAt(checkedId))
									.getLeft(), 0f, 0f);
					_TranslateAnimation
							.setInterpolator(new LinearInterpolator());
					_TranslateAnimation.setDuration(100);
					_TranslateAnimation.setFillAfter(true);
					cursor.startAnimation(_TranslateAnimation);
					vPager.setCurrentItem(checkedId);// 让下方ViewPager跟随上面的HorizontalScrollView切换
					mCurrentCheckedRadioLeft = ((RadioButton) tab_content
							.getChildAt(checkedId)).getLeft();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	};
	private RelativeLayout ileLoading;
	private Button bt_vip_img;

	// 初始化Tabcontent
	private void initTabContent() {
		for (int i = 0; i < rb_pageStr.length; i++) {
			RadioButton radioButton = (RadioButton) mInflater.inflate(
					R.layout.homepage_tabgroup_item, null);
			radioButton.setId(i);
			radioButton.setText(rb_pageStr[i]);
			radioButton.setLayoutParams(new LayoutParams(cursorWidth,
					LayoutParams.FILL_PARENT));
			rb_pages.add(radioButton);

		}
	}

	private void initTabValue() {
		tab_content.removeAllViews();
		for (int i = 0; i < listViews.size(); i++) {
			rb_pages.get(i).setText(rb_pageStr[i]);
			tab_content.addView(rb_pages.get(i));
		}
	}

	public void setSelector(int id) {
		for (int i = 0; i < rb_pageStr.length; i++) {
			if (id == i) {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.color.white);
				rb_pages.get(id).setBackgroundDrawable(
						new BitmapDrawable(bitmap));
				rb_pages.get(id).setTextColor(
						getResources().getColor(R.color.login_title));
				if (i > 1) {
					mhsv.smoothScrollTo((rb_pages.get(i).getWidth() * i - 90),
							0);// 设置居中
				} else {
					mhsv.smoothScrollTo(0, 0);
				}
				vPager.setCurrentItem(i);
			} else {
				rb_pages.get(i).setBackgroundDrawable(new BitmapDrawable());
				rb_pages.get(i).setTextColor(
						getResources().getColor(R.color.black));
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_vip_img:
			toggleDealCtrBtnPlay(v);
			break;
		case R.id.ctrBtnPlay:
			// 播放、暂停
			toggleDealCtrBtnPlay(v);
			break;
		case R.id.btnPlay:
			// 播放、暂停
			toggleDealCtrBtnPlay(v);
			break;
		case R.id.ctrBtnMicFullScreen:
			// 小屏、大屏切换
			videoPlayer.startAnimation(fullAnim);
			break;
		// case R.id.ctrBtnPlayMode:
		// // 播放模式
		// toggleShowPopCtrPlayMode();
		// break;
		// case R.id.ctrBtnVoice:
		// // 开关声音
		// toggleVolume();
		// break;
		// case R.id.ctrBtnItemMore:
		// // 打开显示更多
		// toggleShowPopVideoItemMore();
		// break;
		case R.id.btn_back:
			mHandler.removeMessages(Constant.HANDLER_PROGRESS_CHANGED);
			mHandler.removeMessages(Constant.HANDLER_CTR_HIDE);
			finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_right_out);
			break;
		case R.id.btn_download:

			if (ileLoading.isShown()) {
				StringUtils.showLongToast(M1905VideoPlayerActivity.this,
						"稍后数据加载完成在进行下载操作");
			} else {
				if (videoInfoContent.playurl.equals("136")) {
					// 弹出订购框
					StringUtils.showLongToast(M1905VideoPlayerActivity.this,
							"订购之后才可以下载观看");
				} else if (TianyiContent.user.equals("")) {
					// 弹出登陆框
					StringUtils.showLongToast(M1905VideoPlayerActivity.this,
							"请先登录");
				} else {
					String target = AppConfig.M1905_MOVIE_PATH
							+ videoInfoContent.videoName;
					try {
						StringUtils.showLongToast(M1905VideoPlayerActivity.this,
								"开始进行下载");
						System.out.println("开始下载");
						downloadManager.addNewDownload(
								videoInfoContent.playurl,
								videoInfoContent.videoName, target, true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
								false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
								null);
					} catch (DbException e) {
						StringUtils.showLongToast(
								M1905VideoPlayerActivity.this, "下载出错");
					}
				}
			}
			break;
		case R.id.btn_favorite:
			if (ileLoading.isShown()) {
				StringUtils.showLongToast(M1905VideoPlayerActivity.this,
						"稍后数据加载完成在进行收藏操作");
			} else {
				if (TianyiContent.user.equals("")) {
					// 弹出登陆框
					StringUtils.showLongToast(M1905VideoPlayerActivity.this,
							"请先登录");
				} else {
					FavouriteService service = new FavouriteService(
							M1905VideoPlayerActivity.this);
					boolean iffa = service.addFavourite(new FavouriteContent(id
							+ "", type, videoInfoContent.videoName));
					if (iffa) {
						StringUtils.showLongToast(
								M1905VideoPlayerActivity.this, "已经收藏此视频");
					} else {
						StringUtils.showLongToast(
								M1905VideoPlayerActivity.this, "视频收藏成功");
					}
				}
			}
			break;
		case R.id.btn_photo:
			if (!TextUtils.isEmpty(videoInfoContent.stateUrl)) {
				Intent intent = new Intent();
				intent.putExtra("address", videoInfoContent.stateUrl);
				intent.setClass(M1905VideoPlayerActivity.this, WebAct.class);
				startActivity(intent);
			} else {
				StringUtils.showLongToast(M1905VideoPlayerActivity.this,
						"该影片暂无剧照");
			}

			break;
		default:
			break;
		}
	}

	/**
	 * 开关播放器
	 */
	private void toggleDealCtrBtnPlay(final View v) {
		if (isPlaying) {
			// ss
			videoPlayerPause();
		} else {
			if (!TianyiContent.user.equals("")) {
				if(!videoInfoContent.playurl.equals("")){
					if (videoInfoContent.playurl.equals("136")) {
						buyContent(v);
					} else if (videoInfoContent.playurl.equals("917")) {

						new Thread() {
							@Override
							public void run() {
								super.run();
								TysxOA oa = new TysxOA(
										M1905VideoPlayerActivity.this);
								String paths = oa.getVideoPlayInfo(
										TianyiContent.token, "1", null, id + "",
										null, "AKDY5011101", TianyiContent.appid,
										TianyiContent.devid,
										TianyiContent.appSecret);
								System.out.println("视频路径：" + paths);
								ObjectMapper mapper = new ObjectMapper();
								try {
									ContentPath contentpath = mapper.readValue(
											paths, ContentPath.class);
									vs = v;
									if (contentpath.getCode() == 0) {
										videoInfoContent.playurl = contentpath
												.getInfo().getVideos()[1]
												.getPlayUrl();
										Bundle bundle = getIntent().getExtras();
										if (String.valueOf(bundle.getInt("time")) != null) {
											seekTime = bundle.getInt("time");
											// seekBar_time.setProgress(bundle.getInt("time"));
											videoPlayer.seekTo(seekTime);
											videoPlayer.setVideoPath(
													videoInfoContent.playurl,
													seekTime);
										} else {
											videoPlayer.setVideoPath(
													videoInfoContent.playurl, 0);
										}
										System.out.println("视频路径添加："
												+ videoInfoContent.playurl);
										mHandler.sendEmptyMessage(Constant.HANDLER_IN_LOGIN);
									} else if (contentpath.getCode() == 136) {
										mHandler.sendEmptyMessage(Constant.HANDLER_IN_BUY);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}.start();

					} else {
						bt_vip_img.setVisibility(View.GONE);
						videoPlayerStart();
					}
				}
			} else {
				LayoutInflater inflater = LayoutInflater
						.from(M1905VideoPlayerActivity.this);
				LoginWindow lw = new LoginWindow(M1905VideoPlayerActivity.this,
						inflater);
				lw.showDialog(v,mHandler);
			}

		}
	}

	/**
	 * 弹出订购窗口
	 */
	private void buyContent(View v) {
		// 弹何种支付方式订购窗
		int yys = NetHttpConnection
				.getProvidersName(M1905VideoPlayerActivity.this);
		if (yys != 4) {
			if (yys == 3) {
				if (TianyiContent.user.contains("@")) {
					LayoutInflater inflater = LayoutInflater
							.from(M1905VideoPlayerActivity.this);
					ZhiBuyWindow bw = new ZhiBuyWindow(
							M1905VideoPlayerActivity.this, inflater);
					bw.ShowDialog(v, id + "", mHandler);
				} else {
					LayoutInflater inflater = LayoutInflater
							.from(M1905VideoPlayerActivity.this);
					BuyWindow bw = new BuyWindow(M1905VideoPlayerActivity.this,
							inflater);
					bw.ShowDialog(v, id + "", videoInfoContent);
				}
			} else {
				LayoutInflater inflater = LayoutInflater
						.from(M1905VideoPlayerActivity.this);
				ZhiBuyWindow bw = new ZhiBuyWindow(
						M1905VideoPlayerActivity.this, inflater);
				bw.ShowDialog(v, id + "", mHandler);
			}
		}else{
			LayoutInflater inflater = LayoutInflater
					.from(M1905VideoPlayerActivity.this);
			ZhiBuyWindow bw = new ZhiBuyWindow(
					M1905VideoPlayerActivity.this, inflater);
			bw.ShowDialog(v, id + "", mHandler);
		}
		/*
		 * LayoutInflater inflater =
		 * LayoutInflater.from(M1905VideoPlayerActivity.this); BuyWindow bw =
		 * new BuyWindow(M1905VideoPlayerActivity.this, inflater);
		 * bw.ShowDialog(v,id+"",videoInfoContent);
		 */
	}

	/**
	 * 播放器启动
	 */
	private void videoPlayerStart() {
		videoPlayer.start();
		isPlaying = true;
		// if (selectedPosition < 0) {
		// btnMicPlay.setEnabled(false);
		// } else {
		// btnMicPlay.setEnabled(true);
		// }
		if (mHandler.hasMessages(Constant.HANDLER_WHAT_PAUSE))
			mHandler.removeMessages(Constant.HANDLER_WHAT_PAUSE);
		mHandler.sendEmptyMessage(Constant.HANDLER_WHAT_PLAY);
		mHandler.sendEmptyMessage(Constant.HANDLER_WHAT_REFRESH);
		// 播放，则保持常亮
		videoPlayer.setKeepScreenOn(true);
	}

	/**
	 * 播放器暂停
	 */
	private void videoPlayerPause() {
		videoPlayer.pause();
		isPlaying = false;
		// if (selectedPosition < 0) {
		// btnMicPlay.setEnabled(false);
		// } else {
		// btnMicPlay.setEnabled(true);
		// }
		if (mHandler.hasMessages(Constant.HANDLER_WHAT_PLAY))
			mHandler.removeMessages(Constant.HANDLER_WHAT_PLAY);
		mHandler.sendEmptyMessage(Constant.HANDLER_WHAT_PAUSE);
		// 不是全屏，则不保持常亮
		videoPlayer.setKeepScreenOn(isFull);
	}

	/**
	 * 播放器释放
	 */
	private void videoPlayerStopPlayBack() {
		videoPlayer.stopPlayback();
	}

	/**
	 * 播放器大小旋转
	 * 
	 * @param isFullScreen
	 */
	private void changeVideoPlayerSize(boolean isFullScreen) {
		if (isFullScreen) {
			System.out.println("跳转为竖屏");
			micVideoPlayer();
		} else {
			System.out.println("跳转为横屏");
			fullScreenVideoPlayer();
		}
	}

	/**
	 * 视频播放器转换为竖屏播放
	 */
	private void micVideoPlayer() {
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			getScreenSize();
		}
		findViewById(R.id.rl_content).setVisibility(View.VISIBLE);
		findViewById(R.id.vPager).setVisibility(View.VISIBLE);
		findViewById(R.id.btn_function).setVisibility(View.VISIBLE);
		rltVideoView.setLayoutParams(new RelativeLayout.LayoutParams(
				screenWidth, (int) getResources().getDimension(
						R.dimen.mini_player_height))); 
		// videoPlayer.setVideoScale(screenWidth, (int) getResources()
		// .getDimension(R.dimen.ni_height));
		mHandler.sendEmptyMessage(Constant.HANDLER_WHAT_FULL);
		isFull = false;
	}
	
	private void micVideoPlayers() {
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			getScreenSize();
			setVideoScale(screenWidth, screenHeight);
		//}
		findViewById(R.id.rl_content).setVisibility(View.VISIBLE);
		findViewById(R.id.vPager).setVisibility(View.VISIBLE);
		findViewById(R.id.btn_function).setVisibility(View.VISIBLE);
		rltVideoView.setLayoutParams(new RelativeLayout.LayoutParams(
				screenWidth, (int) getResources().getDimension(
						R.dimen.mini_player_height)));
		// videoPlayer.setVideoScale(screenWidth, (int) getResources()
		// .getDimension(R.dimen.ni_height));
		mHandler.sendEmptyMessage(Constant.HANDLER_WHAT_FULL);
		isFull = false;
	}

	/**
	 * 视频播放器转换为横屏播放
	 */
	private void fullScreenVideoPlayer() {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			getScreenSize();
			setVideoScale(screenWidth, screenHeight);
		}
		findViewById(R.id.rl_content).setVisibility(View.GONE);
		findViewById(R.id.vPager).setVisibility(View.GONE);
		findViewById(R.id.btn_function).setVisibility(View.GONE);
		rltVideoView.setLayoutParams(new RelativeLayout.LayoutParams(
				screenWidth, screenHeight));
		mHandler.sendEmptyMessage(Constant.HANDLER_WHAT_MINI);
		isFull = true;
	}

	private void getScreenSize() {
		Display display = getWindowManager().getDefaultDisplay();
		screenHeight = display.getHeight();
		screenWidth = display.getWidth();
	}

	/**
	 * 媒体控制器开关，当前状态开，则关闭，反之，则开，，5秒后自动关闭
	 */
	private void toggleShowVideoController() {
		if (rltController.getVisibility() != View.VISIBLE) {
			if (mHandler.hasMessages(Constant.HANDLER_CTR_HIDE))
				mHandler.removeMessages(Constant.HANDLER_CTR_HIDE);
			mHandler.sendEmptyMessage(Constant.HANDLER_CTR_SHOW);
			mHandler.sendEmptyMessageDelayed(Constant.HANDLER_CTR_HIDE,
					Constant.CTR_SHOW_TIME);
		} else {
			if (mHandler.hasMessages(Constant.HANDLER_CTR_SHOW))
				mHandler.removeMessages(Constant.HANDLER_CTR_SHOW);
			mHandler.sendEmptyMessage(Constant.HANDLER_CTR_HIDE);
		}
	}

	/**
	 * 显示媒体控制器
	 */
	private void showController() {
		if (mHandler.hasMessages(Constant.HANDLER_CTR_HIDE))
			mHandler.removeMessages(Constant.HANDLER_CTR_HIDE);
		// if (isFull) {
		// popVideoTitle.showAtLocation(videoPlayer, Gravity.TOP, 0, 0);
		// }
		if (rltController.getVisibility() != View.VISIBLE) {
			rltController.setVisibility(View.VISIBLE);
		}
		mSoundView.setVisibility(View.VISIBLE);

		mSoundWindow.showAtLocation(findViewById(R.id.rltVideoView),
				Gravity.LEFT | Gravity.CENTER, 20, 10);
		mSoundWindow.update();
		System.out.println("显示媒体控制器" + isFull);
	}

	/**
	 * 关闭媒体控制器
	 */
	private void hideController() {
		// hidePopCtrBrightness();
		// hidePopCtrPlayMode();
		// hidePopCtrScreenMode();
		// hidePopVideoItemMore();
		if (mHandler.hasMessages(Constant.HANDLER_CTR_SHOW))
			mHandler.removeMessages(Constant.HANDLER_CTR_SHOW);
		if (rltController.getVisibility() == View.VISIBLE) {
			rltController.setVisibility(View.GONE);
		}
		// if(mSoundView.getVisibility() == View.VISIBLE)
		// {
		// mSoundView.setVisibility(View.GONE);
		// }
		if (mSoundWindow.isShowing()) {
			mSoundView.setVisibility(View.INVISIBLE);
			mSoundWindow.dismiss();
		}
		if (fastforwardWindow.isShowing()) {
			fastforwardView.setVisibility(View.INVISIBLE);
			fastforwardWindow.dismiss();
		}
		System.out.println("关闭媒体控制器" + isFull);
		// if (popVideoTitle.isShowing())
		// popVideoTitle.dismiss();
	}

	/**
	 * 关闭声音
	 */
	private void closeVolume() {
		saveCurrentVolume = seekbar_sound.getProgress();
		seekbar_sound.setProgress(0);
	}

	/**
	 * 恢复声音
	 */
	private void resumeVolume() {
		seekbar_sound.setProgress(saveCurrentVolume);
	}

	/**
	 * 开关声音
	 */
	private void toggleVolume() {
		if (seekbar_sound.getProgress() != 0)
			closeVolume();
		else
			resumeVolume();
	}

	/**
	 * 声音调节
	 * 
	 * @param index
	 *            声音大小
	 */
	private void updateVolume(int index) {
		if (mAudioManager != null) {
			mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
			currentVolume = index;
		}
		if (currentVolume == 0)
			volumeBtn.setBackgroundResource(R.drawable.ctr_no_voice);
		else
			volumeBtn.setBackgroundResource(R.drawable.ctr_has_voice);
	}

	/**
	 * 视频和手机屏幕比例缩放 参数为视频源大小
	 * 
	 * @param defaultWidth
	 * @param defaultHeight
	 */
	private void setVideoScale(double defaultWidth, double defaultHeight) {
		int mWidth = screenWidth;
		int mHeight = screenHeight;
		LogUtils.e("mw=" + mWidth + "---mh=" + mHeight);
		LogUtils.e("dw=" + defaultWidth + "---dh=" + defaultHeight);
		if (defaultWidth > 0 && defaultHeight > 0) {
			double wScale = mWidth / defaultWidth;
			double hScale = mHeight / defaultHeight;
			LogUtils.e("wScale=" + wScale + "--hScale=" + hScale);
			if (wScale > hScale) {
				mWidth = (int) (defaultWidth * hScale);
				mHeight = (int) (defaultHeight * hScale);
			} else {
				mWidth = (int) (defaultWidth * wScale);
				mHeight = (int) (defaultHeight * wScale);
			}
		}
		LogUtils.e("mw=" + mWidth + "---mh=" + mHeight);
		videoPlayer.setVideoScale(mWidth, mHeight);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		boolean result = false;
		result = mGestureDetector.onTouchEvent(event);
		// 触控弹起情况
		if (event.getAction() == MotionEvent.ACTION_UP) {
			videoPlayer.requestFocus();
			// 可滑动视频，快进、快退操作
			if (isSeekTo) {
				videoPlayer.seekTo(seekTime);
				isSeekTo = false;
			}
		}
		if (!result) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
			}
			result = super.onTouchEvent(event);
		}

		return result;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// @Override
		// public void onConfigurationChanged(Configuration newConfig) {
		// super.onConfigurationChanged(newConfig);
		// getScreenSize();
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			LogUtils.i("+++++++++++++ORIENTATION_LANDSCAPE++++++++++++++++");
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			LogUtils.i("+++++++++++++ORIENTATION_PORTRAIT++++++++++++++++");
		}

	}

	@Override
	protected void onDestroy() {
		System.out.println("关闭视频播放器ondestroy" + isFull);
		CAN_BUFFER = false;
		videoPlayerStopPlayBack();
		mKeyguardLock.reenableKeyguard();
		if (rltController.getVisibility() == View.VISIBLE) {
			rltController.setVisibility(View.GONE);
		}
		mHandler.removeMessages(Constant.HANDLER_CTR_HIDE);
		mHandler.removeMessages(Constant.HANDLER_PROGRESS_CHANGED);

		if (videoPlayer.isPlaying()) {
			videoPlayer.stopPlayback();
		}
		mKeyguardLock.reenableKeyguard();
		isShowingPromptVipMonth = false;
		super.onDestroy();
	}

}
