package com.fullrune.areashiftertwo.MODEL.Pieces;

import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input;

import java.io.Serializable;

/**
 * Created by Marcus on 2017-04-26.
 */

public class Point implements Serializable {
    private static final long serialVersionUID = -29238982928391L;
    public int x;
    public int y;
    public Input direction;

    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y, Input direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
    }

    public boolean isEqual(Point point){
        return point.x == x && point.y == y;
    }

}
