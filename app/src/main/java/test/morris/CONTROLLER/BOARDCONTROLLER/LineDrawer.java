package test.morris.CONTROLLER.BOARDCONTROLLER;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Marcus on 2016-11-21.
 */

//rita spelplanens linjer beroende på level
public class LineDrawer {

    private float mid;

    public LineDrawer(){
        mid = 0;
    }

    public void makeBoardLines(Canvas ca, Paint paint, int w, int h, boolean ORIENTATION_LAND, int level) {
        float dist = 0;
        if (ORIENTATION_LAND)
            dist = (float) (h * 0.04);
        else
            dist = (float) (w * 0.04);
        mid = dist / 2;

        normalDraw(ca, paint, w, h, ORIENTATION_LAND);

        if(level == 2){
            diagonal1(w, h, ca, paint);
        }
        else if(level == 3){
            diagonal1(w, h, ca, paint);
            diagonal2(w, h, ca, paint);
        }
    }

    private void normalDraw(Canvas ca, Paint paint, int w, int h, boolean ORIENTATION_LAND){
        paint.setColor(Color.WHITE);
        float x1 = w * 0.04f;
        float y1 = h * 0.04f + mid;
        float x2 = w * 0.92f;
        float y2 = h * 0.04f + mid;
        ca.drawLine(x1, y1, x2, y2, paint);
        x1 = w * 0.04f;
        y1 = h * 0.92f + mid;
        x2 = w * 0.92f;
        y2 = h * 0.92f + mid;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.04f + mid;
        y1 = h * 0.92f;
        x2 = w * 0.04f + mid;
        y2 = h * 0.04f;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.92f + mid;
        y1 = h * 0.04f;
        x2 = w * 0.92f + mid;
        y2 = h * 0.92f;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.48f + mid;
        y1 = h * 0.04f;
        x2 = w * 0.48f + mid;
        y2 = h * 0.33f;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.48f + mid;
        y1 = h * 0.63f;
        x2 = w * 0.48f + mid;
        y2 = h * 0.92f;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.04f;
        y1 = h * 0.48f + mid;
        x2 = w * 0.33f;
        y2 = h * 0.48f + mid;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.63f;
        y1 = h * 0.48f + mid;
        x2 = w * 0.92f;
        y2 = h * 0.48f + mid;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.33f + mid;
        y1 = h * 0.33f + mid;
        x2 = w * 0.63f + mid;
        y2 = h * 0.63f + mid;
        paint.setStyle(Paint.Style.STROKE);
        ca.drawRect(x1, y1, x2, y2, paint);
        x1 = w * 0.2f + mid;
        y1 = h * 0.2f + mid;
        x2 = w * 0.76f + mid;
        y2 = h * 0.76f + mid;
        ca.drawRect(x1, y1, x2, y2, paint);
        paint.setStyle(Paint.Style.FILL);
    }


    private void diagonal1(float w, float h, Canvas ca, Paint paint){
        float x1 = w * 0.33f + mid;
        float y1 = h * 0.33f + mid;
        float x2 = w * 0.2f + mid;
        float y2 = h * 0.2f + mid;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.63f + mid;
        y1 = h * 0.63f + mid;
        x2 = w * 0.76f + mid;
        y2 = h * 0.76f + mid;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.63f + mid;
        y1 = h * 0.33f + mid;
        x2 = w * 0.76f + mid;
        y2 = h * 0.2f + mid;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.33f + mid;
        y1 = h * 0.63f + mid;
        x2 = w * 0.2f + mid;
        y2 = h * 0.76f + mid;
        ca.drawLine(x1, y1, x2, y2, paint);
    }

    private void diagonal2(float w, float h, Canvas ca, Paint paint){
        float x1 = w * 0.2f + mid;
        float y1 = h * 0.2f + mid;
        float x2 = w * 0.04f + mid;
        float y2 = h * 0.04f + mid;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.76f + mid;
        y1 = h * 0.76f + mid;
        x2 = w * 0.92f + mid;
        y2 = h * 0.92f + mid;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.76f + mid;
        y1 = h * 0.2f + mid;
        x2 = w * 0.92f + mid;
        y2 = h * 0.04f + mid;
        ca.drawLine(x1, y1, x2, y2, paint);

        x1 = w * 0.2f + mid;
        y1 = h * 0.76f + mid;
        x2 = w * 0.04f + mid;
        y2 = h * 0.92f + mid;
        ca.drawLine(x1, y1, x2, y2, paint);

    }

}
