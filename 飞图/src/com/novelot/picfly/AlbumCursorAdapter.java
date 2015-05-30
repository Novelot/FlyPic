package com.novelot.picfly;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;

import com.novelot.piccache.CacheInfo;
import com.novelot.util.MD5;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class AlbumCursorAdapter extends CursorAdapter {

	private static final String TAG = "AlbumCursorAdapter";
	private int ivWidth;
	private int ivHeight;
	private ContentResolver mResolver;
	// private LruCache<String, SoftReference<Bitmap>> mCache;
	private LruCache<String, Bitmap> mCache2;

	public AlbumCursorAdapter(Context context, Cursor c) {
		super(context, c, true);
		mResolver = context.getContentResolver();
		// mCache = new LruCache<String, SoftReference<Bitmap>>(1024 * 1024 *
		// 2);
		mCache2 = new LruCache<String, Bitmap>(1024 * 1024);

		ivWidth = CacheInfo.getInstance().screenWidth / 2;
		if (ivWidth <= 0)
			ivWidth = CacheInfo.DEFAULT_SCREEN_WIDTH;
		ivHeight = Math.round((ivWidth * 0.618f));
	}

	@Override
	public void bindView(View v, Context ctx, Cursor cursor) {
		// Log.v(TAG, "cache size :" + mCache.maxSize() / 1024);
		// ImageView iv = (ImageView) v;
		// String strUri = cursor.getString(cursor
		// .getColumnIndex(MediaStore.Images.Media.DATA));
		// Log.v(TAG, strUri);
		// String cacheKey = createCacheKey(strUri, ivWidth, ivHeight);
		// SoftReference<Bitmap> softReference = mCache.get(cacheKey);
		// if (softReference == null || softReference.get() == null) {
		// Bitmap thumbBitmap = getThumbnailBitmap(mResolver,
		// Uri.parse("file://" + strUri), ivWidth, ivHeight);
		// mCache.put(cacheKey, new SoftReference<Bitmap>(thumbBitmap));
		// softReference = mCache.get(cacheKey);
		// }
		// Bitmap thumbBitmap = softReference.get();
		// if (thumbBitmap != null)
		// iv.setImageBitmap(thumbBitmap);

		/* 不使用软引用 */
		ImageView iv = (ImageView) v;
		String strUri = cursor.getString(cursor
				.getColumnIndex(MediaStore.Images.Media.DATA));
		Log.v(TAG, strUri);
		String cacheKey = createCacheKey(strUri, ivWidth, ivHeight);
		Bitmap bitmap = mCache2.get(cacheKey);
		if (bitmap == null) {
			bitmap = getThumbnailBitmap(mResolver,
					Uri.parse("file://" + strUri), ivWidth, ivHeight);
			mCache2.put(cacheKey, bitmap);
		}
		if (bitmap != null)
			iv.setImageBitmap(bitmap);
	}

	@Override
	public View newView(Context ctx, Cursor arg1, ViewGroup vp) {
		View v = View.inflate(ctx, R.layout.fragment_grid_item, null);
		return v;
	}

	/**
	 * 获取缩略图
	 * 
	 * @param uri
	 * @param resolver
	 * @return
	 */
	private static Bitmap getThumbnailBitmap(ContentResolver resolver, Uri uri,
			int ivWidth, int ivHeight) {
		Bitmap bitmap = null;
		try {
			InputStream is = resolver.openInputStream(uri);
			Rect outPadding = null;
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			// opts.inSampleSize = 8;
			BitmapFactory.decodeStream(is, outPadding, opts);
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			is = resolver.openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(is, outPadding, opts);
			bitmap = Bitmap.createScaledBitmap(bitmap, ivWidth, ivHeight
					* opts.inSampleSize, true);
			if (bitmap.getHeight() > ivHeight)
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, ivWidth, ivHeight);
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 通过路径,宽高,得到缓存key
	 * 
	 * @param strUri
	 * @param width
	 * @param height
	 * @return
	 */
	private String createCacheKey(String strUri, int width, int height) {
		return MD5.getMD5(MD5.getMD5(strUri) + width + height);
	}

}
