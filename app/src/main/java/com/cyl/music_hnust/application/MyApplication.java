package com.cyl.music_hnust.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cyl.music_hnust.service.MusicPlayService;


public class MyApplication extends Application {
	MusicPlayService mService;
	static RequestQueue mRequestQueue;

	private ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			mService = ((MusicPlayService.LocalBinder) service).getService();//用绑定方法启动service，就是从这里绑定并得到service，然后就可以操作service了
			System.out.println("1null ?"+(null == mService));
			Log.e("===============",""+(null == mService));
			mService.setContext(getApplicationContext());
		}
		@Override
		public void onServiceDisconnected(ComponentName arg0) {

		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		Intent intent = new Intent(this, MusicPlayService.class);
		startService(intent);
		System.out.println("intent?"+(null == intent));
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}

	public MusicPlayService getmService() {
		return mService;
	}

	public void setmService(MusicPlayService mService) {
		this.mService = mService;
	}

	public  RequestQueue getHttpQueues() {
		return mRequestQueue;
	}
}
