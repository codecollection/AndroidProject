package com.android.zouchongjin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.zouchongjin.MyConfig;
import com.android.zouchongjin.R;

/**
 * 碎片
 * <p>
 * 1、如果用的是v4里的Fragment，调用者Activity必须继承FragmentActivity；如果是app里的Fragment，
 * 则直接继承Activity即可。
 * <p>
 * 2、v4的Fragment兼容android1.6及以上；app的Fragment兼容android3.0及以上。
 * 
 * 
 * @author zouchongjin@sina.com
 * @data 2015年6月19日
 */
public class MyFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.v(MyConfig.TAG, "MyFragment onCreateView");

		// 第三个参数：用于在填充时指明填充的布局是否应该附加在container上.如果系统已经插入这个填充布局到容器了就返回false,如果将要在最终布局中创建一个多余的viewgroup,那就返回true.
		return inflater.inflate(R.layout.layout_activity_fragment_first, container, false);
	}

	// 当Fragment和Activity链接起来的时候调用(Activity在这里传送过来).
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		Log.v(MyConfig.TAG, "MyFragment onAttach");
	}

}
