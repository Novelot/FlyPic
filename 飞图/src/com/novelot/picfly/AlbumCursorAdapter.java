package com.novelot.picfly;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.novelot.piccache.BitmapLoader;
import com.novelot.piccache.CacheInfo;
import com.novelot.piccache.Configs;
import com.novelot.piccache.DefaultDecoder;
import com.novelot.piccache.FlyPicLruCache;
import com.novelot.piccache.Utils;
import com.novelot.util.MD5;
import com.novelot.util.SystemUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AlbumCursorAdapter extends CursorAdapter {

	private static final String TAG = "AlbumCursorAdapter";
	private int ivWidth;
	private int ivHeight;
	private ContentResolver mResolver;
	// private LruCache<String, SoftReference<Bitmap>> mCache;
	private FlyPicLruCache mCache2;
	private Context context;

	public AlbumCursorAdapter(Context context, Cursor c) {
		super(context, c, true);
		this.context = context;
		mResolver = context.getContentResolver();
		// mCache = new LruCache<String, SoftReference<Bitmap>>(1024 * 1024 *
		// 2);
		int cacheSize = SystemUtils.getAvailMemory(context) * 1024 * 1024 / 8;
		mCache2 = new FlyPicLruCache(cacheSize);

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
		// String cacheKey = createCacheKey(strUri, ivWidth, ivHeight);
		// Bitmap bitmap = mCache2.get(cacheKey);
		// if (bitmap == null) {
		// bitmap = getThumbnailBitmap(mResolver, strUri, ivWidth, ivHeight);
		// mCache2.put(cacheKey, bitmap);
		// } else {
		// Log.v(TAG, "from membery cache");
		// }
		// if (bitmap != null) {
		// Log.v(TAG, "cache size is " + mCache2.usedSize() + "K");
		// iv.setImageBitmap(bitmap);
		// }

		/* 使用框架v1 */
		if (!BitmapLoader.getInstance().isInited()) {
			Configs configs = new Configs();
			configs.cacheDir = context.getCacheDir().getAbsolutePath();
			int availMemoryMs = SystemUtils.getAvailMemory(context);
			int bitmapCacheMemory = availMemoryMs * 1024 * 1024 / 8;
			configs.cacheSize = bitmapCacheMemory;
			configs.decoder = new DefaultDecoder();
			BitmapLoader.getInstance().init(configs);
		}

		BitmapLoader.getInstance().display(strUri, iv, ivWidth, ivHeight);
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
	private Bitmap getThumbnailBitmap(ContentResolver resolver, String strUri,
			int ivWidth, int ivHeight) {
		Bitmap bitmap = null;
		String cacheKey = createCacheKey(strUri, ivWidth, ivHeight);

		File tmpFile = new File(context.getCacheDir(), cacheKey);
		if (tmpFile != null && tmpFile.exists()) {
			bitmap = BitmapFactory.decodeFile(tmpFile.getAbsolutePath());
			Log.v(TAG, "from disk cache");
			return bitmap;
		}
		try {
			Uri uri = Uri.parse("file://" + strUri);
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
				return null;
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

			Log.v(TAG, "from src");
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			/* save to disk */
			File outFile = new File(context.getCacheDir(), cacheKey);
			OutputStream out = new BufferedOutputStream(new FileOutputStream(
					outFile));
			bitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;

		// return BitmapDecoder.decode(resolver, uri, ivWidth, ivHeight);
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

	// void getBitmapFromDisk() {
	// new AsyncTask<ViewHolder, Void, Bitmap>() {
	// private ViewHolder v;
	//
	// @Override
	// protected Bitmap doInBackground(ViewHolder... params) {
	// v = params[0];
	// return mFakeImageLoader.getImage();
	// }
	//
	// @Override
	// protected void onPostExecute(Bitmap result) {
	// super.onPostExecute(result);
	// if (v.position == position) {
	// // If this item hasn't been recycled already, hide the
	// // progress and set and show the image
	// // v.progress.setVisibility(View.GONE);
	// v.icon.setVisibility(View.VISIBLE);
	// v.icon.setImageBitmap(result);
	// }
	// }
	// }.execute(holder);
	// }
	//
	// static class ViewHolder {
	// int postion;
	// ImageView icon;
	// }

}
