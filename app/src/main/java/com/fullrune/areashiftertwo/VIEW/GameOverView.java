package com.fullrune.areashiftertwo.VIEW;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

public class GameOverView {

    private Board board;
    private Context context;

    private int width, height;
    public GameOverView(Board board, Context context){
        this.board = board;
        this.context = context;
       // width = board.getWidth();
       // height = board.getHeight();
      //  alphaStep = 0;
        gameoverYextra = -200;
    }

    private long oldTime_50, oldTime_2, oldTime_21;
    private int alphaStep, gameoverYextra, gamewinYextra;
    private boolean animationGameOverDone, animationWinDone;

    public boolean isAnimationGameOverDone(){
        return animationGameOverDone;
    }

    public boolean isAnimationWinDone(){
        return animationWinDone;
    }

    private void stepTimer(){

        long currentTime = System.currentTimeMillis();
        if(currentTime - oldTime_50 > 50){
            if(oldTime_50 != 0)
                alphaStep++;
            oldTime_50 = System.currentTimeMillis();
            if(alphaStep > 255)
                alphaStep = 255;
        }
        if(currentTime - oldTime_2 > 3){
            oldTime_2 = System.currentTimeMillis();
            gameoverYextra+= 4;
            if(gameoverYextra > height/2-(height/2)/2){
                gameoverYextra = height/2-(height/2)/2;
                animationGameOverDone = true;
            }
        }

    }

    private void stepTimerWon(){
        long currentTime = System.currentTimeMillis();
        if(currentTime - oldTime_50 > 20){
            if(oldTime_50 != 0)
                alphaStep++;
            oldTime_50 = System.currentTimeMillis();
            if(alphaStep > 50)
                alphaStep = 50;
        }
        if(currentTime - oldTime_21 > 2){
            oldTime_21 = System.currentTimeMillis();
            gamewinYextra += 5;
            if(gamewinYextra > height/2-(height/2)/2){
                gamewinYextra = height/2-(height/2)/2;
                animationWinDone = true;
            }
        }

    }

    private void drawSomeText(String text, Canvas c, Rect localRect){
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(50 * context.getResources().getDisplayMetrics().density);
        textPaint.setColor(Color.YELLOW);
        //int widthTxt = (int) textPaint.measureText(text);
        StaticLayout staticLayout = new StaticLayout(text, textPaint, localRect.width(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0, false);
        c.save();
        int extraTopBoxSpace = (localRect.height()-staticLayout.getHeight())/2;
        c.translate(localRect.left, (localRect.top + extraTopBoxSpace));
        staticLayout.draw(c);
        c.restore();
    }

    public void drawGameLost(Canvas c){
        stepTimer();
        int txtWidth = width;
        Rect rect = new Rect(width/2-txtWidth/2,gameoverYextra,width/2+txtWidth/2,height/5+gameoverYextra);
        c.drawARGB(alphaStep, 30, 30, 30);
        drawSomeText("GAME OVER", c, rect);
    }

    public void drawGameWon(Canvas c){
        stepTimerWon();
        int txtWidth = width;
        Rect rect = new Rect(width/2-txtWidth/2-width/4,gamewinYextra,width/2+txtWidth/2+width/4,height/5+gamewinYextra);
        c.drawARGB(alphaStep, 3, 3, 3);
        if(lvl == 0)
            drawSomeText("LEVEL WON", c, rect);
        else
            drawSomeText("LEVEL " + String.valueOf(lvl) + " WON", c, rect);
    }

    private int lvl;
    public void setLvl(int lvl){
        this.lvl = lvl;
    }

    public void init() {
        width = board.getWidth();
        height = board.getHeight();
    }
}
