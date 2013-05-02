package com.example.servicetest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class TestService extends Service {

	private static final String TAG = TestService.class.getName();

	private MediaPlayer mediaPlayer;

	@Override
	public IBinder onBind(final Intent intent) {
		Log.d(TAG, "onBind");
		if (null == mediaPlayer) {
			mediaPlayer = MediaPlayer.create(this, R.raw.bessie_jackson_barbeque_bess);
		}
		return new SimpleBinder<TestService>(this);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		mediaPlayer.pause();
		super.onDestroy();
	}

	public void startIt() {
		mediaPlayer.start();
		setNotification();
	}

	public void stopIt() {
		mediaPlayer.pause();
		stopForeground(true);
	}

	public boolean isStopped() {
		return !mediaPlayer.isPlaying();
	}

	private void setNotification() {
		Log.d(TAG, "setNotification");

		final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.cancelAll();

		// this is deprecated in sdk 17, but we have to support back to 14
		//noinspection deprecation
		final Notification notification = new Notification(R.drawable.ic_launcher, getText(R.string.app_name), System.currentTimeMillis());

		final Intent intent = new Intent(this, TestActivity.class)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

		Log.d(TAG, "notification extras: " + intent.getExtras());
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// this is deprecated in sdk 17, but we have to support back to 14
		//noinspection deprecation
		notification.setLatestEventInfo(this, "title", "text", pendingIntent);
		notification.flags |= Notification.FLAG_NO_CLEAR;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;

		Log.d(TAG, "starting service as a foreground service");
		startForeground(1234567890, notification);

	}

}
