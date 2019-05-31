package test.gameapp.MODEL.CLIENT;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by Andreas on 2016-12-28.
 */

public class ClientModel implements Serializable {

    private ClientModelPlayer[] players;
    private ClientModelItem[] items;

    private boolean gameover,goldTaken;//false eller true
    private int winner; //0 eller 1

    public ClientModel(){
        players = null;
        items = null;
        gameover = false;
        goldTaken = false;
        winner = -1;
    }

    public ClientModel(ClientModelPlayer[] players,ClientModelItem[] items){
        this.players = players;
        this.items = items;
    }

    public boolean getGameover(){
        return gameover;
    }
    public int getWinner(){
        return winner;
    }
    public boolean getGoldTaken(){
        return goldTaken;
    }

    public void updateModel(Object obj){
        if(obj instanceof ClientModelPlayer) {
            replaceClientModelPlayer((ClientModelPlayer) obj);
        }else if(obj instanceof ClientModelItem) {
            replaceClientModelItem((ClientModelItem) obj);
        }else if(obj instanceof ClientModelGameInfo) {
            gameover = ((ClientModelGameInfo) obj).getGameover();
            if(gameover) {
                winner = ((ClientModelGameInfo) obj).getWinner();
            }else{
                if(((ClientModelGameInfo) obj).getGoldreceiver()==1)
                    goldTaken = true;
            }
        }
    }

    /*
     * Get functions (Used by the View)
     */
    public ClientModelItem[] getClientModelItems(){
        if(items!=null)
            return items;
        return new ClientModelItem[0];
    }
    public ClientModelPlayer[] getClientModelPlayers(){
        if(players!=null)
            return players;
        return new ClientModelPlayer[0];
    }
    public ClientModelItem getClientModelItem(int number){
        if(items!= null && number>=0 && number <items.length)
            return items[number];
        return null;
    }
    public ClientModelPlayer getPlayer(int number){
        if(players!=null && number>=0 && number <players.length)
            return players[number];
        return null;
    }

    /*
 * Set functions (Used by the updateModel(Object obj))
 */
    private void setClientModelItems(ClientModelItem[] items){
        this.items = items;
    }
    private void setClientModelPlayers(ClientModelPlayer[] players){
        this.players = players;
    }

    private void replaceClientModelItem(ClientModelItem item){
        if(items==null){
            items = new ClientModelItem[1];
            return;
        }

        for(int i = 0;i<items.length;i++){
            if(items[i].getId()==item.getId()){
                items[i] = item;
                return;
            }
        }
        //If the code gets to this point it is a new item.
        ClientModelItem[] newitems = new ClientModelItem[items.length+1];
        for(int i = 0;i<items.length;i++)
            newitems[i] = items[i];
        newitems[items.length] = item;
        items = newitems;
    }

    private void replaceClientModelPlayer(ClientModelPlayer player){
        if(players==null)
            players = new ClientModelPlayer[2];
        players[player.getId()] = player;
    }
}
