package test.gameapp.VIEW.MENU;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import test.gameapp.CONTROLLER.ACTIVITY.MenuActivity;

/**
 * Created by Marcus on 2016-12-13.
 */

/***
 *
 * Rita grafik gällande menyn
 *
 * **/

public class MenuRenderer implements GLSurfaceView.Renderer {

    private MenuActivity menuActivity;
    private ArrayList<Line> mLines, eLines, nLines, uLines, uLines2;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private float[] mTempMatrix = new float[16];

    private float[] mRotationMatrix = new float[16];
    public volatile float angle;
    private int counter;
    private boolean check;



    public MenuRenderer(MenuActivity menuActivity) {
        this.menuActivity = menuActivity;
    }

    //**
    // Bakgrunds-objekt till menuactivity initieras här
    // *//
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        counter = 0;
        check = false;
        angle = 0;
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        mLines = new ArrayList<Line>();
        eLines = new ArrayList<Line>();
        nLines = new ArrayList<Line>();
        uLines = new ArrayList<Line>();
        uLines2 = new ArrayList<Line>();
        for (int i = 0; i < 4; i++) {
            mLines.add(new Line());
            eLines.add(new Line());
        }
        for (int i = 0; i < 3; i++) {
            nLines.add(new Line());
        }

        mLines.get(0).setVerts(-0.95f, 0.7f, 0.0f,
                -0.85f, 0.9f, 0.0f);
        mLines.get(1).setVerts(-0.85f, 0.9f, 0.0f,
                -0.75f, 0.7f, 0.0f);
        mLines.get(2).setVerts(-0.75f, 0.7f, 0.0f,
                -0.65f, 0.9f, 0.0f);
        mLines.get(3).setVerts(-0.65f, 0.9f, 0.0f,
                -0.55f, 0.7f, 0.0f);

        eLines.get(0).setVerts(-0.4f, 0.9f, 0.0f,
                -0.4f, 0.7f, 0.0f);
        eLines.get(1).setVerts(-0.4f, 0.9f, 0.0f,
                -0.2f, 0.9f, 0.0f);
        eLines.get(2).setVerts(-0.4f, 0.8f, 0.0f,
                -0.2f, 0.8f, 0.0f);
        eLines.get(3).setVerts(-0.4f, 0.7f, 0.0f,
                -0.2f, 0.7f, 0.0f);

        nLines.get(0).setVerts(-0.05f, 0.7f, 0.0f,
                0.05f, 0.9f, 0.0f);
        nLines.get(1).setVerts(0.05f, 0.9f, 0.0f,
                0.15f, 0.7f, 0.0f);
        nLines.get(2).setVerts(0.15f, 0.7f, 0.0f,
                0.25f, 0.9f, 0.0f);

        uLines.add(new Line());
        uLines.get(0).setVerts(0.4f, 0.9f, 0.0f,
                0.4f, 0.85f, 0.0f);

        float startX = 0.4f;
        float startY = 0.85f;
        float endX = 0.608f;
        float endY = 0.7f;
        float ctrPx = 0.4f;
        float ctrPy = 0.7f;

        float t = 0.02f;

        for (int i = 1; i < 50; i++) { //Bézier-kurva
            uLines.add(new Line());
            float x2 = uLines.get(i - 1).getX2();
            float y2 = uLines.get(i - 1).getY2();

            float x = (1 - t) * (1 - t) * startX + 2 * (1 - t) * t * ctrPx + t * t * endX;
            float y = (1 - t) * (1 - t) * startY + 2 * (1 - t) * t * ctrPy + t * t * endY;

            t += 0.02f;
            uLines.get(i).setVerts(x2, y2, 0.0f,
                    x, y, 0.0f);
        }

        startX = 0.8f;
        startY = 0.85f;
        endX = 0.592f;
        endY = 0.7f;
        ctrPx = 0.8f;
        ctrPy = 0.7f;
        t = 0.02f;

        uLines2.add(new Line());
        uLines2.get(0).setVerts(0.8f, 0.9f, 0.0f, 0.8f, 0.85f, 0.0f);

        for (int i = 1; i < 50; i++) { //Bézier-kurva
            uLines2.add(new Line());

            float x2 = uLines2.get(i - 1).getX2();
            float y2 = uLines2.get(i - 1).getY2();

            float x = (1 - t) * (1 - t) * startX + 2 * (1 - t) * t * ctrPx + t * t * endX;
            float y = (1 - t) * (1 - t) * startY + 2 * (1 - t) * t * ctrPy + t * t * endY;

            t += 0.02f;
            uLines2.get(i).setVerts(x2, y2, 0.0f,
                    x, y, 0.0f);

        }

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    //rita ut meny-bokstäver
    @Override
    public void onDrawFrame(GL10 gl) {
        float[] scratch = new float[16];
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        adjustWidth(gl);

//        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        Matrix.setLookAtM(mMVPMatrix, 0, 0, 0, 1, 0f, 0f, 0f, 0f, 1.0f, 0f);
        for (Line l : mLines)
            l.draw(mMVPMatrix);
        for (Line l : eLines)
            l.draw(mMVPMatrix);
        for (Line l : nLines)
            l.draw(mMVPMatrix);
        for (Line l : uLines)
            l.draw(mMVPMatrix);
        for (Line l : uLines2)
            l.draw(mMVPMatrix);

    }

    private void adjustWidth(GL10 gl){
        gl.glLineWidth(counter);
        if(!check){
            counter++;

            if(counter == 18){
                check = true;
            }
        }
        else{
            counter--;
            if(counter == 1){
                check = false;
            }

        }
    }


}
