package com.fullrune.areashiftertwo.VIEW;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.fullrune.areashiftertwo.MODEL.MapValue;

public class BoardHelp {

    private static int eR = 166, eG = 166, eB = 166;

    private static int bR = 38, bG = 38, bB = 38;

    public static void setEmptyColor(int choice) {
        switch (choice) {
            case 1:
                eR = 115;
                eG = 115;
                eB = 115;
                break;
            case 2:
                eR = 140;
                eG = 140;
                eB = 140;
                break;
            default:
                eR = 166;
                eG = 166;
                eB = 166;
                break;
        }
    }

    public static void setPaintColorByAny(Paint paint, int choice){
        int r = 255;
        int g = 255;
        int b = 255;

        switch (choice) {
            case 1:
                r = bR;
                g = bG;
                b = bB;
                break;

            default:
                r = bR;
                g = bG;
                b = bB;
                break;
        }

        paint.setARGB(255, r, g, b);
    }

    public static void setPaintColorByMap(int[][] map, int x, int y, Paint paint) {
        int r = 255;
        int g = 255;
        int b = 255;

        if (map[x][y] == MapValue.EMPTY.getValue()) {
            r -= eR;
            g -= eG;
            b -= eB;
        }

        if (map[x][y] == MapValue.EDGE.getValue()) {
            r -= 112;
            g -= 195;
            b -= 255;
        }

        if (map[x][y] == MapValue.WALL.getValue()) {
            r -= 237;
            g -= 250;
            b -= 255;
        }

        if (map[x][y] == MapValue.LINE.getValue()) {
            r -= 188;
            g -= 129;
            b -= 11;
        }

        paint.setARGB(255, r, g, b);
    }

    public static Bitmap resizeBitmap(Bitmap image, int destWidth, int destHeight) {
        Bitmap background = Bitmap.createBitmap(destWidth, destHeight, Bitmap.Config.ARGB_8888);
        float originalWidth = image.getWidth();
        float originalHeight = image.getHeight();
        Canvas canvas = new Canvas(background);

        float scaleX = (float) 1280 / originalWidth;
        float scaleY = (float) 720 / originalHeight;

        float xTranslation = 0.0f;
        float yTranslation = 0.0f;
        float scale = 1;

        if (scaleX < scaleY) { // Scale on X, translate on Y
            scale = scaleX;
            yTranslation = (destHeight - originalHeight * scale) / 2.0f;
        } else { // Scale on Y, translate on X
            scale = scaleY;
            xTranslation = (destWidth - originalWidth * scale) / 2.0f;
        }

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(image, transformation, paint);
        return background;
    }

    private static Bitmap resize2(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
