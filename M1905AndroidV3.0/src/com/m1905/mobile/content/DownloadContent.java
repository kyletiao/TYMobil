package com.m1905.mobile.content;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dianxin.mobilefree.R;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;
import com.m1905.mobile.common.Constant;
import com.m1905.mobile.download.DownloadInfo;
import com.m1905.mobile.download.DownloadManager;
import com.m1905.mobile.download.DownloadService;
import com.m1905.mobile.ui.ScrollContent;
import com.m1905.mobile.util.SDUtils;
import com.m1905.mobile.util.StringUtils;
/**
 * 正在下载
 * @author chenxian
 *
 */
public class DownloadContent extends ScrollContent implements
        OnItemClickListener {

    private ListView downloadList;
    private LinearLayout llMenu;
    private Button btnSelectAll, btnDelete;

    private DownloadManager downloadManager;
    private DownloadListAdapter downloadListAdapter;
    private static DownloadContent instance;
    private boolean isEditable = false;
    private ProgressBar pbrsdInfo;
    private TextView tvwSdInfo;
    private Handler handler;

//    private LinearLayout llNoData;
//    private ImageView ivwIcon;
//    private TextView tvwTitle, tvwSubTitle;

    public DownloadContent(Activity activity, int resourceID) {
        super(activity, resourceID);
        instance = this;
        downloadList = (ListView) findViewById(R.id.download_list);
        llMenu = (LinearLayout) findViewById(R.id.ll_menu);
        btnSelectAll = (Button) llMenu.findViewById(R.id.btnSelectAll);
        btnDelete = (Button) llMenu.findViewById(R.id.btnDelete);

//        llNoData = (LinearLayout) findViewById(R.id.llNoData);
//        ivwIcon = (ImageView) llNoData.findViewById(R.id.ivwIcon);
//        tvwTitle = (TextView) llNoData.findViewById(R.id.tvwTitle);
//        tvwSubTitle = (TextView) llNoData.findViewById(R.id.tvwSubTitle);

        downloadManager = DownloadService.getDownloadManager(activity.getApplicationContext());
        pbrsdInfo = (ProgressBar) findViewById(R.id.pbrsdInfo);
        tvwSdInfo = (TextView) findViewById(R.id.tvwSdInfo);
        if (SDUtils.isSDCardWritable()) {
            pbrsdInfo
                    .setProgress((int) ((SDUtils.getSDCardTotalSize() - SDUtils.getSDCardBlockSize()) * 100 / SDUtils.getSDCardTotalSize()));
            tvwSdInfo.setText("剩余空间：" + StringUtils.conversionBytesUnit((SDUtils.getSDCardBlockSize()))
                    + " / 总空间："
                    + StringUtils.conversionBytesUnit(SDUtils.getSDCardTotalSize())
            );
        } else {
            pbrsdInfo.setProgress(0);
        }

        downloadListAdapter = new DownloadListAdapter(activity.getApplicationContext());
        downloadList.setAdapter(downloadListAdapter);
        downloadList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                HttpHandler.State state = downloadManager.getDownloadInfo(position).getState();
                switch (state) {
                    case WAITING:
                    case STARTED:
                    case LOADING:
                        try {
                            downloadManager.stopDownload(downloadManager.getDownloadInfo(position));
                        } catch (DbException e) {
                            LogUtils.e(e.getMessage(), e);
                        }
                        break;
                    case STOPPED:
                    case FAILURE:
                        try {
                            downloadManager.resumeDownload(downloadManager.getDownloadInfo(position),
                                    new DownloadRequestCallBack());
                        } catch (DbException e) {
                            LogUtils.e(e.getMessage(), e);
                        }
                        downloadListAdapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }

            }

        });

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadListAdapter.selectAll();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount = downloadManager.getDownloadInfoListCount();
                ArrayList<DownloadInfo> deleteData = new ArrayList();
                for (int i = 0; i < amount; i++) {
                    if (downloadListAdapter.getIsSelected().get(i)) {
                        try {
                            downloadManager.stopDownload(i);
                            deleteData.add(downloadManager.getDownloadInfo(i));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }

                }
                downloadManager.deleteDownload(deleteData);
                setEditable(false);
                Message m = new Message();
                m.what = Constant.HANDLER_INT_DELETE;
                handler.sendMessage(m);
            }
        });
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
        refreshNoDataView();
    }

    public static DownloadContent getInstance() {
        return instance;
    }

    public void refreshNoDataView() {
        Message m = new Message();
        if (downloadManager.getDownloadInfoListCount() == 0) {
//            llNoData.setVisibility(View.VISIBLE);
//            ivwIcon.setImageResource(R.drawable.icon_empty);
//            tvwTitle.setText(R.string.consumeNullTip);
//            tvwSubTitle.setVisibility(View.VISIBLE);
        	System.out.println("正在下载无内容");
            downloadList.setVisibility(View.INVISIBLE);
            m.what = Constant.HANDLER_INT_NODATA;
        	/*downloadList.setVisibility(View.VISIBLE);
            m.what = Constant.HANDLER_INT_SOMEDATA;*/
        } else {
//            llNoData.setVisibility(View.GONE);
        	System.out.println("正在下载有内容");
            downloadList.setVisibility(View.VISIBLE);
            m.what = Constant.HANDLER_INT_SOMEDATA;
        }
        handler.sendMessage(m);
    }

    public void onResume() {
        downloadListAdapter.notifyDataSetChanged();
    }

    public void onDestroy() {
        try {
            if (downloadListAdapter != null && downloadManager != null) {
                downloadManager.backupDownloadInfoList();
            }
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
        if (isEditable) {
            llMenu.setVisibility(View.VISIBLE);
        } else {
            llMenu.setVisibility(View.GONE);
            downloadListAdapter.initDate();
        }
        downloadListAdapter.notifyDataSetChanged();

    }

    private class DownloadListAdapter extends BaseAdapter {

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

        // 初始化isSelected的数据
        private void initDate() {
            for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
                getIsSelected().put(i, false);
            }
        }

        public void selectAll() {
            for (int i = 0; i < downloadManager.getDownloadInfoListCount(); i++) {
                getIsSelected().put(i, true);
            }
            notifyDataSetChanged();
        }

        public HashMap<Integer, Boolean> getIsSelected() {
            return isSelected;
        }

        public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
            this.isSelected = isSelected;
        }

        @Override
        public int getCount() {
            if (downloadManager == null)
                return 0;
            return downloadManager.getDownloadInfoListCount();
        }

        @Override
        public Object getItem(int i) {
            return downloadManager.getDownloadInfo(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressWarnings("unchecked")
        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            DownloadItemViewHolder holder = null;
            final DownloadInfo downloadInfo = downloadManager
                    .getDownloadInfo(i);
            if (view == null) {
                view = mInflater.inflate(R.layout.download_item, null);
                holder = new DownloadItemViewHolder(downloadInfo);
                holder.tvwDownloadName = (TextView) view
                        .findViewById(R.id.tvwDownloadName);
                holder.tvwDownloadState =
                        (TextView) view.findViewById(R.id.tvwDownloadState);
                holder.pbrDownload = (ProgressBar) view
                        .findViewById(R.id.pbrDownload);
                holder.tvwDownloadInfo = (TextView) view
                        .findViewById(R.id.tvwDownloadInfo);
                holder.btnFunction = (ImageButton) view
                        .findViewById(R.id.btnFunction);
                holder.chkItem = (CheckBox) view.findViewById(R.id.chkItem);
                view.setTag(holder);
                holder.refresh();
            } else {
                holder = (DownloadItemViewHolder) view.getTag();
                holder.update(downloadInfo);
            }

            if (isEditable) {
                downloadList.setEnabled(false);
                holder.chkItem.setVisibility(View.VISIBLE);
                holder.btnFunction.setVisibility(View.GONE);
                holder.tvwDownloadState.setVisibility(View.GONE);
                holder.chkItem.setChecked(isSelected.get(i));
                holder.chkItem.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        isSelected.put(i, isChecked);
                        notifyDataSetChanged();

//								if (isChecked)
//									paths.add(getItem(position));
//								else {
//									int location = paths
//											.indexOf(getItem(position));
//									if (location >= 0)
//										paths.remove(location);
//								}
//								if ((!paths.isEmpty())
//										&& (!btn_clear.isEnabled())) {
//									btn_clear.setEnabled(true);
//									btn_clear.setTextColor(Color
//											.parseColor("#ffffff"));
//								}
                    }
                });

            } else {
                // holder.ivw_delete.setVisibility(View.GONE);
                holder.chkItem.setVisibility(View.GONE);
                holder.btnFunction.setVisibility(View.VISIBLE);
                holder.tvwDownloadState.setVisibility(View.VISIBLE);
                downloadList.setEnabled(true);
            }


            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                RequestCallBack callBack = handler.getRequestCallBack();
                if (callBack instanceof DownloadManager.ManagerCallBack) {
                    DownloadManager.ManagerCallBack managerCallBack = (DownloadManager.ManagerCallBack) callBack;
                    if (managerCallBack.getBaseCallBack() == null) {
                        managerCallBack
                                .setBaseCallBack(new DownloadRequestCallBack());
                    }
                }
                callBack.setUserTag(new WeakReference<DownloadItemViewHolder>(
                        holder));
            }

            return view;
        }
    }

    public class DownloadItemViewHolder {
        TextView tvwDownloadName;
        TextView tvwDownloadState;
        ProgressBar pbrDownload;
        ImageButton btnFunction;
        TextView tvwDownloadInfo;
        CheckBox chkItem;

        private DownloadInfo downloadInfo;

        public DownloadItemViewHolder(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
        }

        public void update(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
            refresh();
        }

        public void refresh() {
            tvwDownloadName.setText(downloadInfo.getFileName());
            tvwDownloadState.setText(downloadInfo.getState().toString());
            if (downloadInfo.getFileLength() > 0) {
                pbrDownload
                        .setProgress((int) (downloadInfo.getProgress() * 100 / downloadInfo
                                .getFileLength()));
                tvwDownloadInfo.setText(StringUtils.conversionByteToM(downloadInfo
                        .getProgress())
                        + "/"
                        + StringUtils.conversionByteToM(downloadInfo
                        .getFileLength())
                        + "("
                        + StringUtils.conversionByteToPer(downloadInfo.getProgress(),
                        downloadInfo.getFileLength()) + ")");
            } else {
                pbrDownload.setProgress(0);
            }

            btnFunction.setVisibility(View.VISIBLE);
            tvwDownloadState.setText(activity.getApplicationContext().getString(R.string.downing));
            btnFunction.setImageResource(R.drawable.down);
            HttpHandler.State state = downloadInfo.getState();
            switch (state) {
                case WAITING:
                    tvwDownloadState.setText(activity.getApplicationContext().getString(R.string.wating));
                    btnFunction.setImageResource(R.drawable.wait);
                    pbrDownload.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_downing));
                    break;
                case STARTED:
                    tvwDownloadState.setText(activity.getApplicationContext().getString(R.string.downing));
                    btnFunction.setImageResource(R.drawable.down);
                    pbrDownload.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_downing));
                    break;
                case LOADING:
                    tvwDownloadState.setText(activity.getApplicationContext().getString(R.string.downing));
                    btnFunction.setImageResource(R.drawable.down);
                    pbrDownload.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_downing));
                    break;
                case STOPPED:
                    tvwDownloadState.setText(activity.getApplicationContext().getString(R.string.stoped));
                    btnFunction.setImageResource(R.drawable.stop);
                    pbrDownload.setProgressDrawable(activity.getResources().getDrawable(R.drawable.progress_pause));
                    break;
                case SUCCESS:
                    btnFunction.setVisibility(View.INVISIBLE);
                    try {
                        downloadManager.removeDownload(downloadInfo);
                        downloadListAdapter.notifyDataSetChanged();
                    } catch (DbException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    break;
                case FAILURE:
                    break;
                default:
                    break;
            }
        }
    }

    private class DownloadRequestCallBack extends RequestCallBack<File> {

        @SuppressWarnings("unchecked")
        private void refreshListItem() {
            if (userTag == null)
                return;
            WeakReference<DownloadItemViewHolder> tag = (WeakReference<DownloadItemViewHolder>) userTag;
            DownloadItemViewHolder holder = tag.get();
            if (holder != null) {
                holder.refresh();
            }
        }

        @Override
        public void onStart() {
            refreshListItem();
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            refreshListItem();
        }

        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            refreshListItem();
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            refreshListItem();
        }

        @Override
        public void onStopped() {
            refreshListItem();
        }
    }
}
