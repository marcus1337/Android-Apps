package test.morris.CONTROLLER.BOARDCONTROLLER;

import android.content.res.Configuration;

import java.util.ArrayList;

import test.morris.MainActivity;

/**
 * Created by Marcus on 2016-11-22.
 */

public class PositionClick {

    private float w, h;
    private final static int extraClickSpace = 28;

    public PositionClick(float w, float h) {
        this.w = w;
        this.h = h;
    }

    //returnerar pjäs-numret när en spelare klickar på skärmen
    public int handleTouch(float x, float y, MainActivity mainActivity, ArrayList<Mark> marks) {
        swapIfLandscape(mainActivity);
        if (marks.size() != 24)
            return -2;

        for (Mark m : marks) {
            if (m.getX() <= x + extraClickSpace && m.getX() + m.getDist() + extraClickSpace >= x &&
                    y + extraClickSpace >= m.getY() && y <= m.getY() + m.getDist() + extraClickSpace) {
                return m.getNumber();
            }
        }

        return -1;
    }

    private void swapIfLandscape(MainActivity mainActivity) {
        if (mainActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            float tmp = w;
            w = h;
            h = tmp;
        }
    }

}
