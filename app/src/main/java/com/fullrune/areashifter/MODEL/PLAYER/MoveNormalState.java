package com.fullrune.areashifter.MODEL.PLAYER;

import android.util.Log;

import com.fullrune.areashifter.MODEL.Model;

/**
 * Created by Marcus on 2017-04-25.
 */

public class MoveNormalState implements PlayerState {
    private static final long serialVersionUID = 68794884007033L;

    @Override
    public PlayerState handleInput(Player player, Input input) {

        if(input == Input.KILLED)
            return new RespawnState();


            if(input.isOpposite(player.getDirection())){
                player.setDirection(null);
                return new WaitingState();
            }


        player.setDirection(input);

        if(!player.isMovePossible()){
            player.setDirection(null);
            return new WaitingState();
        }

        if(player.isMoveRisky())
            return new MoveRiskyState();

        return null;
    }

    private boolean manyPaths(Player player){
        int[][] m  = player.getModel().getMap();
        int counter = 0;
        Model t = player.getModel();
        int x = player.getX();
        int y = player.getY();

        switch(player.getDirection()){
            case MOVE_UP:
                if(t.isPointOnLine(x+1, y))
                    counter++;
                if(t.isPointOnLine(x-1, y))
                    counter++;
                if(t.isPointOnLine(x, y-1))
                    counter++;
                break;
            case MOVE_DOWN:
                if(t.isPointOnLine(x+1, y))
                    counter++;
                if(t.isPointOnLine(x-1, y))
                    counter++;
                if(t.isPointOnLine(x, y+1))
                    counter++;
                break;
            case MOVE_LEFT:
                if(t.isPointOnLine(x+1, y))
                    counter++;
                if(t.isPointOnLine(x, y+1))
                    counter++;
                if(t.isPointOnLine(x, y-1))
                    counter++;
                break;
            case MOVE_RIGHT:
                if(t.isPointOnLine(x, y+1))
                    counter++;
                if(t.isPointOnLine(x-1, y))
                    counter++;
                if(t.isPointOnLine(x, y-1))
                    counter++;
                break;
        }

        return counter >= 2;
    }

    @Override
    public void update(Player player, long timeDiff) {

        if(player.isOnLine()) {
            if(player.isMovePossible())
                player.moveXY(player.getxVel(), player.getyVel());
            else
            {
                //Log.i("HUH1111", " x: " + player.getX() + " y: " + player.getY() + "MAP: " + player.getModel().getMap()[player.getX()][player.getY()+1]);
                player.setDirection(null);
                player.setState(new WaitingState());
                return;
            }

            if(manyPaths(player)){
                player.setDirection(null);
                player.setState(new WaitingState());
                return;
            }

        }
        else{
            while(!player.isOnLine()){
                if(player.getxVel() > 0)
                    player.moveXY(-1,0);
                else if(player.getxVel() < 0)
                    player.moveXY(1, 0);
                else if(player.getyVel() > 0)
                    player.moveXY(0,-1);
                else if (player.getyVel() < 0)
                    player.moveXY(0,1);
            }
            //Log.i("TEST123: ",  "x: " + player.getX() + " y: " + player.getY());
            //player.moveXY(-player.getxVel(), -player.getyVel());
           // player.moveToEdge();
            player.setState(new WaitingState());
        }
    }

    @Override
    public void enter(Player player) {

    }
}
