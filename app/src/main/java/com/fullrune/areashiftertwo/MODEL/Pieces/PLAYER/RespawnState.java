package com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER;


import android.util.Log;

import com.fullrune.areashiftertwo.MODEL.Pieces.Point;
import com.fullrune.areashiftertwo.MODEL.Pieces.UnitHelp;

/**
 * Created by Marcus on 2017-05-02.
 */

public class RespawnState implements PlayerState {
    private static final long serialVersionUID = -29238982928391L;
    private long start_time;

    private boolean timeOverMove(){
        return System.currentTimeMillis() - start_time > 1700;
    }

    private boolean timeOverLife(){
        return System.currentTimeMillis() - start_time > 3000;
    }

    @Override
    public PlayerState handleInput(Player player, Input input) {
        if(timeOverMove()){
            if(player.getDirection() != null){
                if(input.isOpposite(player.getDirection())){
                    player.setDirectionAdjustVelocity(null);
                }
            }
            player.setDirectionAdjustVelocity(input);
            int[][] map = player.getModel().getMap();
            if(!UnitHelp.isMovePossible(map, player) || UnitHelp.isMoveRisky(map, player))
                player.setDirectionAdjustVelocity(null);
        }
        return null;
    }

    @Override
    public void update(Player player) {
        if(timeOverLife()){
            player.setAlive(true);
            player.setState(new WaitingState());
            return;
        }
        if(timeOverMove()){
            player.setMovable(true);
            move(player);
        }
    }

    @Override
    public void enter(Player player) {
       // Log.i("NYSTATE: ", "respawn occured 39442334");
        player.setShouldClearRiskLines(true);
        start_time = System.currentTimeMillis();
        player.setDirectionAdjustVelocity(null);
        player.setAlive(false);
        player.setMovable(false);
        player.setLives(player.getLives()-1);
      //  player.getRiskLines().clear();
    }

    @Override
    public void end(Player player) {

    }


    private void move(Player player){
        if(player.getDirection() == null)
            return;

        int[][] map = player.getModel().getMap();
        Point maxPoint1 = UnitHelp.walkAlongLine(map, player);
        if(maxPoint1 != null){
            player.setDirectionAdjustVelocity(null);
            player.setXY(maxPoint1.x, maxPoint1.y);
            return;
        }
        player.moveXY(player.getxVel(), player.getyVel());
    }



}
