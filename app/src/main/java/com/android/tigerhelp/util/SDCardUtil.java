package com.android.tigerhelp.util;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

public class SDCardUtil {
	public static final String SDCARD_ROOT_PATH = Environment
			.getExternalStorageState();
	public static final File SDCARD_DIRECTORY = Environment
			.getExternalStorageDirectory();

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExists() {
		if (!TextUtils.isEmpty(SDCARD_ROOT_PATH)
				&& SDCARD_ROOT_PATH
						.equals(Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
	
	public static String getSDCardAbsolutePath(){
		String sdCardPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath();
		return sdCardPath;
	}
}
