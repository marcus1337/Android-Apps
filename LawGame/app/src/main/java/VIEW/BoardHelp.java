package VIEW;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

public class BoardHelp {

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
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
}
