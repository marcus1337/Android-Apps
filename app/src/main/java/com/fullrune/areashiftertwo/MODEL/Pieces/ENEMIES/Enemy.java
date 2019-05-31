package com.fullrune.areashiftertwo.MODEL.Pieces.ENEMIES;


import android.util.Log;

import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input;
import com.fullrune.areashiftertwo.MODEL.Pieces.Point;
import com.fullrune.areashiftertwo.MODEL.MapValue;
import com.fullrune.areashiftertwo.MODEL.Model;
import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Player;
import com.fullrune.areashiftertwo.MODEL.Pieces.Unit;
import com.fullrune.areashiftertwo.MODEL.Pieces.UnitHelp;

import java.util.ArrayList;
import java.util.Random;

import static com.fullrune.areashiftertwo.MODEL.MapValue.*;

public abstract class Enemy extends Unit {

    private Physics physics;

    public Enemy(int x, int y, int radius) {
        super(x, y, radius);
        init();
    }

    public void init() {
        physics = new EnemyPhysics1(this);

        while (getxVel() == 0) {
            setxVel(initVelocity());
        }
        while (getyVel() == 0) {
            setyVel(initVelocity());
        }
    }

    public abstract EnemyType getType();

    public void process(Model model) {
        int[][] map = model.getMap();
        Player player = model.getPlayer();

        checkHitPlayer(player, map);

        physics.process(model);
        //moveNormal(map);
       // moveXY(getxVel(), getyVel());
       // bugCorrectionLazy(model.getMapWidth(), model.getMapHeight());
    }

    private void bugCorrectionLazy(int sizeX, int sizeY){
        int rh = getRadius()/2;
        while(getX() > sizeX)
            setX(getX()-2);
        while(getX() < 0)
            setX(getX()+2);
        while(getY() < 0)
            setY(getY()+2);
        while(getY() > sizeY)
            setY(getY()-2);
        if(getX() == sizeX-1)
            setX(getX()-3);
        if(getX() == 0)
            setX(3);
        if(getY() == sizeY-1)
            setY(getY()-3);
        if(getY() == 0)
            setY(3);

    }

    public void moveNormal(int[][] map) {

        int rh = getRadius()/2;
        Point pleft = new Point(getX()-rh, getY());
        Point pright = new Point(getX()+rh, getY());
        Point pup = new Point(getX(), getY() - rh);
        Point pdown = new Point(getX(), getY() + rh);

        Point pleftEND = new Point(pleft.x+ getxVel(), pleft.y+getyVel());
        Point prightEND = new Point(pright.x+ getxVel(), pright.y+getyVel());
        Point pupEND = new Point(pup.x+ getxVel(), pup.y+getyVel());
        Point pdownEND = new Point(pdown.x+ getxVel(), pdown.y+getyVel());

        int posVelx = Math.abs(getxVel());
        int posVely = Math.abs(getyVel());

        Point p = UnitHelp.detectMapHit(pleft, pleftEND, map, null, EDGE);
        if(p != null || UnitHelp.isBlockedEnemy(map, pleftEND.x, pleftEND.y)){
            setxVel(-getxVel());
        }
        p = UnitHelp.detectMapHit(pright, prightEND, map, null, EDGE);
        if(p != null || UnitHelp.isBlockedEnemy(map, prightEND.x, prightEND.y)){
            setxVel(-getxVel());
        }
        p = UnitHelp.detectMapHit(pup, pupEND, map, null, EDGE);
        if(p != null || UnitHelp.isBlockedEnemy(map, pupEND.x, pupEND.y)){
            setyVel(-getyVel());
        }
        p = UnitHelp.detectMapHit(pdown, pdownEND, map, null, EDGE);
        if(p != null || UnitHelp.isBlockedEnemy(map, pdownEND.x, pdownEND.y)){
            setyVel(-getyVel());
        }


        /*Point relP = new Point(0, 0);
        Point oldP = new Point(getX(), getY());
        Point mp = moveNormalUntilHit(map, relP);
        if (mp != null) {
            setXY(mp.x, mp.y);
            int side = getSide(oldP, relP);
            if (side == 1 || side == 3) {
                setyVel(-getyVel());
            } else {
                setxVel(-getxVel());
            }
        } else {
            moveXY(getxVel(), getyVel());
        }*/

    }

   /* private int getSide(Point oldP, Point relP) {
        if (relP.y < oldP.y) //UP
            return 1;
        if (relP.x > oldP.x) // RIGHT
            return 2;
        if (relP.y > oldP.y) //DOWN
            return 3;
        return 4; // LEFT
    }*/

    public int initVelocity() {
        Random rand = new Random();
        int low = -2;   //inclusive
        int high = 2; //exclusive
        int result = rand.nextInt(high - low) + low;
        //int result = rand.nextInt(10 - 5) + 5;
        return result;
    }

   /* private Point moveNormalUntilHit(int[][] map, Point relativePoint) {
        ArrayList<Point> points = UnitHelp.getShortCircleref(this, 0, 360);

        for (Point p : points) {
            int stepsX = getxVel();
            int stepsY = getyVel();
            int extraX = 0;
            int extraY = 0;
            while (stepsX != 0 || stepsY != 0) {
                int nowX = p.x + extraX;
                int nowY = p.y + extraY;
                if (extraX != 0 || extraY != 0)
                    if (!UnitHelp.isPointInside(map, nowX, nowY)
                            || UnitHelp.isPointMapType(map, nowX, nowY, MapValue.EDGE)) {
                        relativePoint.x = p.x;
                        relativePoint.y = p.y;
                        return new Point(getX() + extraX, getY() + extraY);
                    }

                if (stepsX > 0) {
                    stepsX--;
                    extraX--;
                }
                if (stepsX < 0) {
                    stepsX++;
                    extraX++;
                }
                if (stepsY < 0) {
                    stepsY++;
                    extraY++;
                }
                if (stepsY > 0) {
                    stepsY--;
                    extraY--;
                }

            }
        }
        return null;
    }*/

    private void checkHitPlayer(Player player, int[][] map) {
        if (UnitHelp.isUnitCollision(this, player)) {
            killPlayer(player);
        }
        if (hitPlayerLine(map)) {
            killPlayer(player);
        }

    }

    private void killPlayer(Player player) {
        if (player.isAlive()) {
            player.handleInput(Input.KILLED);
        }
    }

    private boolean hitLineMark(int[][] map, int x1, int y1) {
        if (UnitHelp.isPointInside(map, x1, y1)) {
            if (map[x1][y1] == LINE.getValue()) {
                return true;
            }
        }
        return false;
    }

    private boolean hitPlayerLine(int[][] map) {
        int rh = getRadius() / 2;
        int xleft = getX() - rh;
        int xright = getX() + rh;
        int ytop = getY() - rh;
        int ybottom = getY() + rh;
        boolean aHit = false;

        for(int i = xleft; i < xright; i++){
            if (hitLineMark(map, i, getY())) {
                aHit = true;
            }
        }

        for(int i = ytop; i < ybottom; i++){
            if (hitLineMark(map, getX(), i)) {
                aHit = true;
            }
        }
        /*if (hitLineMark(map, xleft, ytop)) {
            aHit = true;
        }
        if (hitLineMark(map, xright, ytop)) {
            aHit = true;
        }
        if (hitLineMark(map, xleft, ybottom)) {
            aHit = true;
        }
        if (hitLineMark(map, xleft, ybottom)) {
            aHit = true;
        }*/
        return aHit;
    }

}
