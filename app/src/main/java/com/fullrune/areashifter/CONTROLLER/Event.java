package com.fullrune.areashifter.CONTROLLER;

/**
 * Created by Marcus on 2017-05-04.
 */

public class Event {

    public enum EV {
        NYLEVEL,
        LOADGAME,
        PAUSE,
        GAMEOVER,
        WINNER,
        RESUME,
        RESTART

    }

    private EV typ;

    public Event(EV typ){
        this.typ = typ;
    }

    public EV getEvent(){
        return typ;
    }

}
