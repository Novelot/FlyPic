package com.novelot.piccache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class DefaultDecoder implements Decoder {

	@Override
	public Bitmap decode(String bitmapPath, int ivWidth, int ivHeight) {
		Bitmap bitmap = null;
		Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(bitmapPath, opts);
		//
		int height = opts.outHeight;
		int width = opts.outWidth;

		if (width > ivWidth || height > ivHeight) {
			if (width / ivWidth > height / ivHeight) {
				opts.inSampleSize = width / ivWidth;
			} else {
				opts.inSampleSize = height / ivHeight;
			}
		} else {
			opts.inSampleSize = 2;
		}
		// opts.outWidth = ivWidth;
		// opts.outHeight = ivHeight;
		opts.inJustDecodeBounds = false;
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		bitmap = BitmapFactory.decodeFile(bitmapPath, opts);
		bitmap = Bitmap.createScaledBitmap(bitmap, ivWidth, ivHeight
				* opts.inSampleSize, true);
		if (bitmap.getHeight() > ivHeight)
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, ivWidth, ivHeight);

		return bitmap;
	}

}
