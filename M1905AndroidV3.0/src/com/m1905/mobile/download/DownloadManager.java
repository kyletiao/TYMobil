package com.m1905.mobile.download;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.db.converter.ColumnConverter;
import com.lidroid.xutils.db.converter.ColumnConverterFactory;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.LogUtils;

/**
 * Author: leepan
 * Date: 2013-12-24
 */
public class DownloadManager {

    private List<DownloadInfo> downloadInfoList;
    private List<DownloadInfo> downloadCompletedList;

    private int maxDownloadThread = 1;

    private Context mContext;
    private DbUtils db;

    /* package */DownloadManager(Context appContext) {
        ColumnConverterFactory.registerColumnConverter(HttpHandler.State.class, new HttpHandlerStateConverter());
        mContext = appContext;
        db = DbUtils.create(mContext);
        try {
            downloadInfoList = db.findAll(Selector.from(DownloadInfo.class).where("state", "<>", 4));
//            LogUtils.e(downloadInfoList.toString());
            downloadCompletedList = db.findAll(Selector.from(DownloadInfo.class).where("state", "=", 4));
//            LogUtils.e(downloadCompletedList.toString());
        } catch (DbException e) {
            LogUtils.e(e.getMessage(), e);
        }
        if (downloadInfoList == null) {
            downloadInfoList = new ArrayList<DownloadInfo>();
        }
        if (downloadCompletedList == null) {
            downloadCompletedList = new ArrayList<DownloadInfo>();
        }
    }

    public int getDownloadInfoListCount() {
        return downloadInfoList.size();
    }

    public DownloadInfo getDownloadInfo(int index) {
        return downloadInfoList.get(index);
    }

    public void setDownloadInfo(int index, DownloadInfo downloadInfo) {
        downloadInfoList.set(index, downloadInfo);
    }

    public int getDownloadCompletedListCount() {
        return downloadCompletedList.size();
    }

    public DownloadInfo getDownloadCompletedInfo(int index) {
        return downloadCompletedList.get(index);
    }

    public void addNewDownload(String url, String fileName, String target,
                               boolean autoResume, boolean autoRename,
                               final RequestCallBack<File> callback) throws DbException {
    	System.out.println("下载名称"+fileName+"下载地址："+url);
        final DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setDownloadUrl(url);
        downloadInfo.setAutoRename(autoRename);
        downloadInfo.setAutoResume(autoResume);
        downloadInfo.setFileName(fileName);
        downloadInfo.setFileSavePath(target);
        HttpUtils http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(url, target, autoResume,
                autoRename, new ManagerCallBack(downloadInfo, callback));
        downloadInfo.setHandler(handler);
        downloadInfo.setState(handler.getState());
        downloadInfoList.add(downloadInfo);
        db.saveBindingId(downloadInfo);
        System.out.println("下载完成");
    }

    public void resumeDownload(int index, final RequestCallBack<File> callback)
            throws DbException {
        final DownloadInfo downloadInfo = downloadInfoList.get(index);
        resumeDownload(downloadInfo, callback);
    }

    public void resumeDownload(DownloadInfo downloadInfo,
                               final RequestCallBack<File> callback) throws DbException {
        HttpUtils http = new HttpUtils();
        http.configRequestThreadPoolSize(maxDownloadThread);
        HttpHandler<File> handler = http.download(
                downloadInfo.getDownloadUrl(), downloadInfo.getFileSavePath(),
                downloadInfo.isAutoResume(), downloadInfo.isAutoRename(),
                new ManagerCallBack(downloadInfo, callback));
        downloadInfo.setHandler(handler);
        downloadInfo.setState(handler.getState());
        db.saveOrUpdate(downloadInfo);
    }

    public void resumeAllDownload(final RequestCallBack<File> callback) throws DbException {
        for (DownloadInfo downloadInfo : downloadInfoList) {
            resumeDownload(downloadInfo, callback);
        }
    }

    public void deleteDownload(ArrayList<DownloadInfo> deleteData) {
        try {
            db.deleteAll(deleteData);
            for (DownloadInfo downloadInfo : deleteData) {
                new File(downloadInfo.getFileSavePath()).delete();
                downloadInfoList.remove(downloadInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteDownloadCompleted(ArrayList<DownloadInfo> deleteData) {
        try {
            db.deleteAll(deleteData);
            for(DownloadInfo downloadInfo : deleteData){
                new File(downloadInfo.getFileSavePath());
                downloadCompletedList.remove(downloadInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeDownload(int index) throws DbException {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        removeDownload(downloadInfo);
    }

    public void removeDownload(DownloadInfo downloadInfo) throws DbException {
//		HttpHandler<File> handler = downloadInfo.getHandler();
//		if (handler != null && !handler.isStopped()) {
//			handler.stop();
//		}
//		try {
//			downloadInfo.setState(State.SUCCESS)
//			db.saveOrUpdate(downloadInfo);
//		} catch (DbException e) {
//			LogUtils.e(e.getMessage(), e);
//		}
        downloadCompletedList.add(downloadInfo);
        downloadInfoList.remove(downloadInfo);

    }

    public void stopDownload(int index) throws DbException {
        DownloadInfo downloadInfo = downloadInfoList.get(index);
        stopDownload(downloadInfo);
    }

    public void stopDownload(DownloadInfo downloadInfo) throws DbException {
        HttpHandler<File> handler = downloadInfo.getHandler();
        if (handler != null && !handler.isStopped()) {
            handler.stop();
        } else {
            downloadInfo.setState(HttpHandler.State.STOPPED);
        }
        db.saveOrUpdate(downloadInfo);
    }

    public void stopAllDownload() throws DbException {
        for (DownloadInfo downloadInfo : downloadInfoList) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null && !handler.isStopped()) {
                handler.stop();
            } else {
                downloadInfo.setState(HttpHandler.State.STOPPED);
            }
        }
        db.saveOrUpdateAll(downloadInfoList);
    }

    public void backupDownloadInfoList() throws DbException {
        if (downloadInfoList.size() != 0) {
            for (DownloadInfo downloadInfo : downloadInfoList) {
                HttpHandler<File> handler = downloadInfo.getHandler();
                if (handler != null) {
                    downloadInfo.setState(handler.getState());
                }
            }
            db.saveOrUpdateAll(downloadInfoList);
        }

    }

    public void backupDownloadCompletedInfoList() throws DbException {
        if (downloadCompletedList.size() != 0) {
            for (DownloadInfo downloadInfo : downloadCompletedList) {
                HttpHandler<File> handler = downloadInfo.getHandler();
                if (handler != null) {
                    downloadInfo.setState(handler.getState());
                }
            }
            db.saveOrUpdateAll(downloadCompletedList);
        }

    }

    public int getMaxDownloadThread() {
        return maxDownloadThread;
    }

    public void setMaxDownloadThread(int maxDownloadThread) {
        this.maxDownloadThread = maxDownloadThread;
    }

    public class ManagerCallBack extends RequestCallBack<File> {
        private DownloadInfo downloadInfo;
        private RequestCallBack<File> baseCallBack;

        public RequestCallBack<File> getBaseCallBack() {
            return baseCallBack;
        }

        public void setBaseCallBack(RequestCallBack<File> baseCallBack) {
            this.baseCallBack = baseCallBack;
        }

        private ManagerCallBack(DownloadInfo downloadInfo,
                                RequestCallBack<File> baseCallBack) {
            this.baseCallBack = baseCallBack;
            this.downloadInfo = downloadInfo;
        }

        @Override
        public Object getUserTag() {
            if (baseCallBack == null)
                return null;
            return baseCallBack.getUserTag();
        }

        @Override
        public void setUserTag(Object userTag) {
            if (baseCallBack == null)
                return;
            baseCallBack.setUserTag(userTag);
        }

        @Override
        public void onStart() {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onStart();
            }
        }

        @Override
        public void onStopped() {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onStopped();
            }
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            downloadInfo.setFileLength(total);
            downloadInfo.setProgress(current);
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onLoading(total, current, isUploading);
            }
        }

        @Override
        public void onSuccess(ResponseInfo<File> responseInfo) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onSuccess(responseInfo);
            }
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            HttpHandler<File> handler = downloadInfo.getHandler();
            if (handler != null) {
                downloadInfo.setState(handler.getState());
            }
            try {
                db.saveOrUpdate(downloadInfo);
            } catch (DbException e) {
                LogUtils.e(e.getMessage(), e);
            }
            if (baseCallBack != null) {
                baseCallBack.onFailure(error, msg);
            }
        }
    }

    private class HttpHandlerStateConverter implements
            ColumnConverter<HttpHandler.State> {

        @Override
        public HttpHandler.State getFiledValue(Cursor cursor, int index) {
            return HttpHandler.State.valueOf(cursor.getInt(index));
        }

        @Override
        public HttpHandler.State getFiledValue(String fieldStringValue) {
            if (fieldStringValue == null)
                return null;
            return HttpHandler.State.valueOf(fieldStringValue);
        }

        @Override
        public Object fieldValue2ColumnValue(HttpHandler.State fieldValue) {
            return fieldValue.value();
        }

        @Override
        public String getColumnDbType() {
            return "INTEGER";
        }
    }
}
