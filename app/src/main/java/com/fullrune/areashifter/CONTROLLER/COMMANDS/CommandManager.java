package com.fullrune.areashifter.CONTROLLER.COMMANDS;

import com.fullrune.areashifter.MODEL.PLAYER.Input;

/**
 * Created by Marcus on 2017-04-25.
 */

public class CommandManager {

    private Command command;

    public boolean isEmpty(){
        return command == null;
    }

    public Command getCommand(){
        Command tmp = command;
        command = null;
        return tmp;
    }

    public void handleInput(Input input){
        switch (input){
            case MOVE_UP:
                command = new UpCommand();
                break;
            case MOVE_DOWN:
                command = new DownCommand();
                break;
            case MOVE_LEFT:
                command = new LeftCommand();
                break;
            case MOVE_RIGHT:
                command = new RightCommand();
                break;
        }
    }

}
