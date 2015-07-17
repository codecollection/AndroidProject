package com.android.zouchongjin.i_thread;

import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.zouchongjin.R;

/**
 * 异步任务
 * 
 * @author ZCJ
 * @data 2013-4-2
 */
public class MyAsyncTaskActivity extends Activity {

	private ImageView imageView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_i_asynctask);

		// 异步显示远程图片
		imageView = (ImageView) findViewById(R.id.imageView_i_asynctask);

		// UI线程中调用
		new DownloadImageTask().execute("http://www.baidu.com/img/shouye_b5486898c692066bd2cbaeda86d74448.gif");
	}

	// 用于异步显示远程图片
	// 第一个参数String：传入的参数；第二个参数Integer：后台任务执行的百分比；第三个参数Drawable：返回值
	private class DownloadImageTask extends AsyncTask<String, Integer, Drawable> {

		// 处理耗时操作，并返回结果
		@Override
		protected Drawable doInBackground(String... urls) {
//			publishProgress(50);// 更新执行进度

			Drawable drawable = null;
			try {
				drawable = Drawable.createFromStream(new URL(urls[0]).openStream(), "image.gif");
			} catch (Exception e) {
				e.printStackTrace();
			}

//			publishProgress(100);// 更新执行进度
			return drawable;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
//			Toast.makeText(MyAsyncTaskActivity.this, progress[0] + "%", Toast.LENGTH_SHORT).show();
		}

		// 接收结果，更新UI界面
		@Override
		protected void onPostExecute(Drawable result) {
			if (result != null) {
				imageView.setImageDrawable(result);
			}
		}
	}

}
