package com.m1905.mobile.activity;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.m1905.mobile.adapter.ConsumeAdapter;
import com.m1905.mobile.bean.PayContentBean;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.util.AppUtils;
import com.m1905.mobile.util.LogUtils;
import com.telecomsdk.phpso.TysxOA;

/**
 * @author leepan
 * @since 2013-12-25
 */
public class ConsumeAct extends Activity implements PullToRefreshBase.OnRefreshListener<ListView> {

    PayContentBean consumeList;

    ListView lvwConsume;
    ConsumeAdapter consumeAdapter;
    private boolean isRefresh = false;

    // 提示的View
    LinearLayout vwTip;
    ImageView ivwIcon;
    TextView tvwTitle, tvwSubTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_consume);
        findViewById(R.id.btnFunc).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.tvwNaviNotice))
                .setText(R.string.func_consume);
        lvwConsume = (ListView) findViewById(R.id.lvwConsume);
        vwTip = (LinearLayout) findViewById(R.id.vwNotLogin);
        ivwIcon = (ImageView) vwTip.findViewById(R.id.ivwIcon);
        tvwTitle = (TextView) vwTip.findViewById(R.id.tvwTitle);
        tvwSubTitle = (TextView) vwTip.findViewById(R.id.tvwSubTitle);

        isRefresh = true; 
        

        /*if (Identify.currentUser == null) { // 未登录
            vwTip.setVisibility(View.VISIBLE);
            tvwSubTitle.setVisibility(View.GONE);
            lvwConsume.setVisibility(View.GONE);
            ivwIcon.setImageResource(R.drawable.icon_notlogin);
        } else {
            vwTip.setVisibility(View.GONE);
            lvwConsume.setVisibility(View.VISIBLE);
            new GetConsumeDataTask().execute();
        }*/
        if(!TianyiContent.user.equals("")){
        	vwTip.setVisibility(View.GONE);
        	lvwConsume.setVisibility(View.VISIBLE);
        	new GetConsumeDataTask().execute();
        }else{
        	vwTip.setVisibility(View.VISIBLE);
        	tvwSubTitle.setVisibility(View.GONE);
        	lvwConsume.setVisibility(View.GONE);
        	ivwIcon.setImageResource(R.drawable.icon_notlogin);
        }
    }

    private void init() {
        //consumeList = new PayContentBean();
        consumeAdapter = new ConsumeAdapter(this, consumeList);
        lvwConsume.setAdapter(consumeAdapter);

    }

    // 获取数据异步类
    private class GetConsumeDataTask extends AsyncTask<Void, Void, PayContentBean> {

        @Override
        protected PayContentBean doInBackground(Void... params) {
            LogUtils.d("开始刷新数据");
            if (isRefresh) {// 刷新
                consumeList=null;
            }
            /*EConsume eConsume = ConsumeService.jsonToEConsume(ConsumeAct.this, "/Pay/payTrade",
                    Identify.currentUser.getUserToken());

            isRefresh = false;
            return eConsume != null ? eConsume.getConsumes() : new ArrayList<Consume>();*/
            ObjectMapper mapper = new ObjectMapper();
            TysxOA oa = new TysxOA(ConsumeAct.this);
            
            String paycontent = oa.getUserSubscription("1", TianyiContent.token, TianyiContent.appid, TianyiContent.devid, TianyiContent.appSecret);
            System.out.println("订购信息反馈："+paycontent);
            try {
				PayContentBean baybean = mapper.readValue(paycontent, PayContentBean.class);
				return baybean;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
        }

        @Override
        protected void onPostExecute(PayContentBean consumes) {
            LogUtils.d("刷新结束");
            if (consumes == null) {
                ivwIcon.setImageResource(R.drawable.icon_empty);
                tvwTitle.setText(R.string.consumeNullTip);
                tvwSubTitle.setVisibility(View.VISIBLE);
                vwTip.setVisibility(View.VISIBLE);
                lvwConsume.setVisibility(View.GONE);
            } else {
            	if(consumes.getInfo().length!=0){
            		System.out.println("内容："+consumes.getInfo()[0].getProductName());
                	consumeList = consumes;
                	init();
            	}else{
            		AppUtils.toastShowMsg(ConsumeAct.this, "您还没有订购信息");
            	}
            	
            }
            /*consumeAdapter.notifyDataSetChanged();
            lvwConsume.onRefreshComplete();*/
            super.onPostExecute(consumes);
        }
    }

    /**
     * 返回按钮操作
     *
     * @param view
     */
    public void back(View view) {
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        LogUtils.d("下拉刷新动作");
        isRefresh = true;
        new GetConsumeDataTask().execute();
    }

}
