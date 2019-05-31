package test.gameapp.MODEL.SERVER;

import java.io.Serializable;
import java.util.ArrayList;

import test.gameapp.MODEL.DATA.PlayerAction;
import test.gameapp.MODEL.DATA.PlayerInfo;
import test.gameapp.MODEL.CLIENT.ClientModel;
import test.gameapp.MODEL.CLIENT.ClientModelGameInfo;
import test.gameapp.MODEL.CLIENT.ClientModelItem;
import test.gameapp.MODEL.CLIENT.ClientModelPlayer;
import test.gameapp.MODEL.SERVER.COLLISION.CollisionDetection;
import test.gameapp.MODEL.SERVER.ITEM.Cannonball;
import test.gameapp.MODEL.SERVER.ITEM.Chest;
import test.gameapp.MODEL.SERVER.ITEM.Item;
import test.gameapp.MODEL.SERVER.ITEM.Tower;
import test.gameapp.MODEL.SERVER.ITEM.Wall;
import test.gameapp.MODEL.SERVER.PLAYER.Attacker;
import test.gameapp.MODEL.SERVER.PLAYER.Defender;
import test.gameapp.MODEL.SERVER.PLAYER.Player;

/**
 * Created by Andreas on 2016-12-28.
 */

public class ServerModel implements Serializable {

    private int mapsizex,mapsizey;

    private Player[] player;//array that holds the players
    private Item[] item;    //array that holds the items

    private boolean gameOver,goldTaken;

    public ServerModel(PlayerInfo defenderInfo,PlayerInfo attackerInfo){
        mapsizex = 2048;
        mapsizey = 2048;
        createAndAddPlayersToArray(defenderInfo,attackerInfo);
        createAndAddItemsToArray();
        gameOver = false;
        goldTaken = false;
    }


    /**
     * This function takes a playernr and a playerAction as arguments.
     * Depending on the playeraction different actions will be made in the ServerModel.
     * The function will then return an arraylist of objects that has changed.
     *
     * @param playernr
     * @param playerAction
     * @return list of Objects that has changed in the model
     */
    public ArrayList<Object> playerAction(int playernr, PlayerAction playerAction){
        ArrayList<Object> returnlist = new ArrayList<Object>();

        if( (playernr < 0 || playernr >1))
            return returnlist;

        if(player[playernr].getDead()){
            returnlist.add(getClientModelPlayer(playernr));
            return returnlist;
        }

        if(playerAction.isButtonAttack())   //If the player pressed the attack-button
            playerAttack(playernr,returnlist);
        else if(playerAction.isButtonUse())
            playerUse(playernr,returnlist);     //If the player pressed the use-button

        float playerPrevX = player[playernr].getX(); //Save the players current x and y position. These will be used if the player moves
        float playerPrevY = player[playernr].getY(); //and collides with something to "reset" the players position at the end of the function.

        if(playerAction.isUp())
            player[playernr].movePlayerUp();
        else if(playerAction.isDown())
            player[playernr].movePlayerDown();
        else if(playerAction.isLeft())
            player[playernr].movePlayerLeft();
        else if(playerAction.isRight())
            player[playernr].movePlayerRight();

        if(playerPrevX!=player[playernr].getX() || playerPrevY!=player[playernr].getY()){//IF the player moved
            if(CollisionDetection.collisionBetweenPlayers(player[0],player[1],15) || CollisionDetection.collisionBetweenPlayerAndItems(player[playernr],item,15)!=null){//If the player collides with something
                player[playernr].setX(playerPrevX);//Set the players X position to the old player X position
                player[playernr].setY(playerPrevY);//Set the players Y position to the old player Y position
            }
            CollisionDetection.keepPlayerInsideArea(player[playernr],205,mapsizex-205,205,mapsizey-205);
        }

        if(player[1].getY()>1600 && goldTaken && !gameOver){
            player[0].setHp(0);
            player[0].setDead(true);
            player[0].setCollision(false);
            gameOver(returnlist);
        }
        returnlist.add(getClientModelPlayer(playernr));
        return returnlist;
    }//public void

    public void gameOver(ArrayList<Object> returnlist){
        gameOver = true;
        item[47].setDead(true);//Kill all
        item[45].setDead(true);//the towers.
        item[43].setDead(true);
        item[41].setDead(true);
        for(int i = 0;i<2;i++){
            if(player[i].getHp()<1){
                long givegold = player[i].getGold();
                if(givegold<0)
                    givegold = 0;
                returnlist.add(new ClientModelGameInfo(false,true,((i-1)*(i-1)),givegold,((i-1)*(i-1))));
                if(givegold>0) {
                    player[((i-1)*(i-1))].setGold(player[i].getGold() + player[((i-1)*(i-1))].getGold());
                    player[i].setGold(0);
                }
            }
        }
    }




    /*
     * Get functions
     */
    public ClientModel getClientModel(){
        ClientModelPlayer[] clientModelPlayers = new ClientModelPlayer[2];
        for(int i =0 ;i<player.length;i++)
            clientModelPlayers[i] = getClientModelPlayer(i);

        ClientModelItem[] clientModelItems = new ClientModelItem[item.length];
        for(int i =0 ;i<item.length;i++){
            ClientModelItem tmp = getClientModelItem(i);
            if(tmp!=null)
                clientModelItems[i] = tmp;
        }
        return new ClientModel(clientModelPlayers,clientModelItems);
    }

    public ArrayList<Tower> getTowers(){
        ArrayList<Tower> towers = new ArrayList<Tower>();
        for(int i=0;i<item.length;i++){
            if(item[i] instanceof Tower)
                towers.add((Tower) item[i]);
        }
        return towers;
    }

    public ArrayList<Cannonball> getCannonballs(){
        ArrayList<Cannonball> cannonballs = new ArrayList<Cannonball>();
        for(int i=0;i<item.length;i++){
            if(item[i] instanceof Cannonball)
                cannonballs.add((Cannonball) item[i]);
        }
        return cannonballs;
    }

    public ClientModelItem getClientModelItem(int itemnr){
        if(itemnr>=0 && itemnr<item.length)
            return new ClientModelItem(item[itemnr].getX(),item[itemnr].getY(),item[itemnr].getAngle(),item[itemnr].getType(),item[itemnr].getId(),item[itemnr].getDraw(),item[itemnr].getDead());
        return null;
    }

    public ClientModelPlayer getClientModelPlayer(int playernr){
        return new ClientModelPlayer(player[playernr].getX(),player[playernr].getY(),player[playernr].getAngle(),player[playernr].getHp(),player[playernr].getId(),player[playernr].getType(),player[playernr].getDead());
    }

    public Player getPlayer(int playernr){
        return player[playernr];
    }

    /*
     * Private functions
     */


    private void playerAttack(int playernr,ArrayList<Object> returnlist){

        if(CollisionDetection.attackPlayer(player[playernr],player[((playernr-1)*(playernr-1))],40)){//Check if the player attacked the other player
            player[((playernr-1)*(playernr-1))].setHp(player[((playernr-1)*(playernr-1))].getHp()-player[playernr].getDamage());//Decreace the other players "hp" with the attacking players "damage"
            if(player[((playernr-1)*(playernr-1))].getHp()<1){//If the attacked player has less than 1hp
                player[((playernr-1)*(playernr-1))].setDead(true);//the attacked player is now dead
                player[((playernr-1)*(playernr-1))].setCollision(false);//the attacked player should not be included in collision
                player[((playernr-1)*(playernr-1))].setHp(0);
                gameOver(returnlist);
            }
            returnlist.add(getClientModelPlayer(((playernr-1)*(playernr-1))));
            return;
        }

        Item attackedItem = CollisionDetection.attackItem(player[playernr],item,40);
        if(attackedItem!=null){//An item was attacked
            attackedItem.setHp(attackedItem.getHp()-player[playernr].getDamage());//Decreace the items "hp" with the attacking players "damage"
            if(attackedItem.getHp()<1){//If the attacked item has less than 1hp
                attackedItem.setDead(true);//the attacked item is now dead.
                if(attackedItem instanceof Wall){//A wall should disappear and be able to walk through when its dead
                    attackedItem.setCollision(false);//The wall should not be included in collision
                    attackedItem.setDraw(false);//The wall should not be drawn anymore
                }
                returnlist.add(new ClientModelItem(attackedItem.getX(),attackedItem.getY(),attackedItem.getAngle(),attackedItem.getType(),attackedItem.getId(),attackedItem.getDraw(),attackedItem.getDead()));
            }
        }
    }

    private void playerUse(int playernr,ArrayList<Object> returnlist){
        if(playernr!=1)
            return;

        Item usedItem = CollisionDetection.attackItem(player[playernr],item,40);
        if(usedItem == null)
            return;

        if(usedItem instanceof Chest) {
            if(usedItem.getType()==10){//Chest is closed.
                usedItem.setType(11);//Chest is now open.
                returnlist.add(new ClientModelItem(usedItem.getX(),usedItem.getY(),usedItem.getAngle(),usedItem.getType(),usedItem.getId(),usedItem.getDraw(),usedItem.getDead()));
                long givegold = player[1].getGold();
                if(givegold<0)
                    givegold = 0;
                goldTaken = true;
                player[0].setMovementspeed(4);
                returnlist.add(new ClientModelGameInfo(false,false,1,givegold,-1));
                if(givegold>0) {
                    player[1].setGold(player[0].getGold() + player[1].getGold());
                    player[0].setGold(0);
                }
            }
        }
    }//private void

    private void createAndAddPlayersToArray(PlayerInfo defenderInfo, PlayerInfo attackerInfo){
        player = new Player[2];
        player[0] = new Defender(/*x*/1000,/*y*/ 250,/*angle*/180,/*movementspeed*/2,/*gold*/ defenderInfo.getGold(),/*hp*/99,/*damage*/10+defenderInfo.getSelectedSword()*5,/*type*/0);
        player[1] = new Attacker(/*x*/1000,/*y*/1750,/*angle*/0,  /*movementspeed*/2,/*gold*/ attackerInfo.getGold(),/*hp*/99,/*damage*/10+attackerInfo.getSelectedSword()*5,/*type*/1);
    }

    private void createAndAddItemsToArray(){
        item = new Item[51];
        for(int i=0;i<41;i++)
            item[i]=new Wall(25+50*i,950,50,50,0,100,3,i);
        item[41]=new Tower(480,800,50,50,0,100,2,41);
        item[42]=new Cannonball(480,800,25,25,0,100,4,42,25);
        item[43]=new Tower(880,800,50,50,0,100,2,43);
        item[44]=new Cannonball(880,800,25,25,0,100,4,44,25);
        item[45]=new Tower(1260,800,50,50,0,100,2,45);
        item[46]=new Cannonball(1260,800,25,25,0,100,4,46,25);
        item[47]=new Tower(1630,800,50,50,0,100,2,47);
        item[48]=new Cannonball(1630,800,25,25,0,100,4,48,25);
        item[49]=new Chest(700,350,25,25,0,100,10,49);
        item[50]=new Chest(1300,350,25,25,0,100,10,50);
    }
}//public class

