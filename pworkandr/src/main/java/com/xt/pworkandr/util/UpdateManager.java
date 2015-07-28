package com.xt.pworkandr.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.xt.pworkandr.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@SuppressLint("HandlerLeak")
public class UpdateManager {

	private static final String packageName = "com.xt.pworkandr";
	
	private static final int DOWNLOAD = 1;
	private static final int DOWNLOAD_FINISH = 2;
	
	private String mSavePath;
	private int progress;
	private boolean cancelUpdate = false;
	private Context mContext;
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD:
				mProgress.setProgress(progress);
				break;
			case DOWNLOAD_FINISH:
				if (mDownloadDialog.isShowing()) {
					mDownloadDialog.dismiss();
				}
				installApk();
				break;
			default:
				break;
			}
		};
	};
	
	public UpdateManager(Context context) {
		this.mContext = context;
	}
	
	public void checkUpdate() {
		if (isUpdate()) {
			showNoticeDialog();
		} else {
			// Toast.makeText(mContext, "已经是最新版了", Toast.LENGTH_LONG).show();
		}
	}
	
	private boolean isUpdate() {
		int versionCode = getVersionCode(mContext);
		if (AndroidConfig.getVersion_android() > versionCode) {
			return true;
		}
		return false;
	}
	
	private int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
	
	private void showNoticeDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("软件更新");
		builder.setMessage("检测到有新版本，是否更新");
		builder.setPositiveButton("更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				showDownLoadDialog();
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		Dialog noticeDialog = builder.create();
		noticeDialog.show();
	}
	
	private void showDownLoadDialog() {
		Builder builder = new Builder(mContext);
		builder.setTitle("正在更新...");
		final LayoutInflater inflate = LayoutInflater.from(mContext);
		View v = inflate.inflate(R.layout.layout_update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progressbar_update_progress);
		builder.setView(v);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				cancelUpdate = true;
			}
		});
		mDownloadDialog = builder.create();
		mDownloadDialog.show();
		downloadApk();
	}
	
	private void downloadApk() {
		new downloadApkThread().start();
	}
	
	private class downloadApkThread extends Thread {
		
		@Override
		public void run() {
			try {
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// 判断SD卡是否存在并是否具有读写权限
					String sdpath = Environment.getExternalStorageDirectory() + "/";
					mSavePath = sdpath + "download";
					
					URL url = new URL(AndroidConfig.getPath_url_androidApkFile());
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					
					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();
					File file = new File(mSavePath);
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, AndroidConfig.getName_apkFile());
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					byte buf[] = new byte[1024];
					do {
						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float)count/length)*100);
						mHandler.sendEmptyMessage(DOWNLOAD);
						if (numread <= 0) {
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						fos.write(buf, 0, numread);
					} while (!cancelUpdate);
					fos.close();
					is.close();
				} else {
					Log.e("zouchongjin", "zouchongjin:SDCard不存在或没有权限");
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (mDownloadDialog.isShowing()) {
					mDownloadDialog.dismiss();
				}
			}
		}
	}
	
	private void installApk() {
		Log.v("zouchongjin", "准备安装APK");
		File apkfile = new File(mSavePath, AndroidConfig.getName_apkFile());
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://"+apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
		Log.v("zouchongjin", "开始安装APK...");
	}
	
}
