package com.thanone.appbuy.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.bean.User;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.thanone.appbuy.web.ServiceResult;
import com.thanone.appbuy.web.SrUserObj;
import com.umeng.socialize.bean.MultiStatus;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.MulStatusListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.zcj.android.util.UtilString;

public class UmengUtil {

	private String WEIXIN_APPID = "wxd7acd36de1df8c67";
	private String WEIXIN_APPSECRET = "db66b56cc2bb099fc4c5ccdb4a060f07";

	public static String QQ_APPID = "1103474944";
	private String QQ_APPKEY = "xKYMupvPtlzI3m2k";
	
	private String RENREN_ID = "473715";
	private String RENREN_KEY = "8826948360854a9f901b4921dafb0ed5";
	private String RENREN_SECRET = "b9c3648a59a24a1f87a5c10e22f599ed";

	// 关注用户的uid
	private String WEIBO_ADMIN = "5335345585";// 代团小助手2156551160,代团官博5335345585

	private boolean weixinOauth;
	private boolean weiboOauth;
	private boolean qqOauth;

	private Activity activity;
	private AppContext appContext;// 全局Context
	private UMSocialService mController;
	
	private SnsPostListener listener;

	public UmengUtil(Activity activity) {
		this.activity = activity;
		this.appContext = (AppContext) activity.getApplication();
		this.mController = UMServiceFactory.getUMSocialService("com.umeng.share");
		init(activity);
	}

	private void init(Activity activity) {
		initWeibo();
		initShare();
		initQQ(activity);
		initWeixin(activity);
		initRenren(activity);
	}
	
	// 关注官方微博
	private void followWeibo() {
		if (UtilString.isNotBlank(WEIBO_ADMIN)) {
			this.mController.follow(activity, SHARE_MEDIA.SINA, new MulStatusListener() {
				@Override
				public void onStart() {
					Log.v("z", "关注开始");
				}
				@Override
				public void onComplete(MultiStatus multiStatus, int st, SocializeEntity entity) {
					if (st == 200) {
						Log.v("z", "关注成功");
					}
				}
			}, WEIBO_ADMIN);
		}
	}

	// 加入支持新浪微博SSO
	private void initWeibo() {
		this.mController.getConfig().setSsoHandler(new SinaSsoHandler());
	}

	@SuppressWarnings("unused")
	private void initTencentWB() {
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
	}

	private void initQQ(Activity activity) {
		// 支持QQ
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, QQ_APPID, QQ_APPKEY);
		qqSsoHandler.addToSocialSDK();
		// 支持QQ空间
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity, QQ_APPID, QQ_APPKEY);
		qZoneSsoHandler.addToSocialSDK();
	}

	private void initWeixin(Context context) {
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context, WEIXIN_APPID, WEIXIN_APPSECRET);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context, WEIXIN_APPID, WEIXIN_APPSECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}
	
	private void initRenren(Context context) {
		this.mController.getConfig().setSsoHandler(new RenrenSsoHandler(context, RENREN_ID, RENREN_KEY, RENREN_SECRET));
	}

	private void initShare() {
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.QQ, SHARE_MEDIA.TENCENT);// 去掉指定分享组件
		mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QZONE);// 排序
	}

	public void logout(Context context) {
		if (weiboOauth) {
			mController.deleteOauth(context, SHARE_MEDIA.SINA, new SocializeClientListener() {
				@Override
				public void onStart() {
				}

				@Override
				public void onComplete(int status, SocializeEntity entity) {
					if (status == 200) {
						Log.v("z", "删除成功");
					} else {
						Log.v("z", "删除失败");
					}
				}
			});
		}
		if (weixinOauth) {
			mController.deleteOauth(context, SHARE_MEDIA.WEIXIN, new SocializeClientListener() {
				@Override
				public void onStart() {
				}

				@Override
				public void onComplete(int status, SocializeEntity entity) {
					if (status == 200) {
						Log.v("z", "删除成功");
					} else {
						Log.v("z", "删除失败");
					}
				}
			});
		}
		if (qqOauth) {
			mController.deleteOauth(context, SHARE_MEDIA.QQ, new SocializeClientListener() {
				@Override
				public void onStart() {
				}

				@Override
				public void onComplete(int status, SocializeEntity entity) {
					if (status == 200) {
						Log.v("z", "删除成功");
					} else {
						Log.v("z", "删除失败");
					}
				}
			});
		}
	}

	public void shareApp() {
		share(HttpUrlUtil.URL_LOGO_SHARE, "代团APP客户端", "代团是一家专做国际佳品的团购平台，这里不止是正品洋货，更是折扣！更是团购！", HttpUrlUtil.URL_DOWNLOAD, null);
	}
	
	public void share(String logo, String title, String content, String url, final Callback successCallback) {
		UMImage shareImage = new UMImage(activity, logo);
		if (content == null) {content = "";}
		if (title == null) {title = "";}
		if (url == null) {url = "";}
		
		mController.setShareContent(content + url);
		mController.setShareMedia(shareImage);
		
		// 微信
		WeiXinShareContent weixinContent = new WeiXinShareContent(shareImage);
		weixinContent.setTitle(title);
		weixinContent.setShareContent(content);
		if (UtilString.isNotBlank(url)) {
			weixinContent.setTargetUrl(url);
		}
		mController.setShareMedia(weixinContent);
		
		// 朋友圈
		CircleShareContent circleContent = new CircleShareContent();
		circleContent.setShareContent(content);
		circleContent.setTitle(title);
		circleContent.setShareImage(shareImage);
		if (UtilString.isNotBlank(url)) {			
			circleContent.setTargetUrl(url);
		}
		mController.setShareMedia(circleContent);
		
		// QQ
		QQShareContent qqContent = new QQShareContent(shareImage);
		qqContent.setTitle(title);
		qqContent.setShareContent(content);
		if (UtilString.isNotBlank(url)) {			
			qqContent.setTargetUrl(url);
		}
		mController.setShareMedia(qqContent);
		
		// QQ空间
		QZoneShareContent qzoneContent = new QZoneShareContent();
		qzoneContent.setShareContent(content);
		if (UtilString.isNotBlank(url)) {			
			qzoneContent.setTargetUrl(url);
		}
		qzoneContent.setTitle(title);
		qzoneContent.setShareImage(shareImage);
		mController.setShareMedia(qzoneContent);
		
		if (listener == null) {
			listener = new SnsPostListener() {
				@Override
				public void onStart() {}
				@Override
				public void onComplete(SHARE_MEDIA arg0, int arg1, SocializeEntity arg2) {
					if (arg1 == 200 && successCallback != null) {
						successCallback.doSamething();
					}
				}
			};
		}
		
		mController.unregisterListener(listener);
		mController.openShare(activity, listener);
	}
	
	public void loginBySina() {
		login(0, SHARE_MEDIA.SINA);
	}
	
	public void loginByQq() {
		login(1, SHARE_MEDIA.QQ);
	}
	
	public void loginByRenren() {
		login(2, SHARE_MEDIA.RENREN);
	}
	
	public void login(final Integer type, SHARE_MEDIA arg1) {
		mController.doOauthVerify(activity, arg1, new UMAuthListener() {
			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				UiUtil.showToast(activity, "授权出错，请重试...");
			}
			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
				Log.v("z", "uid:"+value.getString("uid"));
				if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
					
					if (type == 0) {
						followWeibo();
					}
					
					final String uid = value.getString("uid");
					UiUtil.showToast(activity, "授权成功，正在尝试登录...");
					HttpUtilsHandler.send(activity, HttpUrlUtil.URL_USER_CLOGIN, HttpUrlUtil.user_clogin(uid, type), new HttpCallback() {
						@Override
						public void success(Object d, String resultJson) {
							User obj = ServiceResult.GSON_DT.fromJson(resultJson, SrUserObj.class).getD();
							if (obj != null && UtilString.isNotBlank(obj.getUserName())) {// 绑定过
								appContext.setLoginUser(obj);
								activity.finish();
							} else {// 未绑定过
								UiUtil.toSetNickName(activity, uid, type);
							}
						}
					}, true);
				} else {
					UiUtil.showToast(activity, "授权失败");
				}
			}
			@Override
			public void onCancel(SHARE_MEDIA platform) {
			}
			@Override
			public void onStart(SHARE_MEDIA platform) {
				UiUtil.showToast(activity, "正在授权，请稍等...");
			}
		});
	}

	public interface Callback {
		void doSamething();
	}
	
	/** 使用SSO授权必须添加如下代码 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	public UMSocialService getmController() {
		return mController;
	}

	public void setmController(UMSocialService mController) {
		this.mController = mController;
	}

	public boolean isWeixinOauth() {
		return weixinOauth;
	}

	public void setWeixinOauth(boolean weixinOauth) {
		this.weixinOauth = weixinOauth;
	}

	public boolean isWeiboOauth() {
		return weiboOauth;
	}

	public void setWeiboOauth(boolean weiboOauth) {
		this.weiboOauth = weiboOauth;
	}

	public boolean isQqOauth() {
		return qqOauth;
	}

	public void setQqOauth(boolean qqOauth) {
		this.qqOauth = qqOauth;
	}

}
