package com.thanone.appbuy;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.thanone.appbuy.bean.User;
import com.thanone.appbuy.common.FinalUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.thanone.appbuy.web.ServiceResult;
import com.thanone.appbuy.web.SrUserObj;
import com.zcj.android.util.UtilAppFile;
import com.zcj.android.util.UtilSharedPreferences;
import com.zcj.android.util.UtilString;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AppContext extends Application {

	public static String FILESAVEPATH;
	/** 上传头像时的图片临时保存路径 */
	public static String FILESAVEPATH_PORTRAIT;
	/** 图片文件的保存路径，如二维码 */
	public static String FILESAVEPATH_IMG;

	private User loginUser;
	private Boolean unread;// 是否有未读消息

	public static final int PAGESIZE = 20;// 默认分页大小
	private String praise_checked;// 字符串：已赞
	private String praise_uncheck;// 字符串：未赞
	
	@Override
	public void onCreate() {
		super.onCreate();

		// 极光推送
		JPushInterface.init(this);
		
		if (UtilAppFile.sdcardExist()) {
			FILESAVEPATH = UtilAppFile.getSdcardPath();
		} else {
			FILESAVEPATH = UtilAppFile.getFilesDir(this);
		}
		FILESAVEPATH_PORTRAIT = FILESAVEPATH + "Portrait/";
		FILESAVEPATH_IMG = FILESAVEPATH + "Img/";
		File file = new File(FILESAVEPATH_PORTRAIT);
		if (!file.exists()) {
			file.mkdirs();
		}
		File file2 = new File(FILESAVEPATH_IMG);
		if (!file2.exists()) {
			file2.mkdirs();
		}

		praise_checked = getResources().getText(R.string.praise_checked).toString();
		praise_uncheck = getResources().getText(R.string.praise_uncheck).toString();

		// 自动登录
		checkAutoLogin();
		
		Timer unreadTimer = new Timer();
		unreadTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				initUnread();
			}
		}, 100, 1000 * 3);

	}
	
	private void checkAutoLogin() {
		String username = UtilSharedPreferences.get(this, FinalUtil.XML_NAME, FinalUtil.XML_U);
		String password = UtilSharedPreferences.get(this, FinalUtil.XML_NAME, FinalUtil.XML_P);
		if (UtilString.isNotBlank(username) && UtilString.isNotBlank(password)) {
			HttpUtilsHandler.send(HttpUrlUtil.URL_USER_LOGIN, HttpUrlUtil.user_login(username, password), new HttpCallback() {
				@Override
				public void success(Object d, String result) {
					loginUser = ServiceResult.GSON_DT.fromJson(result, SrUserObj.class).getD();
				}
			});
		}
	}

	public void logout() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(FinalUtil.XML_U, null);
		map.put(FinalUtil.XML_P, null);
		UtilSharedPreferences.save(this, FinalUtil.XML_NAME, map);
		setLoginUser(null);
	}

	public void login(String username, String password, User obj) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(FinalUtil.XML_U, username);
		map.put(FinalUtil.XML_P, password);
		UtilSharedPreferences.save(this, FinalUtil.XML_NAME, map);
		setLoginUser(obj);
	}

	public void updatepassword(String password) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(FinalUtil.XML_P, password);
		UtilSharedPreferences.save(this, FinalUtil.XML_NAME, map);
	}

	/** 刷新是否还有未读消息 */
	private void initUnread() {
		if (getLoginUserID() != null) {
			HttpUtilsHandler.send(HttpUrlUtil.URL_COMMENT_UNREAD, HttpUrlUtil.comment_unread(getLoginUserID()), new HttpCallback() {
				@Override
				public void success(Object d, String result) {
					if ((Boolean) d == Boolean.TRUE) {
						setUnread(Boolean.TRUE);
					} else {
						setUnread(Boolean.FALSE);
					}
				}
			});
		} else {
			setUnread(Boolean.FALSE);
		}
	}

	public String getPraise_checked() {
		if (UtilString.isBlank(praise_checked)) {
			praise_checked = getResources().getText(R.string.praise_checked).toString();
		}
		return praise_checked;
	}

	public String getPraise_uncheck() {
		if (UtilString.isBlank(praise_uncheck)) {
			praise_uncheck = getResources().getText(R.string.praise_uncheck).toString();
		}
		return praise_uncheck;
	}

	public Long getLoginUserID() {
		if (loginUser != null) {
			return loginUser.getUserID();
		} else {
			return null;
		}
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
		if (loginUser != null && loginUser.getUserID() != null) {
			JPushInterface.setAlias(this, String.valueOf(loginUser.getUserID()), new TagAliasCallback() {
				@Override
				public void gotResult(int responseCode, String alias, Set<String> tags) {
					Log.v("zouchongjin", String.valueOf("极光推送设置别名的返回："+responseCode));
				}
			});
		}
	}

	public Boolean getUnread() {
		return unread;
	}

	public void setUnread(Boolean unread) {
		this.unread = unread;
	}

	/** 获取APP安装包信息 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

}
