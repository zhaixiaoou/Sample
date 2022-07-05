package com.zxo.sample;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zxo.sample.aidl.AIDLTestService;

public class TestAIDLActivity extends AppCompatActivity {

  private TextView tvAction;

  private ITestAIDL iTestAIDL;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_aidl);

    tvAction = findViewById(R.id.test_aidl_action);

    tvAction.setOnClickListener(clickListener);
    Intent intent = new Intent(this, AIDLTestService.class);

     boolean isBind = bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    Log.e("aidl", "isBind = "+isBind);
  }

  private ServiceConnection serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
      // 获得代理对象
      Log.e("aidl", "onServiceConnected");
      iTestAIDL = ITestAIDL.Stub.asInterface(binder);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
  };

  private final View.OnClickListener clickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      if (v == tvAction){
        try {
          String name =  iTestAIDL.getName();
          Toast.makeText(TestAIDLActivity.this, name, Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
    }
  };
}
