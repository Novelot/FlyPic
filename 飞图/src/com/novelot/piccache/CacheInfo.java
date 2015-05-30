package com.novelot.piccache;

public class CacheInfo {

	public static final int DEFAULT_SCREEN_WIDTH = 200;
	private static CacheInfo mInstance;

	private CacheInfo() {
	}

	public static CacheInfo getInstance() {
		if (mInstance == null) {
			synchronized (CacheInfo.class) {
				if (mInstance == null) {
					mInstance = new CacheInfo();
				}
			}
		}

		return mInstance;
	}

	public int screenWidth;
}
