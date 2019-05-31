package test.gameapp.MODEL.CLIENT;

import java.io.Serializable;

/**
 * Created by Andreas on 2017-01-06.
 */

public class ClientModelGameInfo implements Serializable {

    private boolean serverclosed,gameover;
    private int goldreceiver,winner;
    private long gold;

    public ClientModelGameInfo(boolean serverclosed,boolean gameover,int goldreceiver,long gold,int winner){
        this.serverclosed = serverclosed;
        this.gameover = gameover;
        this.goldreceiver = goldreceiver;
        this.gold = gold;
        this.winner = winner;
    }

    public boolean getServerclosed(){
        return serverclosed;
    }
    public boolean getGameover(){
        return gameover;
    }
    public int getWinner() { return winner; }

    /*
     * 0 = Defender
     * 1 = Attacker
     */
    public long goldForMe(int playertype){
        if(playertype==0 && goldreceiver==0)
            return gold;
        else if(playertype==0 && goldreceiver==1)
            return -gold;
        else if(playertype==1 && goldreceiver==0)
            return -gold;
        else if(playertype==1 && goldreceiver==1)
            return gold;
        else
            return 0;
    }

    public int getGoldreceiver(){
        return goldreceiver;
    }

}
