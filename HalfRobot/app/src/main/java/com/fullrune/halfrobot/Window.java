package com.fullrune.halfrobot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.fullrune.halfrobot.LOGIC.Model;

/**
 * Created by Marcus on 2017-09-21.
 */

public class Window extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder surfaceHolder;
    private Paint paint;

    public Window(Context context) {
        super(context);
        init();
    }

    public Window(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Window(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    void init(){
        getHolder().addCallback(this);
        surfaceHolder = getHolder();
        paint = new Paint();
    }

    void draw(){
        Canvas c = surfaceHolder.lockCanvas();
        if(c != null) {
            draw2(c);
            surfaceHolder.unlockCanvasAndPost(c);
        }
    }

    void drawModel(Model model){
        Canvas c = surfaceHolder.lockCanvas();
        if(c != null) {
            model.render(c);
            surfaceHolder.unlockCanvasAndPost(c);
        }
    }

    boolean swapper = false;

    private void draw2(Canvas c){
        swapper = !swapper;

        if(swapper)
        paint.setColor(Color.GREEN);
        else
            paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        c.drawRect(0,0,100,100,paint);
        c.drawARGB(0, 0, 0, 0);
        c.drawCircle(100, 100, 30, paint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
