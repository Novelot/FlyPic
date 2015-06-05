package com.novelot.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ApkUtils {

	/**
	 * 安装apk
	 * 
	 * @param context
	 * @param apkPath
	 */
	public static void installApk(Context context, String apkPath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkPath),
				"application/vnd.android.package-archive");
		context.startActivity(i);

	}

	public static void uninstallApk(Context context, Uri uri) {
		Intent intent = new Intent(Intent.ACTION_DELETE, uri);
		context.startActivity(intent);

	}
}
