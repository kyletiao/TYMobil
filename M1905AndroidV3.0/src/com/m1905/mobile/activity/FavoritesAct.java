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
import com.m1905.mobile.adapter.FavouriteAdapter;
import com.m1905.mobile.common.FavouriteContent;
import com.m1905.mobile.service.FavouriteService;

/**
 * 我的收藏
 *
 * @author leepan
 * @since 2013-12-25
 */
public class FavoritesAct extends Activity {
    private Button btnFunc;
    private ListView lvwFavourite;
    List<FavouriteContent> lstFavourites;
    FavouriteAdapter favouriteAdapter;
    FavouriteService favouriteService;
    LinearLayout llBottomBtn;

//    private LinearLayout llNoData;
//    private TextView tvwTitle, tvwSubTitle;
//    private ImageView ivwIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_favorites);
        btnFunc = (Button) findViewById(R.id.btnFunc);
        btnFunc.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_list_delete_selector));
        ((TextView) findViewById(R.id.tvwNaviNotice))
                .setText(R.string.func_favorite);
        lvwFavourite = (ListView) findViewById(R.id.lvwFavourite);
        lstFavourites = new ArrayList<FavouriteContent>();
        llBottomBtn = (LinearLayout) findViewById(R.id.ll_bottom_btn);
//        llNoData = (LinearLayout) findViewById(R.id.llNoData);
//        ivwIcon = (ImageView) llNoData.findViewById(R.id.ivwIcon);
//        tvwTitle = (TextView) llNoData.findViewById(R.id.tvwTitle);
//        tvwSubTitle = (TextView) llNoData.findViewById(R.id.tvwSubTitle);
        btnFunc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favouriteAdapter.isDeleting()) {
                    setDeleteMode(false);
                } else {
                    setDeleteMode(true);
                }
            }
        });

        init();
        loadData();
    }

    private void init() {
        favouriteService = new FavouriteService(this);
        favouriteAdapter = new FavouriteAdapter(this, lstFavourites);
        lvwFavourite.setAdapter(favouriteAdapter);
        lvwFavourite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavouriteContent favouriteContent = lstFavourites.get(position);
                if (favouriteAdapter.isDeleting()) {
                    if (favouriteContent.isChecked()) {
                        favouriteContent.setChecked(false);
                    } else {
                        favouriteContent.setChecked(true);
                    }
                    lstFavourites.set(position, favouriteContent);
                    favouriteAdapter.notifyDataSetChanged();
                } else {

                }
            }
        });
    }

    private void loadData() {
        if (favouriteService.getAllFavourite().size() == 0) {
//            llNoData.setVisibility(View.VISIBLE);
            lvwFavourite.setVisibility(View.GONE);
            btnFunc.setEnabled(false);
//            ivwIcon.setImageResource(R.drawable.icon_empty);
//            tvwTitle.setText(R.string.consumeNullTip);
//            tvwSubTitle.setVisibility(View.VISIBLE);
        } else {
//            llNoData.setVisibility(View.GONE);
            lvwFavourite.setVisibility(View.VISIBLE);
            btnFunc.setEnabled(true);
            lstFavourites.clear();
            lstFavourites.addAll(favouriteService.getAllFavourite());
            favouriteAdapter.notifyDataSetChanged();
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSelectAll:
                for (int i = 0; i < lstFavourites.size(); i++) {
                    FavouriteContent favouriteContent = lstFavourites.get(i);
                    favouriteContent.setChecked(true);
                    lstFavourites.set(i, favouriteContent);
                }
                favouriteAdapter.notifyDataSetChanged();
                break;

            case R.id.btnDelete:
                ArrayList<FavouriteContent> deleteFavourites = new ArrayList<FavouriteContent>();
                for (FavouriteContent favouriteContent : lstFavourites) {
                    if (favouriteContent.isChecked()) {
                        favouriteService.deleteFavouriteByID(favouriteContent.getId());
                        deleteFavourites.add(favouriteContent);
                    }
                }
                lstFavourites.removeAll(deleteFavourites);
                loadData();
                setDeleteMode(false);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * 设置是否删除模式
     */
    private void setDeleteMode(boolean isDeleteMode) {
        favouriteAdapter.setDeleting(isDeleteMode);
        favouriteAdapter.notifyDataSetChanged();
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
