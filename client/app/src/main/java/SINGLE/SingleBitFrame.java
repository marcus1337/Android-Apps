package SINGLE;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;

import STATES.GameState;
import STATES.Player;

public class SingleBitFrame  {

    private Bitmap bitmap;
    private Paint pa;
    private Rect bitRect;
    private Canvas canvas;

    int x, y;

    public Rect getRect(){
        return bitRect;
    }

    private int cols, rows;

    public SingleBitFrame(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        pa = new Paint();
        pa.setColor(Color.BLUE);
        pa.setStyle(Paint.Style.STROKE);
        pa.setStrokeWidth(2);
        cols = 34;
        rows = 20;
        bitmap = Bitmap.createBitmap(cols, rows, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        bitRect = new Rect(x,y,w,h);
    }

    public int getCols(){
        return cols;
    }

    public int getRows(){
        return rows;
    }

    public void notify(SingleGame gameState, int playNum){
        SingPlayer player = gameState.getPlayer();
        ArrayList<Apple> apples = gameState.getApples();
        pa.setStrokeWidth(1f);
        pa.setColor(Color.RED);
  //  System.out.println("hello wurold");
        for(Pair<Integer, Integer> par : player.getPositions()){
            int px = par.first;
            int py = par.second;
            canvas.drawPoint(px, py, pa);
        }
        pa.setColor(Color.YELLOW);

        for(Apple apple : apples){
            if(!apple.eaten){
                canvas.drawPoint(apple.x, apple.y, pa);
            }
        }

        Pair<Integer, Integer> par = player.getDeletedPos();
        pa.setColor(Color.DKGRAY);
        if(par != null)
            canvas.drawPoint(par.first, par.second, pa);


    }

    public void draw(Canvas c){
        c.drawBitmap(bitmap,null,bitRect, null);
    }

}
