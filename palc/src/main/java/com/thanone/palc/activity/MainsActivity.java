package com.thanone.palc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.thanone.palc.MyApplication;
import com.thanone.palc.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.zcj.android.app.AppManager;
import com.zcj.android.app.DoubleClickExitHelper;
import com.zcj.android.util.UtilAndroid;

@ContentView(R.layout.layout_mains)
public class MainsActivity extends FragmentActivity {

    private MyApplication application;

    private Fragment[] mFragments;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @ViewInject(R.id.main_radiogroup)
    private RadioGroup main_radiogroup;
    @ViewInject(R.id.main_footer_1)
    private RadioButton main_footer_1;
    @ViewInject(R.id.main_footer_2)
    private RadioButton main_footer_2;
    @ViewInject(R.id.main_footer_3)
    private RadioButton main_footer_3;
    @ViewInject(R.id.main_footer_4)
    private RadioButton main_footer_4;

    private DoubleClickExitHelper mDoubleClickExitHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);

        ViewUtils.inject(this);

        mDoubleClickExitHelper = new DoubleClickExitHelper(this);

        application = (MyApplication) getApplication();

        mFragments = new Fragment[5];
        fragmentManager = getSupportFragmentManager();
        mFragments[0] = fragmentManager.findFragmentById(R.id.main_fragment_index);
        mFragments[1] = fragmentManager.findFragmentById(R.id.main_fragment_clue);
        mFragments[2] = fragmentManager.findFragmentById(R.id.main_fragment_house);
        mFragments[3] = fragmentManager.findFragmentById(R.id.main_fragment_login);
        mFragments[4] = fragmentManager.findFragmentById(R.id.main_fragment_userinfo);

        setRadioGroupChangeListener();
        main_radiogroup.check(R.id.main_footer_1);

        if (!UtilAndroid.isNetworkConnected(application)) {// 网络连不上
            Toast.makeText(this, "网络连接失败，请检查网络设置", Toast.LENGTH_SHORT).show();
        }

        // 检测更新
        UmengUpdateAgent.setDefault();
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 切换到指定的菜单
     *
     * @param checkedId 菜单的ID
     */
    public void toFragment(int checkedId) {
        if (checkedId == R.id.main_footer_4 && main_radiogroup.getCheckedRadioButtonId() == R.id.main_footer_4) {
            main_radiogroup.clearCheck();
        }
        main_radiogroup.check(checkedId);
    }

    private void setRadioGroupChangeListener() {
        main_radiogroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2])
                        .hide(mFragments[3]).hide(mFragments[4]);
                switch (checkedId) {
                    case R.id.main_footer_1:
                        fragmentTransaction.show(mFragments[0]).commit();
                        break;
                    case R.id.main_footer_2:
                        fragmentTransaction.show(mFragments[1]).commit();
                        break;
                    case R.id.main_footer_3:
                        fragmentTransaction.show(mFragments[2]).commit();
                        break;
                    case R.id.main_footer_4:
                        if (application.getLoginUserId() == null) {
                            fragmentTransaction.show(mFragments[3]).commit();
                        } else {
                            fragmentTransaction.show(mFragments[4]).commit();
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 监听返回--是否退出程序
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean flag = true;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return mDoubleClickExitHelper.onKeyDown(keyCode, event);
        } else {
            flag = super.onKeyDown(keyCode, event);
        }
        return flag;
    }

}
