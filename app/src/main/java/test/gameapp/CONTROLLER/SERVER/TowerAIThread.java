package test.gameapp.CONTROLLER.SERVER;

import android.util.Log;

import java.util.ArrayList;

import test.gameapp.MODEL.CLIENT.ClientModelItem;
import test.gameapp.MODEL.SERVER.COLLISION.CollisionDetection;
import test.gameapp.MODEL.SERVER.ITEM.Cannonball;
import test.gameapp.MODEL.SERVER.ITEM.Tower;
import test.gameapp.MODEL.SERVER.PLAYER.Player;
import test.gameapp.MODEL.SERVER.ServerModel;

/**
 * Created by Andreas on 2017-01-05.
 */

public class TowerAIThread extends Thread {

    private Server server;
    private ServerModel serverModel;
    private Tower tower;
    private Cannonball cannonball;
    private Player attacker;
    private boolean fire;
    private int range;

    public TowerAIThread(Server server, ServerModel serverModel, Tower tower, Cannonball cannonball, Player attacker){
        this.server = server;
        this.serverModel = serverModel;
        this.tower = tower;
        this.cannonball = cannonball;
        this.attacker = attacker;
        fire = false;
        range = -1;
    }

    public void run(){
        while(tower.getDead()==false){
            if(!fire && range==-1){
                if(180>(attacker.getX()-tower.getX()) && (attacker.getX()-tower.getX() > -180)){
                    if(180>(attacker.getY()-tower.getY()) && (attacker.getY()-tower.getY() > -180)){
                        fire = true;
                        range = 50;
                        cannonball.setX(tower.getX());
                        cannonball.setY(tower.getY());
                        float angle = 0;
                        if(attacker.getX()>=tower.getX() && attacker.getY()>=tower.getY())
                            angle = 90 + (float) (Math.atan((attacker.getY() - tower.getY())/(attacker.getX() - tower.getX())) * 180 / Math.PI);
                        else if(attacker.getX()<tower.getX() && attacker.getY()>=tower.getY())
                            angle = 90 + 180 + (float) (Math.atan((attacker.getY() - tower.getY())/(attacker.getX() - tower.getX())) * 180 / Math.PI);
                        else if(attacker.getX()<tower.getX() && attacker.getY()<tower.getY())
                            angle = 90 + 180 + (float) (Math.atan((attacker.getY() - tower.getY())/(attacker.getX() - tower.getX())) * 180 / Math.PI);
                        else if(attacker.getX()>=tower.getX() && attacker.getY()<tower.getY())
                            angle = 90 + 360 + (float) (Math.atan((attacker.getY() - tower.getY())/(attacker.getX() - tower.getX())) * 180 / Math.PI);
                        if(angle>360)
                            angle=angle-360;
                        cannonball.setAngle((angle-360)*-1);
                    }
                }
            }

            if(fire){
                cannonball.setX((float) (cannonball.getX() - 6 * ( Math.sin( Math.toRadians(cannonball.getAngle())))));//Move cannonball
                cannonball.setY((float) (cannonball.getY() - 6 * ( Math.cos( Math.toRadians(cannonball.getAngle())))));//Move cannonball
                if(CollisionDetection.collisionBetweenPlayerAndItem(attacker,cannonball,20)){//Check if the cannonball hits the player
                    cannonball.setX(tower.getX());
                    cannonball.setY(tower.getY());
                    fire = false;
                    attacker.setHp(attacker.getHp()-cannonball.getDamage());
                    server.sendObjectOverUDPToClients(serverModel.getClientModelPlayer(1));
                    if(attacker.getHp()<1) {
                        attacker.setDead(true);//the attacked player is now dead
                        attacker.setCollision(false);//the attacked player should not be included in collision
                        attacker.setHp(0);
                        server.sendObjectOverUDPToClients(serverModel.getClientModelPlayer(1));
                        ArrayList<Object> pushlist = new ArrayList<Object>();
                        serverModel.gameOver(pushlist);
                        for(int i=0;i<pushlist.size();i++)
                            server.sendObjectOverUDPToClients(pushlist.get(i));
                    }
                }
                server.sendObjectOverUDPToClients(new ClientModelItem(cannonball.getX(),cannonball.getY(),cannonball.getAngle(),cannonball.getType(),cannonball.getId(),cannonball.getDraw(),cannonball.getDead()));//cannnonball
            }

            if(range==0) {
                range = -1;
                cannonball.setX(tower.getX());
                cannonball.setY(tower.getY());
                fire = false;
                server.sendObjectOverUDPToClients(new ClientModelItem(cannonball.getX(),cannonball.getY(),cannonball.getAngle(),cannonball.getType(),cannonball.getId(),cannonball.getDraw(),cannonball.getDead()));//cannnonball
            }else if(range>0){
                range = range -1;
            }

            try{
                Thread.sleep(50);
            }catch(Exception e){

            }
        }//while
    }//public void
}
