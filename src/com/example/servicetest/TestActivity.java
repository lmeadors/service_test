package com.example.servicetest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class TestActivity extends Activity {

	private static final String TAG = TestActivity.class.getName();
	private TestService service;
	private Button stopButton;
	private Button startButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		startButton = (Button) findViewById(R.id.start);
		stopButton = (Button) findViewById(R.id.stop);
		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				service.startIt();
				refreshButtons();
			}
		});
		stopButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				service.stopIt();
				refreshButtons();
			}
		});
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume");
		super.onResume();
		final Intent intent = new Intent(this, TestService.class);
		startService(intent);
		bindService(intent, testConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause");
		super.onPause();
		final boolean stopService = service.isStopped();
		unbindService(testConnection);
		if(stopService){
			stopService(new Intent(this, TestService.class));
		}
	}

	private ServiceConnection testConnection = new ServiceConnection() {

		@SuppressWarnings("unchecked")
		@Override
		public void onServiceConnected(final ComponentName name, final IBinder binder) {
			Log.d(TAG, "onServiceConnected");
			service = ((SimpleBinder<TestService>) binder).getService();
			refreshButtons();
			Log.d(TAG, "service: " + service);
		}

		@Override
		public void onServiceDisconnected(final ComponentName name) {
			Log.d(TAG, "onServiceDisconnected");
			service = null;
			Log.d(TAG, "service: " + service);
		}

	};

	private void refreshButtons() {
		stopButton.setEnabled(!service.isStopped());
		startButton.setEnabled(service.isStopped());
	}

}
