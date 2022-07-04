package com.zxo.extra;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;


public class TestImportClass extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNetworkType();
    }

    public void getNetworkType(){
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        int networkType = tm.getNetworkType();
        log(networkType+"");
    }

    private void log(String brand) {
        Log.e("TestImportClass", "brand="+brand);
    }
}
