package com.fullrune.areashifter.MODEL.Pieces;

import com.fullrune.areashifter.MODEL.Model;

/**
 * Created by Marcus on 2017-05-01.
 */

public class Enemy extends Unit {
    private static final long serialVersionUID = -29238982928391L;
    private Model model;

    public Enemy(int x, int y, int radius, Model model) {
        super(x, y, radius);
        this.model = model;
    }
boolean delayer = false;

    public void update(long timeDiff) {
        delayer = !delayer;
        if(delayer)
            return;

        collision();
        moveXY(getxVel(), getyVel());


    }

    private void collision() {
        int[][] map = model.getMap();
        boolean checked = false;
        if (!model.inBounds(getX() + getRadius(), getY()) ||
                map[getX() + getRadius()][getY()] >= 2
                ) {
            setxyVel(-getxVel(), getyVel());
            checked = true;
        } if (!model.inBounds(getX() - getRadius(), getY()) ||
                map[getX() - getRadius()][getY()] >= 2) {
            setxyVel(-getxVel(), getyVel());
            checked = true;
        } if (!model.inBounds(getX(), getY() + getRadius()) ||
                map[getX()][getY() + getRadius()] >= 2) {
            setxyVel(getxVel(), -getyVel());
            checked = true;
        } if (!model.inBounds(getX(), getY() - getRadius()) ||
                map[getX()][getY() - getRadius()] >= 2) {
            setxyVel(getxVel(), -getyVel() );
            checked = true;
        }

        if(!checked) {
            int x1 = (int) (getX() + getRadius() * Math.cos(Math.toRadians(45)));
            int y1 = (int) (getY() + getRadius() * Math.sin(Math.toRadians(45)));
            int x2 = (int) (getX() + getRadius() * Math.cos(Math.toRadians(135)));
            int y2 = (int) (getY() + getRadius() * Math.sin(Math.toRadians(135)));
            int x3 = (int) (getX() + getRadius() * Math.cos(Math.toRadians(225)));
            int y3 = (int) (getY() + getRadius() * Math.sin(Math.toRadians(225)));
            int x4 = (int) (getX() + getRadius() * Math.cos(Math.toRadians(315)));
            int y4 = (int) (getY() + getRadius() * Math.sin(Math.toRadians(315)));

            // Log.i("ABC123 ", "x1: " + x1 + " y1: " + y1 + "  X:" + getX()  + " Y: " + getY());
            //  Log.i("ABC124 ", "x2: " + x2 + " y2: " + y2 + "  X:" + getX()  + " Y: " + getY());
            // Log.i("ABC123 ", "x3: " + x3 + " y3: " + y3 + "  X:" + getX()  + " Y: " + getY());
            // Log.i("ABC124 ", "x4: " + x4 + " y4: " + y4 + "  X:" + getX()  + " Y: " + getY());

            if (map[x1][y1] >= 2 || map[x2][y2] >= 2 || map[x3][y3] >= 2 || map[x4][y4] >= 2) {
                setxyVel(-getxVel(), -getyVel());
            }
        }


    }

}
