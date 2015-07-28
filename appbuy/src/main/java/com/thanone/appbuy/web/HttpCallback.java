package com.thanone.appbuy.web;

public abstract class HttpCallback {

	public abstract void success(Object d, String resultJson);
	
	public void error() {
    }
	
}
