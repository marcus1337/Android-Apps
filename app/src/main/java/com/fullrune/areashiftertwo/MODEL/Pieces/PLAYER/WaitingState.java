package com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER;

import android.util.Log;

import com.fullrune.areashiftertwo.MODEL.Pieces.UnitHelp;

/**
 * Created by Marcus on 2017-04-25.
 */

public class WaitingState implements PlayerState {
    private static final long serialVersionUID = -29238982928391L;
    @Override
    public PlayerState handleInput(Player player, Input input) {

        if(input == Input.KILLED)
            return new RespawnState();

        int[][] map = player.getModel().getMap();
        player.setDirectionAdjustVelocity(input); //KOLLA IFALL MOVE ÄR MÖJLIGT
       // Log.i("NYSTATE", "A: " + (input == Input.MOVE_LEFT) + " test: " +player.getxVel());
        if(!UnitHelp.isMovePossible(map, player)){
            player.setDirectionAdjustVelocity(null);
            return null;
        }
        if(UnitHelp.isMoveRisky(map, player))
            return new MoveRiskyState();
        else
            return new MoveNormalState();

        //return null;
    }

    @Override
    public void update(Player player) {

    }

    @Override
    public void enter(Player player) {
       // Log.i("NYSTATE ", "waitstate1 " + "x: " + player.getX() + " y: " + player.getY());
        player.setDirectionAdjustVelocity(null);
    }

    @Override
    public void end(Player player) {

    }
}
