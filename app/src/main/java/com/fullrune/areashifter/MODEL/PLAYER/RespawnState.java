package com.fullrune.areashifter.MODEL.PLAYER;

import com.fullrune.areashifter.MODEL.Model;

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
                    player.setDirection(null);
                }
            }
            player.setDirection(input);
            if(!player.isMovePossible() ||player.isMoveRisky())
                player.setDirection(null);
        }
        return null;
    }

    @Override
    public void update(Player player, long timeDiff) {
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
        start_time = System.currentTimeMillis();
        player.setDirection(null);
        player.setAlive(false);
        player.setMovable(false);
        player.setLives(player.getLives()-1);
        player.getRiskLines().clear();
    }










    private void move(Player player){
        if(player.getDirection() == null)
            return;

        if(player.isOnLine()) {
            if(player.isMovePossible())
                player.moveXY(player.getxVel(), player.getyVel());
            else
            {
                player.setDirection(null);
                return;
            }

            if(manyPaths(player)){
                player.setDirection(null);
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
            player.setDirection(null);
        }
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

}
