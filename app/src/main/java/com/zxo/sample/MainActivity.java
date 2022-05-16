package com.zxo.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseInfo.init(this);
        String brand = Build.BRAND;
        String brandInfo = BaseInfo.getBrand();
        String version = Build.VERSION.RELEASE;
        String model = "sfsg";

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        int networkType = tm.getNetworkType();
        String test = getTest();

        log(brand);
        log(brandInfo);
        log(version);
        log(model);
        log(test);
        log(networkType+"");
    }

    private void log(String brand) {
        Log.e("MainActivity", "brand="+brand);
    }

    private String getTest(){
        return "hahah";
    }

}