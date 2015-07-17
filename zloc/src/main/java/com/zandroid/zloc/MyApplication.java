package com.zandroid.zloc;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.lidroid.xutils.DbUtils;
import com.zandroid.zloc.service.LocationService;
import com.zcj.android.util.UtilAndroid;

public class MyApplication extends Application {

    private String phoneId;// 手机编号
    private DbUtils dbUtils;// 数据库操作对象

    @Override
    public void onCreate() {
        if (MyConfig.DEBUG) {
            Log.v(MyConfig.TAG, "MyApplication--onCreate");
        }

        try {
            this.phoneId = UtilAndroid.getUdid(this, "zgoogle", "UUID");
            this.dbUtils = DbUtils.create(this);
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onCreate();
    }

    public void init() {
        if (MyConfig.DEBUG) {
            Log.v(MyConfig.TAG, "MyApplication--init");
        }
        startService(new Intent(this, LocationService.class));
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

}
