package com.m1905.mobile.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.adapter.HistoryAdapter;
import com.m1905.mobile.common.HistoryContent;
import com.m1905.mobile.service.HistoryService;

/**
 * @author leepan
 * @since 2013-12-25
 */
public class HistoryAct extends Activity {
    private Button btnFunc;
    private ListView lvwHistory;
    private HistoryAdapter historyAdapter;
    private List<HistoryContent> lstHistories;
    private HistoryService historyService;

    private LinearLayout llBottomBtn;
//    private LinearLayout llNoData;
//    private TextView tvwTitle, tvwSubTitle;
//    private ImageView ivwIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_history);
        btnFunc = (Button) findViewById(R.id.btnFunc);
        btnFunc.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_list_delete_selector));
        ((TextView) findViewById(R.id.tvwNaviNotice))
                .setText(R.string.func_history);

        lvwHistory = (ListView) findViewById(R.id.lvwHistory);
        llBottomBtn = (LinearLayout) findViewById(R.id.ll_bottom_btn);

//        llNoData = (LinearLayout) findViewById(R.id.llNoData);
//        ivwIcon = (ImageView) llNoData.findViewById(R.id.ivwIcon);
//        tvwTitle = (TextView) llNoData.findViewById(R.id.tvwTitle);
//        tvwSubTitle = (TextView) llNoData.findViewById(R.id.tvwSubTitle);


        init();
        loadData();
    }
    @Override
    protected void onResume() {
    	super.onResume();
    	loadData();
    }
    /**
     * 初始化
     */
    private void init() {
        btnFunc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (llBottomBtn.getVisibility() == View.GONE) {
                    setDeleteMode(true);
                } else {
                    setDeleteMode(false);
                }
            }
        });
        lstHistories = new ArrayList<HistoryContent>();
        historyAdapter = new HistoryAdapter(this, lstHistories);
        lvwHistory.setAdapter(historyAdapter);
        lvwHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HistoryContent historyContent = lstHistories.get(position);
                if (historyAdapter.isDeleting()) {
                    if (historyContent.isChecked()) {
                        historyContent.setChecked(false);
                    } else {
                        historyContent.setChecked(true);
                    }
                    lstHistories.set(position, historyContent);
                    historyAdapter.notifyDataSetChanged();
                } else {

                }
            }
        });
        historyService = new HistoryService(this);
    }

    /**
     * 从数据库获取数据
     */
    private void loadData() {
        if (historyService.getAllHistory().size() == 0) {
//            llNoData.setVisibility(View.VISIBLE);
            lvwHistory.setVisibility(View.GONE);
            btnFunc.setEnabled(false);
//            ivwIcon.setImageResource(R.drawable.icon_empty);
//            tvwTitle.setText(R.string.consumeNullTip);
//            tvwSubTitle.setVisibility(View.VISIBLE);
        } else {
//            llNoData.setVisibility(View.GONE);
            lvwHistory.setVisibility(View.VISIBLE);
            btnFunc.setEnabled(true);
            lstHistories.clear();
            lstHistories.addAll(historyService.getAllHistory());
            historyAdapter.notifyDataSetChanged();
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSelectAll:
                for (int i = 0; i < lstHistories.size(); i++) {
                    HistoryContent historyContent = lstHistories.get(i);
                    historyContent.setChecked(true);
                    lstHistories.set(i, historyContent);
                }
                historyAdapter.notifyDataSetChanged();
                break;

            case R.id.btnDelete:
                ArrayList<HistoryContent> deleteHistories = new ArrayList<HistoryContent>();
                for (HistoryContent historyContent : lstHistories) {
                    if (historyContent.isChecked()) {
                        historyService.deleteHistoryByID(historyContent.getId());
                        deleteHistories.add(historyContent);
                    }
                }
                lstHistories.removeAll(deleteHistories);
                loadData();
                setDeleteMode(false);
                break;
        }
    }

    /**
     * 设置是否删除模式
     */
    private void setDeleteMode(boolean isDeleteMode) {
        historyAdapter.setDeleting(isDeleteMode);
        historyAdapter.notifyDataSetChanged();
        if (isDeleteMode) {
            llBottomBtn.setVisibility(View.VISIBLE);
            btnFunc.setBackgroundDrawable(null);
            btnFunc.setText(R.string.cancel);
        } else {
            llBottomBtn.setVisibility(View.GONE);
            btnFunc.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_list_delete_selector));
            btnFunc.setText("");
        }
    }

}
