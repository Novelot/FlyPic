package com.novelot.picfly;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.novelot.piccache.CacheInfo;
import com.novelot.util.SystemUtils;

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
		/* 注册生命周期监听 */
		registerActivityLifecycleCallbacks(this);
		/* 初始化ImageLoader */
		initImageLoader();

		/* 设置缓存 */
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		CacheInfo.getInstance().screenWidth = width;

	}

	/**
	 * 初始化ImageLoader
	 */
	private void initImageLoader() {
		int availMemoryMs = SystemUtils.getAvailMemory(this);
		Log.v(TAG, "the avail memory of this app is " + availMemoryMs + "M");
		int bitmapCacheMemory = availMemoryMs * 1024 * 1024 / 8;

		if (!ImageLoader.getInstance().isInited()) {
			DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder()
					.cacheInMemory(true).build();
			ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
					this)
					.defaultDisplayImageOptions(defaultDisplayImageOptions)
					// .memoryCache(new LruMemoryCache(bitmapCacheMemory))
					// .memoryCacheSize(1024 * 1024)
					.build();
			ImageLoader.getInstance().init(configuration);
		}
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Log.e(TAG,
				String.format("Thread[%d][%s] error.", thread.getId(),
						thread.getName()));
	}

	@Override
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		Log.v(TAG, "an Activity Created");
		mActivityCount++;
	}

	@Override
	public void onActivityStarted(Activity activity) {
		Log.v(TAG, "an Activity Started");
	}

	@Override
	public void onActivityResumed(Activity activity) {
		Log.v(TAG, "an Activity Resumed");
		mFrontActivityCount++;
	}

	@Override
	public void onActivityPaused(Activity activity) {
		Log.v(TAG, "an Activity Paused");
		mFrontActivityCount--;
	}

	@Override
	public void onActivityStopped(Activity activity) {
		Log.v(TAG, "an Activity Stopped");
	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
	}

	@Override
	public void onActivityDestroyed(Activity activity) {
		Log.v(TAG, "an Activity Destroyed");
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
