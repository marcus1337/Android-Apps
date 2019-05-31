package com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER;


import android.util.Log;

import java.util.ArrayList;

import com.fullrune.areashiftertwo.MODEL.LOGIC.PathHelp;
import com.fullrune.areashiftertwo.MODEL.MapValue;
import com.fullrune.areashiftertwo.MODEL.Pieces.Point;
import com.fullrune.areashiftertwo.MODEL.Pieces.UnitHelp;

/**
 * Created by Marcus on 2017-04-25.
 */

public class MoveRiskyState implements PlayerState {

    private int originX, originY;
    private static final long serialVersionUID = 68794884005033L;

    @Override
    public PlayerState handleInput(Player player, Input input) {

        if(input == Input.KILLED){
            player.setX(originX);
            player.setY(originY);
            return new RespawnState();
        }

        if (input.isOpposite(player.getDirection())) {
            return null;
        }else{
         //  Log.i("LANTESTA ", "G: " + input);
         //   player.setDirRiskEnd(input);
         //   Log.i("LANTESTA ", "G2: " + player.getDirRiskEnd());
            player.setDirectionAdjustVelocity(input);
        }

        return null;
    }

    private void moveNormalRisky(Player player){
        int[][] map = player.getModel().getMap();
        Point current = new Point(player.getX(), player.getY());
        Point target = new Point(player.getX()+player.getxVel(), player.getY() + player.getyVel());
        ArrayList<Point> track = new ArrayList<Point>();
        Point wallPoint = PlayerHelp.detectPlayerEdgeHit(current, target, map, track, MapValue.EDGE, player);

        for(Point p : track){
            if(!p.isEqual(target)){
                player.addLineToTrack(p);
                map[p.x][p.y] = MapValue.LINE.getValue();
            }
        }

        if(wallPoint == null) {
            player.moveXY(player.getxVel(), player.getyVel());
        }
        else{
          //  Log.i("WOOP WOOP", "A: " + wallPoint.x + " " + wallPoint.y);
            player.setRiskComplete(true);
            player.setXY(wallPoint.x, wallPoint.y);
            player.setState(new WaitingState());
        }
    }

    private void checkHitRiskLine(Player player){
        int[][] map = player.getModel().getMap();
        int extraX = 0; int extraY = 0;
        /*if(player.getDirection() == Input.MOVE_DOWN){
            extraY = 1;
        }
        if(player.getDirection() == Input.MOVE_UP){
            extraY = -1;
        }
        if(player.getDirection() == Input.MOVE_LEFT){
            extraX = -1;
        }
        if(player.getDirection() == Input.MOVE_RIGHT){
            extraX = 1;
        }*/

        Point current = new Point(player.getX() +extraX, player.getY() + extraY);
        Point target = new Point(player.getX()+player.getxVel(), player.getY() + player.getyVel());
        Point linePoint = UnitHelp.detectMapHit(current, target, map, null, MapValue.LINE);

        if(linePoint != null) {
            player.handleInput(Input.KILLED);
            return;
        }
    }

    @Override
    public void update(Player player) {
        moveNormalRisky(player);
        checkHitRiskLine(player);
    }

    private void moveOneInitialStep(Player player){
        int xVelNorm = 0; int yVelNorm = 0;
        if(player.getxVel() != 0){
            xVelNorm = player.getxVel() / Math.abs(player.getxVel());
        }
        if(player.getyVel() != 0){
            yVelNorm = player.getyVel() / Math.abs(player.getyVel());
        }
        player.moveXY(xVelNorm, yVelNorm);
       // player.addLineToTrack(new Point(player.getX(), player.getY()));
     //   player.getModel().getMap()[player.getX()][player.getY()] = MapValue.LINE.getValue();
    }

    @Override
    public void enter(Player player) {
     //   Log.i("NYSTATE_A ", "moverisky: " + player.getY() + " max: " + PathHelp.boxTop);
        //Log.i("NYSTATE_A ", "MapA: " + player.getModel().getMap()[player.getX()][player.getY()] + " MapB: " + player.getModel().getMap()[player.getX()][PathHelp.boxTop]);
        originX = player.getX();
        originY = player.getY();
        player.setRiskFailed(false);
     //   player.setDirRiskEnd(player.getDirection());
        moveOneInitialStep(player);
        moveNormalRisky(player);
    }

    @Override
    public void end(Player player) {
        if(!player.isRiskComplete())
            player.setRiskFailed(true);
    }
}
