package com.fullrune.areashiftertwo.VIEW;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.fullrune.areashiftertwo.MODEL.Model;
import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Player;
import com.fullrune.areashiftertwo.MODEL.Pieces.Unit;
import com.fullrune.areashiftertwo.R;

import java.util.ArrayList;

public class Board extends SurfaceView implements SurfaceHolder.Callback {

    private Context context;
    private boolean readycheck;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private boolean initDone;
   // private TextBoard textBoard;
    private Model model;

    private Typeface typeface1;

    private ArrayList<Bitmap> ballImg;
    private Rect ballRect;

    private boolean narrowScreen;

    private GameOverView gameOverView;

    public boolean isEndAnimationDone(){
        return gameOverView.isAnimationGameOverDone() || gameOverView.isAnimationWinDone();
    }

    public Board(Context context) {
        super(context);
        init(context);
    }

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Board(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private BitFrame bitFrame;

    public void init(Context context){
        this.context = context;
        gameOverView = new GameOverView(this, context);
        narrowScreen = false;
        getHolder().addCallback(this);
        surfaceHolder = getHolder();
        paint = new Paint();
        initDone = false;

        ballImg = new ArrayList<Bitmap>();
        Drawable d = getResources().getDrawable(R.drawable.ball_red);
        Bitmap redBall = BoardHelp.drawableToBitmap(d); //BitmapFactory.decodeResource(getResources(), R.drawable.ball_red);
        d = getResources().getDrawable(R.drawable.ball_blue);
        Bitmap blueBall = BoardHelp.drawableToBitmap(d); //BitmapFactory.decodeResource(getResources(), R.drawable.ball_blue);
        d = getResources().getDrawable(R.drawable.ball_gray);
        Bitmap grayBall = BoardHelp.drawableToBitmap(d); //BitmapFactory.decodeResource(getResources(), R.drawable.ball_gray);
        d = getResources().getDrawable(R.drawable.ball_yellow);
        Bitmap yellowBall = BoardHelp.drawableToBitmap(d); //BitmapFactory.decodeResource(getResources(), R.drawable.ball_yellow);
        d = getResources().getDrawable(R.drawable.ball_pink);
        Bitmap pinkBall = BoardHelp.drawableToBitmap(d); //BitmapFactory.decodeResource(getResources(), R.drawable.ball_pink);
        d = getResources().getDrawable(R.drawable.ball_black);
        Bitmap blackBall = BoardHelp.drawableToBitmap(d); //BitmapFactory.decodeResource(getResources(), R.drawable.ball_black);
        ballImg.add(redBall);
        ballImg.add(blueBall);
        ballImg.add(grayBall);
        ballImg.add(yellowBall);
        ballImg.add(pinkBall);
        ballImg.add(blackBall);

        typeface1 = Typeface.createFromAsset(getContext().getAssets(),  "fonts/PressStart2P.ttf");
       // paint.setTypeface(typeface1);
    }

    public void init2(Model model){
        this.model = model;
        bitFrame.init(model);
        gameOverView.setLvl(model.getLevel());
      //  textBoard.init(model, context);
        initDone = true;
    }

    private void doDraw(Canvas c){
    if(!readycheck || !initDone)
        return;
       // if(gameState != null && gameState.isGameStarted()){
      //  }
        Paint paint2 = new Paint();
        paint2.setColor(Color.DKGRAY);
        BoardHelp.setPaintColorByAny(paint2, 4);
        paint2.setStyle(Paint.Style.FILL);
        c.drawPaint(paint2);

        bitFrame.draw(c);

        //Rect gameBounds = bitFrame.getRect();
        drawPlayer(c);
        for(Unit u : model.getEnemies()){
            drawUnit(c, u, 5);
        }


        paint.setTextSize(28);
        paint.setColor(Color.YELLOW);

        //c.drawText("Hello worlds test: " + model.getScore(), 50, 50, paint);

        Rect bitRect = bitFrame.getRect();
        ballRect.set(offsetRecX, bitRect.bottom+5, offsetRecX+30, bitRect.bottom +35 );
        c.drawText("Score: " + model.getScore(), offsetRecX,offsetRecY-10,paint);
        c.drawText(model.percTaken() +"%", bitRect.right-55,offsetRecY-10, paint );

        if(model.getPlayer().getLives() >= 0) {
            c.drawText("x "+model.getPlayer().getLives(),ballRect.right+15,ballRect.bottom, paint );
            c.drawBitmap(ballImg.get(0), null, ballRect, null);
        }
        c.drawText("Goal:"+model.getScoreProcentGoal()+"%",bitRect.right-140,bitRect.bottom+35, paint);
    }

    private boolean playImgSwitch;
    private long playImgSwitchTimeOld;

    private void drawPlayer(Canvas c){
        Player player = model.getPlayer();
        int imgtyp = 0;
        if(!player.isAlive()){
            imgtyp = 3;

            long currentTime = model.getGameTicks();

            if(player.isMovable()){
                long limitTimeDiff = 10;
                long diffTime = currentTime - playImgSwitchTimeOld;
                if(diffTime <= limitTimeDiff/2){
                    imgtyp = 2;
                }else{
                    imgtyp = 3;
                }

                if(diffTime >= limitTimeDiff){
                    playImgSwitchTimeOld = model.getGameTicks();
                }
            }
        }
        //Log.i("what123", "HELLO!!");
        drawUnit(c, player, imgtyp);
    }

    public void drawUnit(Canvas c, Unit unit, int imgtyp){
        int px = unit.getX();
        int py = unit.getY();
        int r = unit.getRadius();
        float scale = ((float)bitFrame.getRect().width())/model.getMapWidth();

        px = Math.round(px*scale);
        py = Math.round(py*scale);
        if(narrowScreen){
            float scaleY = ((float)bitFrame.getRect().height())/model.getMapHeight();
            py = Math.round(unit.getY()*scaleY);
        }
        px += bitFrame.getRect().left;
        py += bitFrame.getRect().top;
        r = Math.round(r*scale);
        ballRect = new Rect(px-r,py-r, px+r, py+r );
        c.drawBitmap(ballImg.get(imgtyp), null, ballRect, null);
    }

    public void drawGame(){
        Canvas c = surfaceHolder.lockCanvas();
        if(c != null) {
            doDraw(c);
            if(model.isGameLost()){
                gameOverView.drawGameLost(c);
            }
            if(model.isGameWon()){
                gameOverView.drawGameWon(c);
            }
            surfaceHolder.unlockCanvasAndPost(c);
        }
    }


    private int offsetRecX = 0, offsetRecY = 0;

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        if(readycheck)
            return;

        float localratio = ((float)getWidth())/((float)getHeight());
        float origW = 500.0f;
        float origH = 500.0f;
        float targetratio = origW / origH;
        int nowWidth, nowHeight;
        //int offsetRecX = 0, offsetRecY = 0;

        int spaceTop = (int) (0.05f*getHeight());
        int spaceMid = (int) (0.7f*getHeight());
        int spaceLow = (int) (0.25f*getHeight());

        bitFrame = new BitFrame(context);

        int bitwidth = 0;
        int bitheight = 0;
        if(spaceMid > getWidth()){
            bitwidth = getWidth();
            offsetRecY = spaceTop;
            bitheight = getWidth(); //-offsetRecY;
            narrowScreen = true;
        }else{
            bitwidth = spaceMid;
            offsetRecY = spaceTop;
            bitheight = getWidth(); //-offsetRecY;
            offsetRecX = (getWidth() - spaceMid)/2;
        }

       // Log.i("aVal12: ", "A: " + spaceMid + "  " + getWidth() + "  " + getHeight());

        bitFrame.setFrame(bitwidth, bitheight, offsetRecX, offsetRecY);
        gameOverView.init();
    //    textBoard = new TextBoard(bitFrame.getRect());
        readycheck = true;
    }

    public boolean isReady(){
        return readycheck;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
