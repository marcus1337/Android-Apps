package com.fullrune.areashiftertwo.MODEL.SAVING;

import java.io.Serializable;
import java.util.ArrayList;

public class Progress implements Serializable {

    private ArrayList<Integer> levelsDone;
    private ArrayList<Integer> unlockedLevels;

    public Progress(){
        levelsDone = new ArrayList<Integer>();
        unlockedLevels = new ArrayList<Integer>();
    }

    public void setLevelUnlocked(int lvl){
        unlockedLevels.add(lvl);
    }

    public ArrayList<Integer> getUnlockedLevels() {
        return new ArrayList<>(unlockedLevels);
    }

    public void setLevelsDone(int lvl){
        levelsDone.add(lvl);
    }

    public ArrayList<Integer> getLevelsDone() {
        return new ArrayList<Integer>(levelsDone);
    }
}
