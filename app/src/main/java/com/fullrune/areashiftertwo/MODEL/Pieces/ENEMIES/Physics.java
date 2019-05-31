package com.fullrune.areashiftertwo.MODEL.Pieces.ENEMIES;

import com.fullrune.areashiftertwo.MODEL.Model;
import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Player;
import com.fullrune.areashiftertwo.MODEL.Pieces.Unit;

public abstract class Physics {

    protected Unit unit;

    public Physics(Unit unit){
        this.unit = unit;
    }

    public void process(Model model){
        int[][] map = model.getMap();
        //Player player = model.getPlayer();

        //checkHitPlayer(player, map);
        moveNormal(map);
        unit.moveXY(unit.getxVel(), unit.getyVel());
        bugCorrectionLazy(model.getMapWidth(), model.getMapHeight());
    }

   // public abstract void checkHitPlayer(Player player, int[][] map);
    public abstract void moveNormal(int[][] map);

    public void bugCorrectionLazy(int mwidth, int mheight){
        int rh = unit.getRadius()/2;

        while(unit.getX() > mwidth)
            unit.setX(unit.getX()-2);
        while(unit.getX() < 0)
            unit.setX(unit.getX()+2);
        while(unit.getY() < 0)
            unit.setY(unit.getY()+2);
        while(unit.getY() > mheight)
            unit.setY(unit.getY()-2);
        if(unit.getX() == mwidth-1)
            unit.setX(unit.getX()-3);
        if(unit.getX() == 0)
            unit.setX(3);
        if(unit.getY() == mheight-1)
            unit.setY(unit.getY()-3);
        if(unit.getY() == 0)
            unit.setY(3);
    }




}
