package com.zxo.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String brand = Build.BRAND;
        String model = "sfsg";
        String test = getTest();

        log(brand);
        log(model);
        log(test);
    }

    private void log(String brand) {
        Log.e("MainActivity", "brand="+brand);
    }

    private String getTest(){
        return "hahah";
    }

}