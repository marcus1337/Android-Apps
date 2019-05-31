package com.fullrune.areashiftertwo.CONTROL;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fullrune.areashiftertwo.MODEL.Model;
import com.fullrune.areashiftertwo.MODEL.Pieces.PLAYER.Input;
import com.fullrune.areashiftertwo.MODEL.SAVING.MiscInfo;
import com.fullrune.areashiftertwo.MODEL.SAVING.Progress;
import com.fullrune.areashiftertwo.R;

import com.fullrune.areashiftertwo.VIEW.Board;

import java.util.concurrent.atomic.AtomicBoolean;

public class GameActivity extends Activity {

    private Board board;
    private Thread boardThread, untilInitThread;
    private Model model;
    private OnSwipeTouchListener swipeListener;
    private Context context;

    private Button backButton;
    private Button pauseButton;

    private GameRunner gameRunner;

    public OnSwipeTouchListener getSwipeListener(){
        return swipeListener;
    }

    public Board getBoard(){
        return board;
    }

    public Button getBackButton(){
        return backButton;
    }

    public Button getPauseButton(){
        return pauseButton;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        context = this;
        gameActivity = this;

        backButton = findViewById(R.id.backBtn);
        backButton.setOnClickListener(new BackButtonListener());
        backButton.setVisibility(View.GONE);
        pauseButton = findViewById(R.id.pauseBtn);
        pauseButton.setOnClickListener(new PauseButtonListener());

        Intent intent = getIntent();
        int chosenLvl = intent.getIntExtra("level", 1);

       // chosenLvl = 10;
        model = new Model();
        model.initLevel(chosenLvl);

        board = findViewById(R.id.surfaceBoard);
        swipeListener = new OnSwipeTouchListener(this);
        board.setOnTouchListener(swipeListener);

        MiscInfo miscInfo = new IOHandler(this).loadMiscInfo();
        if(miscInfo == null){
            miscInfo = new MiscInfo();
        }
        miscInfo.setCurrentLevel(chosenLvl);

        untilInitThread = new Thread(new RunUntilInited(this));
        untilInitThread.start();

        gameRunner = new GameRunner(this, model, miscInfo);
        boardThread = new Thread(gameRunner);
        boardThread.start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(boardThread != null)
            boardThread.interrupt();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(gameRunner != null){
            gameRunner.startPause.set(true);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if(gameRunner != null){
            gameRunner.startUnpause.set(true);
        }
    }

    private class BackButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            setResult(RESULT_OK);
            finish();
        }
    }

    private GameActivity gameActivity;

    public void hideEveryThing(){
        gameRunner.startPause.set(true);
        pauseButton.setVisibility(View.GONE);
    }

    public void unHideEveryThing(){
        pauseButton.setVisibility(View.VISIBLE);
        gameRunner.startUnpause.set(true);
    }

    private class PauseButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            hideEveryThing();
            PauseFragment tmpFrag = new PauseFragment();
            gameActivity.getFragmentManager()
                    .beginTransaction()
                    .add(R.id.sometmpframe, tmpFrag, PauseFragment.FRAGMENT_TAG)
                    .disallowAddToBackStack()
                    .commitAllowingStateLoss();
        }
    }

    private ProgressDialog progress;

    private class RunUntilInited implements Runnable{

        private Context context;
        public RunUntilInited(Context context){
            this.context = context;
        }

        @Override
        public void run() {
            boolean keepRunning = true;

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    progress = new ProgressDialog(context, R.style.MyThemeDialog);
                    progress.setTitle("Loading");
                    progress.setMessage("Wait while loading...");
                    progress.setCancelable(false);
                    progress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    progress.show();
                }
            });

            while(keepRunning){
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                }

                if(Thread.interrupted()){
                    keepRunning = false;
                }

                if(gameRunner != null && !gameRunner.gameInitialized.get()){

                }else if(gameRunner != null && gameRunner.gameInitialized.get()){
                    keepRunning = false;
                }

                if(!keepRunning && progress != null){
                    progress.dismiss();
                }
            }

        }
    }

}
