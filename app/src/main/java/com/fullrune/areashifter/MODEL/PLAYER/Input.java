package com.fullrune.areashifter.MODEL.PLAYER;

import java.io.Serializable;

/**
 * Created by Marcus on 2017-04-25.
 */

public enum Input implements Serializable{

    MOVE_UP,
    MOVE_DOWN,
    MOVE_LEFT,
    MOVE_RIGHT,
    KILLED;

    public boolean isOpposite(Input b){
        switch(this){
            case MOVE_UP:
                if(b == MOVE_DOWN)
                    return true;
                break;
            case MOVE_DOWN:
                if(b == MOVE_UP)
                    return true;
                break;
            case MOVE_LEFT:
                if(b == MOVE_RIGHT)
                    return true;
                break;
            case MOVE_RIGHT:
                if(b == MOVE_LEFT)
                    return true;
                break;
        }
        return false;
    }

}
