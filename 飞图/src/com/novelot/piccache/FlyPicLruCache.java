package com.novelot.piccache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

public class FlyPicLruCache extends LruCache<String, Bitmap> {
	private static final String TAG = "AlbumCursorAdapter";
	private int mUsedSize;

	public FlyPicLruCache(int maxSize) {
		super(maxSize);
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		int byteCount = value.getByteCount() / 1024;
		Log.v(TAG, "the bitmap size is " + byteCount + "K");
		mUsedSize += byteCount;
		return byteCount;
	}

	public int usedSize() {
		return mUsedSize;
	}
}
