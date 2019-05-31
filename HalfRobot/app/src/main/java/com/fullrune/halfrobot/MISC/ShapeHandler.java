package com.fullrune.halfrobot.MISC;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Marcus on 2017-09-25.
 */

public class ShapeHandler {

    public static Bitmap heroLadder;
    public static Bitmap heroJump;
    public static Bitmap heroWalk[];
    public static Bitmap heroStill;
    public static Bitmap heroHurt;

    public static Bitmap bull1;

    public static Bitmap heroFireStill;
    public static Bitmap heroFireWalk[];
    public static Bitmap heroFireJump;
    public static Bitmap heroFireLadder;

    public static Bitmap tiles[];

    public static Bitmap healthBar;
    public static Bitmap healthBar_t;

    public static Bitmap audioOn;
    public static Bitmap audioOff;

    public static Bitmap background1;
    public static Bitmap background2;
    public static Bitmap background3;
    public static Bitmap background4;


    public static void init(Context context){
        heroLadder = bitmapByPath(context, "res/unit/player/ladder/1.png");
        heroJump = bitmapByPath(context, "res/unit/player/jump/1.png");
        heroStill = bitmapByPath(context, "res/unit/player/still/1.png");
        heroHurt = bitmapByPath(context, "res/unit/player/hurt/1.png");

        heroWalk = new Bitmap[3];
        heroWalk[0] = bitmapByPath(context, "res/unit/player/walking/1.png");
        heroWalk[1] = bitmapByPath(context, "res/unit/player/walking/2.png");
        heroWalk[2] = bitmapByPath(context, "res/unit/player/walking/3.png");

        heroFireStill = bitmapByPath(context, "res/unit/player/fire/full/1.png");
        heroFireWalk = new Bitmap[3];
        heroFireWalk[0] = bitmapByPath(context, "res/unit/player/fire/full/2.png");
        heroFireWalk[1] = bitmapByPath(context, "res/unit/player/fire/full/3.png");
        heroFireWalk[2] = bitmapByPath(context, "res/unit/player/fire/full/4.png");
        heroFireJump = bitmapByPath(context, "res/unit/player/fire/full/5.png");
        heroFireLadder = bitmapByPath(context, "res/unit/player/fire/full/6.png");

        bull1 = bitmapByPath(context, "res/unit/shots/normal1.png");

        tiles = new Bitmap[3];
        tiles[0] = bitmapByPath(context, "res/tiles/block1.png");
        tiles[1] = bitmapByPath(context, "res/tiles/laddr1.png");
        tiles[2] = bitmapByPath(context, "res/tiles/spik1.png");

        healthBar = bitmapByPath(context, "res/health_bar.png");
        healthBar_t = bitmapByPath(context, "res/health_bar2.png");

        audioOn = bitmapByPath(context, "res/audio1.png");
        audioOff = bitmapByPath(context, "res/audio2.png");

        background1 = bitmapByPath(context, "res/bakgrund1.png");
        background2 = bitmapByPath(context, "res/backg8.png");
        background3 = bitmapByPath(context, "res/back88.png");
        background4 = bitmapByPath(context, "res/backg225.png");


    }

    public static Bitmap bitmapByPath(Context context, String filePath) {
        AssetManager assetManager = context.getAssets();
        InputStream istr;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(filePath);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException e) {
            Toast.makeText(context, "FAILURE: " + filePath, Toast.LENGTH_LONG).show();
        }
        return bitmap;
    }

}
