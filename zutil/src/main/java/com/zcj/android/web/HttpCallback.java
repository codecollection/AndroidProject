package com.zcj.android.web;

public abstract class HttpCallback {

	/**
	 * 请求成功调用的方法（S=1）
	 * @param dataJsonString
	 */
	public abstract void success(String dataJsonString);

	/**
	 * 请求失败调用的方法（包括：网络异常、JSON解析错误、S=0）
	 */
	public void error() {
    }
	
}
