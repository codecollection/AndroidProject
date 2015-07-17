package com.android.zouchongjin.service;

import java.io.File;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.zouchongjin.MyConfig;
import com.zcj.util.UtilDate;

/**
 * 通话中录音的服务
 * 
 * @author ZCJ
 * @data 2013-9-27
 */
public class PhoneService extends Service {
	
	private boolean isOpen = false;
	public static String outCallPhoneNumber = "";

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		if (!isOpen) {
			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			telephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
			isOpen = true;
		}
	}

	private final class PhoneListener extends PhoneStateListener {
		private File file;
		private MediaRecorder mediaRecorder;
		private String phoneNumber;

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			try {
				switch (state) {
				case TelephonyManager.CALL_STATE_RINGING:// 响铃
					this.phoneNumber = incomingNumber;
					Log.v(MyConfig.TAG, "记录来电号码成功："+incomingNumber);
					break;

				case TelephonyManager.CALL_STATE_OFFHOOK:// 接通电话或电话打出去时
					if (this.phoneNumber == null && this.phoneNumber.trim() == "") {
						this.phoneNumber = PhoneService.outCallPhoneNumber;
					}
					File folder = new File(MyConfig.PATH_PHONEAUDIO);
					if (!folder.exists()) {
						folder.mkdir();
					}
					file = new File(MyConfig.PATH_PHONEAUDIO, UtilDate.format(new Date()) + "-" + phoneNumber + ".3gp");
					Log.v(MyConfig.TAG, "开始录音..."+file.getAbsolutePath());
					mediaRecorder = new MediaRecorder();
					mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					mediaRecorder.setOutputFile(file.getAbsolutePath());
					mediaRecorder.prepare();// 准备录音
					mediaRecorder.start();// 开始录音
					break;

				case TelephonyManager.CALL_STATE_IDLE:// 挂断电话后回归到空闲状态
					if (mediaRecorder != null) {
						Log.v(MyConfig.TAG, "停止录音...");
						mediaRecorder.stop();// 停止录音
						mediaRecorder.release();// 释放资源
						mediaRecorder = null;
					}
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

//	<uses-permission android:name="android.permission.READ_PHONE_STATE"/><!-- 监听手机通话 -->
//	<uses-permission android:name="android.permission.RECORD_AUDIO"/><!-- 使用话筒录音 -->
//	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/><!-- 往SDCard写入数据权限 -->
//	<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/><!-- 在SDCard中创建与删除文件权限 -->

//	<service android:name="com.zandroid.googlerun.service.PhoneService"></service>