package test.gameapp.MODEL.CLIENT;

import java.io.Serializable;

/**
 * Created by Andreas on 2016-12-28.
 */

public class ClientModelItem implements Serializable {

    private float x,y,angle;
    private int type,id;
    private boolean draw,dead;

    public ClientModelItem(float x,float y,float angle,int type,int id,boolean draw,boolean dead){
        this.x = x;
        this.y = y;
        this.type =  type;
        this.id =  id;
        this.angle = angle;
        this.draw = draw;
        this.dead = dead;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getAngle() {
        return angle;
    }

    public int getType() {
        return type;
    }

    public int getId(){
        return id;
    }

    public boolean getDraw(){
        return draw;
    }

    public boolean getDead(){
        return dead;
    }
}