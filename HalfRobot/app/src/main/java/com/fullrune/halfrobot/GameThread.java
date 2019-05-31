package com.fullrune.halfrobot;

import com.fullrune.halfrobot.LOGIC.Model;
import com.fullrune.halfrobot.MISC.IOManager;
import com.fullrune.halfrobot.MISC.SaveFile;
import com.fullrune.halfrobot.MISC.World;

/**
 * Created by Marcus on 2017-09-22.
 */

public class GameThread extends Thread {

    private Window window;
    private Model model;
    private SaveFile saveFile;
    private World world;

    public GameThread(Window window, SaveFile saveFile, int world_id){
        this.window = window;
        model = new Model();
        this.saveFile = saveFile;
        world.init(window.getWidth(), window.getHeight(), world_id);

    }

    @Override
    public void run(){

        while(true) {

            window.draw();
            window.drawModel(model);

            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
