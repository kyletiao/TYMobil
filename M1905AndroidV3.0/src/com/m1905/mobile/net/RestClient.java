package com.m1905.mobile.net;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.dianxin.mobilefree.R;
import com.m1905.mobile.common.AppConfig;
import com.m1905.mobile.common.Constant;
import com.m1905.mobile.dao.Identify;
import com.m1905.mobile.util.LogUtils;

public class RestClient {

	private static final int SOCKET_BUFFER_SIZE = 8192;
	//private static String contextPath = null;
	private HttpParams params = null;
	private HttpClient client = null;
	protected BasicHeader[] defaultHeaders = null;
	protected boolean useDefaultHeader = true;

	public RestClient(Context context) {
		params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params,
				AppConfig.CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, AppConfig.SO_TIMEOUT);
		HttpConnectionParams.setSocketBufferSize(params, SOCKET_BUFFER_SIZE);
		HttpClientParams.setRedirecting(params, true);
		HttpProtocolParams.setUserAgent(params, "M1905 Agent");
		initDefaultHeaders();
		//contextPath = context.getResources().getString(R.string.host_uri);
	}

	public static RestClient newInstance(Context context) {
		return new RestClient(context);
	}

	/**
	 * 初始化默认header
	 */
	private void initDefaultHeaders() {
		this.defaultHeaders = new BasicHeader[] {
				new BasicHeader("uid", Identify.getUid()),
				new BasicHeader("pid", String.valueOf(Constant.APP_VERSION
						.getPid())),
				new BasicHeader("ver", Constant.APP_VERSION.getVer()),
				new BasicHeader("did", String.valueOf(Identify.device.getDid())),
				new BasicHeader("sid", Identify.sid),
				new BasicHeader("key", Identify.getKey()) };
	}

	public void setDefaultHeaders(BasicHeader... defaultHeaders) {
		this.defaultHeaders = defaultHeaders;
	}

	public void setUseDefaultHeader(boolean useDefaultHeader) {
		this.useDefaultHeader = useDefaultHeader;
	}

	public void close() {
		if (client != null) {
			client.getConnectionManager().shutdown();
			client = null;
		}
	}

	private HttpClient getClient() {
		if (client == null) {
			client = new DefaultHttpClient(params);
		}
		return client;
	}

	/*public String doPostMethod(String url, BasicNameValuePair... queryData) {
		String result = "";
		String urlAdd = contextPath + url;
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (queryData != null) {
				for (BasicNameValuePair nv : queryData) {
					if (!TextUtils.isEmpty(nv.getValue()))
						params.add(nv);
				}
			}
			HttpPost post = new HttpPost(urlAdd);
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			LogUtils.i("开始设置header");
			if (useDefaultHeader) {
				for (BasicHeader header : defaultHeaders) {
					post.addHeader(header.getName(), header.getValue());
				}
			}
			 发出HTTP request 
			LogUtils.i(post.getURI().toString());
			HttpResponse resp = null;
			synchronized (this) {
				resp = getClient().execute(post);
			}
			int sc = resp.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == sc) {
				InputStream is = resp.getEntity().getContent();
				return inputStreamToString(is);
			} else {
				Log.e("M1905", "invalid HTTP status code: "
						+ sc);
			}
		} catch (Exception e) {
			Log.e("M1905", "Failed to connect: ", e);
		}
		return result;
	}*/

	/*public String doGetMethod(String url, BasicNameValuePair... queryData) {
		StringBuffer sb = new StringBuffer(contextPath + url);
		if (queryData != null) {
			if (sb.toString().indexOf('?') != -1) {
				sb.append('&');
			} else {
				sb.append('?');
			}
			for (BasicNameValuePair nv : queryData) {
				if (nv != null) {
					if (!TextUtils.isEmpty(nv.getValue()))
					{
						sb.append(nv.getName()).append('=')
						.append(nv.getValue()).append('&');
					}
				}
			}
		}
		try {
			HttpGet get = new HttpGet(sb.toString());
			LogUtils.i("开始设置header");
			if (useDefaultHeader) {
				for (BasicHeader header : defaultHeaders) {
					get.addHeader(header.getName(), header.getValue());
				}
			}
			LogUtils.i(get.getURI().toString());
			HttpResponse resp = null;
			synchronized (this) {
				resp = getClient().execute(get);
			}
			int sc = resp.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == sc) {
				InputStream is = resp.getEntity().getContent();
				return inputStreamToString(is);
			}
		} catch (Exception e) {
			 LogUtils.e(e.getMessage());
//			System.out.println(e.getMessage());
		}

		return null;
	}*/

	/**
	 * 
	 * @param method
	 *            true:post
	 * @param url
	 * @param queryData
	 * @return
	 */

	/*public String doMethod(boolean method, String url,
			BasicNameValuePair... queryData) {
		if (method) {
			return doPostMethod(url, queryData);
		} else {
			return doGetMethod(url, queryData);
		}
	}

	public String doMethod(boolean method, String url) {
		if (method) {
			return doPostMethod(url, null);
		} else {
			return doGetMethod(url, null);
		}
	}*/

	private String inputStreamToString(InputStream is) throws IOException {
		String json = null;
		try {
			json = new String();
			ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
			byte[] str_b = new byte[1024];
			int i = -1;
			while ((i = is.read(str_b)) > 0) {
				outputstream.write(str_b, 0, i);
			}
			json = outputstream.toString();
			json = StringEscapeUtils.unescapeHtml(json);
		} catch (Exception e) {
			Log.i("Rock", "Failed to extrace inputstream: ", e);
		}
		return json;
	}

	public boolean saveImageToFile(String imageUrl, File file) {
		InputStream is = getImage(imageUrl);
		if (is != null) {
			BufferedOutputStream bos = null;
			try {
				bos = new BufferedOutputStream(new FileOutputStream(file));
				int len = 0;
				byte[] buff = new byte[4096];
				while ((len = is.read(buff)) != -1) {
					bos.write(buff, 0, len);
				}
				return true;
			} catch (Exception ioe) {
				LogUtils.e(ioe);
			} finally {
				try {
					if (is != null) {
						is.close();
					}
				} catch (Exception ignore) {
				}
				try {
					if (bos != null) {
						bos.close();
					}
				} catch (Exception ignore) {
				}
			}
		}
		return false;
	}

	private InputStream getImage(String imageUrl) {
		try {
			HttpGet get = new HttpGet(imageUrl);
			get.addHeader("accept", "application/octet-stream");
			HttpResponse resp = null;
			synchronized (this) {
				resp = getClient().execute(get);
			}
			int sc = resp.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == sc) {
				return resp.getEntity().getContent();
			} else {
				LogUtils.i("Failed to retrieve image - invalid HTTP status code: "
						+ sc);
			}
		} catch (Exception e) {
			LogUtils.e(e);
		}
		return null;
	}

}
