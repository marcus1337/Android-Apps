package com.fullrune.areashifter.VIEW;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.fullrune.areashifter.CONTROLLER.Event;
import com.fullrune.areashifter.MODEL.Pieces.Line;
import com.fullrune.areashifter.MODEL.Model;
import com.fullrune.areashifter.MODEL.Pieces.Point;

import java.util.ArrayList;

/**
 * Created by Marcus on 2017-04-25.
 */

public class BitFrame {
    private Bitmap bitmap;
    private Paint pa;
    private Rect bitRect;
    private Model model;
    private Canvas canvas;

    int x, y;

    public Rect getRect(){
        return bitRect;
    }

    public BitFrame(int x, int y, int w, int h, Model model){
        this.x = x;
        this.y = y;
        this.model = model;
        pa = new Paint();
        pa.setColor(Color.BLUE);
        pa.setStyle(Paint.Style.STROKE);
        pa.setStrokeWidth(2);

        bitmap = Bitmap.createBitmap(model.getWidth(), model.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        bitRect = new Rect(x,y,w,h);

        ArrayList<Line> lines = model.getSafeLines();
        for(Line l: lines) {
            canvas.drawLine(l.getX(), l.getY(), l.getEndx(), l.getEndy(), pa);
        }

    }

    public void prepareNewLevel(){
        bitmap.eraseColor(Color.TRANSPARENT);
        pa.setColor(Color.BLUE);
        pa.setStyle(Paint.Style.STROKE);
        pa.setStrokeWidth(2);
        for(Line l: model.getSafeLines()) {
            canvas.drawLine(l.getX(), l.getY(), l.getEndx(), l.getEndy(), pa);
        }
    }

    public void notify(Event event){
        if(event.getEvent() == Event.EV.LOADGAME){
            int w = model.getWidth();
            int h = model.getHeight();
            int[][] map = model.getMap();
            for(int i = 0; i < w; i++){
                for(int j = 0; j < h; j++){
                    if(map[i][j] == 3){
                        pa.setColor(Color.BLACK);
                        canvas.drawPoint(i,j, pa);
                    }
                    else if(map[i][j]==2){
                        pa.setColor(Color.BLUE);
                        canvas.drawPoint(i,j, pa);
                    }
                }
            }

        }

    }

    public void draw(Canvas c){

        pa.setColor(Color.BLUE);
        pa.setStyle(Paint.Style.STROKE);
        pa.setStrokeWidth(1);
        for(Line l: model.getSafeLines()) {
            canvas.drawLine(l.getX(), l.getY(), l.getEndx(), l.getEndy(), pa);
        }
        pa.setColor(Color.BLACK);
        pa.setStyle(Paint.Style.FILL);
        pa.setStrokeWidth(1);

        ArrayList<Point> points = model.getFilledPoints();
        for(Point p: points) {
            canvas.drawPoint(p.getX(),p.getY(), pa);
        }
        points.clear();

        c.drawBitmap(bitmap,null,bitRect, null);
    }

}
