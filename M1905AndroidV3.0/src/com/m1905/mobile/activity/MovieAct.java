package com.m1905.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.m1905.mobile.adapter.MovieAdapter;
import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.dao.EVideo;
import com.m1905.mobile.dao.Video;
import com.m1905.mobile.net.VideoService;
import com.m1905.mobile.util.LogUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 电影
 * 
 * @author leepan
 * 
 */
public class MovieAct extends Activity implements OnClickListener,
		OnCheckedChangeListener, OnItemClickListener,
		OnRefreshListener2<GridView> {
	private AsyncLoader loader = null;
	private boolean isInit = true;
	private View parent;
	private RelativeLayout ileErrorBox;
	private TextView tvwNoDataNotice, tvwNoDataDesc;// 数据错误提示、描述
	private RelativeLayout ileLoadingBox;
	// 导航视图
	private Button btnVipFilm;
	private Button btnGenFilm;
	private Button btnOpenFilter;
	private PopupWindow popupFilter;
	private RadioGroup rgpFilmMtype;
	private String[] film_mtype = null;
	private RadioGroup rgpFilmArea;
	private String[] film_area = null;
	private RadioGroup rgpFilmYear;
	private String[] film_year = null;
	private RadioGroup rgpFilmMost;
	private String[] film_most = null;
	private RadioGroup rgpFilmType;
	private String[] film_type = null;
	private Button btnFilter;
	// 数据视图
	private PullToRefreshGridView reGvwData;
	private GridView gvwData;
	private MovieAdapter movieAdapter;
	private boolean isRefresh = false;
	private boolean isChanged = false;
	private int currentPage = 1;
	private List<Video> lstData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_movie);
		init();
		// 初次加载
		isChanged = true;
		loadData();
	}

	@Override
	protected void onResume() {
		movieAdapter.notifyDataSetChanged();
		super.onResume();
		MobclickAgent.onResume(MovieAct.this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(MovieAct.this);
	}

	@Override
	protected void onDestroy() {
		if (popupFilter.isShowing())
			popupFilter.dismiss();
		super.onDestroy();
	}

	private void init() {
		isInit = true;
		initNavi();
		initDataView();
		isInit = false;
	}

	/**
	 * 显示错误视图
	 * 
	 * @param errorNotice
	 * @param errorDesc
	 */
	private void showError(String errorNotice, String errorDesc) {
		reGvwData.setVisibility(View.GONE);
		ileLoadingBox.setVisibility(View.GONE);
		tvwNoDataNotice.setText(errorNotice);
		tvwNoDataDesc.setText(errorDesc);
		ileErrorBox.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示网络请求视图
	 */
	private void showLoading() {
		ileErrorBox.setVisibility(View.GONE);
		reGvwData.setVisibility(View.GONE);
		ileLoadingBox.setVisibility(View.VISIBLE);
	}

	/**
	 * 显示数据视图
	 */
	private void showDataView() {
		ileErrorBox.setVisibility(View.GONE);
		ileLoadingBox.setVisibility(View.GONE);
		reGvwData.setVisibility(View.VISIBLE);
	}

	/**
	 * 切换选项
	 */
	private void selectButton() {
		if (rgpFilmType.getCheckedRadioButtonId() == 0) {
			btnGenFilm.setSelected(false);
			btnVipFilm.setSelected(true);
		} else {
			btnVipFilm.setSelected(false);
			btnGenFilm.setSelected(true);
		}
	}

	/**
	 * 初始化导航功能
	 */
	private void initNavi() {
		parent = findViewById(R.id.rltMovie);
		ileLoadingBox = (RelativeLayout) findViewById(R.id.ileLoadingBox);
		ileErrorBox = (RelativeLayout) findViewById(R.id.ileErrorBox);
		tvwNoDataNotice = (TextView) ileErrorBox
				.findViewById(R.id.tvwNoDataNotice);
		tvwNoDataDesc = (TextView) ileErrorBox.findViewById(R.id.tvwNoDataDesc);
		// ((TextView) ileErrorBox.findViewById(R.id.tvwNoDataNotice))
		// .setText(getString(R.string.filter_noDataNotice));
		// ((TextView) ileErrorBox.findViewById(R.id.tvwNoDataDesc))
		// .setText(getString(R.string.filter_noDataDesc));
		btnVipFilm = (Button) findViewById(R.id.btnVipFilm);
		btnVipFilm.setSelected(true);
		btnGenFilm = (Button) findViewById(R.id.btnGenFilm);
		btnOpenFilter = (Button) findViewById(R.id.btnOpenFilter);
		btnOpenFilter.setOnClickListener(this);
		btnVipFilm.setOnClickListener(this);
		btnGenFilm.setOnClickListener(this);
		film_mtype = this.getResources().getStringArray(R.array.film_mtype);
		film_area = this.getResources().getStringArray(R.array.film_area);
		film_year = this.getResources().getStringArray(R.array.film_year);
		film_most = this.getResources().getStringArray(R.array.film_most);
		film_type = this.getResources().getStringArray(R.array.film_type);
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View view = mLayoutInflater.inflate(R.layout.filter_box, null);
		rgpFilmMtype = (RadioGroup) view.findViewById(R.id.rgpFilmMtype);
		rgpFilmMtype.setOnCheckedChangeListener(this);
		rgpFilmArea = (RadioGroup) view.findViewById(R.id.rgpFilmArea);
		rgpFilmArea.setOnCheckedChangeListener(this);
		rgpFilmYear = (RadioGroup) view.findViewById(R.id.rgpFilmYear);
		rgpFilmYear.setOnCheckedChangeListener(this);
		rgpFilmMost = (RadioGroup) view.findViewById(R.id.rgpFilmMost);
		rgpFilmMost.setOnCheckedChangeListener(this);
		rgpFilmType = (RadioGroup) view.findViewById(R.id.rgpFilmType);
		rgpFilmType.setOnCheckedChangeListener(this);
		btnFilter = (Button) view.findViewById(R.id.btnFilter);
		btnFilter.setOnClickListener(this);
		for (int i = 0; i < film_mtype.length; i++) {
			RadioButton rb = (RadioButton) mLayoutInflater.inflate(
					R.layout.filter_item, null);
			rb.setText(film_mtype[i]);
			rb.setId(i);
			if (i == 0)
				rb.setChecked(true);
			rgpFilmMtype.addView(rb, i);
		}

		for (int i = 0; i < film_area.length; i++) {
			RadioButton rb = (RadioButton) mLayoutInflater.inflate(
					R.layout.filter_item, null);
			rb.setText(film_area[i]);
			rb.setId(i);
			if (i == 0)
				rb.setChecked(true);
			rgpFilmArea.addView(rb, i);
		}

		for (int i = 0; i < film_year.length; i++) {
			RadioButton rb = (RadioButton) mLayoutInflater.inflate(
					R.layout.filter_item, null);
			rb.setText(film_year[i]);
			rb.setId(i);
			if (i == 0)
				rb.setChecked(true);
			rgpFilmYear.addView(rb, i);
		}

		for (int i = 0; i < film_most.length; i++) {
			RadioButton rb = (RadioButton) mLayoutInflater.inflate(
					R.layout.filter_item, null);
			rb.setText(film_most[i]);
			rb.setId(i);
			if (i == 0)
				rb.setChecked(true);
			rgpFilmMost.addView(rb, i);
		}

		for (int i = 0; i < film_type.length; i++) {
			RadioButton rb = (RadioButton) mLayoutInflater.inflate(
					R.layout.filter_item, null);
			rb.setText(film_type[i]);
			rb.setId(i);
			if (i == 0)
				rb.setChecked(true);
			rgpFilmType.addView(rb, i);
		}
		popupFilter = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupFilter.setFocusable(true);
		;
		popupFilter.setOutsideTouchable(true);
		popupFilter.setBackgroundDrawable(new BitmapDrawable());
		popupFilter.setAnimationStyle(R.style.AnimBottom);
	}

	/**
	 * 初始化数据视图
	 */
	private void initDataView() {
		lstData = new ArrayList<Video>();
		movieAdapter = new MovieAdapter(this, lstData);
		reGvwData = (PullToRefreshGridView) findViewById(R.id.reGvwData);
		reGvwData.setOnRefreshListener(this);
		reGvwData.setMode(Mode.BOTH);
		gvwData = reGvwData.getRefreshableView();
		gvwData.setHorizontalSpacing(Math.round(getResources().getDimension(
				R.dimen.gvw_horizontalSpacing)));
		gvwData.setHorizontalFadingEdgeEnabled(false);
		int padding = Math.round(getResources().getDimension(
				R.dimen.horizontalSpacing));
		gvwData.setPadding(padding, padding, padding, 0);
		gvwData.setVerticalScrollBarEnabled(false);
		gvwData.setFadingEdgeLength(0);
		gvwData.setAdapter(movieAdapter);
		gvwData.setNumColumns(3);
		gvwData.setOnItemClickListener(this);
		gvwData.setOnScrollListener(new PauseOnScrollListener(movieAdapter
				.getBitmapUtil(), false, true)); // 快速滑动时，是否允许加载图片
	}

	/**
	 * 显示筛选
	 */
	private void showPopupFilter() {
		// 设置layout在PopupWindow中显示的位置
		int[] location = new int[2];
		((View) btnVipFilm.getParent()).getLocationOnScreen(location);
		popupFilter.showAtLocation(parent, Gravity.TOP, 0, location[1]);
	}

	/**
	 * 关闭筛选
	 */
	private void closePopupFilter() {
		// 设置layout在PopupWindow中显示的位置
		popupFilter.dismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnVipFilm:
			btnOpenFilter.setVisibility(View.VISIBLE);
			// 会员视频
			btnLoadData(0);
			break;
		case R.id.btnGenFilm:
			btnOpenFilter.setVisibility(View.GONE);
			// 免费视频
			btnLoadData(1);
			break;
		case R.id.btnOpenFilter:
			// 弹出筛选框
			showPopupFilter();
			break;
		case R.id.btnFilter:
			// 关闭筛选框
			closePopupFilter();
			break;
		}

	}

	/**
	 * 会员和免费筛选button
	 * 
	 * @param type
	 */
	private void btnLoadData(int type) {
		isInit = true;
		rgpFilmType.check(type);
		isInit = false;
		currentPage = 1;
		isChanged = true;
		isRefresh = false;
		loadData();
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
		reGvwData.setScrollingWhileRefreshingEnabled(false);
		isChanged = false;
		isRefresh = true;
		currentPage = 1;
		loadData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
		reGvwData.setScrollingWhileRefreshingEnabled(true);
		isRefresh = false;
		isChanged = false;
		currentPage++;
		loadData();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(MovieAct.this,
				M1905VideoPlayerActivity.class);
		intent.putExtra("id", lstData.get(arg2).getId());
		intent.putExtra("type", lstData.get(arg2).getType());
		startActivity(intent);
		overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	}

	/**
	 * 加载数据
	 */
	private void loadData() {
		selectButton();
		if (loader != null) {
			loader.cancel(true);
		}
		if (isChanged) {
			reGvwData.onRefreshComplete();
			lstData.clear();
			movieAdapter.notifyDataSetChanged();
			showLoading();
		}
		loader = new AsyncLoader();
		loader.execute();
	}

	/**
	 * 异步加载数据
	 * 
	 * @author Administrator
	 * 
	 */
	private class AsyncLoader extends AsyncTask<Integer, Void, EVideo> {

		@Override
		protected EVideo doInBackground(Integer... arg0) {
			EVideo video = null;
			// 初始化参数
			boolean isVip = (rgpFilmType.getCheckedRadioButtonId() == 0);
			String filmMtype = String.valueOf(rgpFilmMtype
					.getCheckedRadioButtonId());
			String filmArea = String.valueOf(rgpFilmArea
					.getCheckedRadioButtonId());
			String filmYear = film_year[rgpFilmYear.getCheckedRadioButtonId()];
			String filmMost = String.valueOf(rgpFilmMost
					.getCheckedRadioButtonId());
			// 读取数据
			LogUtils.i("筛选参数信息：是否刷新：" + isRefresh + " 第几页：" + currentPage
					+ " VIP影片：" + isVip + " 类型："
					+ film_mtype[rgpFilmMtype.getCheckedRadioButtonId()]
					+ " 地区：" + filmArea + " 年份：" + filmYear + " 排序方式："
					+ film_most[rgpFilmMost.getCheckedRadioButtonId()]);
			if (isChanged) {
				// isRefresh 该为 false
				LogUtils.i("isChanged：" + isChanged + " isRefresh：" + isRefresh
						+ " currentPage：" + currentPage);
				if (isVip) {
					video = VideoService.jsonToVipVideo(MovieAct.this,
							"/Vip/vipList", String.valueOf(7),
							String.valueOf(currentPage),
							String.valueOf(AppConfig.PAGE_SIZE), film_most[rgpFilmMost.getCheckedRadioButtonId()],
							film_mtype[rgpFilmMtype.getCheckedRadioButtonId()], filmYear, filmArea, false);
				} else {
					System.out.println("免费电影");
					video = VideoService.jsonToGeneralVideo(MovieAct.this,
							"/Vod/vodList", String.valueOf(2),
							String.valueOf(currentPage),
							String.valueOf(AppConfig.PAGE_SIZE), filmMost,
							filmMtype, filmYear, filmArea, false);
				}
			} else {
				// 不是筛选 ：刷新或者加载更多
				LogUtils.i("isChanged：" + isChanged + "\tisRefresh："
						+ isRefresh + "\tcurrentPage：" + currentPage);
				if (isRefresh)
					currentPage = 1;
				if (isVip) {
					video = VideoService.jsonToVipVideo(MovieAct.this,
							"/Vip/vipList", String.valueOf(7),
							String.valueOf(currentPage),
							String.valueOf(AppConfig.PAGE_SIZE), film_most[rgpFilmMost.getCheckedRadioButtonId()],
							film_mtype[rgpFilmMtype.getCheckedRadioButtonId()], filmYear, filmArea, isRefresh);
				} else {
					System.out.println("免费电影");
					video = VideoService.jsonToGeneralVideo(MovieAct.this,
							"/Vod/vodList", String.valueOf(2),
							String.valueOf(currentPage),
							String.valueOf(AppConfig.PAGE_SIZE), filmMost,
							filmMtype, filmYear, filmArea, isRefresh);
				}
			}
			return video;
		}
		@Override
		protected void onPostExecute(EVideo result) {
			if (isChanged) {
				if (result != null) {
					if (!result.getVideoList().isEmpty()) {
						lstData.addAll(result.getVideoList());
						movieAdapter.notifyDataSetChanged();
						showDataView();
					} else {
						// 无数据
						showError(getString(R.string.filter_noDataNotice),
								getString(R.string.filter_noDataDesc));
					}
				} else {
					// 网络错误
					showError(getString(R.string.netErrorNotice),
							getString(R.string.netErrorDesc));
				}
				isChanged = false;
			} else {
				// 刷新或者加载更多
				if (result != null) {
					if (!result.getVideoList().isEmpty()) {
						if (isRefresh) {
							lstData.clear();
						}
						lstData.addAll(result.getVideoList());
					} else {
						// 无数据
						if (currentPage > 1) {
							currentPage--;
						}
					}
				} else {
					// 网络错误
					if (currentPage > 1) {
						currentPage--;
					}
				}
				if (isRefresh) {
					isRefresh = false;
				}
				movieAdapter.notifyDataSetChanged();
				reGvwData.onRefreshComplete();
			}
			super.onPostExecute(result);
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (isInit)
			return;
		currentPage = 1;
		isChanged = true;
		isRefresh = false;
		loadData();
	}

}
