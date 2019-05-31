package com.fullrune.areashiftertwo.CONTROL;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fullrune.areashiftertwo.MODEL.Model;
import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input;
import com.fullrune.areashiftertwo.MODEL.SAVING.MiscInfo;
import com.fullrune.areashiftertwo.MODEL.SAVING.Progress;
import com.fullrune.areashiftertwo.VIEW.Board;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameRunner implements Runnable{

    private boolean initBoardOnce;
    private boolean anUpdate;
    private long lag;
    private int MS_FRAME;
    private long currentTime;
    private long previousTime;

    private boolean gamePaused;
    private boolean savedProgressOnce;
    private Button backButton;

    public AtomicBoolean startUnpause, startPause;
    public AtomicBoolean gameInitialized;

    private Model model;
    private Board board;

    private GameActivity gameActivity;
    private MiscInfo miscInfo;

    public GameRunner(GameActivity gameActivity, Model model, MiscInfo miscInfo){
        this.model = model;
        this.miscInfo = miscInfo;
        this.board = gameActivity.getBoard();
        this.gameActivity = gameActivity;
        this.backButton = gameActivity.getBackButton();
        startPause = new AtomicBoolean(false);
        startUnpause = new AtomicBoolean(false);
        gameInitialized = new AtomicBoolean(false);
        MS_FRAME = 55;
        previousTime = currentTime = System.currentTimeMillis();
    }

    private void loopGameStep(){
        if (model != null && (model.isGameWon())) {
            commonWinLoseDrawing();
            if(!savedProgressOnce){
                IOHandler ioHandler = new IOHandler(gameActivity);
                Progress progress = ioHandler.loadProgress();
                if(progress != null){
                    progress.setLevelsDone(model.getLevel());
                    progress.setLevelUnlocked(model.getLevel()+1);
                    ioHandler.saveProgress(progress);
                }
                savedProgressOnce = true;
            }

        }
        else if(model != null && model.isGameLost()){
            commonWinLoseDrawing();

        }else{
            gamePlayStep();
        }
    }

    public void saveMiscInfoData2(){
        if(miscInfo != null && gameActivity != null)
           new IOHandler(gameActivity).saveMiscInfo(miscInfo);
    }

    private void checkPause(){
        if(startUnpause.get()){
            previousTime = System.currentTimeMillis();
            startUnpause.set(false);
            gamePaused = false;
            miscInfo.startTimer();
        }
        if(startPause.get()){
            startPause.set(false);
            gamePaused = true;
            miscInfo.endTimer();
            saveMiscInfoData2();
        }
    }

    @Override
    public void run() {

        miscInfo.startTimer();

        while (true) {

            if(Thread.interrupted()){
                return;
            }

            checkPause();

            if(!gamePaused) {
                loopGameStep();
            }

        }
    }

    private void sleepFor(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    private void gamePlayStep() {
        sleepFor(3);
        if (board != null && !initBoardOnce && board.isReady()) {
            board.init2(model);
            initBoardOnce = true;
            gameInitialized.set(true);
        } else if (!initBoardOnce) {
            return;
        }

        Input swipeInput = gameActivity.getSwipeListener().getLatestInput();
        if (swipeInput != null)
            model.setInput(swipeInput);

        gameTick();

        if (anUpdate)
            board.drawGame();
    }

    private void commonWinLoseDrawing(){
        sleepFor(17);
        board.drawGame();
        if(board.isEndAnimationDone()){
            makeBackBtnVisible();
        }
    }

    private void makeBackBtnVisible(){
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                backButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void gameTick() {
        anUpdate = false;
        currentTime = System.currentTimeMillis();
        long diff = currentTime - previousTime;
        previousTime = currentTime;
        lag += diff;

        if (lag > MS_FRAME * 9) {
            lag = MS_FRAME * 1;
        }

        while (lag >= MS_FRAME) {
            lag -= MS_FRAME;
            model.process();
            anUpdate = true;
           // Log.i("TEST_ABC", "SOME TEST ? " + lag);
        }

    }
}