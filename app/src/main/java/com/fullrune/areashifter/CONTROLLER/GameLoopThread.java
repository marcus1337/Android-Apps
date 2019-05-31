package com.fullrune.areashifter.CONTROLLER;

import android.util.Log;
import android.widget.Toast;

import com.fullrune.areashifter.CONTROLLER.ACTIVITIES.GameActivity;
import com.fullrune.areashifter.CONTROLLER.COMMANDS.CommandManager;
import com.fullrune.areashifter.MODEL.Model;
import com.fullrune.areashifter.VIEW.Board;

/**
 * Created by Marcus on 2017-04-25.
 */

public class GameLoopThread extends Thread {

    private boolean running;
    private Board board;
    // private Model model;
    private CommandManager commandManager;
    private GameActivity gameActivity;
    private boolean sentOnce;

    private boolean loadGame;

    public GameLoopThread(Board board, CommandManager commandManager, GameActivity gameActivity) {
        running = true;
        this.board = board;
        // this.model = model;
        this.gameActivity = gameActivity;
        this.commandManager = commandManager;
        sentOnce = false;
        loadGame = false;

    }

    private Event getEvent(Model model){
        if(model == null)
            return null;
        if (model.isGameOver())
            return new Event(Event.EV.GAMEOVER);
        else if (model.isPaused() && !model.isLevelStarted()) {
            if(model.isGameWon()) {
                return new Event(Event.EV.WINNER);
            }
            else
                return new Event(Event.EV.NYLEVEL);
        }
        else if(model.isPaused()){
            return new Event(Event.EV.PAUSE);
        }
        return null;
    }

    public void notify(Event event) {
        if(event.getEvent() == Event.EV.LOADGAME){
            loadGame = true;
            sentOnce = false;
        }else if(event.getEvent() == Event.EV.RESUME){
            sentOnce = false;
        }

    }

    private void sendNotification(Event event) {
        if (!sentOnce) {
            sentOnce = true;
            gameActivity.notify(event);
        }
    }

    private boolean paused = false;

    public void setPaused(boolean paused){
        this.paused = paused;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {

        while (!board.isReady()) {
        }
        if(loadGame)
            board.notify(new Event(Event.EV.LOADGAME));

        long beginTime = 0;     // the time when the cycle begun
        long timeDiff = 0;      // the time it took for the cycle to execute
        Model model = board.getModel();

        while (running) {

            if(paused)
                continue;


            beginTime = System.currentTimeMillis();
            if (!commandManager.isEmpty())
                commandManager.getCommand().execute(model.getPlayer());
            model.update(timeDiff);
            board.drawGame();
            timeDiff = System.currentTimeMillis() - beginTime;

            Event event = getEvent(model);
            if (event != null){
               // Log.i("HAPPENED43", " " + event.getEvent());
                paused = true;
                gameActivity.notify(event);
                //sendNotification(event);
              //  continue;
            }
            //sentOnce = false;


        }
        board.shutDown();
    }

}
