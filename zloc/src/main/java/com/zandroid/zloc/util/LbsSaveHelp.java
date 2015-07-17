package com.zandroid.zloc.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zandroid.zloc.MyConfig;

import java.io.File;

public class LbsSaveHelp {

	private final static String POI_CREATE = "http://api.map.baidu.com/geodata/v2/poi/create";
//	private final static String POI_LIST = "http://api.map.baidu.com/geodata/v2/poi/list";

	private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	private static HttpUtils http = new HttpUtils();

	private static RequestParams initParams(String[] keys, Object[] values) {
		if (keys == null || values == null || keys.length != values.length) {
			return null;
		}
		RequestParams params = new RequestParams();
		for (int i = 0; i < keys.length; i++) {
			if (values[i] != null) {
				if (values[i] instanceof File) {
					params.addBodyParameter(keys[i], (File) values[i]);
				} else {
					params.addBodyParameter(keys[i], String.valueOf(values[i]));
				}
			}
		}
		return params;
	}

	/**
	 * 保存位置数据(POST)
	 * 
	 * @param longitude
	 *            经度
	 * @param latitude
	 *            纬度
	 * @param address
	 *            可选
	 * @param title
	 *            可选
	 * @param tags
	 *            可选
	 * @return
	 */
	public static void poiCreate(String longitude, String latitude, String address, String title, String tags, final HttpCallback callback) {
		http.send(
				HttpRequest.HttpMethod.POST,
				POI_CREATE,
				initParams(new String[] { "ak", "coord_type", "geotable_id", "longitude", "latitude", "address", "title", "tags" },
						new Object[] { MyConfig.LBS_AK, MyConfig.LBS_COORD_TYPE, MyConfig.LBS_TABLE_ID, longitude, latitude, address,
								title, tags }), new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							PoiCreateResult ss = GSON.fromJson(responseInfo.result, PoiCreateResult.class);
							if (ss.getStatus() == 0) {
								
								if (MyConfig.DEBUG) {
									Log.v(MyConfig.TAG, "上传位置信息成功，表编号：" + MyConfig.LBS_TABLE_ID + "/记录编号：" + ss.getId());
								}
								
								if (callback != null) {
									callback.success(responseInfo.result);
								}
								
							} else {
								if (MyConfig.DEBUG) {
									Log.v(MyConfig.TAG, "网络连接成功，保存位置信息失败，LBS错误代码：" + ss.getStatus());
								}
							}
						} catch (Exception e) {
							if (MyConfig.DEBUG) {
								Log.v(MyConfig.TAG, "网络连接成功，保存位置信息失败，返回字符串：" + responseInfo.result);
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						if (MyConfig.DEBUG) {
							Log.v(MyConfig.TAG, "网络错误");
						}
					}
				});
	}

	/**
	 * 获取位置信息(GET)
	 * 
	 * @param ak
	 * @param geotable_id
	 *            数据库表的ID
	 * @param page_index
	 *            分页索引,默认为0
	 * @param page_size
	 *            分页数目,默认为10
	 * @param tags
	 *            可选
	 * @param title
	 *            可选
	 * @param column
	 *            可选
	 *            column需要设置了is_index_field=1。对于string，是两端匹配。对于int或者double，则是范围查找
	 *            ，传递的格式为最小值,最大值。当无最小值或者最大值时，用-代替，同时，此字段最大长度不超过50，最小值与最大值都是整数.
	 * @return
	 */
//	public static PoiListResult poiList(String ak, int geotable_id, Integer page_index, Integer page_size, String tags, String title,
//			Map<String, Object> column) {
//
//		try {
//			StringBuilder sb = new StringBuilder();
//			sb.append(POI_LIST);
//			sb.append("?ak=" + ak);
//			sb.append("&geotable_id=" + geotable_id);
//			if (page_index != null && page_index >= 0) {
//				sb.append("&page_index=" + page_index);
//			}
//			if (page_size != null && page_size >= 0) {
//				sb.append("&page_size=" + page_size);
//			}
//			if (tags != null) {
//				sb.append("&tags=" + tags);
//			}
//			if (title != null) {
//				sb.append("&title=" + title);
//			}
//			if (column != null && !column.isEmpty()) {
//				for (Map.Entry<String, Object> entry : column.entrySet()) {
//					sb.append("&" + entry.getKey() + "=" + String.valueOf(entry.getValue()));
//				}
//			}
//
//			String result = UtilApacheHttpClient.httpGet(sb.toString());
//			if (!UtilApacheHttpClient.ERROR.equals(result)) {
//				PoiListResult ss = GSON.fromJson(result, PoiListResult.class);
//				if (ss.getStatus() == 0) {
//					Log.v("z", "获取位置信息成功，表编号：" + geotable_id + "/返回的记录数：" + ss.getSize());
//					return ss;
//				}
//				Log.v("z", "网络连接成功，获取位置信息失败，LBS错误代码：" + ss.getStatus());
//			}
//		} catch (Exception e) {
//			Log.v("z", "网络错误");
//		}
//		return null;
//	}
}