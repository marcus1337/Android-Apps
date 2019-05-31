package com.fullrune.areashiftertwo.MODEL.SAVING;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MiscInfo implements Serializable {

    private Date lastLogin;
    private long gamePlayMilli;

    private int currentLevel;

    public Date getLastLogin(){
        return lastLogin;
    }

    private ArrayList<Long> levelGameTimes;

    private long tempTime;

    public void setCurrentLevel(int currentLevel){
        this.currentLevel = currentLevel;
        tempTime = 0;
    }

    public MiscInfo(){
        lastLogin = new Date();
        levelGameTimes = new ArrayList<Long>();
        for(int i = 0 ; i < 12; i++){
            levelGameTimes.add(new Long(0));
        }
    }

    public ArrayList<Long> getLevelGameTimes() {
        return levelGameTimes;
    }

    public void setLevelTime(long timetmp, int lvltmp){
        if(lvltmp > 12 || lvltmp < 1)
            return;
        levelGameTimes.set(lvltmp-1, timetmp + levelGameTimes.get(lvltmp-1));
    }

    public long getLevelTime(int lvl){
        if(lvl > 12 || lvl < 1)
            return 0;
        return levelGameTimes.get(lvl-1);
    }

    private long tempStartTimer;
    private long tempEndTimer;

    public void startTimer(){
        lastLogin = new Date();
        tempStartTimer = System.currentTimeMillis();
    }

    public void endTimer(){
        tempEndTimer = System.currentTimeMillis();
        addCalculatedGamePlayTime();
    }

    public void addCalculatedGamePlayTime(){
        if(tempEndTimer > tempStartTimer){
            gamePlayMilli += Math.abs(tempEndTimer-tempStartTimer);
            tempTime += Math.abs(tempEndTimer-tempStartTimer);
            setLevelTime(tempTime, currentLevel);
        }
    }

    public long getGamePlayMilli(){
        return gamePlayMilli;
    }

    public void setGamePlayMilli(long gamePlayMilli){
        this.gamePlayMilli = gamePlayMilli;
    }



}
