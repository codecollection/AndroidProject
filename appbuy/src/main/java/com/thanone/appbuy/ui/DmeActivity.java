package com.thanone.appbuy.ui;

import java.io.File;
import java.net.URL;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TableLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.thanone.appbuy.AppContext;
import com.thanone.appbuy.R;
import com.thanone.appbuy.common.UiUtil;
import com.thanone.appbuy.web.HttpCallback;
import com.thanone.appbuy.web.HttpUrlUtil;
import com.thanone.appbuy.web.HttpUtilsHandler;
import com.umeng.analytics.MobclickAgent;
import com.zcj.android.util.UtilCleaner;
import com.zcj.android.util.UtilImage;
import com.zcj.android.util.UtilString;
import com.zcj.android.view.circleimageview.CircleImageView;

public class DmeActivity extends Fragment {

	/** 剪裁后返回 */
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	/** 相机拍照后返回 */
	public static final int REQUEST_CODE_GETIMAGE_BYCAMERA = 1;
	/** 相册选图后返回 */
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;

	private AppContext appContext;// 全局Context

	// 修改头像
	private String cutBeforePath;// 拍照保存的路径
	private String cutAfterPath;// 剪裁后的路径

	@ViewInject(R.id.dme_headimage)
	private CircleImageView dme_headimage;
	@ViewInject(R.id.dme_name)
	private TextView dme_name;
	
	@ViewInject(R.id.dme_zoomlayout)
	private TableLayout dme_zoomlayout;
	private boolean firstOpen = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_dme, container, false);
		ViewUtils.inject(this, view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			initData();
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onStart() {
		if (!isHidden()) {
			initData();
		}
		super.onStart();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_GETIMAGE_BYCAMERA:
			cutAfterPath = AppContext.FILESAVEPATH_PORTRAIT + UtilString.getSoleCode() + ".jpg";
			startActionCrop(cutBeforePath, cutAfterPath);// 拍照后裁剪
			break;
		case REQUEST_CODE_GETIMAGE_BYCROP:
			cutAfterPath = AppContext.FILESAVEPATH_PORTRAIT + UtilString.getSoleCode() + ".jpg";
			startActionCrop(UtilImage.getFilePathByUri(getActivity(), data.getData()), cutAfterPath);// 选图后裁剪
			break;
		case REQUEST_CODE_GETIMAGE_BYSDCARD:
			HttpUtilsHandler.send(getActivity(), HttpUrlUtil.URL_USER_UPLOAD, HttpUrlUtil.user_upload(appContext.getLoginUserID(), new File(cutAfterPath)), 
					new HttpCallback() {
						@Override
						public void success(Object d, String resultJson) {
							appContext.getLoginUser().setAvatarUrl(String.valueOf(d));
							dme_headimage.setImageDrawable(UtilImage.getDrawableByFilePath(cutAfterPath));
						}
					}, true);
			break;
		}
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onPageStart("Dme");
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd("Dme");
	}

	/** 裁剪 */
	public void startActionCrop(String sourePath, String savePath) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(Uri.fromFile(new File(sourePath)), "image/*");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(savePath)));
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 200);// 输出图片大小
		intent.putExtra("outputY", 200);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYSDCARD);
	}

	private void initView() {
		appContext = (AppContext) getActivity().getApplication();
	}

	// 团主
	@OnClick(R.id.dme_tz)
	private void dme_tz(View v) {
		UiUtil.toDmeTuanzhu(getActivity());
	}

	// 收藏
	@OnClick(R.id.dme_sc)
	private void dme_sc(View v) {
		if (appContext.getLoginUser() == null) {
			UiUtil.toLogin(getActivity());
		} else {
			UiUtil.toDmeShoucang(getActivity());
		}
	}

	// 分享
	@OnClick(R.id.dme_fx)
	private void dme_fx(View v) {
		if (appContext.getLoginUser() == null) {
			UiUtil.toLogin(getActivity());
		} else {
			UiUtil.toDmeFenxiang(getActivity());
		}
	}

	// 清理缓存
	@OnClick(R.id.dme_ql)
	private void cleanOnClick(View v) {
		UtilCleaner.clearAppCache(getActivity());
	}

	// 消息
	@OnClick(R.id.dme_aq)
	private void dme_aq(View v) {
		if (appContext.getLoginUser() == null) {
			UiUtil.toLogin(getActivity());
		} else {
			UiUtil.toCommentmy(getActivity());
		}
	}

	// 设置
	@OnClick(R.id.dme_sz)
	private void dme_sz(View v) {
		UiUtil.toDmeShezhi(getActivity());
	}

	@OnClick(R.id.dme_name)
	private void dme_name(View v) {
		if (appContext.getLoginUser() == null) {
			UiUtil.toLogin(getActivity());
		}
	}

	@OnClick(R.id.dme_headimage)
	private void changeHeadImage(View v) {
		if (appContext.getLoginUser() != null) {
			AlertDialog imageDialog = new AlertDialog.Builder(getActivity()).setItems(new CharSequence[] { "手机相册", "手机拍照", "返回" },
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							// 相册选图
							if (item == 0) {
								Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
								intent.addCategory(Intent.CATEGORY_OPENABLE);
								intent.setType("image/*");
								startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_GETIMAGE_BYCROP);
							}
							// 手机拍照
							else if (item == 1) {
								// 裁剪头像的绝对路径
								cutBeforePath = AppContext.FILESAVEPATH_PORTRAIT + UtilString.getSoleCode() + ".jpg";
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(cutBeforePath)));
								startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYCAMERA);
							}
						}
					}).create();
			imageDialog.show();
		} else {
			UiUtil.toLogin(getActivity());
		}
	}

	// 重新加载“我”的数据
	public void initData() {
		
		if (firstOpen) {
			Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_bottom_in);
			dme_zoomlayout.startAnimation(animation);
			firstOpen = false;
		}
		
		if (appContext.getLoginUser() != null) {
			String headImage = appContext.getLoginUser().getAvatarUrl();
			if (UtilString.isNotBlank(headImage)) {
				new DownloadImageTask().execute(headImage);
			}
			dme_name.setText(appContext.getLoginUser().getNickName());
		} else {
			dme_headimage.setImageDrawable(getResources().getDrawable(R.drawable.d_default_head));
			dme_name.setText(getResources().getString(R.string.dme_tologin));
		}
	}

	// 用于异步显示远程图片
	private class DownloadImageTask extends AsyncTask<String, Integer, Drawable> {
		@Override
		protected Drawable doInBackground(String... urls) {
			Drawable drawable = null;
			try {
				drawable = Drawable.createFromStream(new URL(urls[0]).openStream(), "image.jpg");
			} catch (Exception e) {
				drawable = getActivity().getResources().getDrawable(R.drawable.d_default_head);
			}
			return drawable;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			dme_headimage.setImageDrawable(result);
		}
		
	}

}
