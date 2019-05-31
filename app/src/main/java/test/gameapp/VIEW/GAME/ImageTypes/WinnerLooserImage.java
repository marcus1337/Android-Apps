package test.gameapp.VIEW.GAME.ImageTypes;

import android.content.Context;

/**
 * Created by Marcus on 2017-01-07.
 */

public class WinnerLooserImage extends SomeImage {

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

    public WinnerLooserImage() {
        super();
    }


    public void setUpUV(boolean winner) {

        if (winner)
            uvs = new float[]{
                    0f, 0.4296875f,
                    0f, 0.5625f,
                    1f, 0.5625f,
                    1f, 0.4296875f
            };
        else {
            uvs = new float[]{
                    0f, 0.625f,
                    0f, 0.7578125f,
                    1f, 0.7578125f,
                    1f, 0.625f
            };
        }

        initUvBuff();
    }

}
