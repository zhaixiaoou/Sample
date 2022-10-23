package com.example.nativejpeg;

import android.graphics.Bitmap;

public class NativeLib {

  // Used to load the 'nativejpeg' library on application startup.
  static {
    System.loadLibrary("nativejpeg");
  }

  public native static int nativeCompressJPEG(Bitmap bitmap, int quality, String outPath);
}