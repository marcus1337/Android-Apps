package com.fullrune.areashiftertwo.VIEW;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.renderscript.RenderScript;
import android.util.Log;


import com.fullrune.areashiftertwo.MODEL.Model;
import com.fullrune.areashiftertwo.MODEL.Pieces.Point;
import com.fullrune.areashiftertwo.R;

import java.util.ArrayList;

public class BitFrame  {

    private Bitmap bitmap;
    private Bitmap linesBitmap;

    private Paint pa, linesPaint, linesPaintRemove;
    private Rect bitRect;
    private Canvas canvas, lineCanvas;
    private Context context;
    private Model model;
    //Bitmap mutableBitmap;
    private int trackCounter;

   // private boolean initializationDone;

    public void init(Model model){
        this.model = model;
        BoardHelp.setEmptyColor(2);
        initView(model);
        setBoardColors();
    }

    private void setBoardColors(){
        int rows = model.getMapHeight();
        int cols = model.getMapWidth();
        int[][] map = model.getMap();
        for(int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                BoardHelp.setPaintColorByMap(map,i,j,pa);
                canvas.drawPoint(i,j,pa);
            }
        }
    }

    private void setBoardColorsOptimized(){
        int rows = model.getBoxRight()+1;
        int cols = model.getBoxBottom()+1;
        if(rows > model.getMapHeight())
            rows = model.getMapHeight();
        if(cols > model.getMapWidth())
            cols = model.getMapWidth();
        int[][] map = model.getMap();

        for(int i = model.getBoxLeft(); i < rows; i++){
            for (int j = model.getBoxTop(); j < cols; j++){
                BoardHelp.setPaintColorByMap(map,i,j,pa);
                canvas.drawPoint(i,j,pa);
            }
        }
    }

    public Rect getRect(){
        return bitRect;
    }

    public void setFrame(int width, int height, int offX, int offY){
        bitRect = new Rect(offX, offY, width+offX, height+offX-offY);
       // Log.i("TEST5" , "ofx: " + offX + " ofy: " + offY + " w: " + width + " h: " + height);
    }

    private void initView(Model model){
        bitmap = Bitmap.createBitmap(model.getMapWidth(), model.getMapHeight(), Bitmap.Config.RGB_565);
        pa = new Paint();
        canvas = new Canvas(bitmap);

        linesPaint = new Paint();
        linesPaintRemove = new Paint();
        linesPaint.setColor(Color.YELLOW);
        linesPaintRemove.setColor(Color.TRANSPARENT);
    }

    public BitFrame(Context context){

        //linesBitmap = Bitmap.createBitmap(400,400, Bitmap.Config.ARGB_8888);

      //  lineCanvas = new Canvas(linesBitmap);
        //linesBitmap.eraseColor(Color.TRANSPARENT);

        //linesPaint.setStrokeWidth(5.0f);

       // lineCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
       // linesPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
      //  bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo);
       // mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

    }

    private void removeYellowLines(){

    }

    private boolean delayBoardUpdate;

    public void draw(Canvas c){

        if(model.isBoardChanged() && delayBoardUpdate){
            if(!model.isOnlyLineChange())
                setBoardColorsOptimized();
            model.setBoardChanged(false);
            delayBoardUpdate = false;
            trackCounter = 0;
            for(Point p : model.getOldRiskLines()){
                BoardHelp.setPaintColorByMap(model.getMap(),p.x,p.y,linesPaintRemove);
                canvas.drawPoint(p.x,p.y,linesPaintRemove);
            }
            model.getOldRiskLines().clear();
        }
        if(model.isBoardChanged()){
            delayBoardUpdate = true;
        }

        if(model.getPlayer().isRiskFailed()){
            trackCounter = 0;
            for(Point p : model.getOldRiskLines()){
                BoardHelp.setPaintColorByMap(model.getMap(),p.x,p.y,linesPaintRemove);
                canvas.drawPoint(p.x,p.y,linesPaintRemove);
            }
            model.getOldRiskLines().clear();
        }

        ArrayList<Point> track = model.getPlayer().getLineTrack();
        for(int i = trackCounter; i < track.size(); i++){
            Point p = track.get(i);
            canvas.drawPoint(p.x, p.y, linesPaint);
        }
        if(!track.isEmpty())
            trackCounter = track.size()-1;

        c.drawBitmap(bitmap,null,bitRect, null);

       // c.drawBitmap(linesBitmap, null, bitRect, null);
    }

}
