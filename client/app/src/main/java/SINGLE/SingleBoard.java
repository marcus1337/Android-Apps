package SINGLE;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;



public class SingleBoard extends SurfaceView implements SurfaceHolder.Callback {

    public SingleBoard(Context context) {
        super(context);
        init();
    }

    public SingleBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SingleBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private SingleBitFrame bitFrame;

    public void init(){
        getHolder().addCallback(this);
        surfaceHolder = getHolder();
        paint = new Paint();
    }

    private Rect bitRect;
    public boolean readycheck;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private SingleGame gameState;

    public void setGameState(SingleGame gameState){
        this.gameState = gameState;
    }

    private void doDraw(Canvas c){
        //c.drawARGB(255, 255, 255, 255);
        // Log.i("test_abc", "a:" + bitRect.width() + " b: " + bitRect.height());
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(5f);
        c.drawRect(bitRect, paint);
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(10f);
        c.drawLine(bitRect.left, bitRect.top, bitRect.right, bitRect.top, paint);
        c.drawLine(bitRect.left, bitRect.top, bitRect.left, bitRect.bottom, paint);
        c.drawLine(bitRect.left, bitRect.bottom, bitRect.right, bitRect.bottom, paint);
        c.drawLine(bitRect.right, bitRect.top, bitRect.right, bitRect.bottom, paint);

        if(gameState != null && gameState.isGameStarted()){
            bitFrame.notify(gameState, 0);
        }

        bitFrame.draw(c);

        int cols = bitFrame.getCols();
        int rows = bitFrame.getRows();

        paint.setStrokeWidth(1f);
        float colspace = ((float)bitRect.width()) / (float) cols;
        float rowspace = ((float)bitRect.height()) / (float) rows;
        for(int i = 1; i <= cols; i++){
            int xPos = Math.round(((float)bitRect.left)+((float)i)*colspace);
            c.drawLine(xPos, bitRect.top, xPos, bitRect.bottom, paint);
        }

        for(int i = 0; i < rows; i++){
            int yPos = Math.round(((float)bitRect.top)+((float)i)*rowspace);
            c.drawLine(bitRect.left, yPos, bitRect.right, yPos, paint);
        }

        if(gameState != null && gameState.isDone()){

            String text = "GAME OVER\n  Score: " + gameState.getScore();

            TextPaint textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(60 * getResources().getDisplayMetrics().density);
            textPaint.setColor(0xFf00e5e0);

            int width = (int) textPaint.measureText(text);
            StaticLayout staticLayout = new StaticLayout(text, textPaint, c.getWidth(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0, false);
            c.translate(0, (getHeight()/2)-(staticLayout.getHeight()));
            staticLayout.draw(c);

          //  c.restore();
            //paint.setColor(Color.argb(255, 120, 155, 0));
           // paint.setTextSize(100);
           // c.drawText("GAME OVER",getWidth()/2 - 50, getHeight()/2,paint);
        }
        //  Log.i("test_abc", "a:" + bitRect.width() + " b: " + bitRect.height());
    }

    public void drawGame(){
        Canvas c = surfaceHolder.lockCanvas();
        if(c != null) {
            doDraw(c);
            surfaceHolder.unlockCanvasAndPost(c);
        }else{
            //  Log.i("test_abc", "something wrong?");
        }
    }

    public boolean readyCheck2;

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        if(readycheck && readyCheck2)
            return;

        int tmpH = (int) (getHeight()*0.8f); //0.562f
        int tmpW = (int)((float) tmpH * 1.66666f);

        // Log.i("TEST_A", "A: " + tmpH);

        int extX = (getWidth()-tmpW)/2;//(int)(getWidth()*0.03f);
        int extY = (getHeight()-tmpH)/2; //(int)(getHeight()*0.03f);

        bitFrame = new SingleBitFrame(extX,extY,tmpW + extX,tmpH + extY);
        bitRect = bitFrame.getRect();
        readycheck = true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
