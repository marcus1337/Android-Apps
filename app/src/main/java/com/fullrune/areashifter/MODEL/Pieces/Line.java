package com.fullrune.areashifter.MODEL.Pieces;

import java.io.Serializable;

/**
 * Created by Marcus on 2017-04-25.
 */

public class Line implements Serializable {
    private static final long serialVersionUID = -29238982928391L;
    private int x;
    private int y;
    private int endx;
    private int endy;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getEndx() {
        return endx;
    }

    public int getEndy() {
        return endy;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int maxXP(){
        return Math.max(getX(), getEndx());
    }

    public int maxYP(){
        return Math.max(getY(), getEndy());
    }

    public int minXP(){
        return Math.min(getX(), getEndx());
    }

    public int minYP(){
        return Math.min(getY(), getEndy());
    }


    public int getWidth(){
        return endx - x;
    }

    public int getHeight(){
        return endy-y;
    }

    public static boolean isVertical(Line line){
        return (line.getX() == line.getEndx());
    }

    public static boolean isHorizontal(Line line){
        return line.getY() == line.getEndy();
    }

    public boolean hasPoint(Point p){
        if( (getX() <= p.getX() && p.getX() <= getEndx()) || (getX() >= p.getX() && p.getX() >= getEndx())){
            if((getY() <= p.getY() && p.getY() <= getEndy()) || (getY() >= p.getY() && p.getY() >= getEndy())){
                return true;
            }
        }
        return false;
    }

    public Point intersect(Line line){

        if(isHorizontal(this)== isHorizontal(line) || isVertical(this)==isVertical(line))
            return null;
        int x0, x1, x2, y0, y1, y2;
        if(isVertical(this)){
            x0 = getX();
            y1 = getY();
            y2 = getEndy();

            y0 = line.getY();
            x1 = line.getX();
            x2 = line.getEndx();

            if((y1 <= y0 && y0 <= y2)||(y2 <= y0 && y0 <= y1)){
                if((x1 <= x0 && x0 <= x2) || (x2 <= x0 && x0 <= x1)){
                    return new Point(x0,y0);
                }
            }

        }else{
            y0 = getY();
            x1 = getX();
            x2 = getEndx();

            x0 = line.getX();
            y1 = line.getY();
            y2 = line.getEndy();


            if((y1 <= y0 && y0 <= y2)||(y2 <= y0 && y0 <= y1)){
                if((x1 <= x0 && x0 <= x2) || (x2 <= x0 && x0 <= x1)){
                    return new Point(x0,y0);
                }
            }

        }

        return null;
    }

    public Line(int x, int y, int endx, int endy){
        this.x=x;this.y=y;this.endx=endx;this.endy=endy;
    }



}
