package com.thanone.palc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.palc.R;
import com.thanone.palc.util.UiUtil;

@ContentView(R.layout.layout_sjhy)
public class SjhyActivity extends Activity {

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

        header_title.setText("手机核验");
        header_back.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.sjhy_submit)
    private void submit(View v) {
        // TODO 点击开始核验按钮
        UiUtil.alert(this, sjhy_password.getText().toString());
    }

    @OnClick(R.id.header_back)
    private void back(View v) {
        finish();
    }

}
