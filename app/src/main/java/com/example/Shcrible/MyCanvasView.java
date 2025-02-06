package com.example.Shcrible;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private int brushSize=12;
    private int typePlayer=1;
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
        if (typePlayer==1) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //when the moving start-start the path on x,y
                    touchStart(x, y, 1);
                    break;
                case MotionEvent.ACTION_MOVE:
                    //when the brush move-start a path to x,y
                    touchMove(x, y, 1);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    //when the moving stop-end the painting
                    touchUp(1);
                    break;
                default:
            }
            return true;
        }
        return true;
    }


    public void drawFromDB(Draw[] draws)
    {
        if(draws==null)
            return;
        for (int i=0;i<draws.length;i++)
        {
            changeBrushColor(draws[i].getColor());
            changeBrushSize(draws[i].getBrushSize());
            if (i==0&& draws[i].getType()!=Consts.START_DRAW)//check the first one if it the continue of the last arr
            {
                if (draws[i].getType()==Consts.DELETE_ALL)
                    delete();
                else
                    touchStart(draws[i].getInitialX(),draws[i].getInitialY(),2);
            }
            if (draws[i].getType()==Consts.START_DRAW)
                touchStart(draws[i].getInitialX(),draws[i].getInitialY(),2);
           if (draws[i].getType()==Consts.MOVE_DRAW)
            touchMove(draws[i].getInitialX(),draws[i].getInitialY(),2);
           if (draws[i].getType()==Consts.END_DRAW)
                touchUp(2);
            if (draws[i].getType()==Consts.DELETE_ALL)
                delete();
           else if (i==draws.length-1)//if the last draw isn't end draw
           {
               if (draws[i].getType()==Consts.DELETE_ALL)
                   delete();
               else
                   touchUp(2);
           }
        }

    }
    private void touchStart(float x, float y,int type) {
        //start the path on x,y
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
        if (type==1) {
            Draw d = new Draw(x, y, 0, 0, Consts.START_DRAW, mDrawColor, brushSize);
            updateCounter++;
            draws.add(d);
            Log.d("DRAWS", draws.toString());
        }

    }



    private void touchMove(float x, float y,int type) {
        //move the path to x,y
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            Draw d=new Draw(mX,mY,(x + mX)/2,(y + mY)/2,Consts.MOVE_DRAW,mDrawColor,brushSize);
            mX = x;
            mY = y;
            mExtraCanvas.drawPath(mPath, mPaint);
            if (type==1) {
                draws.add(d);
                Log.d("DRAWS", draws.toString());
                updateCounter++;
            }
        }
    }
    private void touchUp(int type) {
        Draw d=new Draw(0,0,0,0,Consts.END_DRAW,mDrawColor,brushSize);
        if (type==1) {
            updateCounter++;
            draws.add(d);
            Log.d("DRAWS", draws.toString());
        }

        ArrayList<Draw> a = getArrayList();
        if (a== null || a.size() == 0)
            return;
        db.addDraw(a);

        //db.addDraw((ArrayList<Draw>) draws.subList(lastUpdate,updateCounter),uidRef);

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
        Draw d=new Draw(0,0,0,0,Consts.DELETE_ALL,mBackgroundColor,brushSize);
        draws.add(d);
        updateCounter++;
    }
    public void changeBrushSize(int size)
            //change the size of the brush
    {
        brushSize=size;
        mPaint.setStrokeWidth(size);
    }

    @Override
    public void onDrawComplete(boolean s) {

    }
    public ArrayList<Draw>getArrayList()
    {
        //lastUpdate=updateCounter;
        ArrayList<Draw> arrToDB = new ArrayList<Draw> (draws.subList(lastUpdate,updateCounter)) ;
        lastUpdate = updateCounter;
        //ArrayList<Draw> arrToDB=new ArrayList<>();
        //arrToDB.add(new Draw((float) 12.06,(float) 12.06,0,0,0, Color.BLUE,12));
        //arrToDB.add(new Draw((float) 12.06,(float) 12.06,(float) 25.6789,(float) 25.6789,1, Color.BLUE,12));
        //arrToDB.add(new Draw(0,0,0,0,2, Color.BLUE,12));
        return arrToDB;
    }
    public void ChangePlayerType(int type)
    {
        //1-draw
        //2-not draw
        this.typePlayer=type;
    }
}
