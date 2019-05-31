package com.fullrune.areashifter.MODEL.PLAYER;

import java.io.Serializable;

/**
 * Created by Marcus on 2017-04-25.
 */

public interface PlayerState extends Serializable {
    static final long serialVersionUID = -29238982928391L;
    PlayerState handleInput(Player player, Input input);
    void update(Player player, long timeDiff);
    void enter(Player player);

}
