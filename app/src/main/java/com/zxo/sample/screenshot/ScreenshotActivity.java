package com.zxo.sample.screenshot;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.zxo.sample.R;

public class ScreenshotActivity extends FragmentActivity {
  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_screenshot);
    if (savedInstanceState == null){
      FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
      ScreenshotFragment fragment = new ScreenshotFragment();
      transaction.replace(R.id.screen_fragment, fragment);
      transaction.commit();
    }
  }
}
