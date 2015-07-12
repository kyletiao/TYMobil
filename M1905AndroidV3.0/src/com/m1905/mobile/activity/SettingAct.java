package com.m1905.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.adapter.OtherAdapter;
import com.m1905.mobile.adapter.SettingAdapter;
import com.m1905.mobile.dao.Setting;
import com.m1905.mobile.ui.XListView;
import com.m1905.mobile.util.SDUtils;
import com.m1905.mobile.util.SettingUtils;
import com.m1905.mobile.util.StringUtils;

/**
 * 设置
 *
 * @author forcetech
 */
public class SettingAct extends Activity {
    // 开关设置
    private XListView lvwSettings;
    private List<Setting> lstSettings;
    private SettingAdapter settingAdapter;
    // 分辨率item
    private View resoView;
    private TextView tvwResoNotice;
    private TextView tvwResoValue;
    // 其他设置
    private XListView lvwOtherSettings;
    private List<Setting> lstOtherSettings;
    private OtherAdapter otherAdapter;
    // 缓存item
    private View cacheView;
    private TextView tvwCacheSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_setting);
        findViewById(R.id.btnFunc).setVisibility(View.GONE);
        ((TextView) findViewById(R.id.tvwNaviNotice))
                .setText(R.string.func_setting);
        initSettings();
        initOtherSettings();
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
    protected void onResume() {
        refresh();
        super.onResume();
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * 初始化分辨率item视图
     */
    private void initResolutionView() {
        resoView = LayoutInflater.from(this).inflate(
                R.layout.reso_setting_item, null);
        resoView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingAct.this, ResolutionAct.class));
            }
        });
        tvwResoNotice = (TextView) resoView.findViewById(R.id.tvwResoNotice);
        tvwResoNotice.setText(R.string.funcResolution);
        tvwResoValue = (TextView) resoView.findViewById(R.id.tvwResoValue);
    }

    /**
     * 加载分辨率值
     */
    private void loadResolutionValue() {
        tvwResoValue.setText(getResources().getStringArray(
                R.array.film_resolution)[SettingUtils
                .loadUseFilmResolution(this)]);
    }

    /**
     * 初始化缓存item视图
     */
    private void initCacheView() {
        cacheView = LayoutInflater.from(this)
                .inflate(R.layout.cache_item, null);
        cacheView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                SDUtils.deleteCacheFile();
                refreshOtherSettingsView();
            }
        });
        tvwCacheSize = (TextView) cacheView.findViewById(R.id.tvwCacheSize);
    }

    /**
     * 加载缓存大小
     */
    private void loadCacheValue() {
        tvwCacheSize.setText(StringUtils.conversionBytesUnit(SDUtils
                .getCacheSize()));
    }

    /**
     * 初始化设置界面
     */
    private void initSettingView() {
        lvwSettings = (XListView) findViewById(R.id.lvwSettings);
        lstSettings = new ArrayList<Setting>();
        settingAdapter = new SettingAdapter(this, lstSettings);
        lvwSettings.addFooterView(resoView);
        lvwSettings.setAdapter(settingAdapter);
    }

    /**
     * 初始化其他设置界面
     */
    private void initOtherSettingView() {
        lvwOtherSettings = (XListView) findViewById(R.id.lvwOtherSettings);
        lstOtherSettings = new ArrayList<Setting>();
        otherAdapter = new OtherAdapter(this, lstOtherSettings);
        lvwOtherSettings.addHeaderView(cacheView);
        lvwOtherSettings.setAdapter(otherAdapter);
        lvwOtherSettings.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (lstOtherSettings.get(position - 1).getFuncNoticeId() == R.string.funcScoreNotice) {
                    // 评分
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (lstOtherSettings.get(position - 1).getFuncNoticeId() == R.string.funcAppFuncNotice) {
                    // 功能介绍

                }
            }

        });
    }

    /**
     * 加载其他设置数据
     */
    private void loadOtherSettings() {
        lstOtherSettings.clear();
        lstOtherSettings.add(new Setting(R.string.funcScoreNotice,
                R.string.funcScoreDesc, false));
        lstOtherSettings.add(new Setting(R.string.funcAppFuncNotice,
                R.string.funcAppFuncDesc, false));

    }

    /**
     * 加载开关设置数据
     */
    private void loadSettings() {
        lstSettings.clear();
        lstSettings.add(new Setting(R.string.funcPushNotice,
                R.string.funcPushDesc, SettingUtils.isPush(this)));
        lstSettings.add(new Setting(R.string.funcMoblieNetNotice,
                R.string.funcMoblieNetDesc, SettingUtils
                .isPlayOrDownloadByMoblie(this)));
    }

    /**
     * 初始化上半部分设置
     */
    private void initSettings() {
        initResolutionView();
        initSettingView();
    }

    /**
     * 初始化下半部分设置
     */
    private void initOtherSettings() {
        initCacheView();
        initOtherSettingView();
    }

    /**
     * 刷新其他设置界面
     */
    private void refreshSettingsView() {
        loadResolutionValue();
        loadSettings();
        settingAdapter.notifyDataSetChanged();
    }

    /**
     * 刷新设置界面
     */
    private void refreshOtherSettingsView() {
        loadCacheValue();
        loadOtherSettings();
        otherAdapter.notifyDataSetChanged();
    }

    private void refresh() {
        refreshSettingsView();
        refreshOtherSettingsView();
    }

}
