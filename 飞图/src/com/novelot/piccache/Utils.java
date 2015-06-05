package com.novelot.piccache;

import com.novelot.util.MD5;

public class Utils {

	/**
	 * 通过路径,宽高,得到缓存key
	 * 
	 * @param strUri
	 * @param width
	 * @param height
	 * @return
	 */
	public static String createCacheKey(String strUri, int width, int height) {
		return MD5.getMD5(MD5.getMD5(strUri) + width + height);
	}
}
