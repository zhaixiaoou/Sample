package com.zxo.sample.util;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import androidx.annotation.NonNull;

import java.io.File;

public class FileUtil {
  private static final String DIR_NAME = "ZXO";

  private static final String SEPARATE_CHAR = File.separator;
  private static final String QUERY_CHAR= "?";
  /**
   * 获取存储地址
   * @param url
   * @return
   */
  public static String getSavePath(Context context, @NonNull String url){
    // 外部存储是否可用
    String state = Environment.getExternalStorageState();
    // 可用状态
    if ( Environment.MEDIA_MOUNTED.equals(state)){
      File imagesFolder = new File(Environment.getExternalStorageDirectory(), DIR_NAME);
      if (!imagesFolder.exists()) {
        boolean mk = imagesFolder.mkdirs();
        if (!mk){
          // 创建失败 走私有目录
          return getPrivateSavePath(context, url);
        }
      }
      //  文件存在  返回路径
      File targetFile = new File(imagesFolder, getFileName(url));
      return targetFile.getAbsolutePath();
    } else {
      // 不可用状态，转为私有目录村粗
      return getPrivateSavePath(context, url);
    }

  }

  /**
   * 获取私有目录地址
   * @return
   */
  private static String getPrivateSavePath(Context context, String url){
    File privateFileDir = context.getFilesDir();
    File targetFile = new File(privateFileDir, getFileName(url));
    return targetFile.getAbsolutePath();
  }

  private static String getFileName(@NonNull String url){

    int lastIndex = url.lastIndexOf(SEPARATE_CHAR);
    if (lastIndex != -1){
      url = url.substring(lastIndex+1);
      lastIndex = url.lastIndexOf(QUERY_CHAR);
      if (lastIndex != -1){
        return url.substring(0, lastIndex);
      }
    }
    return url;
  }
}
