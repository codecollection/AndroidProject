package com.thanone.palc.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.palc.R;
import com.zcj.android.view.webviewshell.OnQuitListener;
import com.zcj.android.view.webviewshell.WebViewUtil;

@ContentView(R.layout.layout_webview)
public class WebViewActivity extends Activity {

    @ViewInject(R.id.header_title)
    private TextView header_title;
    @ViewInject(R.id.header_back)
    private ImageView header_back;

    @ViewInject(R.id.webview_content)
    private WebView webview_content;

    private WebViewUtil webViewUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

        header_title.setText(getIntent().getStringExtra("title"));
        header_back.setVisibility(View.VISIBLE);

        initWebView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Boolean result = webViewUtil.onKeyDown(keyCode, event, false, new OnQuitListener() {
            @Override
            public void webViewQuit() {
                finish();
            }
        });
        if (result != null) {
            return result;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void initWebView() {
        String url = getIntent().getStringExtra("url");

        webViewUtil = new WebViewUtil(this, webview_content);
        webViewUtil.init(null, url);
    }

    @OnClick(R.id.header_back)
    private void back(View v) {
        finish();
    }

}
