package com.thanone.appbuy.common;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.ui.AshowActivity;
import com.thanone.appbuy.ui.BshowActivity;
import com.thanone.appbuy.ui.CommentmyActivity;
import com.thanone.appbuy.ui.CtopicActivity;
import com.thanone.appbuy.ui.DmeActivity;
import com.thanone.appbuy.ui.DmeFenxiangActivity;
import com.thanone.appbuy.ui.DmeShezhiActivity;
import com.thanone.appbuy.ui.DmeShezhiDtewmActivity;
import com.thanone.appbuy.ui.DmeShezhiGydtActivity;
import com.thanone.appbuy.ui.DmeShezhiXgmmActivity;
import com.thanone.appbuy.ui.DmeShoucangActivity;
import com.thanone.appbuy.ui.DmeTuanzhuActivity;
import com.thanone.appbuy.ui.LoginActivity;
import com.thanone.appbuy.ui.MainActivity;
import com.thanone.appbuy.ui.ProjectActivity;
import com.thanone.appbuy.ui.RegisterActivity;
import com.thanone.appbuy.ui.ResetPasswordActivity;
import com.thanone.appbuy.ui.SetNickNameActivity;
import com.thanone.appbuy.ui.SubjectActivity;
import com.thanone.appbuy.ui.WebViewActivity;
import com.thanone.appbuy.ui.WelcomeActivity;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.zcj.android.util.UtilAppFile;
import com.zcj.android.util.UtilImage;
import com.zcj.android.util.UtilString;

public class UiUtil {

	/** HTTP请求错误时的提醒 */
	public static void showHttpFailureToast(Context from) {
		Toast.makeText(from, R.string.http_exception_error, Toast.LENGTH_SHORT).show();
	}

	/** 网络无法连接 */
	public static void showNetworkFailureToast(Context from) {
		Toast.makeText(from, R.string.network_not_connected, Toast.LENGTH_SHORT).show();
	}
	
	/** 提示 */
	public static void showToast(Context from, String value) {
		Toast.makeText(from, value, Toast.LENGTH_SHORT).show();
	}

	public static void toWelcome(Activity from) {
		Intent i = new Intent(from, WelcomeActivity.class);
		from.startActivity(i);
	}
	
	public static void toMain(Activity from) {
		Intent i = new Intent(from, MainActivity.class);
		from.startActivity(i);
	}
	
	public static void toAshow(Activity from) {
		Intent i = new Intent(from, AshowActivity.class);
		from.startActivity(i);
	}

	public static void toBshow(Activity from) {
		Intent i = new Intent(from, BshowActivity.class);
		from.startActivity(i);
	}

	public static void toCtopic(Activity from) {
		Intent i = new Intent(from, CtopicActivity.class);
		from.startActivity(i);
	}

	public static void toDme(Activity from) {
		Intent i = new Intent(from, DmeActivity.class);
		from.startActivity(i);
	}

	public static void toGoods(Context from, Long goodsID) {
		Intent intent = new Intent(from, ProjectActivity.class);
		intent.putExtra("id", goodsID);
		from.startActivity(intent);
	}
	
	public static void toSubject(Context from, Long subjectID) {
		Intent intent = new Intent(from, SubjectActivity.class);
		intent.putExtra("id", subjectID);
		from.startActivity(intent);
	}
	
	public static void toCommentmy(Context from) {
		Intent intent = new Intent(from, CommentmyActivity.class);
		from.startActivity(intent);
	}
	
	public static void toLogin(Context from) {
		Intent intent = new Intent(from, LoginActivity.class);
		from.startActivity(intent);
	}
	
	public static void toRegister(Context from) {
		Intent i = new Intent(from, RegisterActivity.class);
		from.startActivity(i);
	}
	
	public static void toDmeTuanzhu(Context from) {
		Intent intent = new Intent(from, DmeTuanzhuActivity.class);
		from.startActivity(intent);
	}
	
	public static void toDmeShoucang(Context from) {
		Intent intent = new Intent(from, DmeShoucangActivity.class);
		from.startActivity(intent);
	}
	
	public static void toDmeFenxiang(Context from) {
		Intent intent = new Intent(from, DmeFenxiangActivity.class);
		from.startActivity(intent);
	}
	
	public static void toDmeShezhi(Context from) {
		Intent intent = new Intent(from, DmeShezhiActivity.class);
		from.startActivity(intent);
	}
	
	public static void toDmeshezhiXgmm(Context from) {
		Intent intent = new Intent(from, DmeShezhiXgmmActivity.class);
		from.startActivity(intent);
	}
	
	public static void toDmeshezhiDtewm(Context from) {
		Intent intent = new Intent(from, DmeShezhiDtewmActivity.class);
		from.startActivity(intent);
	}
	
	public static void toResetPassword(Context from) {
		Intent intent = new Intent(from, ResetPasswordActivity.class);
		from.startActivity(intent);
	}
	
	public static void toDmeshezhiGydt(Context from) {
		Intent intent = new Intent(from, DmeShezhiGydtActivity.class);
		from.startActivity(intent);
	}
	
	public static void toSetNickName(Context from, String usid, Integer type) {
		Intent intent = new Intent(from, SetNickNameActivity.class);
		intent.putExtra("usid", usid);
		intent.putExtra("type", type);
		from.startActivity(intent);
	}
	
	public static void toWebView(Context from, String title, String url) {
		Intent intent = new Intent(from, WebViewActivity.class);
		intent.putExtra(FinalUtil.INTENT_EXTRAS_TITLE, title);
		intent.putExtra(FinalUtil.INTENT_EXTRAS_URL, url);
		from.startActivity(intent);
	}
	
	/**
	 * 点击图片 打开操作对话框：保存、返回
	 * @param from
	 * @param imgSavePath 图片保存的文件夹目录
	 */
	public static void openEwmOper(final Context from, final String imgSavePath, final Bitmap bitmap) {
		AlertDialog.Builder builder = new AlertDialog.Builder(from);
		builder.setItems(new String[] { "保存", "返回" }, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					try {
						String filePath = imgSavePath + UtilString.getSoleCode() + ".jpg";
						UtilImage.saveImage(bitmap, filePath);
						UtilAppFile.scanPhoto(from, filePath);
						Toast.makeText(from, "保存成功，请打开相册查看", Toast.LENGTH_SHORT).show();
					} catch (IOException e) {
						e.printStackTrace();
						Toast.makeText(from, "保存失败", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		builder.show();
	}
	
	/**
	 * 点击收藏和分享列表中的商品 打开操作对话框：查看、删除、返回
	 * @param from
	 * @param goodsID
	 * @param delete
	 */
	public static void openGoodsOper(final Context from, final Long goodsID, final Callback delete) {
		AlertDialog.Builder builder = new AlertDialog.Builder(from);
		builder.setItems(new String[] { "查看", "删除", "返回" }, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					UiUtil.toGoods(from, goodsID);
				} else if (which == 1) {
					delete.doSamething();
				}
			}
		});
		builder.show();
	}
	
	/**
	 * 点击我的互动分享列表中的互动 打开操作对话框：查看、删除、返回
	 * @param from
	 * @param goodsID
	 * @param delete
	 */
	public static void openSubjectOper(final Context from, final Long subjectID, final Callback delete) {
		AlertDialog.Builder builder = new AlertDialog.Builder(from);
		builder.setItems(new String[] { "查看", "删除", "返回" }, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					UiUtil.toSubject(from, subjectID);
				} else if (which == 1) {
					delete.doSamething();
				}
			}
		});
		builder.show();
	}
	
	/** 点击评论打开操作对话框：回复、举报、[删除]、返回
	 * @param from
	 * @param commentId
	 * @param comment 点击评论的事件：openToComment(uid, nickname);
	 * @param delete 点击删除的事件
	 */
	public static void openCommentOper(final Context from, final Long commentId, final Callback comment, final Callback delete) {
		final AppContext appContext = (AppContext) from.getApplicationContext();
		String[] opers;
		if (delete == null) {
			opers = new String[] { "回复", "举报", "返回" };
		} else {
			opers = new String[] { "回复", "举报", "删除", "返回" };
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(from);
		builder.setItems(opers, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					comment.doSamething();
				} else if (which == 1) {
					HttpUtilsHandler.send(from, HttpUrlUtil.URL_COMMENT_REPORT,
							HttpUrlUtil.comment_report(appContext.getLoginUserID(), commentId), new HttpCallback() {
								@Override
								public void success(Object d, String result) {
									UiUtil.showToast(from, "举报成功");
								}
							}, true);
				} else if (delete != null && which == 2) {
					delete.doSamething();
				}
			}
		});
		builder.show();
	}
	
	/**
	 * 话题点赞功能
	 * @param praiseTextView
	 * @param context
	 * @param subjectID
	 * @param changeText 是否显示赞的数量变化
	 */
	public static void addPraiseListener(View layout, TextView praiseTextView, final Context context, final Long subjectID, final boolean changeText) {
		addPraiseListener(layout, praiseTextView, context, subjectID, changeText, R.drawable.d_praise, R.drawable.d_praise_checked);
	}
	
	/**
	 * 话题点赞功能
	 * @param praiseTextView
	 * @param context
	 * @param subjectID
	 * @param changeText 是否显示赞的数量变化
	 */
	public static void addPraiseListener(View layout, final TextView tv, final Context context, final Long subjectID, final boolean changeText, final int uncheckImg, final int checkImg) {
		final AppContext appContext = (AppContext) context.getApplicationContext();
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (appContext.getLoginUserID() == null) {
					UiUtil.toLogin(context);
					return;
				}
				Long pc = 0L;
				try {
					pc = Long.valueOf(tv.getText().toString());
				} catch (NumberFormatException e) {
				}
				boolean checked = appContext.getPraise_checked().equals(tv.getContentDescription());
				if (checked) {// 取消赞
					tv.setCompoundDrawablesWithIntrinsicBounds(uncheckImg, 0, 0, 0);
					tv.setContentDescription(appContext.getPraise_uncheck());
					if (changeText) {						
						tv.setText(String.valueOf((pc - 1)));
					}
					HttpUtilsHandler.send(HttpUrlUtil.URL_SUBJECT_UNFAVOUR,
							HttpUrlUtil.subject_unfavour(appContext.getLoginUserID(), subjectID));
				} else {// 赞
					tv.setCompoundDrawablesWithIntrinsicBounds(checkImg, 0, 0, 0);
					tv.setContentDescription(appContext.getPraise_checked());
					if (changeText) {						
						tv.setText(String.valueOf((pc + 1)));
					}
					HttpUtilsHandler.send(HttpUrlUtil.URL_SUBJECT_FAVOUR,
							HttpUrlUtil.subject_favour(appContext.getLoginUserID(), subjectID));
				}
			}
		});
	}
	
	/** 话题投票功能 */
	public static void addTpListener(final TextView tpTextView, final Context context, final Long subjectID, final String[] names, final Callback successCallback) {
		final AppContext appContext = (AppContext) context.getApplicationContext();
		tpTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (appContext.getLoginUserID() == null) {
					UiUtil.toLogin(context);
					return;
				}
				
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setItems(names, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						HttpUtilsHandler.send(context, HttpUrlUtil.URL_SUBJECT_VOTE, HttpUrlUtil.subject_vote(appContext.getLoginUserID(), subjectID, which),
								new HttpCallback() {
									@Override
									public void success(Object d, String result) {
										UiUtil.showToast(context, "投票成功");
										tpTextView.setOnClickListener(null);
										successCallback.doSamething();
									}
								}, true);
					}
				});
				builder.show();
			}
		});
	}
	
	public interface Callback {
		void doSamething();
	}

}
