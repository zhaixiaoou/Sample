package com.zxo.sample.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.zxo.sample.ITestAIDL;

public class AIDLTestService extends Service {

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    Log.e("aidl", "onBind");
    return new AIDLTestBinder();
  }

  class AIDLTestBinder extends ITestAIDL.Stub {

    @Override
    public String getName() throws RemoteException {
      return "测试AIDL";
    }
  }
}
