package com.fullrune.halfrobot.MISC;

import java.io.Serializable;

/**
 * Created by Marcus on 2017-09-21.
 */

public class SaveFile implements Serializable {

    public SaveFile(){
        lives = 15;
        lvlBeaten = new boolean[8];
        for(int i = 0; i < 8; i++)
            lvlBeaten[i] = false;
        soundOn = true;
    }

    public int getLives(){
        return lives;
    }

    public void setLives(int lives){
        this.lives = lives;
    }

    public void setSoundOn(boolean soundOn){
        this.soundOn = soundOn;
    }

    public boolean isSoundOn(){
        return soundOn;
    }

    public boolean isLvlWon(int idNum){
        return lvlBeaten[idNum];
    }

    public boolean isAllLvlWon(){
        for(int i = 0 ; i < 8; i++)
            if(!lvlBeaten[i])
                return false;
        return true;
    }

    public void setLvlWon(int idNum, boolean isWon){
        lvlBeaten[idNum] = isWon;
    }

    private boolean soundOn;
    private boolean[] lvlBeaten;
    private int lives;

}
