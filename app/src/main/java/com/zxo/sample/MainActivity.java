package com.zxo.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.nativejpeg.NativeLib;
import com.zxo.sample.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private TextView tvCompress;
    private TextView tvLibCompress;

    private final static int SIZE = 100;

    private final View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == tvCompress){
                // 系统压缩
               Bitmap bitmap =  getAssetsBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
                int options = 100;
                while (baos.toByteArray().length / 1024 > SIZE) {  //循环判断如果压缩后图片是否大于(size)kb,大于继续压缩
                    baos.reset();//重置baos即清空baos
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
                    options -= 4;//每次都减少4
                    if (options <= 0){
                        break;
                    }
                }
                String filePath = "";
                try {
                    File tempFile = new File(FileUtil.getSavePath(MainActivity.this, "android_compress.jpeg"));
                    filePath = tempFile.getAbsolutePath();
                    FileOutputStream fos = new FileOutputStream(tempFile);
                    fos.write(baos.toByteArray());
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                    filePath = null;
                }
                Log.e("bitmap_compress", "压缩完成 filePath="+filePath);
            } else if (v == tvLibCompress){
                // so库自定义压缩
                Bitmap bitmap =  getAssetsBitmap();
                NativeLib.nativeCompressJPEG(bitmap, 30, FileUtil.getSavePath(MainActivity.this, "native_compress.jpeg"));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCompress = findViewById(R.id.activity_main_bitmap_compress);
        tvLibCompress = findViewById(R.id.activity_main_native_bitmap_compress);

        tvCompress.setOnClickListener(clickListener);
        tvLibCompress.setOnClickListener(clickListener);

    }

    private Bitmap getAssetsBitmap(){
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

}