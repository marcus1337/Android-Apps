package com.fullrune.halfrobot.LOGIC.PHYSICS;

import com.fullrune.halfrobot.LOGIC.ENTITY.Entity;
import com.fullrune.halfrobot.LOGIC.Model;
import com.fullrune.halfrobot.MISC.Worldmap;

/**
 * Created by Marcus on 2017-09-26.
 */

public class Physics {
    public void update(Model model, Entity entity) {
        Worldmap worldmap = model.getPlayerMap();
        bw = worldmap.getWidth();
    }

    public Physics() {
        bw = 20;
    }

    public  boolean isInAir() { return true; }

    public  boolean isOnLadder() {
        return false;
    }
    public  boolean canUseLadderUP() {
        return false;
    }
    public  boolean canUseLadderDOWN() {
        return false;
    }

    public  void enterLadder() {
    }

    public  void exitLadder() {
    }

    public  boolean isXPathBlocked() {
        return false;
    }

    public  boolean isYPathBlocked() {
        return false;
    }

    protected static int sqrSize = 32;
    protected int bw;

    protected boolean hitWall(int pos, char[] karta) {
        return karta[pos] == 2 || (karta[pos] >= 6 && karta[pos] <= 9);
    }

    protected boolean hitSpike(char[] karta, Entity entity){
        int xVel = entity.getVelocityX();
        int yVel = entity.getVelocityY();

        if (yVel < 0)
            return false;

        int x1 = entity.getX() / 20;
        int x2 = entity.getX2() / 20;
        int x3 = (x2 + x1) / 2;

        int y1 = (entity.getY()-1) / 20;
        int y2 = (entity.getY2()) / 20;
        int y3 = (y1 + y2) / 2;

        int y5 = (entity.getY2()+1) / 20;

        if (yVel == 0) {
            if (karta[x1 + y5 * bw] == 6 && karta[x2 + y5 * bw] == 6 && karta[x3 + y5 * bw] == 6) {
                return true;
            }
        }

        if (karta[x1 + y2 * bw] == 6 || karta[x2 + y2 * bw] == 6 || karta[x3 + y2 * bw] == 6) {
            return true;
        }

        return false;
    }

}
