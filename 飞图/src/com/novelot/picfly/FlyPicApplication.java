package com.novelot.picfly;

import com.novelot.piccache.CacheInfo;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class FlyPicApplication extends Application implements
		Thread.UncaughtExceptionHandler, ActivityLifecycleCallbacks {

	private static final String TAG = "flypic";
	/* activity 总数 */
	private int mActivityCount = 0;
	/* 前台activity数量 */
	private int mFrontActivityCount = 0;

	@Override
	public void onCreate() {
		super.onCreate();

		/* 捕获异常 */
		Thread.setDefaultUncaughtExceptionHandler(this);

		/* 设置缓存 */
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		CacheInfo.getInstance().screenWidth = width;

	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e(TAG,
				String.format("Thread[%d][%s] error.", thread.getId(),
						thread.getName()));
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		mActivityCount++;
	}

	@Override
	public void onActivityStarted(Activity activity) {
	}

	@Override
	public void onActivityResumed(Activity activity) {
		mFrontActivityCount++;
	}

	@Override
	public void onActivityPaused(Activity activity) {
		mFrontActivityCount--;
	}

	@Override
	public void onActivityStopped(Activity activity) {
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		mActivityCount--;
	}

	/**
	 * 判断本app是否在前台显示
	 * 
	 * @return
	 */
	public boolean isInFront() {
		return mFrontActivityCount > 0;
	}
}
