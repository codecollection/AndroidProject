package com.zandroid.zloc.util;

public abstract class HttpCallback {

	public abstract void success(String resultJson);
	
	public void error() {
    }
	
}
