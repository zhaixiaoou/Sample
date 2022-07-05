package com.zxo.sample;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

public class BaseInfo {

    private static Context mContext;

    private BaseInfo instance;

    public static void  init(Context context){
        mContext = context.getApplicationContext();
    }

    public static String getBrand(){
        return Build.BRAND;
    }

    public static String getAndroidVersion(){
        return Build.VERSION.RELEASE;
    }


    public static int getNetworkType(){
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getNetworkType();
    }

}
