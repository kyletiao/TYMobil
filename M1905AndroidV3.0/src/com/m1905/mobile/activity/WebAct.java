package com.m1905.mobile.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.util.AppUtils;

/**
 * 周边详情
 *
 * @author leepan
 */
public class WebAct extends Activity implements OnClickListener {

    private FrameLayout frameLayout = null;
    private WebView webView = null;
    private WebChromeClient chromeClient = null;
    private View myView = null;
    private WebChromeClient.CustomViewCallback myCallBack = null;

    private RelativeLayout rlBottom;
    private Button btnClose;
    private Button btnGoback;
    private Button btnGoforword;
    private Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nearby_detail);

        frameLayout = (FrameLayout)findViewById(R.id.fl_inner);
        webView = (WebView) findViewById(R.id.webView);

        rlBottom = (RelativeLayout) findViewById(R.id.wap_detail_bottom);
        btnClose = (Button) findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);
        btnGoback = (Button) findViewById(R.id.btnGoback);
        btnGoback.setOnClickListener(this);
        btnGoforword = (Button) findViewById(R.id.btnGoforword);
        btnGoforword.setOnClickListener(this);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString("address") != null) {
            setVideoView();
            webView.loadUrl(bundle.getString("address"));

//            webView.setWebViewClient(new WebViewClient() {
//
//                @Override
//                public void onPageFinished(WebView webView, String url) {
//                    if (!webView.canGoBack()) {
//                        btnGoback.setEnabled(false);
//                    } else {
//                        btnGoback.setEnabled(true);
//                    }
//                    if (!webView.canGoForward()) {
//                        btnGoforword.setEnabled(false);
//                    } else {
//                        btnGoforword.setEnabled(true);
//                    }
//                    super.onPageFinished(webView, url);
//                }
//
//            });
        } else {
            AppUtils.toastShowMsg(WebAct.this, "不合法的地址:)");
        }
    }

    /**
     * 设置VideoView
     */
    private void setVideoView(){
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);


        webView.setWebViewClient(new MyWebviewCient());

        chromeClient = new MyChromeClient();

        webView.setWebChromeClient(chromeClient);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);

        final String USER_AGENT_STRING = webView.getSettings().getUserAgentString() + " Rong/2.0";
        webView.getSettings().setUserAgentString( USER_AGENT_STRING );
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setLoadWithOverviewMode(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO 自动生成的方法存根
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        webView.clearCache(true);
        webView.loadUrl("");
        overridePendingTransition(R.anim.push_right_in,
                R.anim.push_right_out);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                finish();
                break;
            case R.id.btnGoback:
                webView.goBack();
                if (webView.canGoBack()) {
                    btnGoback.setEnabled(true);
                } else {
                    btnGoback.setEnabled(false);
                }
                break;
            case R.id.btnGoforword:
                webView.goForward();
                if (webView.canGoForward()) {
                    btnGoforword.setEnabled(true);
                } else {
                    btnGoforword.setEnabled(false);
                }
                break;
            case R.id.btnRefresh:
                webView.reload();
                break;
        }
    }

    public class MyWebviewCient extends WebViewClient{

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!webView.canGoBack()) {
                btnGoback.setEnabled(false);
            } else {
                btnGoback.setEnabled(true);
            }
            if (!webView.canGoForward()) {
                btnGoforword.setEnabled(false);
            } else {
                btnGoforword.setEnabled(true);
            }
            super.onPageFinished(webView, url);
        }
    }

    public class MyChromeClient extends WebChromeClient{

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            if(myView != null){
                callback.onCustomViewHidden();
                return;
            }
            frameLayout.removeView(webView);
            frameLayout.addView(view);
            myView = view;
            myCallBack = callback;
            rlBottom.setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        }

        @Override
        public void onHideCustomView() {
            if(myView == null){
                return;
            }
            frameLayout.removeView(myView);
            myView = null;
            frameLayout.addView(webView);
            myCallBack.onCustomViewHidden();
            rlBottom.setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            // TODO Auto-generated method stub
            Log.d("ZR", consoleMessage.message() + " at " + consoleMessage.sourceId() + ":" + consoleMessage.lineNumber());
            return super.onConsoleMessage(consoleMessage);
        }
    }
}
