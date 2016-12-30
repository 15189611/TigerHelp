package com.android.tigerhelp.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.android.tigerhelp.util.JsonParser;
import com.android.tigerhelp.util.JsonUtil;

/**
 * Created by Charles on 2016/10/12.
 */

public class ShareUtils {

    public static SharedPreferences mPreference;

    public static SharedPreferences getPreference(Context context) {
        if (mPreference == null)
            mPreference = PreferenceManager.getDefaultSharedPreferences(context);
        return mPreference;
    }

    private static SharedPreferences.Editor edit(Context context) {
        return getPreference(context).edit();
    }

    public static boolean save(Context context,String key, String value) {
        return edit(context).putString(key, value).commit();
    }

    public static boolean save(Context context,String key, int value) {
        return edit(context).putInt(key, value).commit();
    }

    public static boolean save(Context context,String key, boolean value) {
        return edit(context).putBoolean(key, value).commit();
    }

    public static boolean save(Context context,String key, float value) {
        return edit(context).putFloat(key, value).commit();
    }

    public static boolean remove(Context context,String key) {
        return edit(context).remove(key).commit();
    }

    // 通过键删除值
    public static void isValue(Context context,String name) {
        getPreference(context).edit().remove(name).commit();
    }

    public static void saveObjecToString(Context context,String key, Object value) {
        String result = null;
        if (value != null) {
            result = JsonUtil.toJson(value);
        }
        save(context,key, result);
    }

    public static  <T> T getObject(Context context,String key, Class<T> clz){
        if(get(context,key) != null){
            T t = JsonParser.deserializeByJson(ShareUtils.get(context, key).toString(), clz);
            return  t;
        }else{
            return null;
        }
    }

    public static Object get(Context context,String key) {
        return getPreference(context).getAll().get(key);
    }

}
