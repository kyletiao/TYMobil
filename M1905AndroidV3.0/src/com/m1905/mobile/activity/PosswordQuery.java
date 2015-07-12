package com.m1905.mobile.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.activity.LoginAct.MyOnPageChangeListener;
import com.m1905.mobile.adapter.MainAdapter;
import com.m1905.mobile.common.TianyiContent;
import com.m1905.mobile.content.EmailContent;
import com.m1905.mobile.content.LoginContent;
import com.m1905.mobile.content.PhoneContent;
import com.m1905.mobile.content.RegisterContent;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.dao.Register;
import com.m1905.mobile.ui.LoginPassEditText;
import com.m1905.mobile.ui.RegisterNameEditText;
import com.m1905.mobile.ui.SyncHorizontalScrollView;
import com.m1905.mobile.ui.VerificationPassEditText;
import com.m1905.mobile.util.DialogUtil;
import com.m1905.mobile.util.StringUtils;
import com.telecomsdk.phpso.TysxOA;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.InputFilter.LengthFilter;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PosswordQuery extends Activity {
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
    private String[] rb_pageStr = {"手机账户", "邮箱账户"};
    private PhoneContent loginContent;
    private EmailContent registerContent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_possword_query);
        loginContent = new PhoneContent(PosswordQuery.this, R.layout.phone_content);
        registerContent = new EmailContent(PosswordQuery.this, R.layout.email_content);
        init();
        setListener();
	}
	@Override
    public void finish() {
        setResult(Identify.currentUser != null ? 1 : 0);
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
	
	private void init() {
    	btnBack = (Button)(findViewById(R.id.btnBack));
    	btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        mInflater = LayoutInflater.from(PosswordQuery.this);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        cursorWidth = 240;

        mhsv = (SyncHorizontalScrollView) findViewById(R.id.mhsv);
        rl_scroll = (RelativeLayout) findViewById(R.id.rl_scroll);
        tab_content = (RadioGroup) findViewById(R.id.tab_content);
        cursor = (ImageView) findViewById(R.id.cursor);
        vPager = (ViewPager) findViewById(R.id.vPager);
        LayoutParams cursor_Params = cursor.getLayoutParams();
        cursor_Params.width = cursorWidth;// 初始化滑动下标的宽
        cursor.setLayoutParams(cursor_Params);

        mhsv.setSomeParam(rl_scroll,
        		PosswordQuery.this);
        listViews.add(loginContent.getView());
        listViews.add(registerContent.getView());
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

    // 页卡切换监听
    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            if (rb_pages != null && rb_pages.size() > position) {
                rb_pages.get(position).performClick();
                setSelector(position);
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
                    showCursorAnimation(checkedId);
                    vPager.setCurrentItem(checkedId);// 让下方ViewPager跟随上面的HorizontalScrollView切换
                    mCurrentCheckedRadioLeft = tab_content
                            .getChildAt(checkedId).getLeft();
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
            //radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, 32);
            radioButton.setLayoutParams(new LayoutParams(cursorWidth,
                    LayoutParams.FILL_PARENT));
            radioButton.setBackgroundResource(R.color.white);
            rb_pages.add(radioButton);

        }
    }

    private void initTabValue() {
        tab_content.removeAllViews();
        for (int i = 0; i < listViews.size(); i++) {
            rb_pages.get(i).setText(rb_pageStr[i]);
            tab_content.addView(rb_pages.get(i));
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
			}

			else {
				rb_pages.get(i).setBackgroundDrawable(new BitmapDrawable());
				rb_pages.get(i).setTextColor(
						getResources().getColor(R.color.black));
			}
		}

	}

    // 动画
    private void showCursorAnimation(int checkedId) {
        TranslateAnimation _TranslateAnimation = new TranslateAnimation(
                mCurrentCheckedRadioLeft,
                tab_content.getChildAt(checkedId)
                        .getLeft(), 0f, 0f);
        _TranslateAnimation
                .setInterpolator(new LinearInterpolator());
        _TranslateAnimation.setDuration(100);
        _TranslateAnimation.setFillAfter(true);
        cursor.startAnimation(_TranslateAnimation);
    }
}
