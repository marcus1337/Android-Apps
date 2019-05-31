package test.morris.CONTROLLER.BOARDCONTROLLER;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;


public class CustomBoard extends View {
    private Paint paint;
    private ArrayList<Mark> marks;
    private int selected;
    private int[] gameboard;
    private CustomBoard currentBoard;
    private AnimateMove animateMove;
    private float aniX, aniY;
    private int turn;
    private int latestMove;
    private int unplacedRed, unplacedBlue;
    private int level;
    private boolean miniOrientation;

    public void setSpecialLayout(boolean miniOrientation){
        this.miniOrientation = true;
    }

    public CustomBoard(Context context) {
        super(context);
        init(context);
    }

    public CustomBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomBoard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        miniOrientation = false;
        currentBoard = this;
        aniX = aniY = -100;
        unplacedRed = unplacedBlue = 0;
        paint = new Paint();
        marks = new ArrayList<Mark>();
        selected = -1;
        latestMove = -1;
        turn = 0;
        level = 1;
        animateMove = new AnimateMove(this);

    }

    public void setTurn(int turn){
        this.turn = turn;
    }

    public void setAniXY(float x, float y) {
        aniX = x;
        aniY = y;
    }

    //denna metod körs flera gånger när invalidate kallas från en ValueAnimator
    private void drawAnimation(Canvas ca, Paint paint) {
        if (aniX < 0)
            return;
        float dist = getDistance();
        if(turn == 1)
            paint.setColor(Color.BLUE);
        if(turn == 2)
            paint.setColor(Color.RED);
        ca.drawRect(aniX, aniY, aniX + dist, aniY + dist, paint);
    }


    @Override
    public void onDraw(Canvas ca) {
        ca.drawColor(Color.BLACK);
        drawLines(ca, paint); //rita linjer till spelet

        marks = new ArrayList<Mark>();

        drawMark(0.04f, 0.04f, ca, 3); //03
        drawMark(0.48f, 0.04f, ca, 6); //06
        drawMark(0.92f, 0.04f, ca, 9); //09

        drawMark(0.04f, 0.92f, ca, 21); //21
        drawMark(0.48f, 0.92f, ca, 18); //18
        drawMark(0.92f, 0.92f, ca, 15); //15

        drawMark(0.2f, 0.2f, ca, 2); //02
        drawMark(0.48f, 0.2f, ca, 5); //05
        drawMark(0.76f, 0.2f, ca, 8); //08

        drawMark(0.76f, 0.76f, ca, 14); //14
        drawMark(0.2f, 0.76f, ca, 20);  //20
        drawMark(0.48f, 0.76f, ca, 17); //17

        drawMark(0.63f, 0.63f, ca, 13); //13
        drawMark(0.33f, 0.63f, ca, 19); //19
        drawMark(0.48f, 0.63f, ca, 16); //16

        drawMark(0.33f, 0.33f, ca, 1); //01
        drawMark(0.63f, 0.33f, ca, 7); //07
        drawMark(0.48f, 0.33f, ca, 4); //04

        drawMark(0.92f, 0.48f, ca, 12); //12
        drawMark(0.2f, 0.48f, ca, 23); //23
        drawMark(0.76f, 0.48f, ca, 11); //11

        drawMark(0.04f, 0.48f, ca, 24); //24
        drawMark(0.63f, 0.48f, ca, 10); //10
        drawMark(0.33f, 0.48f, ca, 22); //22

        if(unplacedBlue > 0 || unplacedRed > 0){ //visa oplacerade pjäser i rutor
            paint.setColor(Color.RED);
            paint.setStrokeWidth(3f);
            paint.setStyle(Paint.Style.STROKE);
            int w = getWidth();
            int h = getHeight();
            float dist = getDistance();
            float x = 0.1f;
            float y = 0.95f;;
            x = w * x;
            y = h * y;
            ca.drawRect(x, y, x+dist*9, y+dist, paint);
            paint.setStyle(Paint.Style.FILL);

            x += dist/2;
            y += dist/2;
            float tmpX = x;
            for(int i = 0; i < unplacedRed; i++){
                x = tmpX + i*dist;
                ca.drawCircle(x,y, dist/3, paint);
            }

            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.STROKE);
            w = getWidth();
            h = getHeight();
            dist = getDistance();
            x = 0.54f;
            y = 0.95f;;
            x = w * x;
            y = h * y;
            ca.drawRect(x, y, x+dist*9, y+dist, paint);
            paint.setStyle(Paint.Style.FILL);

            x += dist/2;
            y += dist/2;
            tmpX = x;
            for(int i = 0; i < unplacedBlue; i++){
                x = tmpX + i*dist;
                ca.drawCircle(x,y, dist/3, paint);
            }
        }
        paint.setStrokeWidth(1f);

        drawAnimation(ca, paint); //animation
    }

    //rita pjäser på brädet
    private void drawMark(float x, float y, Canvas ca, int pointNumber) {
        int w = getWidth();
        int h = getHeight();
        float dist = getDistance();
        x = w * x;
        y = h * y;

        marks.add(new Mark(x, y, dist, pointNumber));
        float x2 = x + dist;
        float y2 = y + dist;

        paint.setColor(Color.YELLOW);

        if (gameboard != null && gameboard[pointNumber] == 4) {
            paint.setColor(Color.BLUE);
        }
        if (gameboard != null && gameboard[pointNumber] == 5) {
            paint.setColor(Color.RED);
        }
        if(latestMove != -1 && !animateMove.isFinished() && pointNumber == latestMove){
            paint.setColor(Color.YELLOW);
        }

        ca.drawRect(x, y, x2, y2, paint);
        if (pointNumber == selected)
            drawSelectedCircle(x, y, dist, ca, paint);
    }

    //visa markerad pjäs
    private void drawSelectedCircle(float x, float y, float dist, Canvas ca, Paint paint) {
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        int tmp = (int) dist;
        for (int i = tmp; i > tmp*0.8; i--)
            ca.drawCircle(x + (dist / 2), y + (dist / 2), i, paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.YELLOW);
    }

    private void drawLines(Canvas ca, Paint p) {
        boolean check1 = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        new LineDrawer().makeBoardLines(ca, p, getWidth(), getHeight(), check1, level);
    }

    //anger storleken för pjäser beroende på telefonens skärmstorlek
    private float getDistance() {
        float dist = 0;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            dist = (float) (getHeight() * 0.04);
        else
            dist = (float) (getWidth() * 0.04);
        if(miniOrientation)
            dist = (float) (getWidth() * 0.03);

        return dist;
    }

    public ArrayList<Mark> getMarks() {
        ArrayList<Mark> tmp = marks;
        return tmp;
    }

    //starta animation från x y till x2 y2
    private void runAnimation(float frX, float frY, float toX, float toY) {
        if (!animateMove.isFinished())
            return;
        animateMove = new AnimateMove(this);
        animateMove.init(frX, frY, toX, toY, turn);
        animateMove.start();
    }

    public void setSelected(int selected) {
        this.selected = selected;
        this.invalidate();
    }

    public void setLatestMove(int latestMove){
        this.latestMove = latestMove;
    }

    //mainactivity kallar på denna för att starta en animering
    public void moveToAnimation(int to){
        latestMove = to;
        Mark m1 = getMarkByNumber(selected);
        Mark m2 = getMarkByNumber(to);
        runAnimation(m1.getX(), m1.getY(), m2.getX(), m2.getY());
    }

    //returnerar värden för en pjäs beroende på nummer
    private Mark getMarkByNumber(int num){
        for(Mark m: marks){
            if(m.getNumber() == num)
                return m;
        }
        return null;
    }

    //updatera spelbordet med nya värden och rita om
    public void updateMarks(int[] gameboard, int unPlacedBlue, int unPlacedRed, int level) {
        this.gameboard = gameboard;
        this.unplacedRed = unPlacedRed;
        this.unplacedBlue = unPlacedBlue;
        this.level = level;
        this.invalidate();
    }


}
