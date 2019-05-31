package test.gameapp.VIEW.GAME.ImageTypes;

import android.content.Context;

/**
 * Created by Marcus on 2017-01-06.
 */

public class HPImage extends SomeImage {

    public HPImage(int position) {
        super(position);
    }

    @Override
    protected void setupTriangle() {

        if (position == 0)
            vertices = new float[]
                    {-1.0f, 0.82f, 0.0f,  // The bottom left corner
                            -1.0f, 0.67f, 0.0f,   // The top left corner
                            -0.85f, 0.67f, 0.0f, // The top right corner
                            -0.85f, 0.8f, 0.0f, // The bottom right corner
                    };
        else if (position == 1) {
            vertices = new float[]
                    {-0.85f, 0.82f, 0.0f,  // The bottom left corner
                            -0.85f, 0.67f, 0.0f,   // The top left corner
                            -0.7f, 0.67f, 0.0f, // The top right corner
                            -0.7f, 0.82f, 0.0f, // The bottom right corner
                    };
        }
        initVertices();

    }

    public void setUpUV(int number) {

        float ax = number * 0.171875f; //0.046
        float ay = 0f;

        if (number > 5 && number < 10) {
            ay = 0.20312f;
            ax = (number - 5) * 0.10156f;
            if (number == 9)
                ax += 0.10156f;
            if (number == 6)
                ax -= 0.10156f;

        }

        uvs = new float[]{
                0.171875f + ax, 0.0f + ay,
                0.171875f + ax, 0.20312f + ay,
                0.0f + ax, 0.20312f + ay,
                0.0f + ax, 0.0f + ay
        };

        initUvBuff();
    }

}
