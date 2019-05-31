package com.fullrune.halfrobot.LOGIC.PHYSICS;

import com.fullrune.halfrobot.LOGIC.ENTITY.Entity;
import com.fullrune.halfrobot.LOGIC.Model;
import com.fullrune.halfrobot.MISC.Worldmap;

/**
 * Created by Marcus on 2017-09-26.
 */

public class PhysicsBullet extends Physics {

    public void update(Model model, Entity entity){
        Worldmap worldmap = model.getPlayerMap();
        bw = worldmap.getWidth();
	    char[] karta = worldmap.getMapArr();
        int xVel = entity.getVelocityX();
        int yVel = entity.getVelocityY();
        int y = (entity.getY()) / 20;
        int x = (entity.getX()) / 20;
        if (karta[x + y * bw] == 1) {
            entity.setDead(true);
        }
        entity.setX(entity.getX() + xVel);
        entity.setY(entity.getY() + yVel);
    }
}
