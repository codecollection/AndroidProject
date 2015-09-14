package com.zcj.android.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import android.provider.Settings;
import android.widget.Toast;

public class UtilIntent {

    /**
     * 打开网页
     *
     * @param context
     * @param url     如：http://www.baidu.com
     */
    public static void startWeb(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "无法浏览此网页", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 拨打电话
     *
     * @param context
     * @param phone   如：13788888888
     */
    public static void startPhone(Context context, String phone) {
        Uri uri = Uri.parse("tel:" + phone);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(it);
    }

    /**
     * 打开录音机
     *
     * @param context
     */
    public static void startAudioRecord(Context context) {
        Intent mi = new Intent(Media.RECORD_SOUND_ACTION);
        context.startActivity(mi);
    }

    /**
     * 安装应用
     *
     * @param context
     * @param packageName
     */
    public static void installApk(Context context, String packageName) {
        Uri installUri = Uri.fromParts("package", packageName, null);
        Intent it = new Intent(Intent.ACTION_PACKAGE_ADDED, installUri);
        context.startActivity(it);
    }

    /**
     * 卸载应用
     *
     * @param context
     * @param packageName
     */
    public static void uninstallApk(Context context, String packageName) {
        Uri uri = Uri.fromParts("package", packageName, null);
        Intent it = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(it);
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 打开GPS设置页面
     */
    public static void openGpsSetting(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                context.startActivity(intent);
            } catch (Exception e) {
            }
        }
    }

}
