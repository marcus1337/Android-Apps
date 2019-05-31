package com.fullrune.areashifter.CONTROLLER.COMMANDS;

import com.fullrune.areashifter.MODEL.PLAYER.Input;
import com.fullrune.areashifter.MODEL.PLAYER.Player;

/**
 * Created by Marcus on 2017-04-25.
 */

public class DownCommand implements Command {
    @Override
    public void execute(Player player) {
        player.handleInput(Input.MOVE_DOWN);
    }
}
