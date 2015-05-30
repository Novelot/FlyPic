package com.novelot.picfly;

import com.novelot.piccache.CacheInfo;

import android.app.Application;
import android.content.Context;
import android.view.WindowManager;

public class FlyPicApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		CacheInfo.getInstance().screenWidth = width;
	}
}
