package com.fullrune.areashifter.CONTROLLER.COMMANDS;

import com.fullrune.areashifter.MODEL.PLAYER.Player;

/**
 * Created by Marcus on 2017-04-25.
 */

public interface Command {
    void execute(Player player);

}
