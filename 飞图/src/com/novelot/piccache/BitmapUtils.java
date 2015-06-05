package com.novelot.piccache;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

public class BitmapUtils {

	public static Bitmap createVideoThumbnail(String filePath, int kind) {
		return ThumbnailUtils.createVideoThumbnail(filePath, kind);
	}
}
