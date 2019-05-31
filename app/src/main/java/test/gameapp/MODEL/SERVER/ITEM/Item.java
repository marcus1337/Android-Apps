package test.gameapp.MODEL.SERVER.ITEM;

import java.io.Serializable;

/**
 * Created by Andreas on 2016-12-28.
 */

public abstract class Item  implements Serializable {

    private float x,y,width,height,angle;
    private int hp,type,id;
    private boolean dead,draw,collision;

    public Item(float x,float y,float width, float height,float angle,int hp, int type,int id){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.angle = angle;
        this.hp = hp;
        this.type= type;
        this.id = id;
        draw = true;
        collision = true;
        dead = false;
    }

    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public void setAngle(float angle){ this.angle = angle; }
    public void setHp(int hp) { this.hp = hp; }
    public void setDead(boolean dead){
        this.dead=dead;
    }
    public void setDraw(boolean draw){
        this.draw=draw;
    }
    public void setCollision(boolean collision){
        this.collision=collision;
    }
    public void setType(int type){ this.type = type; }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getWidth(){
        return width;
    }
    public float getHeight(){
        return height;
    }
    public float getAngle() { return angle; }
    public int getHp() { return hp; }
    public int getType(){
        return type;
    }
    public int getId() { return id; }
    public boolean getDead(){
        return dead;
    }
    public boolean getDraw(){
        return draw;
    }
    public boolean getCollision(){
        return collision;
    }
}
