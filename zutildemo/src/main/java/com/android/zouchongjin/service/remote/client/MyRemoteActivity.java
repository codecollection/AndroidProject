package com.android.zouchongjin.service.remote.client;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.android.zouchongjin.R;
import com.android.zouchongjin.MyServiceInterface;

/**
 * 客户端
 * 	PS:客户端需要引入服务端的AIDL文件(包名和类名都要一样)
 * @author ZCJ
 * @data 2013-9-28
 */
public class MyRemoteActivity extends Activity {

	private MyServiceInterface myServiceInterface;
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			myServiceInterface = MyServiceInterface.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			myServiceInterface = null;
		}
	};
	
	public void query(View v) {
		try {
			String result = myServiceInterface.query();
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		} catch (RemoteException e) {
			Toast.makeText(this, "远程服务调用失败", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_service_remote);
		
		Intent service = new Intent("com.android.zouchongjin.remoteService.MyService");
		bindService(service, conn, BIND_AUTO_CREATE);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
	}

}
