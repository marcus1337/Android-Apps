package com.fullrune.lawgame;

import android.app.Activity;
import android.os.Bundle;

import MODEL.Model;
import VIEW.Board;

public class GameActivity extends Activity {

    private Board board;
    private Thread boardThread;
    private Model model;
    private boolean initBoardOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        model = new Model();

        board = findViewById(R.id.gameBoard2);

        boardThread = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                    }
                    if(board != null && !initBoardOnce && board.isReady()) {
                        board.init2(model);
                        initBoardOnce = true;
                    }
                    board.drawGame();
                }
            }
        });

        boardThread.start();
    }

}
