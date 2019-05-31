package test.gameapp.VIEW.GAME.ImageTypes;

import android.content.Context;

/**
 * Created by Marcus on 2017-01-07.
 */

public class GoldTextImage extends SomeImage {

    @Override
    protected void setupTriangle() {
        vertices = new float[]
                {1.0f, 1.0f, 0.0f,
                        1.0f, 0.5f, 0.0f,
                        -1.0f, 0.5f, 0.0f,
                        -1.0f, 1.0f, 0.0f,
                };

        initVertices();
    }

    public GoldTextImage() {
        super();
        setUpUV();
    }


    private void setUpUV() {

        uvs = new float[]{
                0f, 0.82f,
                0f, 0.95f,
                0.64f, 0.95f,
                0.64f, 0.82f
        };

        initUvBuff();
    }

}
