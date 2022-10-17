package com.duolabao.duolabaoagent.widget.input;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.MetricAffectingSpan;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpaceSpan extends ReplacementSpan implements IFormatSpan {

  private TextPaint textPaint;

  private SpaceSpan(){}

  public static SpaceSpan create(){
    return  new SpaceSpan();
  }

  @Override
  public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
    textPaint = new TextPaint(paint);
    float spaceWidth = textPaint.measureText(" ");

    if (text instanceof Spanned){
      Spanned spanned = (Spanned) text;

      CharacterStyle[] spans = spanned.getSpans(start, end, CharacterStyle.class);
      for (CharacterStyle item : spans){
        // 除了特定样式的spanned，其他的都用统一的paint处理
        if (item instanceof MetricAffectingSpan) {
          continue;
        }
        item.updateDrawState(textPaint);
      }
    }
    return (int) (spaceWidth + textPaint.measureText(text, start, end));
  }

  @Override
  public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
    canvas.drawText(text, start, end, x, y, textPaint);
  }
}
