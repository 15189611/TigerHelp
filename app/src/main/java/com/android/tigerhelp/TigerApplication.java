package com.android.tigerhelp;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import com.android.tigerhelp.util.Screen;

/**
 * Created by Charles on 2016/12/22.
 */

public class TigerApplication extends Application {

    public static Application application;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        context = application.getApplicationContext();
        Screen.initScreen(getApplicationContext());
    }

    /**
     * 获取app当前版本
     */
    public static String getAppVersion(){
        PackageManager packageManager =application.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(application.getPackageName(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        return packInfo.versionName;
    }

    /**
     * 获取用户设备 ID
     */
    public static String getDeviceId(){
        TelephonyManager tm = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static Context getMyApplicationContext(){
        if(context != null){
            return context;
        }
        return null;
    }
}
