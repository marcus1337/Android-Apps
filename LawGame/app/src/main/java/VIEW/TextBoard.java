package VIEW;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import MODEL.Model;

public class TextBoard {

    private Rect frameRect;
    private Rect localRect;
    private boolean isAnimating;
    private Model model;
    private Context context;

    public void init(Model model, Context context){
        this.model = model;
        this.context = context;
    }

    public TextBoard(Rect frameRect){
        this.frameRect = frameRect;
        int width = (int) (frameRect.width()*0.8f);
        int height = (int) (frameRect.height()*0.3f);
        int offX = (frameRect.width() - width)/2;
        int offY = (frameRect.bottom) - (int) (frameRect.height() *0.03f);
        localRect = new Rect(offX, offY - height, width + offX, offY);
    }

    public void draw(Canvas c){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(10,10, 10));
        paint.setAlpha(190);
        c.drawRect(localRect, paint);
        paint.setAlpha(255);
        // border
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        c.drawRect(localRect, paint);

        drawText(c);
    }

    public void drawText(Canvas c){
        TextPaint textPaint = new TextPaint();
        String text = model.getTextCurrent();

        textPaint.setAntiAlias(true);
        textPaint.setTextSize(30 * context.getResources().getDisplayMetrics().density);
        textPaint.setColor(0xffeed000);

        int width = (int) textPaint.measureText(text);
        StaticLayout staticLayout = new StaticLayout(text, textPaint, localRect.width(), Layout.Alignment.ALIGN_CENTER, 1.0f, 0, false);
        c.save();
        int extraTopBoxSpace = (localRect.height()-staticLayout.getHeight())/2;
        c.translate(localRect.left, (localRect.top + extraTopBoxSpace));
        staticLayout.draw(c);
        c.restore();
    }

}
