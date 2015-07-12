package com.m1905.mobile.content;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.dianxin.mobilefree.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.m1905.mobile.activity.M1905VideoPlayerActivity;
import com.m1905.mobile.activity.RecommentAct;
import com.m1905.mobile.adapter.ReleatedAdapter;
import com.m1905.mobile.dao.EReleated;
import com.m1905.mobile.dao.Releated;
import com.m1905.mobile.net.VideoService;
import com.m1905.mobile.ui.ScrollContent;
import com.m1905.mobile.ui.XGridView;
import com.m1905.mobile.util.StringUtils;

@SuppressWarnings("deprecation")
public class ReleatedContent extends ScrollContent implements OnRefreshListener<ListView>{

	private static ReleatedContent instance;
	private XGridView releatedListView;
	private int id;//类容id
	private int type;
	private ReleatedAdapter releatedAdapter;
	private boolean isRefresh = false;
	private ArrayList<Releated> releatedList;
	private EReleated eReleated;
	private Activity activitys;
	
	public ReleatedContent(final Activity activity, int resourceID) {
		super(activity, resourceID);
		instance = this;
		isRefresh = true;
		activitys = activity;
		releatedList = new ArrayList<Releated>();
		releatedListView = (XGridView)findViewById(R.id.gvwRecoVideos);
		Bundle bundle = activity.getIntent().getExtras();
		if (bundle != null && String.valueOf(bundle.getInt("id"))!=null&&String.valueOf(bundle.getInt("type"))!=null) {
			id = bundle.getInt("id");
			type = bundle.getInt("type");
			//new GetReleatedVideoTask().execute(type,id);
		} else {
			StringUtils.showLongToast(activity, "不合法的用户ID:)");
		}
	}

	private class GetReleatedVideoTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {

			/*if(params[0]==7)
			{
				eReleated = VideoService.jsonToVideoReleated(activity.getApplicationContext(),
						"/Vip/vipRelate/relateid/"+params[1],params[0],params[1], isRefresh);
			}else if(params[0]==2)
			{*/
				eReleated = VideoService.jsonToVideoReleated(activity.getApplicationContext(),
						mtype,params[0],params[1], isRefresh);
			//}
			if (isRefresh) {// 刷新
				releatedList.clear();
			}
			
			//if (eReleated != null && !eReleated.getReleatedList().isEmpty()&&eReleated.getResult()==0) {
				System.out.println("添加相关内容");
				releatedList.addAll(eReleated.getReleatedList());
			//} else {
				// 无数处理
				//System.out.println("相关无处理");
				//return 1;
			//}
			isRefresh = false;
			return 2;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case 1:
//				StringUtils.showLongToast(activity.getApplicationContext(), "该影片没有相关");
				break;
			case 2:
				releatedAdapter = new ReleatedAdapter(activity.getApplicationContext(),releatedList);
				System.out.println("相关推荐展示");
				releatedListView.setAdapter(releatedAdapter);
				//releatedAdapter.notifyDataSetChanged();
				break;
			}
		}
	}
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		isRefresh = true;
		new GetReleatedVideoTask().execute(type,id);
	}
	String mtype = "";
	public void updatacontnt(final VideoInfoContent contents){
		if(mtype.equals("")){
			isRefresh = true;
			if(contents.mtype.contains(",")){
				
				String[] mtypes = contents.mtype.split(",");
				mtype = mtypes[0];
			}else{
				mtype = contents.mtype;
			}
			releatedListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					activitys.finish();
					Intent intent = new Intent(activity,
							M1905VideoPlayerActivity.class);
					intent.putExtra("id",eReleated.getReleatedList().get(position).getId());
					intent.putExtra("type", -1);
					startActivity(intent);
				}

			});
			new GetReleatedVideoTask().execute(type,id);
		}
	}

}
