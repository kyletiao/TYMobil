package com.m1905.mobile.window;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.dianxin.mobilefree.R;
import com.lidroid.xutils.BitmapUtils;
import com.m1905.mobile.activity.RecommentAct;
import com.m1905.mobile.bean.UpdataBean;
import com.m1905.mobile.common.AppConfig;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class UpdataWindow {
	private Context context;
	private LayoutInflater inflater;
	private View contentView;
	private PopupWindow window;
	private Button bt_close;
	private TextView tv_updata_content;
	private ImageView iv_updata_image;
	private Button btn_ok;
	private Button btn_no;
	public UpdataWindow(final Context context,LayoutInflater inflater){
		this.context = context;
		this.inflater = inflater;
		contentView = inflater.inflate(R.layout.updata_window, null);
		window = new PopupWindow(contentView, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		bt_close = (Button) contentView.findViewById(R.id.bt_close);
		bt_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		tv_updata_content = (TextView) contentView.findViewById(R.id.tv_updata_content);
		iv_updata_image = (ImageView) contentView.findViewById(R.id.iv_updata_image);
		btn_ok = (Button) contentView.findViewById(R.id.btn_ok);
		btn_no = (Button) contentView.findViewById(R.id.btn_no);
	}
	
	public void showUpdataDialog(View parent,final UpdataBean bean){
		BitmapUtils bitmapUtil = new BitmapUtils(context,
				AppConfig.M1905_CACHE_PATH);
		tv_updata_content.setText(bean.getInfo().getList()[0].getDescription());
		bitmapUtil.display(iv_updata_image, bean.getInfo().getList()[0].getUpdateImg());
		btn_ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				window.dismiss();
				DownLoadFileThreadTask task = new DownLoadFileThreadTask(bean.getInfo().getList()[0].getPath(), "/sdcard/m1905100/tv189.apk");
				downloadDialog = new ProgressDialog(context);
				downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				downloadDialog.setMessage("正在下载......");
				downloadDialog.show();
				new Thread(task).start();
			}
		});
		btn_no.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		window.setBackgroundDrawable(new BitmapDrawable());
		window.setFocusable(true);
		window.setOutsideTouchable(true);
		window.update();
		window.showAtLocation(parent, Gravity.CENTER, 0, 0);
	}
	/**
	 * 下载进度条显示
	 */
	private ProgressDialog downloadDialog;
	
	/**
	 * 下载apk操作
	 */
	private class DownLoadFileThreadTask implements Runnable {

		private String apkUrl;
		private String url;

		public DownLoadFileThreadTask(String apkurl, String urls) {
			this.apkUrl = apkurl;
			this.url = urls;
		}

		@Override
		public void run() {
			try {
				File file = getFiles(apkUrl, url,
						downloadDialog);
				downloadDialog.dismiss();
				install(file);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	public File getFiles(String path,String filepath,ProgressDialog downloadDialog){
		URL url;
		try {
			url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			if(conn.getResponseCode() == 200){
				downloadDialog.show();
				int total =  conn.getContentLength();
				downloadDialog.setMax(total/1024);
				InputStream is = conn.getInputStream();
				File file = new File(filepath);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				int process = 0;
				while((len = is.read(buffer))!=-1){
					fos.write(buffer, 0, len);
					process +=len;
					downloadDialog.setProgress(process/1024);
				}
				fos.flush();
				fos.close();
				is.close();
				
				return file;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return null;
		
	}
	/**
	 * 安装apk
	 */
	private void install(File file) {
		Intent intent = new Intent();
		intent.setAction(intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		((Activity) context).finish();
		context.startActivity(intent);
	}
	
}
