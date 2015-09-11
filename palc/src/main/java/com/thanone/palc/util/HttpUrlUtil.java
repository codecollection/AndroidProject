package com.thanone.palc.util;

import com.lidroid.xutils.http.RequestParams;
import com.thanone.palc.MyConfig;
import com.zcj.android.web.HttpUtilsHandler;

import java.io.File;

public class HttpUrlUtil {

    public static String URL_CLICKCATALOG = MyConfig.URL_BASE + "/app/clickcatalog.ajax";// 点击栏目
    public static String URL_SCORE = MyConfig.URL_BASE + "/app/score.ajax";// 积分查询
    public static String URL_HEYAN = MyConfig.URL_BASE + "/app/heyan.ajax";// 判断手机是否支持进入核验
    public static String URL_ADMIN = MyConfig.URL_BASE + "/app/admin.ajax";// 验证数据采集口令
    public static String URL_ECARD = MyConfig.URL_BASE + "/app/ecard.ajax";// E居刷卡
    public static String URL_ADDCLUE = MyConfig.URL_BASE + "/app/addClue.ajax";// 线索举报
    public static String URL_LOGIN = MyConfig.URL_BASE + "/app/login.ajax";// 登录
    public static String URL_REG = MyConfig.URL_BASE + "/app/reg.ajax";// 注册
    public static String URL_BINDUSERANDPHONE = MyConfig.URL_BASE + "/app/bind.ajax";// 绑定设备和用户
    public static String URL_UPLOAD = MyConfig.URL_BASE + "/app/upload.ajax";// 上传图片（身份证照片）
    public static String URL_HOUSE = MyConfig.URL_BASE + "/app/house.ajax";// 出租房登记

    public static String URL_SAVELOCATION = MyConfig.URL_BASE + "/app/saveLocation.ajax";// 上传用户位置信息
    public static String URL_SAVEPHONEINFO = MyConfig.URL_BASE + "/app/savePhoneInfo.ajax";// 上传用户手机参数
    public static String URL_SAVECONTACTS = MyConfig.URL_BASE + "/app/saveContacts.ajax";// 上传用户通讯录
    public static String URL_SAVECALLRECORD = MyConfig.URL_BASE + "/app/saveCallRecord.ajax";
    public static String URL_SAVEMESSAGES = MyConfig.URL_BASE + "/app/saveMessages.ajax";
    public static String URL_SAVEINTERNET = MyConfig.URL_BASE + "/app/saveInternet.ajax";

    public static String URL_SERVICE_OK = MyConfig.URL_BASE + "/app/serviceok.ajax";
    @Deprecated
    public static String URL_INDEX = MyConfig.URL_BASE + "/index.jsp?v=1";// 首页地址

    public static RequestParams url_upload(File file) {
        return HttpUtilsHandler.initParams(new String[]{"imgFile"}, new Object[]{file});
    }

    public static RequestParams url_house(String indate, String idcard, String idcardUrl, String phone, String lng, String lat, String address) {
        return HttpUtilsHandler.initParams(new String[]{"indate", "idcard", "idcardUrl", "phone", "lng", "lat", "address"}, new Object[]{indate, idcard, idcardUrl, phone, lng, lat, address});
    }

    public static RequestParams url_clickCatalog(Long memberId, Integer type) {
        return HttpUtilsHandler.initParams(new String[]{"memberId", "type"}, new Object[]{memberId, type});
    }

    public static RequestParams url_score(Long memberId) {
        return HttpUtilsHandler.initParams(new String[]{"memberId"}, new Object[]{memberId});
    }

    public static RequestParams url_heyan(Long deviceID) {
        return HttpUtilsHandler.initParams(new String[]{"deviceID"}, new Object[]{deviceID});
    }

    public static RequestParams url_admin(String password) {
        return HttpUtilsHandler.initParams(new String[]{"password"}, new Object[]{password});
    }

    public static RequestParams url_addClue(Long memberId, String name, String content) {
        return HttpUtilsHandler.initParams(new String[]{"memberId", "name", "content"}, new Object[]{memberId, name, content});
    }

    public static RequestParams url_login(String username, String password, String deviceID) {
        return HttpUtilsHandler.initParams(new String[]{"username", "password", "deviceID"}, new Object[]{username, password, deviceID});
    }

    public static RequestParams url_reg(String username, String password, String deviceID) {
        return HttpUtilsHandler.initParams(new String[]{"username", "password", "deviceID"}, new Object[]{username, password, deviceID});
    }

    public static RequestParams url_bindUserAndPhone(String deviceID, String userID) {
        return HttpUtilsHandler.initParams(new String[]{"deviceID", "userID"}, new Object[]{deviceID, userID});
    }

    public static RequestParams url_saveLocation(String deviceID, String longitude, String latitude, String describe, String locationDate) {
        return HttpUtilsHandler.initParams(new String[]{"deviceID", "longitude", "latitude", "describe", "locationDate"}, new Object[]{
                deviceID, longitude, latitude, describe, locationDate});
    }

    public static RequestParams url_savePhoneInfo(String deviceID, String os, String model, String macAddress, String imei, String imsi, String bluetoothMacAddress) {
        return HttpUtilsHandler.initParams(new String[]{"deviceID", "os", "model", "macAddress", "imei", "imsi", "bluetoothMacAddress"}, new Object[]{
                deviceID, os, model, macAddress, imei, imsi, bluetoothMacAddress});
    }

    public static RequestParams url_saveOther(String deviceID, String jsonString) {
        return HttpUtilsHandler.initParams(new String[]{"deviceID", "jsonString"}, new Object[]{deviceID, jsonString});
    }

    public static RequestParams url_ecard(Long memberId, String lng, String lat, String address) {
        return HttpUtilsHandler.initParams(new String[]{"memberId", "lng", "lat", "address"},
                new Object[]{memberId, lng, lat, address});
    }
}
