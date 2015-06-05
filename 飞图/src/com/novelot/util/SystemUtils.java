package com.novelot.util;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

public class SystemUtils {

	/**
	 * 获取APP可用内存,单位兆
	 * 
	 * @param ctx
	 * @return
	 */
	public static int getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getMemoryClass();
	}
}
