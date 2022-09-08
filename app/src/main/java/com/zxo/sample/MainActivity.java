package com.zxo.sample;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zxo.widget.LargeImageView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

  private TextView tvCompress;
  private TextView tvLibCompress;

  private TextView tvWebview;
  private TextView tvAIDL;
  private TextView tvText;

  private LargeImageView largeImageView;
  
  private final static int SIZE = 100;

  private final View.OnClickListener clickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
      if (v == tvCompress) {
        // 系统压缩
        //               Bitmap bitmap =  getAssetsBitmap();
        //                ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        //                int options = 100;
        //                while (baos.toByteArray().length / 1024 > SIZE) {  //循环判断如果压缩后图片是否大于(size)kb,大于继续压缩
        //                    baos.reset();//重置baos即清空baos
        //                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        //                    options -= 4;//每次都减少4
        //                    if (options <= 0){
        //                        break;
        //                    }
        //                }
        //                String filePath = "";
        //                try {
        //                    File tempFile = new File(FileUtil.getSavePath(MainActivity.this, "android_compress.jpeg"));
        //                    filePath = tempFile.getAbsolutePath();
        //                    FileOutputStream fos = new FileOutputStream(tempFile);
        //                    fos.write(baos.toByteArray());
        //                    fos.flush();
        //                    fos.close();
        //                } catch (Exception e) {
        //                    filePath = null;
        //                }
        //                Log.e("bitmap_compress", "压缩完成 filePath="+filePath);
        tvLibCompress.setText("点击开启线程");
        //                Thread thread = new Thread(new Runnable() {
        //                    @Override
        //                    public void run() {
        //                        try {
        //                            Thread.sleep(5000L);
        //                        } catch (InterruptedException e) {
        //                            e.printStackTrace();
        //                        }
        //                        Log.e("测试主线程阻塞","线程执行完毕");
        //                    }
        //                });
        //
        //                thread.start();
        //
        //                try {
        //                    thread.join();
        //                } catch (InterruptedException e) {
        //                    e.printStackTrace();
        //                } finally {
        //                    tvLibCompress.setText("主线程开始执行");
        //                    Log.e("测试主线程阻塞","主线程开始执行");
        //                }
        try {
          Thread.sleep(3000L);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        Log.e("测试主线程阻塞", "线程执行完毕");

      } else if (v == tvLibCompress) {

        tvLibCompress.setText("主线程刷新");

        //                // so库自定义压缩
        //                Bitmap bitmap =  getAssetsBitmap();
        //                NativeLib.nativeCompressJPEG(bitmap, 30, FileUtil.getSavePath(MainActivity.this, "native_compress.jpeg"));
      } else if (v == tvWebview) {
        startActivity(new Intent(MainActivity.this, WebviewActivity.class));
      } else if (v == tvAIDL) {
        startActivity(new Intent(MainActivity.this, TestAIDLActivity.class));
      } else if (v == tvText) {
        startActivity(new Intent(MainActivity.this, TestTextActivity.class));
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    tvCompress = findViewById(R.id.activity_main_bitmap_compress);
    tvLibCompress = findViewById(R.id.activity_main_native_bitmap_compress);
    tvWebview = findViewById(R.id.activity_main_webview);
    tvAIDL = findViewById(R.id.activity_main_aidl);
    tvText = findViewById(R.id.activity_main_text);
    
    largeImageView = findViewById(R.id.activity_main_test_large_image);

    tvCompress.setOnClickListener(clickListener);
    tvLibCompress.setOnClickListener(clickListener);
    tvWebview.setOnClickListener(clickListener);
    tvAIDL.setOnClickListener(clickListener);
    tvText.setOnClickListener(clickListener);
    
    loadLargeImageView();

    BaseInfo.init(this);
    String brand = Build.BRAND;
    String brandInfo = BaseInfo.getBrand();
    String version = Build.VERSION.RELEASE;
    String model = "sfsg";

    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

    int networkType = tm.getNetworkType();
    String test = getTest();


    if (TextUtils.isEmpty(test)) {
      log(brand);
      log(brandInfo);
      log(version);
      log(model);
    } else {
      log(test);
      log(networkType + "");
    }
  }

  private void loadLargeImageView() {

    try {
      InputStream is = getAssets().open("big_image.jpg");
      largeImageView.setImageUrl(is);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Bitmap getAssetsBitmap() {
    Bitmap bitmap = null;
    AssetManager assetManager = this.getAssets();
    try {
      InputStream inputStream = assetManager.open("test.jpeg");//filename是assets目录下的图片名
      bitmap = BitmapFactory.decodeStream(inputStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bitmap;
  }

  private void log(String brand) {
    try {
      int a = 1 / 0;
    } catch (Exception e) {
      e.printStackTrace();
    }

    Log.e("MainActivity", "brand=" + brand);
  }

  private String getTest() {
    return "hahah";
  }

}