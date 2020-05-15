package com.example.qaz.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyImageView extends androidx.appcompat.widget.AppCompatImageView{
    List<Paint> mPaint = new ArrayList<>();
    int nowPaint = 0;
    Bitmap mBitmap;
    Canvas mCanvas;
    List<Line> mLine = new ArrayList<>();
    Paint mBitmapPaint;
    int id_d;

    public MyImageView(Context context) {
        super(context);
        init_all();
    }
    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init_all();
    }

    public void init_all(){
        for (int i = 0; i < 4; i++) {
            this.mPaint.add(new Paint());
            this.mPaint.get(i).setAntiAlias(true);
            this.mPaint.get(i).setDither(true);
            this.mPaint.get(i).setStyle(Paint.Style.STROKE);
            this.mPaint.get(i).setStrokeJoin(Paint.Join.ROUND);
            this.mPaint.get(i).setStrokeCap(Paint.Cap.ROUND);
            this.mPaint.get(i).setStrokeWidth(20);
        }
        this.mPaint.get(0).setColor(Color.BLUE);
        this.mPaint.get(1).setColor(Color.GREEN);
        this.mPaint.get(2).setColor(Color.RED);
        this.mPaint.get(3).setColor(Color.YELLOW);
        this.mBitmapPaint = new Paint();
        this.mBitmapPaint.setColor(Color.RED);
    }



    public void setId_d(int id) {
        this.id_d = id;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public void setmBitmap(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public void setnowPaint(String color){
        switch (color){
            case "red": this.nowPaint = 2; break;
            case "green": this.nowPaint = 1; break;
            case "yellow": this.nowPaint = 3; break;
            case "blue": this.nowPaint = 0; break;
        }
    }

    public void canvasDrawPath(){
        for (int i = 0; i < this.mLine.size(); i++){
            Line line = this.mLine.get(i);
            this.mCanvas.drawPath(line.path, line.paint);
        }
    }

    public void setmCanvas() {
        this.mCanvas = new Canvas(this.mBitmap);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Bitmap workingBitmap = Bitmap.createScaledBitmap(this.mBitmap, w, h, true);
        this.mBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);
        //mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.mCanvas = new Canvas(this.mBitmap);
    }

    @Override
    public void draw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.draw(canvas);
        canvas.drawBitmap(this.mBitmap, 0, 0, this.mBitmapPaint);
        for (int i = 0; i < this.mLine.size(); i++){
            Line line = this.mLine.get(i);
            this.mCanvas.drawPath(line.path, line.paint);
        }
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        //mPath.reset();
        Line line = new Line();
        line.paint = this.mPaint.get(this.nowPaint);
        line.path = new Path();
        line.path.moveTo(x, y);
        this.mLine.add(line);
        this.mX = x;
        this.mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            this.mLine.get(this.mLine.size()-1).path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            this.mX = x;
            this.mY = y;
        }
    }
    private void touch_up() {
        this.mLine.get(this.mLine.size()-1).path.lineTo(mX, mY);
        for (int i = 0; i < this.mLine.size(); i++){
            Line line = this.mLine.get(i);
            this.mCanvas.drawPath(line.path, line.paint);
        }
        //mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

}