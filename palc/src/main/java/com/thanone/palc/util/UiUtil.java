package com.thanone.palc.util;

import android.app.Activity;
import android.content.Intent;

import com.thanone.palc.activity.SjhyActivity;
import com.thanone.palc.activity.WebViewActivity;
import com.zcj.android.util.UtilDialog;

public class UiUtil {

    public static void alert(Activity from, String value) {
        UtilDialog.builderAlertDialog(from, "提示", value);
    }

    public static void toLogin(Activity from) {
        // TODO 转到登录页面
    }

    public static void toSjhy(Activity from) {
        Intent i = new Intent(from, SjhyActivity.class);
        from.startActivity(i);
    }

    public static void toLcjwgc(Activity from) {
        Intent i = new Intent(from, WebViewActivity.class);
        i.putExtra("title", "鹿城警务观察");
        i.putExtra("url", "http://www.wxapi.cn/wxapi.php?ac=listhome4&tid=241723");
        from.startActivity(i);
    }

    public static void toJsyb(Activity from) {
        Intent i = new Intent(from, WebViewActivity.class);
        i.putExtra("title", "警示预报");
        i.putExtra("url", "http://www.wxapi.cn/wxapi.php?ac=listhome4&tid=241723");
        from.startActivity(i);
    }

    public static void toCrj(Activity from) {
        Intent i = new Intent(from, WebViewActivity.class);
        i.putExtra("title", "出入境");
        i.putExtra("url", "http://wx.teabox.cc/index.php?s=/addon/WeiSite/WeiSite/index/token/gh_7a6703f6e673/openid/oKSyms4kO-RvB2n_WyLZjOmfHeOA.html");
        from.startActivity(i);
    }

    public static void toYwcx(Activity from) {
        Intent i = new Intent(from, WebViewActivity.class);
        i.putExtra("title", "业务查询");
        i.putExtra("url", "http://service.wzga.gov.cn/newwzwas/wap/lcga/index.jsp");
        from.startActivity(i);
    }

}
