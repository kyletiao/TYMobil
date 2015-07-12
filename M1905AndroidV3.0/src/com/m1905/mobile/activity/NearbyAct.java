package com.m1905.mobile.activity;

import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.dianxin.mobilefree.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.m1905.mobile.adapter.ColumnAdapter;
import com.m1905.mobile.adapter.TopicsAdapter;
import com.m1905.mobile.bean.TopicsBean;
import com.m1905.mobile.dao.Column;
import com.m1905.mobile.dao.EColumn;
import com.m1905.mobile.net.NearByService;
import com.m1905.mobile.util.NetHttpConnection;
import com.m1905.mobile.util.NetUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 专题
 * 
 * @author leepan
 * 
 */
public class NearbyAct extends Activity implements OnRefreshListener<GridView> {

	private PullToRefreshGridView neayBy_gridview;
	private GridView nearByGrid;
	private ColumnAdapter columnAdapter;
	private boolean isRefresh = false;
	private ArrayList<Column> list;
	private ListView lv_topics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_nearby);
		ileLoading = (RelativeLayout) findViewById(R.id.ileLoading);
		ileLoading.setVisibility(View.VISIBLE);
		init();
		/*initContentView();
		isRefresh = true;*/
		new GetColumnDataTask().execute();
	}
	private TopicsBean bean;
	private RelativeLayout ileLoading;
	/**
	 * 处理数据
	 * 
	 * @author forcetech
	 * 
	 */
	private class GetColumnDataTask extends AsyncTask<Void, Void, Integer> {

		

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				if(NetUtils.isConnect(NearbyAct.this)){
					String ztcontent = NetHttpConnection.httpGet("http://180.168.69.121:8089/webroot/clt4/kpcp/szyx/khd4ejym/ztlb/ztlb/index.json", null);
					//TopicsBean bean = mapper.readValue(ztcontent, TopicsBean.class);
					String newContent = ztcontent.replaceAll("\n", "");
					String newcontents = newContent.replaceAll(" ", "");
					System.out.println("专题数据："+newcontents);
					bean = mapper.readValue(newcontents, TopicsBean.class);
					System.out.println("标题"+bean.getData()[0].getTitle());
					return 100;
				}else{
					return 10;
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return 10;
			}
			/*EColumn eColumn = NearByService.jsonToNearBy(NearbyAct.this,
					"/Round/roundList", isRefresh);
			if (isRefresh) {// 刷新
				list.clear();
			}
			if (eColumn != null && !eColumn.getColumnList().isEmpty()) {
				list.addAll(eColumn.getColumnList());
			} else {
				// 无数处理
			}
			isRefresh = false;
			return 100;*/
		}

		@Override
		protected void onPostExecute(Integer result) {
			/*columnAdapter.notifyDataSetChanged();
			neayBy_gridview.onRefreshComplete();*/
			if(result==100){
				ileLoading.setVisibility(View.GONE);
				System.out.println("专题个数：：："+bean.getData().length);
				TopicsAdapter adapter = new TopicsAdapter(NearbyAct.this, bean);
				lv_topics.setAdapter(adapter); 
			}else{
				ileLoading.setVisibility(View.GONE);
				Toast.makeText(NearbyAct.this, "数据获取失败", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}
	}
	
	private void init(){
		lv_topics = (ListView) findViewById(R.id.lv_topics);
		lv_topics.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				System.out.println("点击跳转"+bean.getData()[arg2].getTitle()+"内容页");
				Intent intent = new Intent(NearbyAct.this, NearbyContentAct.class);
				intent.putExtra("path", bean.getData()[arg2].getClickParam());
				intent.putExtra("name", bean.getData()[arg2].getTitle());
				startActivity(intent);
			}
		});
	}

	/**
	 * 初始化界面
	 */
	/*private void initContentView() {
		list = new ArrayList<Column>();
		columnAdapter = new ColumnAdapter(this, list);
		neayBy_gridview = (PullToRefreshGridView) findViewById(R.id.neary_by);
		neayBy_gridview.setOnRefreshListener(this);
		nearByGrid = neayBy_gridview.getRefreshableView();
		nearByGrid.setNumColumns(2);
		nearByGrid.setHorizontalFadingEdgeEnabled(false);
		nearByGrid.setVerticalScrollBarEnabled(false);
		nearByGrid.setFadingEdgeLength(0);
		nearByGrid.setHorizontalSpacing(1);
		nearByGrid.setVerticalSpacing(1);
		nearByGrid.setAdapter(columnAdapter);
		nearByGrid.setBackgroundResource(R.color.column_background);
		nearByGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(NearbyAct.this, WebAct.class);
				intent.putExtra("address", list.get(position).getUrl());
				startActivity(intent);
				overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
			}

		});
	}*/

	@Override
	public void onRefresh(PullToRefreshBase<GridView> arg0) {
		isRefresh = true;
		new GetColumnDataTask().execute();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(NearbyAct.this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(NearbyAct.this);
	}
}
