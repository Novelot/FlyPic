package com.novelot.piccache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;

public class BitmapDecoder {

	protected static Options prepareDecodingOptions(int srcWidth,
			int srcHeight, int targetWidth, int targetHeight) {
		Options decodingOptions = new Options();
		int scale = ImageSizeUtils.computeImageSampleSize(srcWidth, srcHeight,
				targetWidth, targetHeight, true);
		decodingOptions.inSampleSize = scale;
		decodingOptions.inPreferredConfig = Bitmap.Config.RGB_565;
		return decodingOptions;
	}

	public static Bitmap decode(ContentResolver resolver, Uri uri,
			int targetWidth, int targetHeight) {
		Bitmap decodedBitmap;
		int srcWidth;
		int srcHeight;

		/* 演习 */
		InputStream imageStream = null;
		Options options = new Options();
		try {
			imageStream = resolver.openInputStream(uri);
			if (imageStream == null) {
				return null;
			}
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(imageStream, null, options);
			imageStream.close();
			srcWidth = options.outWidth;
			srcHeight = options.outHeight;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (imageStream != null)
				try {
					imageStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
		}

		/* 实际 */
		try {
			imageStream = resolver.openInputStream(uri);
			if (imageStream == null) {
				return null;
			}
			options = prepareDecodingOptions(srcWidth, srcHeight, targetWidth,
					targetHeight);
			decodedBitmap = BitmapFactory.decodeStream(imageStream, null,
					options);
			imageStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (imageStream != null)
				try {
					imageStream.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
		}

		if (decodedBitmap != null) {
			decodedBitmap = considerExactScaleAndOrientatiton(decodedBitmap,
					srcWidth, srcHeight, targetWidth, targetHeight, 0, false);
		}
		return decodedBitmap;
	}

	protected static Bitmap considerExactScaleAndOrientatiton(
			Bitmap subsampledBitmap, int srcWidth, int srcHeight,
			int targetWidth, int targetHeight, int rotation,
			boolean flipHorizontal) {
		Matrix m = new Matrix();
		// Scale to exact size if need
		float scale = ImageSizeUtils.computeImageScale(srcWidth, srcHeight,
				targetWidth, targetHeight, true);
		if (Float.compare(scale, 1f) != 0) {
			m.setScale(scale, scale);

		}
		// Flip bitmap if need
		if (flipHorizontal) {
			m.postScale(-1, 1);

		}
		// Rotate bitmap if need
		if (rotation != 0) {
			m.postRotate(rotation);
		}

		Bitmap finalBitmap = Bitmap.createBitmap(subsampledBitmap, 0, 0,
				subsampledBitmap.getWidth(), subsampledBitmap.getHeight(), m,
				true);
		if (finalBitmap != subsampledBitmap) {
			subsampledBitmap.recycle();
		}
		return finalBitmap;
	}
}
