package com.zxo.sample;

import android.os.Build;

public class BaseInfo {

    public static String getBrand(){
        return Build.BRAND;
    }

    public static String getAndroidVersion(){
        return Build.VERSION.RELEASE;
    }

}
