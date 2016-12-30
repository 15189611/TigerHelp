package com.android.tigerhelp.util;

import android.database.Cursor;

/**
 * Cursor 工具类
 */
public class CursorUtil {

	/**
	 * 关闭游标
	 * 
	 * @param cursor
	 */
	public static void closeCursor(Cursor cursor) {
		try {
			if (null != cursor && !cursor.isClosed()) {
				cursor.close();
				cursor = null;
			}
		} catch (Exception e) {
		}
	}
}
