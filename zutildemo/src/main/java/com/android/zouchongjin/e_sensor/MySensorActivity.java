package com.android.zouchongjin.e_sensor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import com.android.zouchongjin.R;

/** 传感器 */
public class MySensorActivity extends Activity implements SensorEventListener {

	private SensorManager sensorManager;
	private Sensor sensor;
	
	private MediaPlayer mediaPlayer;
	
	// 用于感应触发的参数
	private float x,y,z,last_x,last_y,last_z;
	private long lastUpdate;
	private static final int SHAKE_THRESHOLD = 3000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_e_sensor_main);
		
		sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	
		mediaPlayer = MediaPlayer.create(this, R.raw.song);
	}

	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		x = event.values[0];
		y = event.values[1];
		z = event.values[2];
		((TextView)findViewById(R.id.textView_e_1)).setText(String.valueOf(x));
		((TextView)findViewById(R.id.textView_e_2)).setText(String.valueOf(y));
		((TextView)findViewById(R.id.textView_e_3)).setText(String.valueOf(z));
	
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			long curTime = System.currentTimeMillis();
			if ((curTime-lastUpdate)>100) {
				long diffTime = (curTime-lastUpdate);
				float speed = Math.abs(x+y-last_x-last_y-last_z)/diffTime*10000;
				if (speed > SHAKE_THRESHOLD) {
					doing();
				}
				last_x = x;
				last_y = y;
				last_z = z;
				lastUpdate = curTime;
			}
		}
	
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}
	
	private void doing() {
		if (!mediaPlayer.isPlaying()) {
			mediaPlayer.start();
		} else {
			mediaPlayer.pause();
		}
	}

}
