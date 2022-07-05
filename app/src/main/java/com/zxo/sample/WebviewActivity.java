package com.zxo.sample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebviewActivity extends AppCompatActivity {

  private TextView tvJump;
  private WebView webView;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_webview);

    tvJump = findViewById(R.id.test_jump);
    webView = findViewById(R.id.test_webview);


    initWebview();

    tvJump.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        webView.evaluateJavascript("window.location.href=\"https://www.jd.com\"", new ValueCallback<String>() {
          @Override
          public void onReceiveValue(String value) {
            Log.d("zxo-webview", "onReceiveValue value="+value);
          }
        });
      }
    });
  }

  private void initWebview() {

    //启用支持javascript
    WebSettings settings = webView.getSettings();

    settings.setJavaScriptEnabled(true);

    webView.loadUrl("https://blog.csdn.net/herr_kun/article/details/84146462");

    webView.setWebViewClient(new WebViewClient(){
      @Override
      public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        Log.d("zxo-webview", "onPageStarted url="+url);
      }

      @Override
      public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.d("zxo-webview", "onPageFinished url="+url);
      }
    });
  }
}
