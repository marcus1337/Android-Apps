package com.fullrune.areashifter.MODEL.Pieces;

import java.io.Serializable;

/**
 * Created by Marcus on 2017-04-25.
 */

public abstract class Unit implements Serializable {

    private static final long serialVersionUID = 687948840043L;

    private int x;
    private int y;
    private int radius;

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

    private int xVel;
    private int yVel;

    public Unit(int x, int y, int radius){
        this.x = x;
        this.y = y;
        this.radius = radius;
        xVel = yVel = 0;
    }

    boolean isCollision(Unit unit){
        if(Math.pow((unit.getX()-x),2) + Math.pow((y-unit.getY()),2) <= Math.pow((radius+unit.getRadius()),2))
            return true;
        return false;
    }

    boolean isCollision(Line line){
        if(x + radius > line.getEndx() || x - radius < line.getX() || y+radius > line.getEndy() || y-radius < line.getY())
            return false;
        return true;
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
    public void moveXY(int moveX, int moveY){
        x += moveX;
        y += moveY;
    }




}
