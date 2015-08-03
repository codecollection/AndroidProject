package com.zandroid.zway.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zandroid.zlibbmap.lbs.bean.PoiCreateResult;
import com.zandroid.zway.MyConfig;
import com.zcj.android.web.HttpCallback;
import com.zcj.android.web.HttpUtilsHandler;

import java.util.Map;

public class LbsSaveHelp {

    private static final String LBS_AK = "566b7f727ea9fe87503114c0a479a724";// 服务端
    private static final int LBS_TABLE_ID = 115911;
    private static final int LBS_COORD_TYPE = 3;// 1：GPS经纬度坐标;2：国测局加密经纬度坐标;3：百度加密经纬度坐标;4：百度加密墨卡托坐标

    private final static String POI_CREATE = "http://api.map.baidu.com/geodata/v3/poi/create";
    private final static String POI_LIST = "http://api.map.baidu.com/geodata/v3/poi/list";

    public static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 保存位置数据(POST)
     *
     * @param longitude
     * @param latitude
     * @param address
     * @param title     poi名称
     * @param tags
     * @param callback
     */
    public static void poiCreate(String longitude, String latitude, String address, String title, String tags, final HttpCallback callback) {
        HttpUtilsHandler.getInstance().send(
                HttpRequest.HttpMethod.POST,
                POI_CREATE,
                HttpUtilsHandler.initParams(new String[]{"ak", "coord_type", "geotable_id", "longitude", "latitude", "address", "title", "tags"},
                        new Object[]{LBS_AK, LBS_COORD_TYPE, LBS_TABLE_ID, longitude, latitude, address,
                                title, tags}), new RequestCallBack<String>() {

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        try {
                            PoiCreateResult ss = GSON.fromJson(responseInfo.result, PoiCreateResult.class);
                            if (ss.getStatus().equals(0)) {

                                MyConfig.log("上传位置信息成功，表编号：" + LBS_TABLE_ID + "/记录编号：" + ss.getId());

                                if (callback != null) {
                                    callback.success(responseInfo.result);
                                }

                            } else {
                                MyConfig.log("网络连接成功，保存位置信息失败，LBS错误代码：" + ss.getStatus());
                            }
                        } catch (Exception e) {
                            MyConfig.log("网络连接成功，保存位置信息失败，返回字符串：" + responseInfo.result);
                        }
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        MyConfig.log("网络错误");
                    }
                });
    }

    /**
     * 获取位置信息的URL（GET）
     *
     * @param column      column需要设置了is_index_field=1。
     *                    对于string，是两端匹配。
     *                    对于int或者double，则是范围查找，传递的格式为最小值,最大值。
     *                    当无最小值或者最大值时，用-代替，同时，此字段最大长度不超过50，最小值与最大值都是整数。
     *                    例：如加入一个命名为color数据类型为string的column，在检索是可设置为“color=red”的形式来检索color字段为red的POI
     * @param title       可选
     * @param tags        可选
     * @param page_index  默认为0，最大为9
     * @param page_size   默认为10，上限为200
     * @return
     */
    public static String poiListUrl(Map<String, Object> column, String title, String tags, Integer page_index, Integer page_size) {
        StringBuilder sb = new StringBuilder();
        sb.append(POI_LIST);
        sb.append("?ak=" + LBS_AK);
        sb.append("&geotable_id=" + LBS_TABLE_ID);
        if (page_index != null && page_index >= 0) {
            sb.append("&page_index=" + page_index);
        }
        if (page_size != null && page_size >= 0) {
            sb.append("&page_size=" + page_size);
        }
        if (tags != null) {
            sb.append("&tags=" + tags);
        }
        if (title != null) {
            sb.append("&title=" + title);
        }
        if (column != null && !column.isEmpty()) {
            for (Map.Entry<String, Object> entry : column.entrySet()) {
                sb.append("&" + entry.getKey() + "=" + String.valueOf(entry.getValue()));
            }
        }
        return sb.toString();
    }

}