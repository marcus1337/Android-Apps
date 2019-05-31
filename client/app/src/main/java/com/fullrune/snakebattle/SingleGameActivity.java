package com.fullrune.snakebattle;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fullrune.snakebattle.GameActivity;
import com.fullrune.snakebattle.OnSwipeListener;
import com.fullrune.snakebattle.R;

import SINGLE.SingleGame;
import STATES.Client;
import STATES.GameState;
import SINGLE.SingleBoard;

public class SingleGameActivity extends Activity {

    private SingleBoard board;
    private Context context;
    private SingleGame gameState;
    private TextView scoreText, infoText;
    private Button returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlegame);

        gameState = new SingleGame();
        board =  findViewById(R.id.gameBoard3);
        scoreText = findViewById(R.id.scoreText2);
        infoText = findViewById(R.id.infoText5);
        returnBtn = findViewById(R.id.returnBtn);
        scoreText.setTextColor(Color.RED);
        infoText.setTextColor(Color.RED);

        board.readyCheck2 = true;
        context = getApplicationContext();
        board.setGameState(gameState);

        board.setOnTouchListener(new OnSwipeListener(this, gameState));

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    while(!board.readycheck){
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                    }
                    if(!gameState.isGameStarted()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                infoText.setText("Starts in: " + gameState.secondsLeft() +"s");
                                scoreText.setText("Score: " + gameState.getScore());
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                infoText.setText("");
                                scoreText.setText("Score: " + gameState.getScore());
                            }
                        });
                    }

                    //scoreText.setText("Score: " + gameState.getScore());
                    board.drawGame();
                }
            }
        }).start();
        gameThread = new Thread(new GameRunnable());
        gameThread.start();
        returnBtn.setVisibility(View.GONE);
        returnBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private Thread gameThread;

    private class GameRunnable implements Runnable{

        @Override
        public void run() {
            while(!Thread.interrupted()){
                try {
                    if(gameState.isDone()){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                returnBtn.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    int calcSleep = gameState.getScore();
                    calcSleep = 480 - calcSleep*3;
                    if(calcSleep < 60){
                        calcSleep = 60;
                    }

                    Thread.sleep(calcSleep );
                } catch (InterruptedException e) {
                }
               // System.out.println("heya");
                gameState.process();
            }
        }
    }

}
