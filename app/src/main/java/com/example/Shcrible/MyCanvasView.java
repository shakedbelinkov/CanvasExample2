package com.example.Shcrible;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class MyCanvasView extends View implements DBDraw.AddDrawComplete {
    private Paint mPaint;
    private Path mPath;
    private int mDrawColor;
    private int mBackgroundColor;
    private Canvas mExtraCanvas;
    private Bitmap mExtraBitmap;
    private String uidRef;
    private float mX, mY;
    private int updateCounter=0;
    private int lastUpdate=0;
    private static final float TOUCH_TOLERANCE = 4;//the pixel gap from which i will start drawing new path
    private ArrayList<Draw> draws=new ArrayList<>();// arr of draws for the db
    private DBDraw db=new DBDraw(this);
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
        //create new path and paint and set style and stroke
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
        //create new path and paint and set style and stroke
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
                //when the moving start-start the path on x,y
                touchStart(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                //when the brush move-start a path to x,y
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //when the moving stop-end the painting
                touchUp();
                break;
            default:
        }
        return true;
    }


    public void drawFromDB(ArrayList<Draw> draws)
    {
        for (int i=0;i<draws.size()-1;i++)
        {
           if (draws.get(i).getType()==Consts.MOVE_DRAW)
            touchStart(draws.get(i).getInitialX(),draws.get(i).getInitialY());
           if (draws.get(i).getType()==Consts.END_DRAW)
                touchMove(draws.get(i).getEndX(),draws.get(i).getEndY());
        }

    }
    private void touchStart(float x, float y) {
        //start the path on x,y
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        Draw d=new Draw(x,y,0,0,Consts.START_DRAW,mDrawColor);
        updateCounter++;
        draws.add(d);
    }



    private void touchMove(float x, float y) {
        //move the path to x,y
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            Draw d=new Draw(mX,mY,(x + mX)/2,(y + mY)/2,Consts.MOVE_DRAW,mDrawColor);
            updateCounter++;
            mX = x;
            mY = y;
            mExtraCanvas.drawPath(mPath, mPaint);
            draws.add(d);
        }
    }
    private void touchUp() {
        Draw d=new Draw(0,0,0,0,Consts.END_DRAW,mDrawColor);
        updateCounter++;
        draws.add(d);
        //db.addDraw((ArrayList<Draw>) draws.subList(lastUpdate,updateCounter),uidRef);
        lastUpdate=updateCounter;
        mPath.reset();
    }
    public void changeBrushColor(int color)
            //change the color of the brush to "color"
    {
        mDrawColor = color;
        mPaint.setColor(mDrawColor);
    }
    public void eraser()
            //change the color of the brush to the color of the background(eraser)
    {
        mDrawColor = mBackgroundColor;
        mPaint.setColor(mDrawColor);
    }
    public void delete()
            //put a layer of color (background color)
    {
        changeBackgroundColor(mBackgroundColor);
    }
    public void changeBrushSize(int size)
            //change the size of the brush
    {
        mPaint.setStrokeWidth(size);
    }

    @Override
    public void onDrawComplete(boolean s) {

    }
    public ArrayList<Draw>getArrayList()
    {
        return (ArrayList<Draw>) draws.subList(lastUpdate,updateCounter);
    }
}
