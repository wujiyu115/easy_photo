package com.farwmarth.easyphoto.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.farwmarth.easyphoto.MainActivity;
import com.farwmarth.easyphoto.R;
import com.farwmarth.easyphoto.util.DownLoadUtil;

@SuppressLint("NewApi")
public class DownloadService extends Service {

	private ClipboardManager clipBoard;
	private Notification notification;

	public IBinder onBind(Intent paramIntent) {
		return null;
	}

	public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.clipBoard = ((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE));
		ClipboardListener localClipboardListener = new ClipboardListener();
		this.clipBoard.addPrimaryClipChangedListener(localClipboardListener);

		Intent localIntent2 = new Intent(getApplicationContext(),
				MainActivity.class);
		localIntent2.putExtra("close", true);
		PendingIntent localPendingIntent2 = PendingIntent.getActivity(
				getApplicationContext(), 0, localIntent2,
				Intent.FLAG_ACTIVITY_CLEAR_TOP);

		Intent localIntent3 = new Intent(getApplicationContext(),
				MainActivity.class);
		localIntent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		PendingIntent localPendingIntent3 = PendingIntent.getActivity(
				getApplicationContext(), 0, localIntent3,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		this.notification = new NotificationCompat.Builder(
				getApplicationContext())
				.setContentTitle(getString(R.string.running))
				.setContentText(getString(R.string.running)).setSmallIcon(R.drawable.icon)
				.addAction(R.drawable.icon_close, "", localPendingIntent2)
				.setPriority(2).setOngoing(false)
				.setContentIntent(localPendingIntent3).build();
		this.notification.flags = (Notification.FLAG_ONGOING_EVENT | this.notification.flags);
		startForeground(1, this.notification);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stop();
	}

	public void stop() {
		Log.i("onStop", "Stop");
		stopForeground(true);
		stopSelf();
		Process.killProcess(Process.myPid());
	}

	class ClipboardListener implements
			ClipboardManager.OnPrimaryClipChangedListener {
		ClipboardListener() {
		}

		public void onPrimaryClipChanged() {
			ClipData data = clipBoard.getPrimaryClip();
			Item item = data.getItemAt(0);
			String shareUrl = item.getText().toString();
//			System.out.println(shareUrl);
			DownLoadUtil.downLoad(shareUrl.trim(), getApplicationContext());
		}
	}

}
