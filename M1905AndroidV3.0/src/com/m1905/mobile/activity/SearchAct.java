package com.m1905.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
import com.m1905.mobile.adapter.HotWordAdapter;
import com.m1905.mobile.adapter.SearchAdapter;
import com.m1905.mobile.dao.EHot;
import com.m1905.mobile.dao.ESearchContent;
import com.m1905.mobile.dao.Hot;
import com.m1905.mobile.dao.SearchContent;
import com.m1905.mobile.net.SearchService;
import com.umeng.analytics.MobclickAgent;

/**
 * 搜索
 * 
 * @author forcetech
 * 
 */
public class SearchAct extends Activity implements OnClickListener {
	// result box
	private RelativeLayout rltResultBox;
	// relate box
	private RelativeLayout ileRelateBox; // 显示或者隐藏
	private PullToRefreshListView reLvwResult; // 搜索结果视图（相关）
	private ListView lvwResult;// 搜索结果视图（相关）
	private List<SearchContent> lstSearch;
	private SearchAdapter searchAdapter;
	private int pageIndex = 1;
	// 网络、加载
	private RelativeLayout ileError, ileLoading; // 显示或者隐藏
	private TextView tvwNoDataNotice, tvwNoDataDesc;// 数据错误提示、描述

	// search box
	private EditText edtSearchBox;
	private Button btnSearch, btnDelete;

	// hot box 热门影片
	private RelativeLayout ileHotBox; // 关闭、打开显示使用
	private ListView lvwHotWords;
	private List<Hot> lstHot;
	private HotWordAdapter hotAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_search);
		init();
		loadHotData();
	}

	@Override
	protected void onResume() {
		searchAdapter.notifyDataSetChanged();
		super.onResume();
		MobclickAgent.onResume(SearchAct.this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(SearchAct.this);
	}
	
	private void init() {
		// 搜索盒子
		inintSearchBox();
		// 热词
		initHotBox();
		// 搜索
		initResultBox();
		// 网络
		initOther();
	}

	/**
	 * 初始化搜索盒子
	 */
	private void inintSearchBox() {
		btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(this);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(this);
		edtSearchBox = (EditText) findViewById(R.id.edtSearchBox);
		edtSearchBox.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				int length = edtSearchBox.getText().toString().length();
				if (hasFocus)
					edtSearchBox.setSelection(length);
				if (hasFocus) {
					if (length > 0) {
						btnDelete.setVisibility(View.VISIBLE);
						if (!btnSearch.isEnabled())
							btnSearch.setEnabled(true);
					} else {
						btnSearch.setEnabled(false);
						btnDelete.setVisibility(View.INVISIBLE);
					}
				} else {
					if (length > 0) {
						btnDelete.setVisibility(View.VISIBLE);
						if (!btnSearch.isEnabled())
							btnSearch.setEnabled(true);
					} else {
						btnSearch.setEnabled(false);
						btnDelete.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
		edtSearchBox.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(edtSearchBox.getText().toString())) {
					if (!btnSearch.isEnabled())
						btnSearch.setEnabled(true);
					btnDelete.setVisibility(View.VISIBLE);
				} else {
					btnSearch.setEnabled(false);
					btnDelete.setVisibility(View.INVISIBLE);
				}
			}
		});
		edtSearchBox.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (!TextUtils.isEmpty(edtSearchBox.getText().toString())
						&& keyCode == KeyEvent.KEYCODE_ENTER
						& event.getAction() == KeyEvent.ACTION_DOWN) {
					loadSearch(edtSearchBox.getText().toString());
				}
				return false;
			}
		});

	}

	/**
	 * 初始化热词控件
	 */
	private void initHotBox() {
		ileHotBox = (RelativeLayout) findViewById(R.id.ileHotBox);
		lvwHotWords = (ListView) findViewById(R.id.lvwHotWords);
		lstHot = new ArrayList<Hot>();
		hotAdapter = new HotWordAdapter(this, lstHot);
		lvwHotWords.setAdapter(hotAdapter);
		lvwHotWords.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String kw = ((Hot) hotAdapter.getItem(arg2)).getTitle();
				if (!TextUtils.isEmpty(kw)) {
					edtSearchBox.setText(kw);
					loadSearch(kw);
				}
			}
		});
	}

	/**
	 * 初始化搜索结果控件
	 */
	private void initResultBox() {
		lstSearch = new ArrayList<SearchContent>();
		searchAdapter = new SearchAdapter(SearchAct.this, lstSearch);
		ileRelateBox = (RelativeLayout) findViewById(R.id.ileRelateBox);
		reLvwResult = (PullToRefreshListView) findViewById(R.id.reLvwResult);
		reLvwResult.setMode(Mode.DISABLED);
		lvwResult = reLvwResult.getRefreshableView();
		lvwResult.setAdapter(searchAdapter);
		lvwResult.setOnScrollListener(new PauseOnScrollListener(searchAdapter
				.getBitmapUtil(), false, true));
		lvwResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (arg3 == -1) {
					return;
				}
				SearchContent search = (SearchContent) searchAdapter
						.getItem((int) arg3);
				Intent intent = new Intent(SearchAct.this,
						M1905VideoPlayerActivity.class);
				intent.putExtra("id", search.getId());
				intent.putExtra("type", search.getType());
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化网络提示控件
	 */
	private void initOther() {
		rltResultBox = (RelativeLayout) findViewById(R.id.rltResultBox);
		ileError = (RelativeLayout) findViewById(R.id.ileError);
		tvwNoDataNotice = (TextView) ileError
				.findViewById(R.id.tvwNoDataNotice);
		tvwNoDataDesc = (TextView) ileError.findViewById(R.id.tvwNoDataDesc);
		ileLoading = (RelativeLayout) findViewById(R.id.ileLoading);
	}

	/**
	 * 显示错误视图
	 * 
	 * @param errorNotice
	 * @param errorDesc
	 */
	private void showError(String errorNotice, String errorDesc) {
		ileLoading.setVisibility(View.GONE);
		reLvwResult.setVisibility(View.GONE);
		ileRelateBox.setVisibility(View.GONE);
		tvwNoDataNotice.setText(errorNotice);
		tvwNoDataDesc.setText(errorDesc);
		ileError.setVisibility(View.VISIBLE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnDelete:
			edtSearchBox.setText("");
			showHotWordView();
			break;
		case R.id.btnSearch:
			loadSearch(edtSearchBox.getText().toString());
			break;
		}
	}

	/**
	 * 加载热词数据
	 */
	private void loadHotData() {
		new AsyncHotLoader().execute();
	}

	/**
	 * 热词异步加载数据
	 * 
	 * @author Administrator
	 * 
	 */
	private class AsyncHotLoader extends AsyncTask<Void, Void, EHot> {

		@Override
		protected EHot doInBackground(Void... params) {
			return SearchService.getHot(SearchAct.this, true);
		}

		@Override
		protected void onPostExecute(EHot result) {
			if (result == null) {
				// 网络错误
			} else {
				lstHot.clear();
				lstHot.addAll(result.getLstHots());
				if (lstHot.isEmpty()) {
					// 无数据、或者数据错误
				} else {

				}
				hotAdapter.notifyDataSetChanged();
			}
			super.onPostExecute(result);
		}

	}

	AsyncSearchLoader searchLoader;

	private void loadSearch(String kw) {
		ileHotBox.setVisibility(View.GONE);
		cancleSearchLoader();
		// 显示搜索视图
		showSearchView();
		showSearchLoading();
		searchLoader = new AsyncSearchLoader();
		searchLoader.execute(kw);
	}

	private void showSearchLoading() {
		// 隐藏错误信息提示
		ileError.setVisibility(View.GONE);
		// 暂无相关提示
		ileRelateBox.setVisibility(View.GONE);
		// 搜索结果
		reLvwResult.setVisibility(View.GONE);
		// 加载视图
		ileLoading.setVisibility(View.VISIBLE);
	}

	private void cancleSearchLoader() {
		if (searchLoader != null) {
			searchLoader.cancel(true);
		}
	}

	/**
	 * 显示搜索视图 关闭热词视图
	 */
	private void showSearchView() {
		ileHotBox.setVisibility(View.GONE);
		rltResultBox.setVisibility(View.VISIBLE);
	}

	/**
	 * 关闭搜索视图 显示热词视图
	 */
	private void showHotWordView() {
		rltResultBox.setVisibility(View.GONE);
		ileHotBox.setVisibility(View.VISIBLE);
	}

	/**
	 * 搜索异步加载数据
	 * 
	 * @author Administrator
	 * 
	 */
	private class AsyncSearchLoader extends
			AsyncTask<String, Void, ESearchContent> {

		@Override
		protected ESearchContent doInBackground(String... params) {
			String kw = params[0];
			return SearchService.search(SearchAct.this, kw, pageIndex, true);
		}

		@Override
		protected void onPostExecute(ESearchContent result) {
			notifySearch(result);
			super.onPostExecute(result);
		}

	}

	private void notifySearch(ESearchContent result) {
		if (result == null) {
			// 网络错误
			showError(getString(R.string.netErrorNotice),
					getString(R.string.netErrorDesc));
			return;
		} else {
			lstSearch.clear();
			lstSearch.addAll(result.getLstSearchContents());
			if (lstSearch.isEmpty()) {
				// 无数据、或者数据错误
				showError(getString(R.string.noDataNotice),
						getString(R.string.noDataDesc));
			} else {
				showSearchResult();
			}
		}

	}

	private void showSearchResult() {
		// 加载视图
		ileLoading.setVisibility(View.GONE);
		// 隐藏错误信息提示
		ileError.setVisibility(View.GONE);
		// 暂无相关提示
		ileRelateBox.setVisibility(View.GONE);
		// 是否显示相关视图
		toggleShowRelateBox();
		searchAdapter.notifyDataSetChanged();
		if (pageIndex == 1) {
			lvwResult.setSelection(0);
		}
		// 搜索结果
		reLvwResult.setVisibility(View.VISIBLE);
	}

	private void toggleShowRelateBox() {
		if (!lstSearch.isEmpty()) {
			SearchContent result = lstSearch.get(0);
			if (result != null) {
				if (result.getType() == 0) {
					ileRelateBox.setVisibility(View.VISIBLE);
				} else {
					ileRelateBox.setVisibility(View.GONE);
				}
			}
		}
	}
}
