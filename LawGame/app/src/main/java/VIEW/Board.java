package VIEW;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import MODEL.Model;

public class Board extends SurfaceView implements SurfaceHolder.Callback {

    private Context context;
    private boolean readycheck;
    private SurfaceHolder surfaceHolder;
    private Paint paint;
    private boolean initDone;
    private TextBoard textBoard;
    private Model model;

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
        getHolder().addCallback(this);
        surfaceHolder = getHolder();
        paint = new Paint();
        initDone = false;
    }

    public void init2(Model model){
        this.model = model;
        bitFrame.init(model);
        textBoard.init(model, context);
        initDone = true;
    }

    private void doDraw(Canvas c){
    if(!readycheck || !initDone)
        return;
       // if(gameState != null && gameState.isGameStarted()){
      //  }
        Paint paint2 = new Paint();
        paint2.setColor(Color.DKGRAY);
        paint2.setStyle(Paint.Style.FILL);
        c.drawPaint(paint2);


        bitFrame.draw(c);
        textBoard.draw(c);

    }

    public void drawGame(){
        Canvas c = surfaceHolder.lockCanvas();
        if(c != null) {
            doDraw(c);
            surfaceHolder.unlockCanvasAndPost(c);
        }
    }



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        if(readycheck)
            return;

        float localratio = ((float)getWidth())/((float)getHeight());
        float origW = 1920.0f;
        float origH = 1080.0f;
        float targetratio = origW / origH;
        int nowWidth, nowHeight;
        int offsetRecX = 0, offsetRecY = 0;
        if(localratio < targetratio){ //scale width = max
            nowWidth = getWidth();
            //original height / original width * new width = new height
            nowHeight = Math.round((origH / origW) * nowWidth);
            offsetRecY = (getHeight() - nowHeight)/2;
        }else{ //scale height = max
            nowHeight = getHeight();
            //orignal width / orignal height * new height = new width
            nowWidth = Math.round((origW/origH)*nowHeight);
            offsetRecX = (getWidth() - nowWidth)/2;
        }

        bitFrame = new BitFrame(context);
        bitFrame.setFrame(nowWidth, nowHeight, offsetRecX, offsetRecY);
        textBoard = new TextBoard(bitFrame.getRect());
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
