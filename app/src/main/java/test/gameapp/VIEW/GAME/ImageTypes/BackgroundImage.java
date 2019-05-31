package test.gameapp.VIEW.GAME.ImageTypes;
import android.content.Context;

/**
 * Created by Marcus on 2016-12-27.
 */

public class BackgroundImage extends SomeImage {

    public BackgroundImage(){
        super();
    }

    @Override
    protected void setupTriangle()
    {
        vertices = new float[]
                {       1.0f, 1.0f, 0.0f,
                        1.0f, -1.0f, 0.0f,
                        -1.0f, -1.0f, 0.0f,
                        -1.0f, 1.0f, 0.0f,
                };

        initVertices();

    }

    public void setUpUV(float locationX, float locationY, float mapSizeX, float mapSizeY){

        float ax = locationX/mapSizeX;
        float ay = locationY/mapSizeY;

        uvs = new float[] {
                -0.1f + ax, -0.1f + ay,
                -0.1f + ax, 0.1f + ay,
                0.1f + ax, 0.1f + ay,
                0.1f + ax, -0.1f + ay
        };

        initUvBuff();
    }




}