package test.gameapp.VIEW.GAME;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;

import test.gameapp.MODEL.CLIENT.ClientModel;

/**
 * Created by Marcus on 2016-12-08.
 */

/**OpenGL-ES Surfaceview för spelgrafiken
 * **/
public class GameSurfaceView extends GLSurfaceView {

    private final GameRenderer mRenderer;

    public GameSurfaceView(Context context, ClientModel clientModel, int playerNumber){
        super(context);
        setEGLContextClientVersion(2);
        mRenderer = new GameRenderer(context, clientModel, playerNumber);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public void updateEvent(){
        try {
            requestRender();

        } catch (Exception e) {
            Log.i("hmmm", e.toString());
        }
    }

}
