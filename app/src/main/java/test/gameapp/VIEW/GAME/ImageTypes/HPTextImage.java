package test.gameapp.VIEW.GAME.ImageTypes;

import android.content.Context;

/**
 * Created by Marcus on 2017-01-06.
 */

public class HPTextImage extends SomeImage {


    public HPTextImage() {
        super();
    }

    @Override
    protected void setupTriangle() {
        vertices = new float[]
                {-1.0f, 1.0f, 0.0f,  // The bottom left corner
                        -1.0f, 0.85f, 0.0f,   // The top left corner
                        -0.75f, 0.85f, 0.0f, // The top right corner
                        -0.75f, 1.0f, 0.0f, // The bottom right corner
                };

        initVertices();

    }

    public void setUpUV() {

        uvs = new float[]{
                1.0f, 0.23f,
                1.0f, 0.39f,
                0.70f, 0.39f,
                0.70f, 0.23f
        };

        initUvBuff();
    }

}
