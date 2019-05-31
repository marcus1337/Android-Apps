package test.gameapp.VIEW.MENU;

import android.opengl.GLSurfaceView;

import java.util.Timer;
import java.util.TimerTask;

import test.gameapp.CONTROLLER.ACTIVITY.MenuActivity;

/**
 * Created by Marcus on 2016-12-13.
 *
 *
 * SurfaceView för menu-activity
 */

public class MenuSurfaceView extends GLSurfaceView {

    private final MenuRenderer menuRenderer;
    private MenuActivity menuActivity;

    private Timer timer;

    public MenuSurfaceView(MenuActivity menuActivity) {
        super(menuActivity.getApplicationContext());
        setEGLContextClientVersion(2);
        this.menuActivity = menuActivity;
        menuRenderer = new MenuRenderer(menuActivity);
        setRenderer(menuRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        timer = new Timer();
        timer.scheduleAtFixedRate(new UpdateMenuTask(), 0, 100);

    }

    //Animationstråd till en flashig bakgrund.
    private class UpdateMenuTask extends TimerTask {
        public void run() {
            updateRender();
        }
    }
    public void updateRender(){
        requestRender();
    }


}
