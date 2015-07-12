package com.m1905.mobile.content;

import java.util.HashMap;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.common.Constant;
import com.m1905.mobile.dao.VipVideoDetail;
import com.m1905.mobile.dao.VodVideoDetail;
import com.m1905.mobile.net.VideoService;
import com.m1905.mobile.ui.ScrollContent;
import com.m1905.mobile.util.StringUtils;
import com.umeng.analytics.MobclickAgent;

public class VideoInfoContent extends ScrollContent{

	private static VideoInfoContent instance;
	private boolean isRefresh = false;
	private ScrollView infoScroll;
	private VipVideoDetail vipVideoDetail;
	private VodVideoDetail vodVideoDetail;
	private TextView tvwVideoName;
	private TextView tvwVideoScore;
	private TextView tvwVideoDirector;
	private TextView tvwVideoActor;
	private TextView tvwVideoType;
	private TextView tvwVideoArea;
	private TextView tvwVideoLength;
	private TextView tvwVideoTime;
	private TextView tvwVideoContentNotice;
	private int id;// 类容id
	private int type;//类型type
	private Handler handler;
	public String mtype;
	public String playurl="";//播放地址
	public String videoName;//视频名称
	public String stateUrl;//剧照地址
	
	public VideoInfoContent(Activity activity,Handler handler, int resourceID) {
		super(activity, resourceID);
		instance = this;
		this.handler = handler;
		initContentView();
		Bundle bundle = activity.getIntent().getExtras();
		System.out.println("id:"+bundle.getInt("id")+"---type:"+bundle.getInt("type"));
		if (bundle != null && String.valueOf(bundle.getInt("id"))!=null&&String.valueOf(bundle.getInt("type"))!=null) {
			id = bundle.getInt("id");
			type = bundle.getInt("type");
			if(type == 7)
			{
				//new GetVipVideoDetialTask().execute(id);
			}else if(type == 2)
			{
				new GetVodVideoDetialTask().execute(id);
			}
			//new GetVipVideoDetialTask().execute(id);
			new GetVodVideoDetialTask().execute(id);
		} else {
			StringUtils.showLongToast(activity, "不合法的用户ID:)");
		}
		
	}
	
	public static VideoInfoContent getInstance() {
		return instance;
	}
	
	private void initContentView() {
		infoScroll = (ScrollView) findViewById(R.id.player_video_into_Content); 
		infoScroll.setHorizontalFadingEdgeEnabled(false);
		infoScroll.setVerticalScrollBarEnabled(false);
		infoScroll.setFadingEdgeLength(0);
		tvwVideoName = (TextView)findViewById(R.id.tvwVideoName);
		tvwVideoScore = (TextView)findViewById(R.id.tvwVideoScore);
		tvwVideoDirector = (TextView)findViewById(R.id.tvwVideoDirector);
		tvwVideoActor = (TextView)findViewById(R.id.tvwVideoActor);
		tvwVideoType = (TextView)findViewById(R.id.tvwVideoType);
		tvwVideoArea = (TextView)findViewById(R.id.tvwVideoArea);
		tvwVideoLength = (TextView)findViewById(R.id.tvwVideoLength);
		tvwVideoTime = (TextView)findViewById(R.id.tvwVideoTime);
		tvwVideoContentNotice = (TextView)findViewById(R.id.tvwVideoContentNotice);
	}
	
	/*class GetVipVideoDetialTask extends AsyncTask<Integer, Void, VipVideoDetail> {

		@Override
		protected VipVideoDetail doInBackground(Integer... params) {
			System.out.println("获取VIP");
			vipVideoDetail = VideoService.jsonToVipVideoDetail(activity.getApplicationContext(),
						"/Vip/vipDetail/id/"+params[0],params[0], isRefresh);
			return vipVideoDetail;
		}

		@Override
		protected void onPostExecute(VipVideoDetail result) {
			super.onPostExecute(result);
			if(result!=null)
			{
				tvwVideoName.setText(result.getTitle());
				tvwVideoScore.setText(result.getScore()+"");
				tvwVideoDirector.setText(result.getDirector());
				tvwVideoActor.setText(result.getActor());
				tvwVideoType.setText(result.getMtype());
				tvwVideoArea.setText(result.getArea());
				tvwVideoLength.setText(result.getDuration()/60000+"分钟");
				tvwVideoTime.setText(result.getYears());
				tvwVideoContentNotice.setText(result.getDescription());
				if(!TextUtils.isEmpty(result.getHdplayurl()))
				{
					playurl = result.getHdplayurl();
				}
				if(!TextUtils.isEmpty(result.getTitle()))
				{
					videoName = result.getTitle();
				}
				if(!TextUtils.isEmpty(result.getStageurl()))
				{
					stateUrl = result.getStageurl();
				}
				handler.sendEmptyMessage(Constant.HANDLER_INIT_PLAY);
			}
			isRefresh = false;
		}
	}*/
	
	class GetVodVideoDetialTask extends AsyncTask<Integer, Void, VodVideoDetail> {

		@Override
		protected VodVideoDetail doInBackground(Integer... params) {
			System.out.println("获取免费"+params[0]);
			
			vodVideoDetail = VideoService.jsonToVodVideoDetail(activity.getApplicationContext(),
						"/Vod/vodDetail/id/"+params[0],params[0], isRefresh);
			return vodVideoDetail;
		}

		@Override
		protected void onPostExecute(VodVideoDetail result) {
			super.onPostExecute(result);
			if(result!=null)
			{
				HashMap<String ,String> voidename = new HashMap<String,String>();
				voidename.put("视频名称",result.getTitle());
				MobclickAgent.onEvent(activity, "video_name",voidename);
				tvwVideoName.setText(result.getTitle());
				tvwVideoScore.setText(result.getScore()+"");
				tvwVideoDirector.setText(result.getDirector());
				tvwVideoActor.setText(result.getActor());
				mtype = result.getMtype();
				tvwVideoType.setText(result.getMtype());
				tvwVideoArea.setText(result.getArea());
				tvwVideoLength.setText(result.getDuration()/60+"分钟");
				tvwVideoTime.setText(result.getYears());
				tvwVideoContentNotice.setText(result.getDescription());
				if(!TextUtils.isEmpty(result.getPlayurl()))
				{
					playurl = result.getPlayurl();
				}
				if(!TextUtils.isEmpty(result.getTitle()))
				{
					videoName = result.getTitle();
				}
				if(!TextUtils.isEmpty(result.getStageurl()))
				{
					stateUrl = result.getStageurl();
				}
				handler.sendEmptyMessage(Constant.HANDLER_INIT_PLAY);
			}
			isRefresh = false;
		}
	}

}
