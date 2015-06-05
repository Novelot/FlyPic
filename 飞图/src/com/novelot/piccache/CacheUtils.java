package com.novelot.piccache;

import android.app.ActivityManager;

public class CacheUtils {

	public static void clearApplicationUserData(ActivityManager am) {
		am.clearApplicationUserData();
	}
}
