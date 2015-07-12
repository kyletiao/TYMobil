package com.m1905.mobile.content;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dianxin.mobilefree.R;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.m1905.mobile.activity.M1905VideoPlayerActivity;
import com.m1905.mobile.common.Constant;
import com.m1905.mobile.download.DownloadInfo;
import com.m1905.mobile.download.DownloadManager;
import com.m1905.mobile.download.DownloadService;
import com.m1905.mobile.ui.ScrollContent;
import com.m1905.mobile.util.StringUtils;
/**
 * 已完成下载
 * @author chenxian
 *
 */
public class DownloadCompleteContent extends ScrollContent implements
        OnItemClickListener {


    private static DownloadCompleteContent instance;
    private ListView downloadCompletedList;
    private LinearLayout llMenu;
    private Button btnSelectAll, btnDelete;

    private DownloadManager downloadManager;
    private DownloadListAdapter downloadCompletedListAdapter;

    private boolean isEditable = false;
    private Handler handler;

//    private LinearLayout llNoData;
//    private ImageView ivwIcon;
//    private TextView tvwTitle, tvwSubTitle;


    public DownloadCompleteContent(final Activity activity, int resourceID) {
        super(activity, resourceID);
        instance = this;
        downloadCompletedList = (ListView) findViewById(R.id.download_complete_list);
        llMenu = (LinearLayout) findViewById(R.id.ll_menu);
        btnSelectAll = (Button) llMenu.findViewById(R.id.btnSelectAll);
        btnDelete = (Button) llMenu.findViewById(R.id.btnDelete);

//        llNoData = (LinearLayout) findViewById(R.id.llNoData);
//        ivwIcon = (ImageView) llNoData.findViewById(R.id.ivwIcon);
//        tvwTitle = (TextView) llNoData.findViewById(R.id.tvwTitle);
//        tvwSubTitle = (TextView) llNoData.findViewById(R.id.tvwSubTitle);

        downloadManager = DownloadService.getDownloadManager(activity.getApplicationContext());

        downloadCompletedListAdapter = new DownloadListAdapter(activity.getApplicationContext());
        downloadCompletedList.setAdapter(downloadCompletedListAdapter);
        downloadCompletedList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // downloadManager.getDownloadInfo(position)
                Toast.makeText(
                        activity.getApplicationContext(),
                        downloadManager.getDownloadCompletedInfo(position)
                                .getFileName()
                                + ":"
                                + downloadManager.getDownloadCompletedInfo(
                                position).getFileLength(),
                        Toast.LENGTH_LONG).show();

            }

        });

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadCompletedListAdapter.selectAll();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = downloadCompletedListAdapter.getIsSelected().size();
                ArrayList<DownloadInfo> deleteData = new ArrayList<DownloadInfo>();
                for (int i = 0; i < size; i++) {
                    if (downloadCompletedListAdapter.isSelected.get(i)) {
                        deleteData.add(downloadManager.getDownloadCompletedInfo(i));
                    }
                }
                downloadManager.deleteDownloadCompleted(deleteData);
                setEditable(false);
                Message m = new Message();
                m.what = Constant.HANDLER_INT_DELETE;
                handler.sendMessage(m);
            }
        });

    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setHandler(Handler handler){
        this.handler = handler;
        refreshNoDataView();
    }

    public void refreshNoDataView(){
        Message m = new Message();
        if(downloadManager.getDownloadCompletedListCount() == 0){
//            llNoData.setVisibility(View.VISIBLE);
//            ivwIcon.setImageResource(R.drawable.icon_empty);
//            tvwTitle.setText(R.string.consumeNullTip);
//            tvwSubTitle.setVisibility(View.VISIBLE);
        	System.out.println("已下载无内容");
            downloadCompletedList.setVisibility(View.GONE);
            m.what = Constant.HANDLER_INT_NODATA;
        } else {
        	System.out.println("已下载有内容");
//            llNoData.setVisibility(View.GONE);
            downloadCompletedList.setVisibility(View.VISIBLE);
            m.what = Constant.HANDLER_INT_SOMEDATA;
        }
        handler.sendMessage(m); 
    }

    public void setEditable(boolean isEditable) {
    	downloadCompletedListAdapter.newSelectAll();
        this.isEditable = isEditable;
        if (isEditable) {
            llMenu.setVisibility(View.VISIBLE);
        } else {
            llMenu.setVisibility(View.GONE);
        }
        downloadCompletedListAdapter.notifyDataSetChanged();
    }

    public static DownloadCompleteContent getInstance() {
        return instance;
    }


    public void onResume() {
        downloadCompletedListAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        try {
            if (downloadCompletedListAdapter != null && downloadManager != null) {
                downloadManager.backupDownloadCompletedInfoList();
            }
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
    }

    public class DownloadListAdapter extends BaseAdapter {

        private final Context mContext;
        private final LayoutInflater mInflater;
        // 用来控制CheckBox的选中状况
        private HashMap<Integer, Boolean> isSelected;

        private DownloadListAdapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            isSelected = new HashMap<Integer, Boolean>();
            initDate();
        }

        public void selectAll() {
            for (int i = 0; i < downloadManager.getDownloadCompletedListCount(); i++) {
                getIsSelected().put(i, true);
            }
            notifyDataSetChanged();
        }
        public void newSelectAll() {
        	System.out.println("重新加载数据");
    		isSelected = null;
        	isSelected = new HashMap<Integer, Boolean>();
        	for (int i = 0; i < downloadManager.getDownloadCompletedListCount(); i++) {
        		isSelected.put(i, false);
            }
    	}

        // 初始化isSelected的数据
        private void initDate() {
            for (int i = 0; i < downloadManager.getDownloadCompletedListCount(); i++) {
                getIsSelected().put(i, false);
            }
        }
       
        public HashMap<Integer, Boolean> getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
            this.isSelected = isSelected;
        }

        @Override
        public int getCount() {
            if (downloadManager == null){
            	System.out.println("当前正在下载数据为0");
                return 0;
            }
            return downloadManager.getDownloadCompletedListCount();
        }

        @Override
        public Object getItem(int i) {
            return downloadManager.getDownloadCompletedInfo(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressWarnings("unchecked")
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            DownloadItemViewHolder holder = null;
            final DownloadInfo downloadInfo = downloadManager.getDownloadCompletedInfo(i);
            if (view == null) {
                view = mInflater
                        .inflate(R.layout.local_item, null);
                holder = new DownloadItemViewHolder(downloadInfo);
                holder.chkItem = (CheckBox) view.findViewById(R.id.chkItem);
                holder.tvwFileName = (TextView) view
                        .findViewById(R.id.tvwFileName);
                holder.tvwFileSize = (TextView) view
                        .findViewById(R.id.tvwFileSize);
                holder.btnPlay = (ImageButton) view
                        .findViewById(R.id.btnPlay);
                holder.tvwFileSize.setText(StringUtils
                        .conversionByteToM(downloadInfo.getFileLength()));
                holder.tvwFileName.setText(downloadInfo.getFileName());
                view.setTag(holder);
            } else {
                holder = (DownloadItemViewHolder) view.getTag();
            }
            if (isEditable) {
            	//initDate(); 
                holder.chkItem.setVisibility(View.VISIBLE);
                holder.btnPlay.setVisibility(View.GONE);
                System.out.println("当前下载内容boolean值为"+isSelected.get(i));
                holder.chkItem.setChecked(isSelected.get(i));
                holder.chkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        isSelected.put(i, b);
                        notifyDataSetChanged();
                    }
                });
            } else {
                holder.chkItem.setVisibility(View.GONE);
                holder.btnPlay.setVisibility(View.VISIBLE);
            }
            holder.btnPlay.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("下载内容数据"+downloadInfo.getDownloadUrl()+";;;;;"+downloadInfo.getFileSavePath()+";;;;;"+downloadInfo.getProgress());
					Intent intent = new Intent(activity,
							M1905VideoPlayerActivity.class);
					//intent.putExtra("id", Integer.parseInt("123"));
					intent.putExtra("type", -1);
					intent.putExtra("path",downloadInfo.getFileSavePath());
					context.startActivity(intent);
				}
			});
            return view;
        }
    }

    public class DownloadItemViewHolder {
        CheckBox chkItem;
        TextView tvwFileName;
        ImageButton btnPlay;
        TextView tvwFileSize;

        private DownloadInfo downloadInfo;

        public DownloadItemViewHolder(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        public void update(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }
    }
	
}
