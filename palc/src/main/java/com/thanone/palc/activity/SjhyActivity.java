package com.thanone.palc.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.palc.MyApplication;
import com.thanone.palc.R;
import com.thanone.palc.util.HttpUrlUtil;
import com.thanone.palc.util.UiUtil;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.app.BaseActivity;
import com.zcj.android.web.HttpCallback;
import com.zcj.android.web.HttpUtilsHandler;

@ContentView(R.layout.layout_sjhy)
public class SjhyActivity extends BaseActivity {

    private MyApplication application;

    Handler mainHandler1;
    /** 弹出通知 */
    public static final int MESSAGE_WHAT_ALERTDIALOG = 1;

    @ViewInject(R.id.header_title)
    private TextView header_title;
    @ViewInject(R.id.header_back)
    private ImageView header_back;

    @ViewInject(R.id.sjhy_password)
    private EditText sjhy_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        application = (MyApplication) getApplication();

        mainHandler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MESSAGE_WHAT_ALERTDIALOG:
                        if (!isFinishing()) {
                            UiUtil.alert(SjhyActivity.this, String.valueOf(msg.obj));
                        }
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        header_title.setText("手机核验");
        header_back.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.sjhy_submit)
    private void submit(View v) {
        String val = sjhy_password.getText().toString();
        httpAdmin(val, application.getPhoneId());
    }

    @OnClick(R.id.header_back)
    private void back(View v) {
        finish();
    }

    // 第一步：验证核验密码。如果成功；则继续下一步；如果失败，则弹出提示
    private void httpAdmin(String password, final String phoneId) {
        HttpUtilsHandler.send(application, HttpUrlUtil.URL_ADMIN, HttpUrlUtil.url_admin(password), new HttpCallback() {
            @Override
            public void success(String dataJsonString) {
                httpHeyan(phoneId);
            }
        }, true);
    }

    // 第二步：验证是否已经核验过。如果成功；则继续下一步；如果失败，则弹出提示
    private void httpHeyan(String phoneId) {
        HttpUtilsHandler.send(application, HttpUrlUtil.URL_HEYAN, HttpUrlUtil.url_heyan(phoneId), new HttpCallback() {
            @Override
            public void success(String dataJsonString) {
                heyan();
            }
        }, true);
    }

    // 第三步：开始核验手机
    private void heyan() {
        application.readInfoAndUploadToWeb(true, true, true, true, true, true, "此手机非被盗抢手机", mainHandler1);
    }

}
