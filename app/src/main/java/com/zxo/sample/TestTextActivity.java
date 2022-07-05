package com.zxo.sample;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zxo.sample.text.IFormatSpan;

public class TestTextActivity extends AppCompatActivity {

  private EditText editText;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_test_text);

    editText = findViewById(R.id.test_text_edit);

    InputFilter[] filters = new InputFilter[]{new InputFilter.LengthFilter(10), new TestFilter()};

    editText.setFilters(filters);
  }

  private class TestFilter implements InputFilter{

    /**
     * 当缓存区dest中的dstart..dend区域替换为source的start..end区域的内容时，调用该方法
     * @param source 当前输入的内容
     * @param start 输入内容开始的index
     * @param end 输入内容结束的index  end-start代表source当前输入的长度，
     * @param dest 当前输入框中缓存的数据即已经输入的数据
     * @param dstart 替换开始的位置
     * @param dend 替换结束的位置
     * @return
     */
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
      Log.i("TestTextActivity", "source="+source);
      Log.i("TestTextActivity", "start="+start+"，end="+end);
      Log.i("TestTextActivity", "Spanned dest="+dest);
      Log.i("TestTextActivity", "dstart="+dstart+"，dend="+dend);
      if (source == null) {
        source = "";
      }
      SpannableStringBuilder builder = new SpannableStringBuilder();
      builder.replace(0, 0, source, start, end);
      removeSpan(builder);
      return builder.subSequence(start, end);
    }

    private void removeSpan(CharSequence source){
      if (source instanceof Spannable) {
        Spannable spannable = (Spannable) source;
        IFormatSpan[] spans = spannable.getSpans(0, spannable.length(), IFormatSpan.class);
        for (IFormatSpan spacingSpan : spans) {
          spannable.removeSpan(spacingSpan);
        }
      }
    }
  }
}
