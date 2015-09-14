package com.thanone.palc.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.palc.MyApplication;
import com.thanone.palc.R;
import com.thanone.palc.util.HttpUrlUtil;
import com.thanone.palc.util.UiUtil;
import com.zcj.android.util.UtilDialog;
import com.zcj.android.web.HttpCallback;
import com.zcj.android.web.HttpUtilsHandler;
import com.zcj.util.UtilString;

public class ClueFragment extends Fragment {

    private MyApplication application;
    private MainsActivity activity;

    @ViewInject(R.id.header_title)
    private TextView header_title;
    @ViewInject(R.id.header_back)
    private ImageView header_back;

    @ViewInject(R.id.clue_content)
    private EditText clue_content;
    @ViewInject(R.id.clue_lxr)
    private EditText clue_lxr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_clue, container, false);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        application = (MyApplication) getActivity().getApplication();
        activity = (MainsActivity) getActivity();

        header_title.setText("线索举报");
        header_back.setVisibility(View.GONE);
    }

    @OnClick(R.id.clue_submit)
    private void clue_submit(View v) {
        String content = clue_content.getText().toString();
        String lxr = clue_lxr.getText().toString();
        Long userId = application.getLoginUserId();

        if (userId == null) {
            activity.toFragment(R.id.main_footer_4);
        } else if (UtilString.isBlank(content)) {
            UiUtil.alert(getActivity(), "请输入举报内容");
        } else {
            http_addClue(userId, content, lxr);
        }
    }

    private void http_addClue(Long userId, String content, String lxr) {
        HttpUtilsHandler.send(getActivity(), HttpUrlUtil.URL_ADDCLUE, HttpUrlUtil.url_addClue(userId, lxr, content), new HttpCallback() {
            @Override
            public void success(String dataJsonString) {
                UtilDialog.builderAlertDialog(getActivity(), null, dataJsonString, new UtilDialog.DialogCallback() {
                    @Override
                    public void doSomething_ChickOK() {
                        // 清空举报内容
                        clue_content.setText("");
                        clue_lxr.setText("");
                        // 转到首页
                        activity.toFragment(R.id.main_footer_1);
                    }
                });
            }
        }, true);
    }

}
