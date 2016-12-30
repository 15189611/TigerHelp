package com.android.tigerhelp.util;

import java.io.File;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 图片库工具类
 * 
 * @author Administrator
 * 
 */
public class GalleryUtil {
	public static final String TAG = GalleryUtil.class.getSimpleName();

	public static final int GET_IMAGE_BY_CAMERA = 5001;
	public static final int REQUEST_GALLERT_FROM_LOACL = 5002;
	public static final int CROP_IMAGE = 5003;
	public static final int REQUEST_CODE_CAMERA = 5004;

	/**
	 * 访问本地图片库
	 */
	public static void openGalleryFromLocal(Activity activity) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(VERSION.SDK_INT > 19 ? Intent.ACTION_OPEN_DOCUMENT
				: Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		activity.startActivityForResult(intent, REQUEST_GALLERT_FROM_LOACL);
	}

	/**
	 * 打开系统图片相机
	 * 
	 * @param activity
	 * @param tempFile
	 *            输出目标文件
	 * @return
	 */
	public static boolean openImageCamera(Activity activity, File tempFile) {
		try {
			if (null == tempFile) {
				return false;
			}
			Uri srcUri = Uri.fromFile(tempFile);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, srcUri);
			activity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 生成临时文件
	 * 
	 * @return
	 */
	public static File generateTempFiles() {
		File tempFile = null;
		try {
			if (!SDCardUtil.isExists()) {
				Log.e(TAG, "Not found SDCard !");
				return null;
			}
			tempFile = new File(SDCardUtil.SDCARD_DIRECTORY + "/BESTTONE",
					"ICON_" + System.currentTimeMillis() + ".jpg");
			if (!tempFile.getParentFile().exists()) {
				tempFile.getParentFile().mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, e.getMessage());
		}
		return tempFile;
	}

	/**
	 * 裁剪图片
	 * 
	 * @param activity
	 * @param srcUri
	 * @return File 裁剪后的临时图片文件
	 */
	public static File cropImageByUri(Activity activity, Uri srcUri) {
		if (srcUri == null) {
			return null;
		}
		// 原图
		File srcfile = null;
		// 裁剪后的临时图片文件
		File cropTempFile = null;
		if (VERSION.SDK_INT >= 19) {
			String path = getPath(activity, srcUri);
			srcfile = new File(path);
			srcUri = Uri.fromFile(srcfile);
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(srcUri, "image/*");// 不去图库 直接去裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是裁剪框宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪后生成图片的宽高
		intent.putExtra("outputX", 90);
		intent.putExtra("outputY", 90);
		intent.putExtra("return-data", true);
		if (VERSION.SDK_INT >= 19) {
			String cropTempFilePath = srcfile.getParent() + File.separator
					+ "ICON_" + System.currentTimeMillis() + ".jpg";
			cropTempFile = new File(cropTempFilePath);
			Uri cropTempFileUri = Uri.fromFile(cropTempFile);
			Log.e(TAG, cropTempFileUri.toString());
			intent.putExtra(MediaStore.EXTRA_OUTPUT, cropTempFileUri);
		}

		activity.startActivityForResult(intent, CROP_IMAGE);
		return cropTempFile;
	}

	@SuppressLint("NewApi")
	public static String getPath(final Context context, final Uri uri) {
		final boolean isKitKat = VERSION.SDK_INT >= VERSION_CODES.KITKAT;
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			} else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));
				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };
				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			// Return the remote address
			if (isGooglePhotosUri(uri)) {
				return uri.getLastPathSegment();
			}
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}

	public static String getDataColumn(Context context, Uri uri,
			String selection, String[] selectionArgs) {
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };
		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			CursorUtil.closeCursor(cursor);
		}
		return null;
	}

	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	public static boolean isGooglePhotosUri(Uri uri) {
		return "com.google.android.apps.photos.content".equals(uri
				.getAuthority());
	}
}
