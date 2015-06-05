package com.novelot.picfly;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.novelot.piccache.CacheInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AlbumUILAdapter extends CursorAdapter {

	private static final String TAG = "AlbumCursorAdapter";
	private int ivWidth;
	private int ivHeight;
	private ContentResolver mResolver;

	// private LruCache<String, SoftReference<Bitmap>> mCache;

	public AlbumUILAdapter(Context context, Cursor c) {
		super(context, c, true);
		mResolver = context.getContentResolver();
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

		String uri = Uri.parse("file://" + strUri).toString();
		Log.v(TAG, uri);

		ImageLoader.getInstance().displayImage(uri, iv);
	}

	@Override
	public View newView(Context ctx, Cursor arg1, ViewGroup vp) {
		View v = View.inflate(ctx, R.layout.fragment_grid_item, null);
		return v;
	}

}
