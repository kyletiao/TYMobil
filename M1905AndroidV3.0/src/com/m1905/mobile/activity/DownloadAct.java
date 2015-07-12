package com.m1905.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.adapter.MainAdapter;
import com.m1905.mobile.common.Constant;
import com.m1905.mobile.content.DownloadCompleteContent;
import com.m1905.mobile.content.DownloadContent;
import com.m1905.mobile.ui.SyncHorizontalScrollView;

public class DownloadAct extends Activity {

    Button btnDelete;

    // UI
    private SyncHorizontalScrollView mhsv;
    private RelativeLayout rl_scroll;
    private RadioGroup tab_content;
    private ImageView cursor;
    private ViewPager vPager;
    private Button btnBack;
    // DATA
    private MainAdapter mainAdapter;
    private List<RadioButton> rb_pages = new ArrayList<RadioButton>();
    private int mCurrentCheckedRadioLeft;// 当前的Tab距离左侧的距离
    private List<View> listViews = new ArrayList<View>();
    private LayoutInflater mInflater;
    private int cursorWidth;// 宽度
    private String[] rb_pageStr = {"已完成", "下载中"};
    private DownloadContent downloadContent;
    private DownloadCompleteContent downloadCompleteContent;
    private Handler handler;

    @Override
    protected void onResume() {
        super.onResume();
        int checkId = 0;
        tab_content.removeView(rb_pages.get(rb_pageStr.length - 1));
        tab_content.addView(rb_pages.get(rb_pageStr.length - 1));
        if (tab_content != null && tab_content.getChildAt(checkId) != null) {
            ((RadioButton) tab_content.getChildAt(checkId)).setChecked(true);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.download); 

//        btnFunc = (Button) findViewById(R.id.btnFunc);
//        btnFunc.setBackgroundResource(R.drawable.btn_list_delete_selector);

        downloadContent = new DownloadContent(DownloadAct.this, R.layout.download_list);
        downloadCompleteContent = new DownloadCompleteContent(DownloadAct.this, R.layout.download_complete_list);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constant.HANDLER_INT_DELETE) {
                    setBtnEditState(false);
                } else if (msg.what == Constant.HANDLER_INT_NODATA) {
                    btnDelete.setEnabled(false);
                } else if (msg.what == Constant.HANDLER_INT_SOMEDATA) {
                    btnDelete.setEnabled(true);
                }
            }
        }; 
        downloadContent.setHandler(handler);
        downloadCompleteContent.setHandler(handler);

        init();
        setListener();
    }

    private void setBtnEditState(boolean isEditable) {
        if (isEditable) {
            btnDelete.setText(R.string.cancel);
            btnDelete.setBackgroundDrawable(null);
        } else {
            btnDelete.setText("");
            btnDelete.setBackgroundResource(R.drawable.btn_list_delete_selector);
        }
    }

    private void init() {
        btnBack = (Button) (findViewById(R.id.btnBack));
        btnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vPager.getCurrentItem() == 0) { // 当前是在已完成界面
                	System.out.println("当前是已完成界面");
                    if (downloadCompleteContent.isEditable()) {
                        downloadCompleteContent.setEditable(false); 
                        setBtnEditState(false);
                    } else {
                        downloadCompleteContent.setEditable(true); 
                        setBtnEditState(true);
                    }
                } else if (vPager.getCurrentItem() == 1) { // 当前是在下载界面
                	System.out.println("当前是下载界面");
                    if (downloadContent.isEditable()) {
                        downloadContent.setEditable(false);
                        setBtnEditState(false);
                    } else {
                        downloadContent.setEditable(true);
                        setBtnEditState(true);
                    }
                }
            }
        });

        mhsv = (SyncHorizontalScrollView) findViewById(R.id.mhsv);
        rl_scroll = (RelativeLayout) findViewById(R.id.rl_scroll);
        tab_content = (RadioGroup) findViewById(R.id.tab_content); 
        cursor = (ImageView) findViewById(R.id.cursor);
        vPager = (ViewPager) findViewById(R.id.vPager);

        mInflater = LayoutInflater.from(DownloadAct.this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm); 
        cursorWidth = 160;

        LayoutParams cursor_Params = cursor.getLayoutParams();
        cursor_Params.width = cursorWidth;// 初始化滑动下标的宽
        cursor.setLayoutParams(cursor_Params); 

        mhsv.setSomeParam(rl_scroll, DownloadAct.this);
        listViews.add(downloadCompleteContent.getView());
        listViews.add(downloadContent.getView());
        mainAdapter = new MainAdapter(listViews);
        vPager.setAdapter(mainAdapter);
        vPager.setCurrentItem(0);
        initTabContent();
        initTabValue();
        setSelector(0);
        mainAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        tab_content.setOnCheckedChangeListener(tab_onCheckedChangeListener);
        vPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    public void back(View view) {
        finish();
    }


    // 页卡切换监听
    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            setBtnEditState(false);
            downloadContent.setEditable(false);
            downloadCompleteContent.setEditable(false);
            if (rb_pages != null && rb_pages.size() > position) {
                ((RadioButton) rb_pages.get(position)).performClick();
                setSelector(position);
            }
            if (position == 0) {
                downloadCompleteContent.refreshNoDataView();
            } else {
                downloadContent.refreshNoDataView();
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

    // 初始化Tabcontent
    private void initTabContent() {
        for (int i = 0; i < rb_pageStr.length; i++) {
            RadioButton radioButton = (RadioButton) mInflater.inflate(
                    R.layout.homepage_tabgroup_item, null);
            radioButton.setId(i);
            radioButton.setText(rb_pageStr[i]);
            radioButton.setTextSize(16);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(cursorWidth, LayoutParams.MATCH_PARENT);
            radioButton.setLayoutParams(layoutParams);
            rb_pages.add(radioButton);

        }
    }

    private void initTabValue() {
        tab_content.removeAllViews();
        for (int i = 0; i < listViews.size(); i++) {
            rb_pages.get(i).setText(rb_pageStr[i]);
            tab_content.addView(rb_pages.get(i));
            // list.add(rb_pageStr[i]);
        }
    }

    public void setSelector(int id) {
        for (int i = 0; i < rb_pageStr.length; i++) {
            if (id == i) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                        R.color.white);
                rb_pages.get(id).setBackgroundDrawable(
                        new BitmapDrawable(bitmap));
                rb_pages.get(id).setTextColor(getResources().getColor(R.color.login_title));
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
