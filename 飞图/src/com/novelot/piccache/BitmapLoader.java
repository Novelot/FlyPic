package com.novelot.piccache;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

public class BitmapLoader {

	protected static final int GOT_BITMAP = 0;
	private static BitmapLoader instance;
	protected Configs mConfigs;
	private EventHandler mEventHandler;
	protected static FlyPicLruCache mMemoryCache;

	private BitmapLoader() {
		Looper looper;
		if ((looper = Looper.myLooper()) != null) {
			mEventHandler = new EventHandler(this, looper);
		} else if ((looper = Looper.getMainLooper()) != null) {
			mEventHandler = new EventHandler(this, looper);
		} else {
			mEventHandler = null;
		}

	}

	public static BitmapLoader getInstance() {
		if (instance == null)
			synchronized (BitmapLoader.class) {
				if (instance == null) {
					instance = new BitmapLoader();
				}
			}

		return instance;
	}

	public void init(Configs configs) {
		this.mConfigs = configs;
		mMemoryCache = new FlyPicLruCache(mConfigs.cacheSize);
	}

	public boolean isInited() {
		return mConfigs != null;
	}

	public synchronized void display(String bitmapPath, ImageView iv,
			int width, int height) {
		String key = Utils.createCacheKey(bitmapPath, width, height);
		Bitmap bitmap = getFromMemoryCache(key);
		if (bitmap == null) {
			getFromDisk(bitmapPath, iv, width, height);
		} else {
			iv.setImageBitmap(bitmap);
		}
	}

	private synchronized void getFromDisk(final String bitmapPath,
			final ImageView iv, final int width, final int height) {
		new Thread() {
			@Override
			public void run() {
				String key = Utils.createCacheKey(bitmapPath, width, height);
				Bitmap bitmap;
				File tmpFile = new File(mConfigs.cacheDir, key);
				if (tmpFile != null && tmpFile.exists()) {
					bitmap = BitmapFactory
							.decodeFile(tmpFile.getAbsolutePath());
				} else {
					bitmap = getBitmapFromPath(bitmapPath, width, height);
					saveDiskCache(bitmap, key);
				}

				if (bitmap != null) {
					saveMemoryCache(key, bitmap);
					Message msg = Message.obtain();
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("bitmap", bitmap);
					map.put("imageview", iv);
					msg.obj = map;
					msg.what = GOT_BITMAP;
					mEventHandler.sendMessage(msg);
				}

			}
		}.start();
	}

	protected synchronized void saveDiskCache(Bitmap bitmap, String cacheKey) {
		BufferedOutputStream out = null;
		/* save to disk */
		try {
			File outFile = new File(mConfigs.cacheDir, cacheKey);
			out = new BufferedOutputStream(new FileOutputStream(outFile));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	protected synchronized void saveMemoryCache(String key, Bitmap bitmap) {
		mMemoryCache.put(key, bitmap);
	}

	protected Bitmap getBitmapFromPath(String bitmapPath, int width, int height) {
		Bitmap bitmap = null;

		if (mConfigs.decoder != null) {
			bitmap = mConfigs.decoder.decode(bitmapPath, width, height);
		}
		if (bitmap != null) {

		}
		return bitmap;
	}

	private Bitmap getFromMemoryCache(String key) {
		return mMemoryCache.get(key);
	}

	static class EventHandler extends Handler {
		public EventHandler(BitmapLoader bitmapLoader, Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GOT_BITMAP:
				HashMap<String, Object> map = new HashMap<String, Object>();
				Bitmap bitmap = (Bitmap) map.get("bitmap");
				ImageView iv = (ImageView) map.get("imageview");
				iv.setImageBitmap(bitmap);
				break;
			}
			super.handleMessage(msg);
		}

	}
}
