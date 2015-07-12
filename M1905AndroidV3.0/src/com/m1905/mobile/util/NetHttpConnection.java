package com.m1905.mobile.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.telephony.TelephonyManager;

public class NetHttpConnection {

	/**
	 * HttpConnection——GET(String)
	 * @param path			路径
	 * @param params		参数
	 * @return				返回字符串
	 * @throws Exception	各种异常
	 */
	public static String httpGet(String path, Map<String, String> params) throws Exception {
		// 拼接参数打开连接
		HttpURLConnection connection = httpConnectionGet(path, params);
		// 判断是否连接成功
		if(connection.getResponseCode() == 200) {
			InputStream inputStream = connection.getInputStream();
			return inputSwitch(inputStream);
		} else {
			throw new Exception("Connection Fail");
		}
	}
	/**
	 * HttpConnection——GET(InputStream)
	 * @param path		路径
	 * @param params	参数
	 * @return			返回InputStream
	 * @throws Exception	各种异常
	 */
	public static InputStream httpGetStream(String path, Map<String, String> params) throws Exception {
		// 拼接参数打开连接
		HttpURLConnection connection = httpConnectionGet(path, params);
		// 判断是否连接成功
		if(connection.getResponseCode() == 200) {
			return connection.getInputStream();
		} else {
			throw new Exception("Connection Fail");
		}
	}
	/**
	 * HttpConnection——POST(String)
	 * @param path		路径
	 * @param params	参数
	 * @return			返回字符串
	 * @throws Exception	各种异常
	 */
	public static String HttpPost(String path, Map<String, String> params) throws Exception {
		// 拼接请求体打开连接
		HttpURLConnection connection = HttpConnectionPost(path, params);
		// 切记,将参数写出后,并没有发送到服务器的,需要向服务器获取一次状态吗才算发出了
		System.out.println("返回状态码："+connection.getResponseCode());
		if(connection.getResponseCode() == 200) {
			InputStream in = connection.getInputStream();
			return inputSwitch(in);
		} else {
			throw new Exception("Connection Fail");
		}
	}
	/**
	 * HttpConnection——POST(InputStream)
	 * @param path			路径
	 * @param params		参数
	 * @return				返回InputStream
	 * @throws Exception	各种异常
	 */
	public static InputStream HttpPostStream(String path, Map<String, String> params) throws Exception {
		// 拼接请求体打开连接
		HttpURLConnection connection = HttpConnectionPost(path, params);
		// 切记,将参数写出后,并没有发送到服务器的,需要向服务器获取一次状态吗才算发出了
		if(connection.getResponseCode() == 200) {
			return connection.getInputStream();
		} else {
			throw new Exception("Connection Fail");
		}
	}
	
	/**
	 * 拼接参数打开连接
	 * @param path			路径
	 * @param params		参数
	 * @return				返回HttpURLConnection
	 * @throws Exception	各种异常
	 */
	private static HttpURLConnection httpConnectionGet(String path, Map<String, String> params) throws Exception {
		// 使用GET提交数据,需要将路径和参数拼接
		StringBuffer uri = new StringBuffer(path);
		// 判断参数不为空时
		if(params != null) {
			uri.append("?");
			// 拼接参数
			for (Entry<String, String> entry : params.entrySet()) {
				uri.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
			}
			// 去除最后的&符号
			uri.deleteCharAt(uri.length() - 1);
		}
		// 创建URL,将拼接好的路径传递给url
		URL url = new URL(uri.toString());
		// 通过URL打开一个连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 设置超时时间以及提交的方式,GET必须大写
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);
		connection.setRequestMethod("GET");
		return connection;
	}
	
	/**
	 * 拼接请求体打开连接
	 * @param path			路径
	 * @param params		参数
	 * @return				返回HttpURLConnection
	 * @throws Exception	各种参数
	 */
	private static HttpURLConnection HttpConnectionPost(String path, Map<String, String> params) throws Exception {
		// 由于是post提交,无需将参数拼接到路径后面,直接创建URL
		URL url = new URL(path);
		// 拼接请求体中的参数
		StringBuffer data = new StringBuffer();
		// 判断参数不为空时
		if(params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				data.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8")).append("&");
			}
			// 去除最后一个&符号
			data.deleteCharAt(data.length() - 1);
		}
		// 将拼接的参数转成字节数组
		byte[] bytes = data.toString().getBytes();
		// 打开连接
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		// 设置超时时间and请求方式
		connection.setConnectTimeout(10000);
		connection.setReadTimeout(10000);
		connection.setRequestMethod("POST");
		// 切记,需要设置允许对外输出数据
		connection.setDoOutput(true);
		// 绝对不能少的,数据类型以及数据的大小
		connection.setRequestProperty("Content-Type", "application/x-www-from-urlencoded");
		connection.setRequestProperty("Content-Length", bytes.length+"");
		// 将参数写出
		connection.getOutputStream().write(bytes);
		
		return connection;
	}
	/**将留转换成字符串*/
	public static String inputSwitch(InputStream in) throws IOException {
		String result = null;
		ByteArrayOutputStream out = null;
		try{
			byte[] buffer = new byte[1024];
			int len;
			out = new ByteArrayOutputStream();
			while((len = in.read(buffer))!=-1) {
				out.write(buffer, 0, len);
			}
			result = out.toString();
		} finally {
			if(in != null) {
				in.close();
			}
			if(out != null) {
				out.close();
			}
		}
		return result;
	}
	
	public static int getProvidersName(Context c) {
        int ProvidersName = 0;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) c
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String operator = telephonyManager.getSimOperator();
            if (operator == null || operator.equals("")) {
                operator = telephonyManager.getSubscriberId();
            }
            if (operator == null || operator.equals("")) {
               return 4;
            }
            if (operator != null) {
                if (operator.startsWith("46000")
                        || operator.startsWith("46002")) {
                    ProvidersName = 1;
                } else if (operator.startsWith("46001")) {
                    ProvidersName = 2;
                } else if (operator.startsWith("46003")) {
                    ProvidersName = 3;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ProvidersName;
    }

}
