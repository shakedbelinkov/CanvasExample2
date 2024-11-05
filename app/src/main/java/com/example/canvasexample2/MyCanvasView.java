package com.example.canvasexample2;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

public class MyCanvasView extends View {
    private Paint mPaint;
    private Path mPath;
    private int mDrawColor;
    private int mBackgroundColor;
    private Canvas mExtraCanvas;
    private Bitmap mExtraBitmap;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    MyCanvasView(Context context, Bitmap mExtraBitmap) {
        this(context, null, mExtraBitmap);
    }

    public MyCanvasView(Context context)
    {
        super(context);
    }

    public MyCanvasView(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
        this.mExtraBitmap = mExtraBitmap;
        mBackgroundColor = ResourcesCompat.getColor(getResources(),R.color.white_board, null);
        mDrawColor = ResourcesCompat.getColor(getResources(),R.color.black, null);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(mDrawColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }
    public MyCanvasView(Context context, AttributeSet attributeSet, Bitmap mExtraBitmap) {
        super(context);
        this.mExtraBitmap = mExtraBitmap;
        mBackgroundColor = ResourcesCompat.getColor(getResources(),R.color.white_board, null);
        mDrawColor = ResourcesCompat.getColor(getResources(),R.color.black, null);

        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(mDrawColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }
    @Override
    protected void onSizeChanged(int width, int height,int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        mExtraBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        mExtraCanvas = new Canvas(mExtraBitmap);
        mExtraCanvas.drawColor(mBackgroundColor);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mExtraBitmap, 0, 0, null);
    }




    public void changeBackgroundColor(int color)
    {
        mExtraCanvas = new Canvas(mExtraBitmap);
        mExtraCanvas.drawColor(color);
        invalidate();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                break;
            default:
        }
        return true;
    }
    private void touchStart(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
            mExtraCanvas.drawPath(mPath, mPaint);
        }
    }
    private void touchUp() {
        mPath.reset();
    }
    public void changeBrushColor(int color)
    {
        mDrawColor = color;
        mPaint.setColor(mDrawColor);
    }
    public void eraser()
    {
        mDrawColor = mBackgroundColor;
        mPaint.setColor(mDrawColor);
    }
    public void delete(){
        changeBackgroundColor(mBackgroundColor);
    }
}
