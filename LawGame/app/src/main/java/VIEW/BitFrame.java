package VIEW;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.Display;


import com.fullrune.lawgame.R;

import MODEL.Model;

public class BitFrame  {

    private Bitmap bitmap;
    private Paint pa;
    private Rect bitRect;
    private Canvas canvas;
    private Context context;
    private Model model;
    Bitmap mutableBitmap;

    public void init(Model model){
        this.model = model;
    }
    public Rect getRect(){
        return bitRect;
    }

    public void setFrame(int width, int height, int offX, int offY){
        bitRect = new Rect(offX, offY, width+offX, height+offY);
       // Log.i("TEST5" , "ofx: " + offX + " ofy: " + offY + " w: " + width + " h: " + height);
    }

    public BitFrame(Context context){

      //  bitmap = Bitmap.createBitmap(1920, 1080, Bitmap.Config.ARGB_8888);
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.platt1);
        mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        pa = new Paint();
        canvas = new Canvas(mutableBitmap);
        bitRect = new Rect(0,0,1920,1080);
    }


    public void draw(Canvas c){
        c.drawBitmap(mutableBitmap,null,bitRect, null);
    }

}
