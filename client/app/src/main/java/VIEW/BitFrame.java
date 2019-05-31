package VIEW;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import STATES.Player;

public class BitFrame  {

    private Bitmap bitmap;
    private Paint pa;
    private Rect bitRect;
    private Canvas canvas;

    int x, y;

    public Rect getRect(){
        return bitRect;
    }

    private int cols, rows;

    public BitFrame(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        pa = new Paint();
        pa.setColor(Color.BLUE);
        pa.setStyle(Paint.Style.STROKE);
        pa.setStrokeWidth(2);
        cols = 50;
        rows = 30;
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

    public void notify(Player player, int playNum){
        pa.setStrokeWidth(1f);
        if(playNum == 0){
            pa.setColor(Color.BLUE);
        }
        else{
            pa.setColor(Color.RED);
        }
        int px = player.x;
        int py = player.y;
        canvas.drawPoint(px, py, pa);
        ArrayList<Player.Move> moves = player.recentMoves;

        for(Player.Move m : moves){
            if(m == Player.Move.UP){
                py--;
            }
            if(m == Player.Move.DOWN){
                py++;
            }
            if(m == Player.Move.LEFT){
                px++;
            }
            if(m == Player.Move.RIGHT){
                px--;
            }
         //  canvas.drawPoint(px, py, pa);
        }

    }

    public void draw(Canvas c){
        c.drawBitmap(bitmap,null,bitRect, null);
    }

}
