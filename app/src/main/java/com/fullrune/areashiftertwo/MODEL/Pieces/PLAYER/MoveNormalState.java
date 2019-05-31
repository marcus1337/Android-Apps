package com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER;

/**
 * Created by Marcus on 2017-04-25.
 */


import android.util.Log;

import com.fullrune.areashiftertwo.MODEL.Pieces.Point;
import com.fullrune.areashiftertwo.MODEL.Pieces.UnitHelp;

/**
 * TODO
 * detect MANY paths, if so set state to Waiting
 * <p>
 * only move along a line and stop if opposite direction OR end of line OR MANY paths
 * <p>
 * if move goes into EMPTY-mapvalue change state to RiskyState
 */

public class MoveNormalState implements PlayerState {
    private static final long serialVersionUID = 68794884007033L;
    private boolean firstMove;

    @Override
    public PlayerState handleInput(Player player, Input input) {

        if (input == Input.KILLED)
            return new RespawnState();

        if (input.isOpposite(player.getDirection())) {
            player.setDirectionAdjustVelocity(null);
            return new WaitingState();
        }

        player.setDirectionAdjustVelocity(input);

        if (!UnitHelp.isMovePossible(player.getModel().getMap(), player)) {
            player.setDirectionAdjustVelocity(null);
            return new WaitingState();
        }

        if (UnitHelp.isMoveRisky(player.getModel().getMap(), player))
            return new MoveRiskyState();

        return null;
    }

    @Override
    public void update(Player player) {
        int[][] map = player.getModel().getMap();

        Point maxCrossing = UnitHelp.walkDetectCrossing(map, player);
        if(maxCrossing != null && firstMove){
            player.setDirectionAdjustVelocity(null);
            player.setXY(maxCrossing.x, maxCrossing.y);
            player.setState(new WaitingState());
            return;
        }

        Point maxPoint1 = UnitHelp.walkAlongLine(map, player);
        if(maxPoint1 != null){
            player.setDirectionAdjustVelocity(null);
            player.setXY(maxPoint1.x, maxPoint1.y);
            player.setState(new WaitingState());
            return;
        }

        player.moveXY(player.getxVel(), player.getyVel());
        firstMove = true;
    }

    @Override
    public void enter(Player player) {
     //   Log.i("NYSTATE ", "movestate2 "+ "x: " + player.getX() + " y: " + player.getY());
    }

    @Override
    public void end(Player player) {

    }
}
