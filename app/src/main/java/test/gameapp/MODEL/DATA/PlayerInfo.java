package test.gameapp.MODEL.DATA;

import java.io.Serializable;

/**
 * Created by Marcus on 2016-12-27.
 */

public class PlayerInfo implements Serializable {

    private long gold;
    private int selectedSword;//0 1 2 3

    public PlayerInfo(){
        gold = 0;
        selectedSword = -1;
    }

    public int getSelectedSword(){
        return selectedSword;
    }

    public void setSelectedSword(int nySelection){
        this.selectedSword = nySelection;
    }

    public void setGold(long gold){
        this.gold = gold;
    }

    public long getGold(){
        return gold;
    }

}
