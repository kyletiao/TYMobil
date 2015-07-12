package com.m1905.mobile.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.dao.Identify;

/**
 * @author leepan
 * @since 2013-12-25
 */
public class SelectLoginAct extends Activity implements OnClickListener {

    private LinearLayout sinaLogin;
    private LinearLayout tencentLogin;
    private LinearLayout m1905Login;
    private RelativeLayout register;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_login);
        sinaLogin = (LinearLayout) findViewById(R.id.btn_sina_login);
        tencentLogin = (LinearLayout) findViewById(R.id.btn_tencent);
        m1905Login = (LinearLayout) findViewById(R.id.btn_m1905);
        register = (RelativeLayout) findViewById(R.id.rlt_register);
        btnBack = (Button) findViewById(R.id.btnBack);
        sinaLogin.setOnClickListener(this);
        tencentLogin.setOnClickListener(this);
        m1905Login.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_sina_login:
                intent.setClass(SelectLoginAct.this, ThirdLoginAct.class);
                intent.putExtra("urlKey", 0);
                startActivityForResult(intent, 999);
                break;
            case R.id.btn_tencent:
                intent.setClass(SelectLoginAct.this, ThirdLoginAct.class);
                intent.putExtra("urlKey", 1);
                startActivityForResult(intent, 999);
                break;
            case R.id.btn_m1905:
                intent.setClass(SelectLoginAct.this, LoginAct.class);
                startActivityForResult(intent, 999);
                break;
            case R.id.btnBack:
                finish();
                break;

            case R.id.rlt_register:
                intent.setClass(SelectLoginAct.this, LoginAct.class);
                intent.putExtra("show_item", 1);
                startActivityForResult(intent, 999);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Identify.currentUser != null) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

}
