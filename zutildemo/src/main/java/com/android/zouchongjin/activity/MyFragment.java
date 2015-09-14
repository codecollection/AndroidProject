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
 * <p/>
 * 1、如果用的是v4里的Fragment，调用者Activity必须继承FragmentActivity；如果是app里的Fragment，
 * 则直接继承Activity即可。
 * <p/>
 * 2、v4的Fragment兼容android1.6及以上；app的Fragment兼容android3.0及以上。
 *
 * @author zouchongjin@sina.com
 * @data 2015年6月19日
 */
public class MyFragment extends Fragment {

    // 系统在添加Fragment到Activity的时候调用(Activity在这里传送过来).
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v(MyConfig.TAG, "MyFragment onAttach");
    }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.v(MyConfig.TAG, "MyFragment onCreate");
        }

            // 在第一次为fragment绘制用户界面时系统会调用此方法。
            @Override
            public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                Log.v(MyConfig.TAG, "MyFragment onCreateView");

                // 为fragment绘制用户界面，这个函数必须要返回所绘出的fragment的根View。如果fragment没有用户界面可以返回空。

                // 第三个参数：用于在填充时指明填充的布局是否应该附加在container上.如果系统已经插入这个填充布局到容器了就返回false,如果将要在最终布局中创建一个多余的viewgroup,那就返回true.
                return inflater.inflate(R.layout.layout_activity_fragment_first, container, false);
            }

            // 当Activity的onCreate返回的时候执行.
            @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                Log.v(MyConfig.TAG, "MyFragment onActivityCreated");
            }

                // 当Fragment的显示状态转变时调用
                @Override
                public void onHiddenChanged(boolean hidden) {
                    Log.v(MyConfig.TAG, "MyFragment onHiddenChanged");
                    super.onHiddenChanged(hidden);
                }

                @Override
                public void onStart() {
                    Log.v(MyConfig.TAG, "MyFragment onStart");
                    super.onStart();
                }

                    @Override
                    public void onResume() {
                        Log.v(MyConfig.TAG, "MyFragment onResume");
                        super.onResume();
                    }

                    @Override
                    public void onPause() {
                        super.onPause();
                    }

                @Override
                public void onStop() {
                    super.onStop();
                }

            // 当Fragment的试图层被移除的时候执行.
            @Override
            public void onDestroyView() {
                super.onDestroyView();
            }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

    // 当Fragment和Activity分离的时候执行.
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
