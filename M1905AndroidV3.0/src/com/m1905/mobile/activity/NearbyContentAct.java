package com.m1905.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import com.dianxin.mobilefree.R;
import com.lidroid.xutils.BitmapUtils;
import com.m1905.mobile.bean.TopicsBean;
import com.m1905.mobile.bean.TopicsContentBean;
import com.m1905.mobile.bean.TopicsListBean;
import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.util.NetHttpConnection;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NearbyContentAct extends Activity {
	private String path;
	private BitmapUtils bitmapUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_content);
		path = NearbyContentAct.this.getIntent().getStringExtra("path");
		titles = NearbyContentAct.this.getIntent().getStringExtra("name");
		System.out.println("路径：" + path);
		init();
		// clt4/kpcp/szyx/khd4zt/hgjddyhg/index.json
	}

	public void init() {
		Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NearbyContentAct.this.finish();
			}
		});
		TextView tvwNaviNotice = (TextView) findViewById(R.id.tvwNaviNotice);
		tvwNaviNotice.setText(titles);
		
		Button btnFunc = (Button) findViewById(R.id.btnFunc);
		btnFunc.setVisibility(View.GONE);
		bitmapUtil = new BitmapUtils(NearbyContentAct.this,
				AppConfig.M1905_CACHE_PATH);
		new GetTopicsContentTask().execute();
		lv_ncontent = (ListView) findViewById(R.id.lv_ncontent);
	}

	private List<TopicsContentBean> beans;
	private ListView lv_ncontent;
	private String titles;

	private class GetTopicsContentTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			ObjectMapper mapper = new ObjectMapper();
			beans = new ArrayList<TopicsContentBean>();
			try {
				String ncontent = NetHttpConnection.httpGet(
						"http://180.168.69.121:8089/webroot" + path, null);
				ncontent = ncontent.replaceAll("\n", "").replaceAll(" ", "");
				TopicsListBean listbean = mapper.readValue(ncontent,
						TopicsListBean.class);
				for (int i = 0; i < listbean.getData().length; i++) {
					String ncontents = NetHttpConnection.httpGet(
							"http://180.168.69.121:8089/webroot"
									+ listbean.getData()[i].getPath(), null);
					System.out.println("当前内容"+i+":::"+ncontents.replaceAll("\n", ""));
					ncontents = ncontents.replaceAll("\n", "");
					ncontents = ncontents.replaceAll("\t", "");
					ncontents = ncontents.replaceAll(" ", "");
					TopicsContentBean bean = mapper.readValue(ncontents,
							TopicsContentBean.class);
					beans.add(bean);
					System.out.println("专题内容页" + i + "::"
							+ ncontents.replaceAll("\n", ""));
				}

				// TopicsBean topbean =
				// mapper.readValue(ncontents.replaceAll("\n", ""),
				// TopicsBean.class);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return 100;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			TopicsContentAdapter adapter = new TopicsContentAdapter();
			lv_ncontent.setAdapter(adapter);
		}

	}

	public class TopicsContentAdapter extends BaseAdapter {

		private ImageView iv_top;

		@Override
		public int getCount() {
			return beans.size();
		}

		@Override
		public Object getItem(int position) {
			return beans.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			if (beans.get(position).getAreaCode() == 1) {
				convertView = LayoutInflater.from(NearbyContentAct.this).inflate(R.layout.topics_con_one, null);
				iv_top = (ImageView) convertView.findViewById(R.id.iv_top);
				bitmapUtil.display(iv_top, "http://img3.tv189.cn/"+beans.get(position).getData()[0].getCover());
			}else if(beans.get(position).getAreaCode()==6){
				convertView = LayoutInflater.from(NearbyContentAct.this).inflate(R.layout.topics_con_two, null);
				TextView tv_two_title = (TextView) convertView.findViewById(R.id.tv_two_title);
				tv_two_title.setText(beans.get(position).getLabel().getName());
				TextView tv_one = (TextView) convertView.findViewById(R.id.tv_one);
				tv_one.setText(beans.get(position).getData()[0].getTitle());
				TextView tv_two = (TextView) convertView.findViewById(R.id.tv_two);
				tv_two.setText(beans.get(position).getData()[1].getTitle());
				TextView tv_three = (TextView) convertView.findViewById(R.id.tv_three);
				tv_three.setText(beans.get(position).getData()[2].getTitle());
				TextView tv_four = (TextView) convertView.findViewById(R.id.tv_four);
				tv_four.setText(beans.get(position).getData()[3].getTitle());
				ImageView iv_one = (ImageView) convertView.findViewById(R.id.iv_one);
				bitmapUtil.display(iv_one, "http://img3.tv189.cn/"+beans.get(position).getData()[0].getCover());
				iv_one.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(NearbyContentAct.this,
								M1905VideoPlayerActivity.class);
						System.out.println(beans.get(position).getData()[0].getContentId());
						intent.putExtra("id", Integer.parseInt(beans.get(position).getData()[0].getContentId()));
						intent.putExtra("type", -1);
						startActivity(intent);
					}
				});
				ImageView iv_two = (ImageView) convertView.findViewById(R.id.iv_two);
				bitmapUtil.display(iv_two, "http://img3.tv189.cn/"+beans.get(position).getData()[1].getCover());
				iv_two.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(NearbyContentAct.this,
								M1905VideoPlayerActivity.class);
						intent.putExtra("id", Integer.parseInt(beans.get(position).getData()[1].getContentId()));
						intent.putExtra("type", -1);
						startActivity(intent);
					}
				});
				ImageView iv_three = (ImageView) convertView.findViewById(R.id.iv_three);
				bitmapUtil.display(iv_three, "http://img3.tv189.cn/"+beans.get(position).getData()[2].getCover());
				iv_three.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(NearbyContentAct.this,
								M1905VideoPlayerActivity.class);
						intent.putExtra("id", Integer.parseInt(beans.get(position).getData()[2].getContentId()));
						intent.putExtra("type", -1);
						startActivity(intent);
					}
				});
				ImageView iv_four = (ImageView) convertView.findViewById(R.id.iv_four);
				bitmapUtil.display(iv_four, "http://img3.tv189.cn/"+beans.get(position).getData()[3].getCover());
				iv_four.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(NearbyContentAct.this,
								M1905VideoPlayerActivity.class);
						intent.putExtra("id", Integer.parseInt(beans.get(position).getData()[3].getContentId()));
						intent.putExtra("type", -1);
						startActivity(intent);
					}
				});
			}else if(beans.get(position).getAreaCode()==4){
				convertView = LayoutInflater.from(NearbyContentAct.this).inflate(R.layout.topics_con_four, null);
				TextView tv_three_title = (TextView) convertView.findViewById(R.id.tv_three_title);
				tv_three_title.setText(beans.get(position).getLabel().getName());
				TextView tv_ones = (TextView) convertView.findViewById(R.id.tv_ones);
				tv_ones.setText(beans.get(position).getData()[0].getTitle());
				TextView tv_twos = (TextView) convertView.findViewById(R.id.tv_twos);
				tv_twos.setText(beans.get(position).getData()[1].getTitle());
				TextView tv_threes = (TextView) convertView.findViewById(R.id.tv_threes);
				tv_threes.setText(beans.get(position).getData()[2].getTitle());
				TextView tv_fours = (TextView) convertView.findViewById(R.id.tv_fours);
				tv_fours.setText(beans.get(position).getData()[3].getTitle());
				TextView tv_fives = (TextView) convertView.findViewById(R.id.tv_fives);
				tv_fives.setText(beans.get(position).getData()[4].getTitle());
				TextView tv_sixs = (TextView) convertView.findViewById(R.id.tv_sixs);
				tv_sixs.setText(beans.get(position).getData()[5].getTitle());
				ImageView iv_ones = (ImageView) convertView.findViewById(R.id.iv_ones);
				bitmapUtil.display(iv_ones, "http://img3.tv189.cn/"+beans.get(position).getData()[0].getCover());
				iv_ones.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(NearbyContentAct.this,
								M1905VideoPlayerActivity.class);
						intent.putExtra("id", Integer.parseInt(beans.get(position).getData()[0].getContentId()));
						intent.putExtra("type", -1);
						startActivity(intent);
					}
				});
				ImageView iv_twos = (ImageView) convertView.findViewById(R.id.iv_twos);
				bitmapUtil.display(iv_twos, "http://img3.tv189.cn/"+beans.get(position).getData()[1].getCover());
				iv_twos.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(NearbyContentAct.this,
								M1905VideoPlayerActivity.class);
						intent.putExtra("id", Integer.parseInt(beans.get(position).getData()[1].getContentId()));
						intent.putExtra("type", -1);
						startActivity(intent);
					}
				});
				ImageView iv_threes = (ImageView) convertView.findViewById(R.id.iv_threes);
				bitmapUtil.display(iv_threes, "http://img3.tv189.cn/"+beans.get(position).getData()[2].getCover());
				iv_threes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(NearbyContentAct.this,
								M1905VideoPlayerActivity.class);
						intent.putExtra("id", Integer.parseInt(beans.get(position).getData()[2].getContentId()));
						intent.putExtra("type", -1);
						startActivity(intent);
					}
				});
				ImageView iv_fours = (ImageView) convertView.findViewById(R.id.iv_fours);
				bitmapUtil.display(iv_fours, "http://img3.tv189.cn/"+beans.get(position).getData()[3].getCover());
				iv_fours.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(NearbyContentAct.this,
								M1905VideoPlayerActivity.class);
						intent.putExtra("id", Integer.parseInt(beans.get(position).getData()[3].getContentId()));
						intent.putExtra("type", -1);
						startActivity(intent);
					}
				});
				ImageView iv_fives = (ImageView) convertView.findViewById(R.id.iv_fives);
				bitmapUtil.display(iv_fives, "http://img3.tv189.cn/"+beans.get(position).getData()[4].getCover());
				iv_fives.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(NearbyContentAct.this,
								M1905VideoPlayerActivity.class);
						intent.putExtra("id", Integer.parseInt(beans.get(position).getData()[4].getContentId()));
						intent.putExtra("type", -1);
						startActivity(intent);
					}
				});
				ImageView iv_sixs = (ImageView) convertView.findViewById(R.id.iv_sixs);
				bitmapUtil.display(iv_sixs, "http://img3.tv189.cn/"+beans.get(position).getData()[5].getCover());
				iv_sixs.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(NearbyContentAct.this,
								M1905VideoPlayerActivity.class);
						intent.putExtra("id", Integer.parseInt(beans.get(position).getData()[5].getContentId()));
						intent.putExtra("type", -1);
						startActivity(intent);
					}
				});
			}

			return convertView;
		}

	}
}
