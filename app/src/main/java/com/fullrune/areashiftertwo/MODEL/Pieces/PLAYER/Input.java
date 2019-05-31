package com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER;

import com.fullrune.areashiftertwo.MODEL.Pieces.Point;

import java.io.Serializable;

/**
 * Created by Marcus on 2017-04-25.
 */

public enum Input implements Serializable {

    MOVE_UP,
    MOVE_DOWN,
    MOVE_LEFT,
    MOVE_RIGHT,
    KILLED,

    W,SW,NW,E,SE,NE,N,S, STOP;

    public static int getValue(Input input){
        if(input == MOVE_UP)
            return 0;
        if(input == MOVE_RIGHT)
            return 1;
        if(input == MOVE_DOWN)
            return 2;
        if(input == MOVE_LEFT)
            return 3;

        return -100;
    }

    public static Input intToDirection(int value){
        if(value == 0)
            return MOVE_UP;
        if(value == 1)
            return MOVE_RIGHT;
        if(value == 2)
            return MOVE_DOWN;
        if(value == 3)
            return MOVE_LEFT;

        return null;
    }

    public static Point getMovePoint(int x,int y, Input input){
        Point result = new Point(x,y);
        if(input == MOVE_UP)
            result.y -= 1;
        if(input == MOVE_RIGHT)
            result.x += 1;
        if(input == MOVE_DOWN)
            result.y += 1;
        if(input == MOVE_LEFT)
            result.x -= 1;
        result.direction = input;
        return result;
    }

    public static Input getStartWheel(Input start){
        if(start == MOVE_UP)
            return MOVE_LEFT;
        if(start == MOVE_LEFT)
            return MOVE_DOWN;
        if(start == MOVE_DOWN)
            return MOVE_RIGHT;
        if(start == MOVE_RIGHT)
            return MOVE_UP;
        return null;
    }

    public static Input getIncrRight(Input input, int i){
        int curVal = Input.getValue(input);
        return Input.intToDirection((curVal+i)%4);
    }

    public Input getDirection(int velX, int velY){
        if(velX == 0){
            if(velY > 0){
                return S;
            }
            if(velY < 0){
                return N;
            }
            return STOP;
        }
        if(velY == 0){
            if(velX > 0){
                return E;
            }
            if(velX < 0){
                return W;
            }
            return STOP;
        }

        if(velX < 0 && velY < 0){
            return NW;
        }
        if(velX > 0 && velY < 0){
            return NE;
        }
        if(velX < 0 && velY > 0){
            return SW;
        }
        if(velX > 0 && velY > 0){
            return SE;
        }

        return STOP;
    }

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
