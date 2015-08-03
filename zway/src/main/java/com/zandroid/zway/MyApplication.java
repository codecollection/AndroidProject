package com.zandroid.zway;

import android.app.Application;
import android.content.Intent;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.lidroid.xutils.DbUtils;
import com.zandroid.zway.service.LocationService;
import com.zcj.android.util.UtilAndroid;
import com.zcj.android.util.UtilAppFile;

import java.io.File;

public class MyApplication extends Application {

    private String phoneId;// 手机编号
    private DbUtils dbUtils;// 数据库操作对象

    // 定位图标
    private BitmapDescriptor bitmap_begin;
    private BitmapDescriptor bitmap_end;
    private BitmapDescriptor bitmap_uncheck;
    private BitmapDescriptor bitmap_check;

    /**
     * 文件保存的根目录
     */
    public static String FILESAVEPATH;
    /**
     * 截图的保存路径
     */
    public static String FILESAVEPATH_SNAPSHOT;

    @Override
    public void onCreate() {
        MyConfig.log("MyApplication--onCreate");

        SDKInitializer.initialize(getApplicationContext());

        setBitmap_begin(BitmapDescriptorFactory.fromResource(R.drawable.icon_loc_4));
        setBitmap_check(BitmapDescriptorFactory.fromResource(R.drawable.icon_loc_3));
        setBitmap_end(BitmapDescriptorFactory.fromResource(R.drawable.icon_loc_5));
        setBitmap_uncheck(BitmapDescriptorFactory.fromResource(R.drawable.icon_loc_1));

        if (UtilAppFile.sdcardExist()) {
            FILESAVEPATH = UtilAppFile.getSdcardPath();
        } else {
            FILESAVEPATH = UtilAppFile.getFilesDir(this);
        }
        FILESAVEPATH_SNAPSHOT = FILESAVEPATH + "Snapshot/";
        File file = new File(FILESAVEPATH_SNAPSHOT);
        if (!file.exists()) {
            file.mkdirs();
        }

        try {
            setPhoneId(UtilAndroid.getUdid(this, "zway", "UUID"));
            setDbUtils(DbUtils.create(this));
            startAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onCreate();
    }

    public void startAll() {
        startService(new Intent(this, LocationService.class));
        MyConfig.log("--startService");
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public DbUtils getDbUtils() {
        return dbUtils;
    }

    public void setDbUtils(DbUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    public BitmapDescriptor getBitmap_begin() {
        return bitmap_begin;
    }

    public void setBitmap_begin(BitmapDescriptor bitmap_begin) {
        this.bitmap_begin = bitmap_begin;
    }

    public BitmapDescriptor getBitmap_end() {
        return bitmap_end;
    }

    public void setBitmap_end(BitmapDescriptor bitmap_end) {
        this.bitmap_end = bitmap_end;
    }

    public BitmapDescriptor getBitmap_uncheck() {
        return bitmap_uncheck;
    }

    public void setBitmap_uncheck(BitmapDescriptor bitmap_uncheck) {
        this.bitmap_uncheck = bitmap_uncheck;
    }

    public BitmapDescriptor getBitmap_check() {
        return bitmap_check;
    }

    public void setBitmap_check(BitmapDescriptor bitmap_check) {
        this.bitmap_check = bitmap_check;
    }

}
