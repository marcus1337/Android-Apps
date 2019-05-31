package test.gameapp.MODEL.SERVER.PLAYER;

/**
 * Created by Andreas on 2016-12-28.
 */

public class Defender extends Player{
    public Defender(float x,float y,float angle,float movementspeed,long gold,int hp,int damage,int type){
        super(x,y,angle,movementspeed,gold,hp,damage,type,0);
    }
}