package com.fullrune.areashifter.MODEL.PLAYER;

import com.fullrune.areashifter.MODEL.Pieces.Line;

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
            player.setDirection(input);
            player.getRiskLines().get(0).setX(player.getX());
            player.getRiskLines().get(0).setY(player.getY());
           // Log.i("huh5555", " x: " + player.getX() + " y: " + player.getY() + "MAP: " + player.getModel().getMap()[player.getX()][player.getY()]);
            player.getRiskLines().add(0, new Line(player.getX(), player.getY(), player.getX(), player.getY()));
        }

        return null;
    }

    @Override
    public void update(Player player, long timeDiff) {


        player.getRiskLines().get(0).setX(player.getX());
        player.getRiskLines().get(0).setY(player.getY());
        boolean hitWall = player.hasHitWall();
        if(!hitWall) {
            player.moveXY(player.getxVel(), player.getyVel());
        }
        else
            player.setState(new WaitingState());

        if(player.hitRiskLine()){
            player.handleInput(Input.KILLED);
            return;
        }


        //if(hitWall){
            //player.registerLines();
         //   player.setState(new WaitingState());
       // }

    }

    @Override
    public void enter(Player player) {
       // Log.i("HUH222", "x: " + player.getX() + " y: " + player.getY()+ "MAP: " + player.getModel().getMap()[player.getX()][player.getY()]);
        originX = player.getX();
        originY = player.getY();
        player.moveXY(player.getxVel(), player.getyVel());
        player.getRiskLines().add(new Line(player.getX(), player.getY(),originX, originY));

    }
}
