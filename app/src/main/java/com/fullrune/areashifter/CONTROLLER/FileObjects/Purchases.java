package com.fullrune.areashifter.CONTROLLER.FileObjects;

import java.io.Serializable;

/**
 * Created by Marcus on 2017-05-09.
 */

public class Purchases implements Serializable {

    private static final long serialVersionUID = 6879488033L;

    boolean premium;
    boolean extraLives;

    public Purchases() {
        premium = false;
        extraLives = false;
    }

    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public boolean isExtraLives() {
        return extraLives;
    }

    public void setExtraLives(boolean extraLives) {
        this.extraLives = extraLives;
    }


}
