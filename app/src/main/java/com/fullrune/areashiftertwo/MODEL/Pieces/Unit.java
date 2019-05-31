package com.fullrune.areashiftertwo.MODEL.Pieces;

import java.io.Serializable;

import com.fullrune.areashiftertwo.MODEL.MapValue;

/**
 * Created by Marcus on 2017-04-25.
 */

public abstract class Unit implements Serializable {

    private static final long serialVersionUID = 687948840043L;

    private int x;
    private int y;
    private int radius;

    private int xVel;
    private int yVel;
    private int baseVelocity;

    public int getBaseVelocity(){
        return baseVelocity;
    }

    public void setBaseVelocity(int baseVelocity){
        this.baseVelocity = baseVelocity;
    }

    public int getxVel() {
        return xVel;
    }

    public void setxVel(int xVel) {
        this.xVel = xVel;
    }

    public int getyVel() {
        return yVel;
    }

    public void setyVel(int yVel) {
        this.yVel = yVel;
    }

    public void setxyVel(int xVel, int yVel){
        this.xVel = xVel;
        this.yVel = yVel;
    }

    public Unit(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        xVel = yVel = 0;
    }

    public void setRadius(int radius){
        this.radius = radius;
    }
    public int getRadius(){
        return radius;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void setXY(int x, int y){
        this.x = x; this.y = y;
    }

    public void moveXY(int moveX, int moveY){
        x += moveX;
        y += moveY;
    }





}
