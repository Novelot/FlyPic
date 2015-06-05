package com.novelot.piccache;

import android.graphics.Bitmap;

public interface Decoder {

	public Bitmap decode(String bitmapPath, int width, int height);
}
