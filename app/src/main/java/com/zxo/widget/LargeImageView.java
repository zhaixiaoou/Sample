package com.zxo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class LargeImageView extends View implements GestureDetector.OnGestureListener, View.OnTouchListener {

  private static final String TAG = LargeImageView.class.getSimpleName();

  // 显示区域
  private Rect mRect;
  // bitmap参数
  private BitmapFactory.Options mOptions;
  // 手势
  private GestureDetector mGestureDetector;
  // 滑动
  private Scroller mScroller;

  private BitmapRegionDecoder mRegionDecoder;

  private Matrix matrix;

  private Bitmap mutableBitmap;

  private Context context;
  // 图片宽高
  private int imageWidth;
  private int imageHeight;
  // UI控件大小
  private int viewWidth;
  private int viewHeight;

  private float originScale;
  private float mScale;



  public LargeImageView(Context context) {
    this(context, null);
  }

  public LargeImageView(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public LargeImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.context = context;
    init();
  }

  private void init(){
    mRect = new Rect();
    mOptions =new  BitmapFactory.Options();
    mGestureDetector = new GestureDetector(context,this);
    mScroller = new Scroller(context);
    setOnTouchListener(this);
  }

  public void setImageUrl(InputStream inputStream){
    // 获取图片宽高，不加载进内存
    mOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeStream(inputStream, null, mOptions);
    imageWidth = mOptions.outWidth;
    imageHeight = mOptions.outHeight;
    //恢复该值为false
    mOptions.inJustDecodeBounds = false;

    // 允许bitmap复用， 但是无法设置硬件加速
    mOptions.inMutable = true;
    mOptions.inPreferredConfig = Bitmap.Config.RGB_565;

    try {
      // 创建区域解码器
      mRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // 重新绘制UI
    requestLayout();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    viewWidth = getMeasuredWidth();
    viewHeight = getMeasuredHeight();

    Log.i(TAG, "imageWidth ="+imageWidth + " imageHeight="+imageHeight);
    Log.i(TAG, "viewWidth ="+viewWidth + " viewHeight="+viewHeight);

//    originScale = viewWidth/ (float) imageWidth;
    originScale = viewHeight/ (float) imageHeight;
    mScale = originScale;

    Log.i(TAG, "scale = "+originScale);
    //分页首次展示的图片
    mRect.left = 0;
    mRect.top = 0;
    mRect.right = (int) (viewWidth/mScale);
    mRect.bottom = imageHeight;

  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if (mRegionDecoder == null) {
      return;
    }

    mOptions.inBitmap = mutableBitmap;
    mutableBitmap = mRegionDecoder.decodeRegion(mRect, mOptions);

    if (matrix == null){
      matrix = new Matrix();
    }

    matrix.setScale(mScale, mScale);
    if (mutableBitmap != null){
      canvas.drawBitmap(mutableBitmap, matrix, null);
    }
  }

  @Override
  public boolean onDown(MotionEvent e) {
    // 当手指按下，如果滑动还没有停止，则调用停止
    if (!mScroller.isFinished()){
      mScroller.forceFinished(true);
    }
    return true;
  }

  @Override
  public void onShowPress(MotionEvent e) {

  }

  @Override
  public boolean onSingleTapUp(MotionEvent e) {
    return false;
  }

  @Override
  public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
    mRect.offset((int) distanceX, (int) distanceY);

    boundary();

    postInvalidate();
    return false;
  }

  @Override
  public void onLongPress(MotionEvent e) {

  }

  @Override
  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    mScroller.fling(mRect.left, mRect.top, (int) -velocityX, (int)-velocityY, 0, imageWidth-viewWidth, 0, imageHeight-viewHeight);
    return false;
  }

  private void boundary(){

    Log.i(TAG, "bottom = "+mRect.bottom);
    // 边界处理
    if (mRect.bottom > imageHeight){
      mRect.bottom = imageHeight;
      mRect.top = imageHeight - (int) (viewHeight/mScale);
    }
    Log.i(TAG, "right = "+mRect.right);
    if (mRect.right > imageWidth ){
      mRect.right = imageWidth;
      mRect.left = imageWidth - (int)(viewWidth/mScale);
    }

    if (mRect.top < 0){
      mRect.top = 0;
      mRect.bottom = (int) (viewHeight/mScale);
    }
    Log.i(TAG, "left = "+mRect.left);
    if (mRect.left < 0){
      mRect.left = 0;
      mRect.right = (int)(viewWidth/mScale);
    }
  }

  @Override
  public void computeScroll() {
    super.computeScroll();
    if (mScroller.isFinished()){
      return;
    }
    if (mScroller.computeScrollOffset()){
      // 边界处理
      mRect.top = mScroller.getCurrY();
      mRect.bottom = mScroller.getCurrY() + (int) (viewHeight / mScale);
      mRect.left = mScroller.getCurrX();
      mRect.right = mScroller.getCurrX() + (int) (viewWidth / mScale);

      boundary();

      postInvalidate();
    }
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    return mGestureDetector.onTouchEvent(event);
  }
}
