package test.gameapp.MODEL.CLIENT;

import java.io.Serializable;

/**
 * Created by Andreas on 2016-12-28.
 */

public class ClientModelPlayer implements Serializable {

    private float x,y,angle;
    private int hp,id,type;
    private boolean dead;

    public ClientModelPlayer(float x,float y,float angle,int hp,int id,int type,boolean dead){
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.hp = hp;
        this.id= id;
        this.type= type;
        this.dead = dead;
    }

    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public float getAngle(){
        return angle;
    }
    public int getHp(){ return hp; }
    public int getId(){
        return id;
    }
    public int getType(){
        return type;
    }
    public boolean getDead(){
        return dead;
    }
}
