package com.fullrune.areashifter.MODEL.PLAYER;

/**
 * Created by Marcus on 2017-04-25.
 */

public class WaitingState implements PlayerState {
    private static final long serialVersionUID = -29238982928391L;
    @Override
    public PlayerState handleInput(Player player, Input input) {

        if(input == Input.KILLED)
            return new RespawnState();

        player.setDirection(input); //KOLLA IFALL MOVE ÄR MÖJLIGT
        if(!player.isMovePossible()){
            player.setDirection(null);
            return null;
        }
        if(player.isMoveRisky())
            return new MoveRiskyState();
        else
            return new MoveNormalState();

        //return null;
    }

    @Override
    public void update(Player player, long timeDiff) {

    }

    @Override
    public void enter(Player player) {
        player.setDirection(null);
    }
}
