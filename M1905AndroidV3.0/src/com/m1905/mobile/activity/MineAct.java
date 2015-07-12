package com.m1905.mobile.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alipay.android.app.sdk.R.color;
import com.dianxin.mobilefree.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.m1905.mobile.bean.UpdataBean;
import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.dao.ETopAd;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.dao.TopAd;
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
 * 我的
 * 
 * @author leepan
 */
public class MineAct extends Activity implements OnTouchListener {
	// 整体视图
	private ScrollView svwMineContent;
	// 广告视图
	// private RelativeLayout ileAdBox;
	// private XViewFlipper vfrAdImgs;
	// private List<TopAd> lstAds; // 广告数据
	private BitmapUtils bitmapUtil;
	private BitmapDisplayConfig config;
	private boolean isFling = false;
	// 广告动画
	private Animation slideLeftIn, slideLeftOut, slideRightIn, slideRightOut,
			animation_in, animation_out;

	// 我的功能
	private XGridView gvwFunction;
	FunctionAdapter funcAdapter;
	private List<Function> lstFunctions;
	private LinearLayout btnLogin;
	private String userName;
	private int isVip;
	private TextView userNameTv;
	private Button isVipIv;
	private ImageView ivwUserPhoto;
	private TextView btnLogout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.act_mine);  
		initView();
		// 初始化
		init();
		// 功能视图
		loadFunctions();
		// new AsyncLoader().execute();
	}

	@Override
	protected void onResume() {
		/*
		 * if (lstAds != null && !lstAds.isEmpty()) selfFlipping();
		 */
		initLoginView();
		super.onResume();
		MobclickAgent.onResume(MineAct.this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(MineAct.this);
	}

	@Override
	protected void onStop() {
		stopFlipping();
		super.onStop();
	}

	private void init() {
		// 初始化广告
		// initAd();
		// 初始化功能
		initFunc();
		LayoutInflater inflater = LayoutInflater.from(MineAct.this);
		updata = new UpdataWindow(MineAct.this, inflater);
		oa = new TysxOA(MineAct.this);
		userNameTv = (TextView) findViewById(R.id.tvwUserName);
		isVipIv = (Button) findViewById(R.id.ivwVipLogo);
		ivwUserPhoto = (ImageView) findViewById(R.id.ivwUserPhoto);
		btnLogout = (TextView) findViewById(R.id.btnLogout);
		initLoginView();
	}

	private void initLoginView() {
		if (!TianyiContent.user.equals("")) {
			findViewById(R.id.iv_not_login).setVisibility(View.GONE);
			findViewById(R.id.btnLogin).setVisibility(View.GONE);
			findViewById(R.id.rltUserInfo).setVisibility(View.VISIBLE);
			userNameTv.setText(TianyiContent.user);
			/*
			 * if (Identify.currentUser.getVipInfo().getM1905Vip() == 1) {
			 * isVipIv.setVisibility(View.VISIBLE); } else if
			 * (Identify.currentUser.getVipInfo().getM1905Vip() == 0) {
			 * isVipIv.setVisibility(View.GONE); }
			 */
			isVipIv.setVisibility(View.GONE);
			btnLogout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					logout();
				}
			});
		} else {
			findViewById(R.id.iv_not_login).setVisibility(View.VISIBLE);
			findViewById(R.id.btnLogin).setVisibility(View.VISIBLE);
			findViewById(R.id.rltUserInfo).setVisibility(View.GONE);
		}
		/*
		 * if (Identify.currentUser != null) {
		 * findViewById(R.id.iv_not_login).setVisibility(View.GONE);
		 * findViewById(R.id.btnLogin).setVisibility(View.GONE);
		 * findViewById(R.id.rltUserInfo).setVisibility(View.VISIBLE);
		 * userNameTv.setText(Identify.currentUser.getUserName()); if
		 * (Identify.currentUser.getVipInfo().getM1905Vip() == 1) {
		 * isVipIv.setVisibility(View.VISIBLE); } else if
		 * (Identify.currentUser.getVipInfo().getM1905Vip() == 0) {
		 * isVipIv.setVisibility(View.GONE); } btnLogout.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { logout(); } }); } else {
		 * findViewById(R.id.iv_not_login).setVisibility(View.VISIBLE);
		 * findViewById(R.id.btnLogin).setVisibility(View.VISIBLE);
		 * findViewById(R.id.rltUserInfo).setVisibility(View.GONE); }
		 */
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				findViewById(R.id.iv_not_login).setVisibility(View.VISIBLE);
				findViewById(R.id.btnLogin).setVisibility(View.VISIBLE);
				findViewById(R.id.rltUserInfo).setVisibility(View.GONE);
				TianyiContent.user = "";
				AppUtils.toastShowMsg(MineAct.this, "注销成功");
				break;
			case 2:
				updata.showUpdataDialog(views, uBean);
				break;
			case 3:
				AppUtils.toastShowMsg(MineAct.this, "检查更新失败，请稍后再试");
				break;
			case 4:
				AppUtils.toastShowMsg(MineAct.this, "当前已经是最新版本了");
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 注销处理
	 */
	public void logout() {
		/*
		 * Intent intent = new Intent(); intent.setAction("EXIT");
		 * sendBroadcast(intent); Identify.currentUser = null;
		 */
		new Thread() {

			@Override
			public void run() {
				super.run();
				
				String logout = oa.logout(TianyiContent.token,
						TianyiContent.devid, TianyiContent.appid,
						TianyiContent.appSecret);
				if (logout.contains("OK")) {
					handler.sendEmptyMessage(1);
				}
			}
		}.start();

	}

	private void initView() {
		btnLogin = (LinearLayout) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MineAct.this, NewLoginAct.class);
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_right_out);
			}
		});
	}

	/**
	 * 初始化功能
	 */
	private void initFunc() {
		gvwFunction = (XGridView) findViewById(R.id.gvwFunction);
		gvwFunction.setSelector(new ColorDrawable(Color.TRANSPARENT));
		lstFunctions = new ArrayList<MineAct.Function>();
		funcAdapter = new FunctionAdapter(MineAct.this, lstFunctions);
		gvwFunction.setAdapter(funcAdapter);
		gvwFunction.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				doFunction(lstFunctions.get(position).getFuncNameId(),view);
			}
		});
	}

	/**
	 * 此处添加更多功能视图
	 */
	private void loadFunctions() {
		lstFunctions.clear();
		lstFunctions.add(new Function(R.drawable.btn_history,
				R.string.func_history));
		lstFunctions.add(new Function(R.drawable.btn_favourite,
				R.string.func_favorite));
		lstFunctions.add(new Function(R.drawable.btn_download,
				R.string.func_download));

		/*lstFunctions.add(new Function(R.drawable.btn_setting,
				R.string.func_setting)); */
		lstFunctions.add(new Function(R.drawable.btn_consume,
				R.string.func_consume));

		lstFunctions.add(new Function(R.drawable.btn_update,
				R.string.func_update));

		lstFunctions.add(new Function(R.drawable.btn_feedback,
				R.string.func_feedback));

		funcAdapter.notifyDataSetChanged(); 
	}

	/**
	 * 初始化广告控件
	 */
	private void initAd() {
		config = new BitmapDisplayConfig();
		config.setBitmapConfig(Bitmap.Config.RGB_565);
		config.setLoadingDrawable(getResources().getDrawable(R.drawable.t4));
		config.setLoadFailedDrawable(getResources().getDrawable(R.drawable.t4));
		bitmapUtil = new BitmapUtils(MineAct.this, AppConfig.M1905_CACHE_PATH);
		// 动画
		animation_in = AnimationUtils.loadAnimation(this, R.anim.animation_in);
		animation_out = AnimationUtils
				.loadAnimation(this, R.anim.animation_out);
		FlingAnimationListener listener = new FlingAnimationListener();
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.slide_left_in);
		slideLeftOut = AnimationUtils
				.loadAnimation(this, R.anim.slide_left_out);
		slideLeftOut.setAnimationListener(listener);
		slideRightIn = AnimationUtils
				.loadAnimation(this, R.anim.slide_right_in);
		slideRightOut = AnimationUtils.loadAnimation(this,
				R.anim.slide_right_out);
		slideRightOut.setAnimationListener(listener);
		svwMineContent = (ScrollView) findViewById(R.id.svwMineContent);
		/*
		 * ileAdBox = (RelativeLayout) findViewById(R.id.ileAdBox); vfrAdImgs =
		 * (XViewFlipper) findViewById(R.id.vfrAdImgs);
		 * vfrAdImgs.setFlipInterval(3000);
		 * vfrAdImgs.setInAnimation(slideLeftIn);
		 * vfrAdImgs.setOutAnimation(slideLeftOut);
		 * vfrAdImgs.setScrollView(svwMineContent);
		 * vfrAdImgs.setGestureDetector(mGestureDetector);
		 * vfrAdImgs.setOnTouchListener(this);
		 */
	}

	/**
	 * 向右滑动
	 */
	private void flingToRight() {
		stopFlipping();
		/*
		 * vfrAdImgs.setInAnimation(slideLeftIn);
		 * vfrAdImgs.setOutAnimation(slideRightOut); vfrAdImgs.showPrevious();
		 */
	}

	/**
	 * 向左滑动
	 */
	private void flingToLeft() {
		stopFlipping();
		/*
		 * vfrAdImgs.setInAnimation(slideRightIn);
		 * vfrAdImgs.setOutAnimation(slideLeftOut); vfrAdImgs.showNext();
		 */
	}

	/**
	 * 自动循环
	 */
	private void selfFlipping() {
		stopFlipping();
		/*
		 * vfrAdImgs.setInAnimation(null); vfrAdImgs.setOutAnimation(null);
		 * vfrAdImgs.clearAnimation(); vfrAdImgs.setFlipInterval(3000);
		 * vfrAdImgs.startFlipping(); vfrAdImgs.setInAnimation(animation_in);
		 * vfrAdImgs.setOutAnimation(animation_out);
		 */
	}

	/**
	 * 停止循环
	 */
	private void stopFlipping() {
		// vfrAdImgs.stopFlipping();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	/**
	 * 获得广告图高度
	 * 
	 * @return
	 */
	private int getAdImageHeight() {
		return (int) (DeviceUtils.getDisplayMetrics(this).widthPixels / 480.0 * 206.67);
	}

	/**
	 * 加载广告视图
	 */
	/*
	 * private void initAdView() { if (lstAds == null || lstAds.isEmpty()) {
	 * ileAdBox.setVisibility(View.GONE); return; } // 清除旧的界面
	 * vfrAdImgs.removeAllViews(); TopAd topAd = null; for (int index = 0; index
	 * < lstAds.size(); index++) { // 广告图 View view =
	 * LayoutInflater.from(this).inflate(R.layout.top_item, null); topAd =
	 * lstAds.get(index); ImageView imgTop = (ImageView)
	 * view.findViewById(R.id.ivwTopImg); bitmapUtil.display(imgTop,
	 * topAd.getImg(), config, null); android.view.ViewGroup.LayoutParams params
	 * = imgTop .getLayoutParams(); params.width =
	 * DeviceUtils.getDisplayMetrics(this).widthPixels; params.height =
	 * getAdImageHeight(); imgTop.setLayoutParams(params); view.setTag("当前第" +
	 * index + "张广告图"); view.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { // 滑动操作，非点击操作 if (isFling)
	 * return; AppUtils.toastShowMsg(MineAct.this, v.getTag().toString()); } });
	 * vfrAdImgs.addView(view); } ileAdBox.setVisibility(View.VISIBLE); //
	 * 开始自动滚动 selfFlipping(); }
	 */

	/**
	 * 异步加载广告数据
	 * 
	 * @author forcetech
	 */
	/*
	 * class AsyncLoader extends AsyncTask<Void, Void, ETopAd> {
	 * 
	 * @Override protected ETopAd doInBackground(Void... params) { return
	 * InitService.getUserTopAd(MineAct.this, false); }
	 * 
	 * @Override protected void onPostExecute(ETopAd result) { if (result !=
	 * null && !result.getLstTopAd().isEmpty()) { lstAds = result.getLstTopAd();
	 * } initAdView(); super.onPostExecute(result); } }
	 */

	/**
	 * 滑动手势动画监听器
	 * 
	 * @author forcetech
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

	/**
	 * @author forcetech
	 */
	class ViewHolder {
		ImageView ivwFuncLogo;
		TextView tvwFuncName;
	}

	class FunctionAdapter extends BaseAdapter {
		List<Function> lstFunctions;
		Context context;

		public FunctionAdapter(Context context, List<Function> lstFunctions) {
			this.lstFunctions = lstFunctions;
			this.context = context;
		}

		@Override
		public int getCount() {
			return lstFunctions.size();
		}

		@Override
		public Object getItem(int position) {
			return lstFunctions.get(position);
		}

		@Override
		public long getItemId(int position) { 
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.func_item, null);
				holder = new ViewHolder();
				holder.ivwFuncLogo = (ImageView) convertView
						.findViewById(R.id.ivwFuncLogo);
				holder.tvwFuncName = (TextView) convertView
						.findViewById(R.id.tvwFuncName);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Function func = (Function) getItem(position);
			holder.ivwFuncLogo.setImageResource(func.getFuncLogo());
			holder.tvwFuncName.setText(getString(func.getFuncNameId()));
			return convertView;
		}

	}

	/**
	 * 功能操作
	 * 
	 * @param id
	 */
	private void doFunction(int id,View view) {
		switch (id) {
		case R.string.func_history:
			startActivity(new Intent(this, HistoryAct.class));
			break;
		case R.string.func_favorite:
			startActivity(new Intent(this, FavoritesAct.class));
			break;
		case R.string.func_download:
			startActivity(new Intent(this, DownloadAct.class));
			break;
		case R.string.func_setting:
			//startActivity(new Intent(this, SettingAct.class));
			startActivity(new Intent(this,TestActivity.class));
			break;
		case R.string.func_consume:
			startActivity(new Intent(this, ConsumeAct.class));
			break;
		case R.string.func_update:
			ifUpdataVersion(view);
			break;
		case R.string.func_feedback:
			startActivity(new Intent(this, KbuyAct.class));
			break;
		default:
			AppUtils.toastShowMsg(this, "未添加此功能操作");
			break;
		}

	}

	/**
	 * 我的功能
	 * 
	 * @author forcetech
	 */
	class Function {
		private int funcLogo;
		private int funcNameId;

		public int getFuncLogo() {
			return funcLogo;
		}

		public void setFuncLogo(int funcLogo) {
			this.funcLogo = funcLogo;
		}

		public int getFuncNameId() {
			return funcNameId;
		}

		public void setFuncNameId(int funcNameId) {
			this.funcNameId = funcNameId;
		}

		public Function(int funcLogo, int funcNameId) {
			this.funcLogo = funcLogo;
			this.funcNameId = funcNameId;
		}

	}

	/**
	 * 广告手势
	 */
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
	private TysxOA oa;
	private UpdataWindow updata;
	private UpdataBean uBean;
	private View views;
	private void ifUpdataVersion(final View view){
		new Thread(){
			@Override
			public void run() {
				super.run();
				ObjectMapper mapper = new ObjectMapper();
				String updContent = oa.updateApp(TianyiContent.token, TianyiContent.devid, TianyiContent.appid, TianyiContent.appSecret);
				System.out.println("更新内容："+updContent);
				try {
					PackageManager manager = MineAct.this.getPackageManager();
					PackageInfo info = manager.getPackageInfo(MineAct.this.getPackageName(), 0);
					String version = info.versionName;
					
					uBean = mapper.readValue(updContent, UpdataBean.class);
					System.out.println("服务器版本："+uBean.getInfo().getList()[0].getVersion()+"版本图片："+uBean.getInfo().getList()[0].getUpdateImg()+"更新内容："+uBean.getInfo().getList()[0].getDescription());
					
					if(uBean.getInfo().getList().length!=0){
						if(!version.equals(uBean.getInfo().getList()[0].getVersion())){
							views = view;
							handler.sendEmptyMessage(2);
						}else{
							handler.sendEmptyMessage(4);
						}
					}else{
						handler.sendEmptyMessage(3);
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					
				}
			}
		}.start();
	}

}
