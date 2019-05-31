package com.fullrune.halfrobot.LOGIC;

import android.graphics.Canvas;

import com.fullrune.halfrobot.LOGIC.ENTITY.Entity;
import com.fullrune.halfrobot.MISC.Worldmap;

import java.util.ArrayList;

/**
 * Created by Marcus on 2017-09-22.
 */

public class Model {

    public Model(){

    }


    public void update(){

    }

    public void render(Canvas c){

    }

    public long getTicks(){

        return 0;
    }

    public Entity getPlayerEntity() {
        return null;
    }

    public ArrayList<Entity> getAis() {
        return null;
    }

    public Entity getBoss() {
        return null;
    }

    public Worldmap getPlayerMap() {
        return null;
    }
}
