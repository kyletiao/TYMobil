package com.m1905.mobile.activity;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.m1905.mobile.adapter.Video2Adapter;
import com.m1905.mobile.adapter.VideoAdapter;
import com.m1905.mobile.bean.UpdataBean;
import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.dao.HomePage;
import com.m1905.mobile.dao.Top;
import com.m1905.mobile.net.InitService;
import com.m1905.mobile.ui.XGridView;
import com.m1905.mobile.ui.XViewFlipper;
import com.m1905.mobile.util.AppUtils;
import com.m1905.mobile.util.DeviceUtils;
import com.m1905.mobile.util.LogUtils;
import com.m1905.mobile.window.UpdataWindow;
import com.telecomsdk.phpso.TysxOA;
import com.umeng.analytics.MobclickAgent;

/**
 * 推荐
 * 
 * @author forcetech
 * 
 */
public class RecommentAct extends Activity implements OnTouchListener {
	private int scrollPosition = 0;
	private BitmapUtils topBitmapUtil;
	private BitmapDisplayConfig config;
	private VideoAdapter recoAdapter = null, latestAdapter = null,
			hotAdapter = null, cctv6Adapter = null;
	// 推荐数据
	private HomePage homePage;
	private LinearLayout lltContentInfo;
	private PullToRefreshScrollView svwContent;
	private ScrollView scrollView;
	// 网络错误
	private RelativeLayout ileNetError;
	private RelativeLayout ileLoading;
	// 控制头图显示
	private RelativeLayout rltTopImgs;
	private XViewFlipper vfrTopImgs;
	private TextView tvwTopInfo;
	private ImageView dotCurrent = null;
	private RelativeLayout rltTopInfo;
	private LinearLayout lltTopDots;
	private boolean isFling = false;
	private boolean isRefresh = false;
	// 头图动画
	private Animation slideLeftIn, slideLeftOut, slideRightIn, slideRightOut,
			animation_in, animation_out, alpha_in; // alpha_out;
	// 头图手势
	private GestureDetector mGestureDetector = new GestureDetector(
			new SimpleOnGestureListener() {
				@Override
				public boolean onSingleTapUp(MotionEvent e) {
					isFling = false;
					return false;
				}

				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {
					float disX = e1.getX() - e2.getX();
					float disY = e1.getY() - e2.getY();
					float disAbsX = Math.abs(disX);
					float disAbsY = Math.abs(disY);
					isFling = true;
					if (disX < -50 && disAbsX > disAbsY) {// fling right
						LogUtils.i("fling right");
						flingToRight();
						return true;
					} else if (disX > 50 && disAbsX > disAbsY) {// fling left
						LogUtils.i("fling left");
						flingToLeft();
						return true;
					}
					LogUtils.i(" no fling");
					return super.onFling(e1, e2, velocityX, velocityY);
				}

			});

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_recomment);
		init();
		loadData(true);
		ifUpdataVersion();
	}

	@Override
	protected void onResume() {
		selfFlipping();
		notifyDataView();
		scrollView.fling(scrollPosition);// .scrollTo(0, scrollPosition);
		super.onResume();
		MobclickAgent.onResume(RecommentAct.this);
	}

	@Override
	protected void onStop() {
		stopFlipping();
		scrollPosition = scrollView.getScrollY();
		super.onStop();
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(RecommentAct.this);
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		config = new BitmapDisplayConfig();
		config.setBitmapConfig(Bitmap.Config.RGB_565);
		config.setLoadingDrawable(getResources().getDrawable(
				R.drawable.default_top));
		config.setLoadFailedDrawable(getResources().getDrawable(
				R.drawable.default_top));
		topBitmapUtil = new BitmapUtils(RecommentAct.this,
				AppConfig.M1905_CACHE_PATH);
		// 动画
		animation_in = AnimationUtils.loadAnimation(this, R.anim.animation_in);
		animation_in.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				showTopInfo();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}
		});
		animation_out = AnimationUtils
				.loadAnimation(this, R.anim.animation_out);
		alpha_in = AnimationUtils.loadAnimation(this, R.anim.alpha_in);
		alpha_in.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				if (dotCurrent != null)
					dotCurrent.setBackgroundResource(R.drawable.dot_n);
				dotCurrent = (ImageView) findViewById(vfrTopImgs
						.getDisplayedChild());
				dotCurrent.setBackgroundResource(R.drawable.dot_s);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				tvwTopInfo.setText(homePage.getTopData().getTopList()
						.get(vfrTopImgs.getDisplayedChild()).getTitle());
			}
		});
		FlingAnimationListener listener = new FlingAnimationListener();
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		// slideLeftIn.setAnimationListener(listener);
		slideLeftOut = AnimationUtils
				.loadAnimation(this, R.anim.slide_left_out);
		slideLeftOut.setAnimationListener(listener);
		slideRightIn = AnimationUtils
				.loadAnimation(this, R.anim.slide_right_in);
		// slideRightIn.setAnimationListener(listener);
		slideRightOut = AnimationUtils.loadAnimation(this,
				R.anim.slide_right_out);
		slideRightOut.setAnimationListener(listener);
		// 控件
		ileNetError = (RelativeLayout) findViewById(R.id.ileNetError);
		ileNetError.findViewById(R.id.btnRefresh).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						isRefresh = true;
						loadData(true);
					}
				});
		ileLoading = (RelativeLayout) findViewById(R.id.ileLoading);
		svwContent = (PullToRefreshScrollView) findViewById(R.id.svwContent);
		svwContent.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> arg0) {
				isRefresh = true;
				loadData(false);
			}
		});
		scrollView = svwContent.getRefreshableView();

		View view = LayoutInflater.from(this).inflate(
				R.layout.act_reco_content, null);
		lltContentInfo = (LinearLayout) view.findViewById(R.id.lltContentInfo);
		rltTopImgs = (RelativeLayout) view.findViewById(R.id.rltTopImgs);
		rltTopInfo = (RelativeLayout) view.findViewById(R.id.rltTopInfo);
		rltTopInfo.getBackground().setAlpha(90);
		vfrTopImgs = (XViewFlipper) view.findViewById(R.id.vfrTopImgs);
		vfrTopImgs.setFlipInterval(3000);
		vfrTopImgs.setInAnimation(slideLeftIn);
		vfrTopImgs.setOutAnimation(slideLeftOut);
		vfrTopImgs.setScrollView(scrollView);
		vfrTopImgs.setGestureDetector(mGestureDetector);
		vfrTopImgs.setOnTouchListener(this);
		tvwTopInfo = (TextView) view.findViewById(R.id.tvwTopInfo);
		lltTopDots = (LinearLayout) view.findViewById(R.id.lltTopDots);
		scrollView.addView(view);
	}

	private void loadData(boolean isLoadingView) {
		if (ileNetError.getVisibility() != View.GONE)
			ileNetError.setVisibility(View.GONE);
		if (isLoadingView)
			ileLoading.setVisibility(View.VISIBLE);
		else
			ileLoading.setVisibility(View.GONE);
		new AsyncLoader().execute();
	}

	/**
	 * 加载头图数据
	 */
	private void initTopView() {
		if (homePage == null || homePage.getTopDataSize() <= 0) {
			rltTopImgs.setVisibility(View.GONE);
			return;
		}
		// 清除旧的界面
		vfrTopImgs.removeAllViews();
		lltTopDots.removeAllViews();
		int size = homePage.getTopDataSize();
		size = size > 6 ? 6 : size;
		// 设置dot大小、间距
		ImageView img;
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 0, 10, 0);
		Top top = null;
		for (int index = 0; index < size; index++) {
			img = new ImageView(RecommentAct.this);
			img.setId(index);
			img.setBackgroundResource(R.drawable.dot_n);
			img.setLayoutParams(lp);
			lltTopDots.addView(img);
			// top 图
			View view = LayoutInflater.from(this).inflate(R.layout.top_item,
					null);
			top = homePage.getTopData().getTopList().get(index);
			ImageView imgTop = (ImageView) view.findViewById(R.id.ivwTopImg);
			topBitmapUtil.display(imgTop, top.getImg(), config, null);
			android.view.ViewGroup.LayoutParams params = imgTop
					.getLayoutParams();
			params.width = DeviceUtils.getDisplayMetrics(this).widthPixels;
			params.height = getTopImageHeight();
			imgTop.setLayoutParams(params);
			LogUtils.i("头图大小：" + params.width + "-" + params.height);
			view.setTag(top.getTitle());
			final int i = index;
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 滑动操作，非点击操作
					if (isFling)
						return;
					/*AppUtils.toastShowMsg(RecommentAct.this, v.getTag()
							.toString());*/
					Intent intent = new Intent(RecommentAct.this,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id", homePage.getTopData().getTopList().get(i).getId());
					intent.putExtra("type", homePage.getTopData().getTopList().get(i).getType());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_right_out);
				}
			});
			vfrTopImgs.addView(view);
		}
		showTopInfo();
		rltTopImgs.setVisibility(View.VISIBLE);
	}

	private void initContentView() {
		// 此处判断不完整，仅供测试，显示效果 所有视频不存在，则关闭显示
		if (homePage == null || homePage.getContentSize() <= 0) {
			lltContentInfo.setVisibility(View.GONE);
			return;
		}
		// 强片推荐
		View view = lltContentInfo.findViewById(R.id.ileRecoVideos);
		if (homePage.getTodayDataSize() <= 0) {
			view.setVisibility(View.GONE);
		} else {
			XGridView gvwRecoVideos = (XGridView) view
					.findViewById(R.id.gvwRecoVideos);
			TextView tvwRecoTitle = (TextView) view
					.findViewById(R.id.tvwRecoTitle);
			tvwRecoTitle.setText(homePage.getTodayData().getTitle());
			recoAdapter = new VideoAdapter(this, homePage.getTodayData()
					.getContentList());
			gvwRecoVideos.setAdapter(recoAdapter);
			gvwRecoVideos.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(RecommentAct.this,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id", homePage.getTodayData()
							.getContentList().get(position).getId());
					intent.putExtra("type", homePage.getTodayData()
							.getContentList().get(position).getType());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_right_out);
				}

			});
			view.setVisibility(View.VISIBLE);
		}
		// 新片预告
		view = lltContentInfo.findViewById(R.id.ileNewVideos);
		if (homePage.getLatestDataSize() <= 0) {
			view.setVisibility(View.GONE);
		} else {
			BitmapUtils bitmapUtil = new BitmapUtils(RecommentAct.this,
					AppConfig.M1905_CACHE_PATH);
			TextView tvwRecoTitles = (TextView) view
					.findViewById(R.id.tvwRecoTitles);
			TextView tv_top_trailer = (TextView) view
					.findViewById(R.id.tv_top_trailer);
			TextView tv_trailer_one = (TextView) view
					.findViewById(R.id.tv_trailer_one);
			TextView tv_trailer_two = (TextView) view
					.findViewById(R.id.tv_trailer_two);
			TextView tv_trailer_three = (TextView) view
					.findViewById(R.id.tv_trailer_three);
			TextView tvwVideotop = (TextView) view
					.findViewById(R.id.tvwVideotop);
			TextView tvwVideoone = (TextView) view
					.findViewById(R.id.tvwVideoone);
			TextView tvwVideotwo = (TextView) view
					.findViewById(R.id.tvwVideotwo);
			TextView tvwVideothree = (TextView) view
					.findViewById(R.id.tvwVideothree);
			ImageView iv_top_trailer = (ImageView) view
					.findViewById(R.id.iv_top_trailer);
			iv_top_trailer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(RecommentAct.this,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id", homePage.getLatestData()
							.getContentList().get(0).getId());
					intent.putExtra("type", homePage.getLatestData()
							.getContentList().get(0).getType());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_right_out);
				}
			});

			ImageView iv_trailer_one = (ImageView) view
					.findViewById(R.id.iv_trailer_one);
			iv_trailer_one.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(RecommentAct.this,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id", homePage.getLatestData()
							.getContentList().get(1).getId());
					intent.putExtra("type", homePage.getLatestData()
							.getContentList().get(1).getType());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_right_out);
				}
			});
			ImageView iv_trailer_two = (ImageView) view
					.findViewById(R.id.iv_trailer_two);
			iv_trailer_two.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(RecommentAct.this,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id", homePage.getLatestData()
							.getContentList().get(2).getId());
					intent.putExtra("type", homePage.getLatestData()
							.getContentList().get(2).getType());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_right_out);
				}
			});
			ImageView iv_trailer_three = (ImageView) view
					.findViewById(R.id.iv_trailer_three);
			iv_trailer_three.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(RecommentAct.this,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id", homePage.getLatestData()
							.getContentList().get(3).getId());
					intent.putExtra("type", homePage.getLatestData()
							.getContentList().get(3).getType());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_right_out);
				}
			});
			tvwRecoTitles.setText(homePage.getLatestData().getTitle());
			System.out
					.println("图片：：：：：："
							+ homePage.getLatestData().getContentList().get(0)
									.getImg());
			bitmapUtil.display(iv_top_trailer, homePage.getLatestData()
					.getContentList().get(0).getImg());
			tv_top_trailer.setText(homePage.getLatestData().getContentList()
					.get(0).getTitle());
			tv_trailer_one.setText(homePage.getLatestData().getContentList()
					.get(1).getTitle());
			tv_trailer_two.setText(homePage.getLatestData().getContentList()
					.get(2).getTitle());
			tv_trailer_three.setText(homePage.getLatestData().getContentList()
					.get(3).getTitle());
			
			tvwVideotop.setText(homePage.getLatestData().getContentList()
					.get(0).getUrl());
			tvwVideoone.setText(homePage.getLatestData().getContentList()
					.get(1).getUrl());
			tvwVideotwo.setText(homePage.getLatestData().getContentList()
					.get(2).getUrl());
			tvwVideothree.setText(homePage.getLatestData().getContentList()
					.get(3).getUrl());
			
			bitmapUtil.display(iv_trailer_one, homePage.getLatestData()
					.getContentList().get(1).getImg());
			bitmapUtil.display(iv_trailer_two, homePage.getLatestData()
					.getContentList().get(2).getImg());
			bitmapUtil.display(iv_trailer_three, homePage.getLatestData()
					.getContentList().get(3).getImg());
			view.setVisibility(View.VISIBLE);
			/*
			 * XGridView gvwNewVideos = (XGridView) view
			 * .findViewById(R.id.gvwRecoVideos); TextView tvwRecoTitle =
			 * (TextView) view .findViewById(R.id.tvwRecoTitle);
			 * tvwRecoTitle.setText(homePage.getLatestData().getTitle());
			 * latestAdapter = new VideoAdapter(this, homePage.getLatestData()
			 * .getContentList()); gvwNewVideos.setAdapter(latestAdapter);
			 * gvwNewVideos.setOnItemClickListener(new OnItemClickListener() {
			 * 
			 * @Override public void onItemClick(AdapterView<?> parent, View
			 * view, int position, long id) { Intent intent = new
			 * Intent(RecommentAct.this, M1905VideoPlayerActivity.class);
			 * intent.putExtra("id", homePage.getLatestData()
			 * .getContentList().get(position).getId()); intent.putExtra("type",
			 * homePage.getLatestData()
			 * .getContentList().get(position).getType());
			 * startActivity(intent);
			 * overridePendingTransition(R.anim.push_left_in,
			 * R.anim.push_right_out); }
			 * 
			 * }); view.setVisibility(View.VISIBLE);
			 */
		}
		// 好莱坞
		view = lltContentInfo.findViewById(R.id.ileHotVideos);
		if (homePage.getHotDataSize() <= 0) {
			view.setVisibility(View.GONE);
		} else {
			XGridView gvwHotVideos = (XGridView) view
					.findViewById(R.id.gvwRecoVideos);
			TextView tvwRecoTitle = (TextView) view
					.findViewById(R.id.tvwRecoTitle);
			tvwRecoTitle.setText(homePage.getHotData().getTitle());
			hotAdapter = new VideoAdapter(this, homePage.getHotData()
					.getContentList());
			gvwHotVideos.setAdapter(hotAdapter);
			gvwHotVideos.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(RecommentAct.this,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id", homePage.getHotData()
							.getContentList().get(position).getId());
					intent.putExtra("type", homePage.getHotData()
							.getContentList().get(position).getType());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_right_out);
				}

			});
			view.setVisibility(View.VISIBLE);
		}
		// 华语强档
		view = lltContentInfo.findViewById(R.id.ileCCTVVideos);
		if (homePage.getCctv6DataSize() <= 0) {
			view.setVisibility(View.GONE);
		} else {
			XGridView gvwCCTVVideos = (XGridView) view
					.findViewById(R.id.gvwRecoVideos);
			TextView tvwRecoTitle = (TextView) view
					.findViewById(R.id.tvwRecoTitle);
			tvwRecoTitle.setText(homePage.getCctv6Data().getTitle());
			cctv6Adapter = new VideoAdapter(this, homePage.getCctv6Data()
					.getContentList());
			gvwCCTVVideos.setAdapter(cctv6Adapter);
			gvwCCTVVideos.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(RecommentAct.this,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id", homePage.getCctv6Data()
							.getContentList().get(position).getId());
					intent.putExtra("type", homePage.getCctv6Data()
							.getContentList().get(position).getType());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_right_out);
				}

			});
			view.setVisibility(View.VISIBLE);
		}
		// 日韩专区
		view = lltContentInfo.findViewById(R.id.ileFreeVideos);
		if (homePage.getRhDataSize() <= 0) {
			view.setVisibility(View.GONE);
		} else {
			XGridView gvwCCTVVideos = (XGridView) view
					.findViewById(R.id.gvwRecoVideos);
			TextView tvwRecoTitle = (TextView) view
					.findViewById(R.id.tvwRecoTitle);
			tvwRecoTitle.setText(homePage.getRhData().getTitle());
			cctv6Adapter = new VideoAdapter(this, homePage.getRhData()
					.getContentList());
			gvwCCTVVideos.setAdapter(cctv6Adapter);
			gvwCCTVVideos.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(RecommentAct.this,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id", homePage.getRhData().getContentList()
							.get(position).getId());
					intent.putExtra("type", homePage.getRhData()
							.getContentList().get(position).getType());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_right_out);
				}

			});
			view.setVisibility(View.VISIBLE);
		}

		// 异色欧美
		view = lltContentInfo.findViewById(R.id.ileEsVideos);
		if (homePage.getYsDataSize() <= 0) {
			view.setVisibility(View.GONE);
		} else {
			XGridView gvwCCTVVideos = (XGridView) view
					.findViewById(R.id.gvwRecoVideos);
			TextView tvwRecoTitle = (TextView) view
					.findViewById(R.id.tvwRecoTitle);
			tvwRecoTitle.setText(homePage.getYsData().getTitle());
			cctv6Adapter = new VideoAdapter(this, homePage.getYsData()
					.getContentList());
			gvwCCTVVideos.setAdapter(cctv6Adapter);
			gvwCCTVVideos.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(RecommentAct.this,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id", homePage.getYsData().getContentList()
							.get(position).getId());
					intent.putExtra("type", homePage.getYsData()
							.getContentList().get(position).getType());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_right_out);
				}

			});
			view.setVisibility(View.VISIBLE);
		}
		// 微片
		view = lltContentInfo.findViewById(R.id.ileWpVideos);
		if (homePage.getWpDataSize() <= 0) {
			view.setVisibility(View.GONE);
		} else {
			XGridView gvwCCTVVideos = (XGridView) view
					.findViewById(R.id.gvwRecoVideos);
			TextView tvwRecoTitle = (TextView) view
					.findViewById(R.id.tvwRecoTitle);
			tvwRecoTitle.setText(homePage.getWpData().getTitle());
			cctv6Adapter = new VideoAdapter(this, homePage.getWpData()
					.getContentList());
			gvwCCTVVideos.setAdapter(cctv6Adapter);
			gvwCCTVVideos.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(RecommentAct.this,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id", homePage.getWpData().getContentList()
							.get(position).getId());
					intent.putExtra("type", homePage.getWpData()
							.getContentList().get(position).getType());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_right_out);
				}

			});
			view.setVisibility(View.VISIBLE);
		}
		// 免费专区
		view = lltContentInfo.findViewById(R.id.ileMfVideos);
		if (homePage.getMfDataSize() <= 0) {
			view.setVisibility(View.GONE);
		} else {
			XGridView gvwCCTVVideos = (XGridView) view
					.findViewById(R.id.gvwRecoVideos);
			TextView tvwRecoTitle = (TextView) view
					.findViewById(R.id.tvwRecoTitle);
			tvwRecoTitle.setText(homePage.getMfData().getTitle());
			cctv6Adapter = new VideoAdapter(this, homePage.getMfData()
					.getContentList());
			gvwCCTVVideos.setAdapter(cctv6Adapter);
			gvwCCTVVideos.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(RecommentAct.this,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id", homePage.getMfData().getContentList()
							.get(position).getId()); 
					intent.putExtra("type", homePage.getMfData()
							.getContentList().get(position).getType());
					startActivity(intent);
					overridePendingTransition(R.anim.push_left_in,
							R.anim.push_right_out);
				}

			});
			view.setVisibility(View.VISIBLE);
		}
		lltContentInfo.setVisibility(View.VISIBLE);
	}

	private void notifyDataView() {
		if (recoAdapter != null)
			recoAdapter.notifyDataSetChanged();
		if (latestAdapter != null)
			latestAdapter.notifyDataSetChanged();
		if (hotAdapter != null)
			hotAdapter.notifyDataSetChanged();
		if (cctv6Adapter != null)
			cctv6Adapter.notifyDataSetChanged();
	}

	/**
	 * 获得头图高度
	 * 
	 * @return
	 */
	private int getTopImageHeight() {
		return (int) (DeviceUtils.getDisplayMetrics(this).widthPixels / 450.0 * 250);
	}

	private void showTopInfo() {
		tvwTopInfo.startAnimation(alpha_in);
	}

	/**
	 * 向右滑动
	 */
	private void flingToRight() {
		stopFlipping();
		vfrTopImgs.setInAnimation(slideLeftIn);
		vfrTopImgs.setOutAnimation(slideRightOut);
		vfrTopImgs.showPrevious();
		showTopInfo();

	}

	/**
	 * 向左滑动
	 */
	private void flingToLeft() {
		stopFlipping();
		vfrTopImgs.setInAnimation(slideRightIn);
		vfrTopImgs.setOutAnimation(slideLeftOut);
		vfrTopImgs.showNext();
		showTopInfo();
	}

	/**
	 * 自动循环
	 */
	private void selfFlipping() {
		stopFlipping();
		vfrTopImgs.setInAnimation(null);
		vfrTopImgs.setOutAnimation(null);
		vfrTopImgs.clearAnimation();
		vfrTopImgs.setFlipInterval(3000);
		vfrTopImgs.startFlipping();
		vfrTopImgs.setInAnimation(animation_in);
		vfrTopImgs.setOutAnimation(animation_out);
	}

	/**
	 * 停止循环
	 */
	private void stopFlipping() {
		vfrTopImgs.stopFlipping();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	/**
	 * 滑动手势动画监听器
	 * 
	 * @author forcetech
	 * 
	 */
	class FlingAnimationListener implements AnimationListener {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			selfFlipping();
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	class AsyncLoader extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			homePage = InitService
					.jsonToHomePage(
							RecommentAct.this,
							"http://180.168.69.121:8089/webroot/clt4/kpcp/szyx/gzz/clt5vb/sywhq/rmtj/kvt/index.json",
							true);
			return 100;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (ileLoading.getVisibility() != View.GONE)
				ileLoading.setVisibility(View.GONE);
			svwContent.onRefreshComplete();
			initTopView();
			initContentView();
			isRefresh = false;
			if (homePage == null) {
				System.out.println("为空");
				svwContent.setVisibility(View.GONE);
				ileNetError.setVisibility(View.VISIBLE);
			} else {
				System.out.println("不为空");
				ileNetError.setVisibility(View.GONE);
				svwContent.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}
	}
	
	private TysxOA oa;
	private UpdataWindow updata;
	private UpdataBean uBean;
	private void ifUpdataVersion(){
		System.out.println("进入更新");
		LayoutInflater inflater = LayoutInflater.from(RecommentAct.this);
		updata = new UpdataWindow(RecommentAct.this, inflater);
		new Thread(){
			@Override
			public void run() {
				super.run();
				try {
					System.out.println("进入更新1");
					ObjectMapper mapper = new ObjectMapper();
					oa = new TysxOA(RecommentAct.this);
					String updContent = oa.updateApp(TianyiContent.token, TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret);
					System.out.println("更新内容："+updContent);
					PackageManager manager = RecommentAct.this.getPackageManager();
					PackageInfo info = manager.getPackageInfo(RecommentAct.this.getPackageName(), 0);
					String version = info.versionName;
					
					uBean = mapper.readValue(updContent, UpdataBean.class);
					System.out.println("服务器版本："+uBean.getInfo().getList()[0].getVersion()+"版本图片："+uBean.getInfo().getList()[0].getUpdateImg()+"更新内容："+uBean.getInfo().getList()[0].getDescription());
					
					if(uBean.getInfo().getList().length!=0){
						if(!version.equals(uBean.getInfo().getList()[0].getVersion())){
							System.out.println("进入更新2");
							handler.sendEmptyMessage(2);
						}else{
						}
					}else{
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				updata.showUpdataDialog(lltContentInfo,uBean);
				break;
			default:
				break;
			}
		}
	};

}
