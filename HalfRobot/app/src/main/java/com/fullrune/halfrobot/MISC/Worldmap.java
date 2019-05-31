package com.fullrune.halfrobot.MISC;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Marcus on 2017-09-25.
 */

public class Worldmap {

    public Worldmap() {
        _w = 0;
        _h = 0;
        karta = null;
        texture = null;
    }

    public void initArray(int w, int h) {
        _w = w*20;
        _h = h*15;

        karta = new char[_w * _h];
        for (int i = 0; i < (_w * _h); i++) {
            karta[i] = 1;
        }
    }

    public void placePiece(int blockX, int blockY, char[] pieceArr) {

        blockX = blockX * 20;
        blockY = blockY * 15;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 20; j++) {
                int i2 = i + blockY;
                int j2 = j + blockX;

                karta[j2 + i2*_w] = pieceArr[j + i * 20];

            }
        }

    }

    public void initWorld() {

        texture = Bitmap.createBitmap(_w*20, _h*20, Bitmap.Config.ARGB_8888);

        Bitmap tmpTex;
        Canvas canvas = new Canvas(texture);
        Rect tmpRect = new Rect( 0,0,20,20 );
        Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.BLACK);

        for (int a = 0; a < _h; a++) {
            for (int b = 0; b < _w; b++) {
                char val = karta[b + a * _w];
                tmpRect.set(b*20, a*20, 20, 20);

                if (val == 0) {
                    canvas.drawRect(tmpRect.left, tmpRect.top, tmpRect.width(), tmpRect.height(), p);

                }
                if (val == 1) {
                    tmpTex = ShapeHandler.tiles[0];

                    canvas.drawBitmap(tmpTex, null, tmpRect, null);
                }
                if (val == 2) {
                    tmpTex = ShapeHandler.tiles[1];
                    canvas.drawBitmap(tmpTex, null, tmpRect, null);
                }

                if (val == 6) {
                    tmpTex = ShapeHandler.tiles[2];
                    canvas.drawBitmap(tmpTex, null, tmpRect, null);
                }


            }

        }

    }

    public Bitmap getTexture() {
        return texture;
    }

    public char[] getMapArr() {
        return karta;
    }

    public int getWidth() {
        return _w;
    }

    public int getHeight() {
        return _h;
    }

    private int _w, _h;
    private char[] karta;
    private Bitmap texture;
}
