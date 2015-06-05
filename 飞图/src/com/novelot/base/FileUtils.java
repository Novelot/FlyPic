package com.novelot.base;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.os.MemoryFile;
import android.util.Log;

/*
 * 
 */
public class FileUtils {

	private static final String TAG = "FileUtils";

	public static void saveToDisk() {
		try {
			MemoryFile mf = new MemoryFile("/sdcard/flypic/memoryfile.test",
					1024);
			byte[] buffer = "Hello World".getBytes();
			mf.writeBytes(buffer, 0, 0, buffer.length);
			mf.close();

		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "the memory file has been purged or deactivated.");
		}
	}
}
