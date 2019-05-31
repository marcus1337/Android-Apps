package com.fullrune.areashifter.VIEW;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.fullrune.areashifter.CONTROLLER.Event;
import com.fullrune.areashifter.MODEL.Pieces.Enemy;
import com.fullrune.areashifter.MODEL.Pieces.Line;
import com.fullrune.areashifter.MODEL.Model;
import com.fullrune.areashifter.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Marcus on 2017-04-25.
 */

public class Board extends SurfaceView implements SurfaceHolder.Callback {

    private Paint paint;
    private SurfaceHolder surfaceHolder;
   // private FrameShape frameShape;
    private Model model;
    private boolean readycheck;
    private int extX, extY;

    private ArrayList<Bitmap> ballImg;
    private Rect ballRect;

    private RespawnNoMove respawnTimer;
    private Typeface typeface;

    private boolean canDraw;

    public Model getModel(){
        return model;
    }

    public void setModel(Model model){
        this.model = model;
    }

    public Board(Context context) {
        super(context);
        init();
    }

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Board(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setCanDraw(boolean canDraw){
        this.canDraw = canDraw;
    }

    private void init(){
        canDraw = false;
        readycheck = false;
        ballImg = new ArrayList<Bitmap>();
        Bitmap redBall = BitmapFactory.decodeResource(getResources(), R.drawable.ball_red);
        Bitmap blueBall = BitmapFactory.decodeResource(getResources(), R.drawable.ball_blue);
        Bitmap grayBall = BitmapFactory.decodeResource(getResources(), R.drawable.ball_gray);
        Bitmap yellowBall = BitmapFactory.decodeResource(getResources(), R.drawable.ball_yellow);
        Bitmap pinkBall = BitmapFactory.decodeResource(getResources(), R.drawable.ball_pink);
        ballImg.add(redBall);
        ballImg.add(blueBall);
        ballImg.add(grayBall);
        ballImg.add(yellowBall);
        ballImg.add(pinkBall);
        normalColor = true;
        playImg = 0;
        timer = new Timer();

        typeface = Typeface.createFromAsset(getContext().getAssets(),  "fonts/PressStart2P.ttf");
        paint = new Paint();
        paint.setTypeface(typeface);
        getHolder().addCallback(this);
        surfaceHolder = getHolder();

    }


    private BitFrame bitFrame;
    private boolean normalColor;
    private int playImg;
    private Timer timer;

    private class RespawnNoMove extends TimerTask {
        private boolean hasStarted = false;
        public void run() {
            hasStarted = true;
            if(normalColor){
                playImg = 0;
            }else{
                playImg = 1;
            }
            normalColor = ! normalColor;
        }

        public boolean isStarted(){
            return hasStarted;
        }
    }

    private String tmpScore = "+ ";
    private long textTmpTime = 0;



    private static final int FADE_MILLISECONDS = 15000; // 3 second fade effect
    private static final int FADE_STEP = 120;          // 120ms refresh
    private static final int ALPHA_STEP = 255 / (FADE_MILLISECONDS / FADE_STEP);
    private Paint alphaPaint = new Paint();
    private int currentAlpha = 255;



    private void doDraw(Canvas c){
        c.drawARGB(255, 255, 255, 255);
        //frameShape.draw(c);

        paint.setColor(Color.BLACK);

        paint.setTextSize(28);
        int tempScore = model.getExtraScore();
        if(tempScore != 0){
            textTmpTime = System.currentTimeMillis();
            tmpScore = " +" + tempScore;
        }
        if(System.currentTimeMillis() - textTmpTime > 1111)
            c.drawText("Score: " + model.getTotalScore(), extX,extY-10,paint);
        else
            c.drawText("Score: " + model.getTotalScore() + tmpScore, extX,extY-10,paint);
        c.drawText(model.percTaken() +"%", bitRect.right-45,extY-10, paint );

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);

        bitmap.eraseColor(Color.TRANSPARENT);

        ArrayList<Line> lines = model.getPlayer().getRiskLines();
        for(Line l : lines){
            canvas.drawLine(l.getX(), l.getY(), l.getEndx(), l.getEndy(), paint);
          //  c.drawLine(l.getX()+extX, l.getY()+extY, l.getEndx()+extX, l.getEndy()+extY, paint);
        }

        bitFrame.draw(c);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.DKGRAY);
        ArrayList<Enemy> enemies = model.getEnemies();
        for(Enemy u : enemies){
            canvas.drawCircle(u.getX(), u.getY(), u.getRadius(), paint);
        }
        paint.setColor(Color.RED);
      //  canvas.drawCircle(model.getPlayer().getX(), model.getPlayer().getY(), model.getPlayer().getRadius(), paint);
        int x = model.getPlayer().getX();
        int y = model.getPlayer().getY();
        int r = model.getPlayer().getRadius();

        ballRect.set(x-r,y-r, x+r, y+r );
        canvas.drawBitmap(ballImg.get(playImg), null, ballRect, null);

       /* if (currentAlpha < 255) {
            canvas.drawBitmap(ballImg.get(playImg), null, ballRect, alphaPaint);
            alphaPaint.setAlpha(currentAlpha);
            currentAlpha += ALPHA_STEP;

        } else {
            currentAlpha = 0;
            alphaPaint.setAlpha(currentAlpha);
        }*/

        c.drawBitmap(bitmap,null,bitRect, null);

        ballRect.set(extX, bitRect.bottom+5, extX+30, bitRect.bottom +35 );
        paint.setColor(Color.BLACK);
        c.drawText("x "+model.getPlayer().getLives(),ballRect.right+15,ballRect.bottom, paint );
        c.drawText("Goal:"+model.getGoal()+"%",bitRect.right-212,bitRect.bottom+35, paint);
        c.drawBitmap(ballImg.get(0), null, ballRect, null);

    }

    public void shutDown(){
        if(respawnTimer != null)
            respawnTimer.cancel();
        if(timer != null)
            timer.cancel();
    }

    public void notify(Event event){

        if(event.getEvent() == Event.EV.LOADGAME){
            bitFrame.notify(event);
        }
        else if(event.getEvent() == Event.EV.NYLEVEL)
        {
           // Log.i("TEST33322", "HELO123");
            model.beginNextLevel();
            if(model.getLevel() != 1)
                bitFrame.prepareNewLevel();
        }
        else if(event.getEvent() == Event.EV.RESTART){
            model.restart();
            bitFrame.prepareNewLevel();
        }
        else if(event.getEvent() == Event.EV.RESUME){
            model.setPaused(false);

        }

    }

    public void drawGame(){
        if(!canDraw)
            return;

        if(!model.getPlayer().isAlive()){
            if(model.getPlayer().isMovable()){
                if(respawnTimer != null){
                    respawnTimer.cancel();
                    timer.cancel();
                    respawnTimer = null;
                    timer = null;
                    playImg = 3;
                }
            }else
            if(respawnTimer == null){
                timer = new Timer();
                respawnTimer = new RespawnNoMove();
                timer.schedule(respawnTimer, 0, 300);
            }
        }
        else
            playImg = 0;

        Canvas c = surfaceHolder.lockCanvas();
        if(c != null) {
            doDraw(c);
            surfaceHolder.unlockCanvasAndPost(c);
        }
    }


    public boolean isReady(){
        return readycheck;
    }

   // private float rH, rW;
    private Bitmap bitmap;
    private Rect bitRect;
    private Canvas canvas;

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //int tmpW = (int)(getWidth()*0.95f);
        //int tmpH = (int)(getHeight()*0.95f);
        if(model != null && bitFrame != null){

            return;
        }

        int tmpH = (int) (getHeight()*0.6f); //0.562f
        int tmpW = tmpH * 2;
        //Log.i("WOLLO3344", "W: " + tmpW + " H: " + tmpH + " ratio1: " + tmpH/500f + " ratio2: " + tmpW/1000f);
       // rH = tmpH / (float)modH;
      //  rW = tmpW / (float) modW;
       // Log.i("WOLLO3344", "W: " + tmpW + " H: " + tmpH + " ratio1: " + rH + " ratio2: " + rW);

        extX = (getWidth()-tmpW)/2;//(int)(getWidth()*0.03f);
        extY = (getHeight()-tmpH)/5; //(int)(getHeight()*0.03f);

        bitFrame = new BitFrame(extX,extY,tmpW + extX,tmpH + extY, model);
        bitRect = bitFrame.getRect();
        bitmap = Bitmap.createBitmap(model.getWidth(), model.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        /*for(int i = 0; i < ballImg.size(); i++){
            Bitmap bitmap = getResizedBitmap(ballImg.get(i), model.getPlayer().getRadius(), model.getPlayer().getRadius());
            ballImg.set(i, bitmap);
        }*/
        ballRect = new Rect(0,0,model.getPlayer().getRadius(),model.getPlayer().getRadius());

        readycheck = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
