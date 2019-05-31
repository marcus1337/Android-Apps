package test.gameapp.MODEL.SERVER.PLAYER;

import java.io.Serializable;

/**
 * Created by Andreas on 2016-12-28.
 */

public abstract class Player  implements Serializable {

    private float x,y,angle, movementspeed;
    private int hp,damage,type,id;
    private boolean dead,draw,collision;
    private long gold;

    public Player(float x,float y,float angle,float movementspeed,long gold, int hp,int damage,int type,int id){
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.movementspeed = movementspeed;
        this.gold = gold;
        this.hp=hp;
        this.damage = damage;
        this.type = type;
        this.id = id;
        draw = true;
        collision = true;
        dead = false;
    }

    /*
     * Button-functions
     */

    public void movePlayerUp(){
        angle = 0f;
        y = y-movementspeed;
    }

    public void movePlayerDown(){
        angle = 180f;
        y = y+movementspeed;
    }
    public void movePlayerLeft(){
        angle = 90f;
        x = x-movementspeed;
    }
    public void movePlayerRight(){
        angle = 270f;
        x = x+movementspeed;
    }

    /*
     * Set-functions
     */
    public void setX(float x){
        this.x = x;
    }
    public void setY(float y){
        this.y = y;
    }
    public void setAngle(float angle){
        this.angle = angle;
    }
    public void setHp(int hp){
        this.hp = hp;
    }
    public void setDead(boolean dead){
        this.dead=dead;
    }
    public void setDraw(boolean draw){
        this.draw=draw;
    }
    public void setCollision(boolean collision){
        this.collision=collision;
    }
    public void setGold(long gold){ this.gold = gold; }
    /*
     * Get-functions
     */
    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void setMovementspeed(float movementspeed){
        this.movementspeed = movementspeed;
    }
    public float getAngle(){
        return angle;
    }

    public int getDamage(){
        return damage;
    }

    public int getHp(){
        return hp;
    }

    public int getType(){
        return type;
    }

    public int getId(){ return id; }

    public long getGold(){
        return gold;
    }

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


