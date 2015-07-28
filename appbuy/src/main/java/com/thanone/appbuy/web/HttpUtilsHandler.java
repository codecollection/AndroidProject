package com.thanone.appbuy.web;

import android.app.ProgressDialog;
import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.thanone.appbuy.common.UiUtil;

public class HttpUtilsHandler {

	private static HttpUtils http;
	
	private HttpUtilsHandler() {
		
	}
	
	public static synchronized HttpUtils getInstance() {
		if (http == null) {
			http = new HttpUtils();
		}
		return http;
	}

	public static void send(String url, RequestParams params) {
		send(null, url, params, null, false);
	}
	
	public static void send(final String url, final RequestParams params, final HttpCallback callback) {
		send(null, url, params, callback, false);
	}
	
	public static void send(final String url, final RequestParams params, final HttpCallback callback, final ProgressDialog loadingDialog) {
		send(null, url, params, callback, false, loadingDialog);
	}
	
	public static void send(final Context context, String url, RequestParams params, final HttpCallback callback, final boolean alertErrorString) {
		sendFunction(context, url, params, callback, alertErrorString, true, null);
	}
	
	public static void send(final Context context, String url, RequestParams params, final HttpCallback callback, final boolean alertErrorString, final ProgressDialog loadingDialog) {
		sendFunction(context, url, params, callback, alertErrorString, true, loadingDialog);
	}
	
	private static void sendFunction(final Context context, final String url, final RequestParams params, final HttpCallback callback, final boolean alertErrorString, final boolean reload, final ProgressDialog loadingDialog) {
		if (loadingDialog != null) {
			loadingDialog.setMessage("加载中...");
			loadingDialog.show();
		}
		HttpUtilsHandler.getInstance().send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				ServiceResult sr = ServiceResult.GSON_DT.fromJson(result, ServiceResult.class);
				if (sr.success()) {
					if (callback != null) {
						callback.success(sr.getD(), result);
					}
				} else {
					if (callback != null) {
						callback.error();
					}
					if (alertErrorString) {
						String errorString = "";
						if (sr.getD() != null) {
							errorString = "：" + String.valueOf(sr.getD());
						}
						UiUtil.showToast(context, "操作失败" + errorString);
					}
				}
				if (loadingDialog != null) {
					loadingDialog.dismiss();
				}
			}
			@Override
			public void onFailure(HttpException error, String msg) {
				if (reload) {
					sendFunction(context, url, params, callback, alertErrorString, false, loadingDialog);
				} else {
					if (callback != null) {
						callback.error();
					}
					if (alertErrorString) {
						UiUtil.showHttpFailureToast(context);
					}
					if (loadingDialog != null) {
						loadingDialog.dismiss();
					}
				}
			}
		});
	}
	
}
