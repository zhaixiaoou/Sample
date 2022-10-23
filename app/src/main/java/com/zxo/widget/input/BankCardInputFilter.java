package com.duolabao.duolabaoagent.widget.input;

import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class BankCardInputFilter implements InputFilter {

  static final int NBSP_CODE_POINT = 160;

  @Override
  public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
    SpannableStringBuilder filterSpannable = createBuilder(source, start, end);
    removeWhiteSpace(filterSpannable);

    // 移除自己定义的span
    removeFormatSpan(filterSpannable);
    removeFormatSpan(dest);

    // 创建span
    if (dest instanceof Spannable) {
      applySpan(filterSpannable, (Spannable) dest, dstart, dend);
    } else if (TextUtils.isEmpty(dest)){
      applySpan(filterSpannable, createBuilder("", 0, 0), dstart, dend);
    }

    return filterSpannable;
  }

  private void applySpan(@NonNull Spannable source, @NonNull Spannable dest, int dStart, int dEnd) {
    int sourceLength = source.length();
    int delta = dStart - dEnd;
    int length = dest.length() + delta + sourceLength;
    int sourceEnd = dStart + sourceLength;

    ArrayList<Integer> spaceList = getFormatIndexList(length);
    for (Integer i: spaceList){
      if (i< dStart){
        dest.setSpan(createSpan(), i, i+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      } else if (i< sourceEnd) {
        int spannedStart = i-dStart;
        source.setSpan(createSpan(), spannedStart, spannedStart+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      } else {
        dest.setSpan(createSpan(), sourceEnd, sourceEnd+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      }
    }
  }

  private IFormatSpan createSpan(){
    return SpaceSpan.create();
  }

  private ArrayList<Integer> getFormatIndexList(int length){
    ArrayList<Integer> filter = new ArrayList<>();
    for (int  i =3; i<length; i=i+4) {
      filter.add(i);
    }
    return filter;
  }

  /**
   * 保留原有样式不变
   *
   * @param source
   * @param start
   * @param end
   * @return
   */
  private SpannableStringBuilder createBuilder(CharSequence source, int start, int end) {
    if (source == null) {
      source = "";
    }
    SpannableStringBuilder builder = new SpannableStringBuilder();
    builder.replace(0, 0, source, start, end);
    return builder;
  }

  /**
   * 移除空白字符
   *
   * @param source
   */
  private void removeWhiteSpace(SpannableStringBuilder source) {
    int i = 0;
    while (i < source.length()) {
      int codePointAt = Character.codePointAt(source, i);
      int charCount = Character.charCount(codePointAt);
      if (Character.isWhitespace(codePointAt) || codePointAt == BankCardInputFilter.NBSP_CODE_POINT) {
        source.delete(i, i + charCount);
      } else {
        i = i + charCount;
      }
    }
  }

  /**
   * 移除自己定义的span
   *
   * @param source
   */
  private void removeFormatSpan(CharSequence source) {
    if (source instanceof IFormatSpan) {
      Spannable spannable = (Spannable) source;
      IFormatSpan[] spans = spannable.getSpans(0, source.length(), IFormatSpan.class);
      for (IFormatSpan span : spans) {
        ((Spannable) source).removeSpan(span);
      }
    }
  }


}
