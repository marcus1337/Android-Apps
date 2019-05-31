package com.fullrune.snakebattle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import STATES.CState;
import STATES.Client;
import STATES.GameState;
import VIEW.Board;

public class GameActivity extends AppCompatActivity {

    private Client client;
    private GameState gameState;
    private TextView infoText1, infoText2;
    private Context context;
    private Board board;
    private Button returnBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        infoText1 = findViewById(R.id.infoText);
        infoText2 = findViewById(R.id.infoText2);
        board = findViewById(R.id.gameBoard2);
        returnBtn = findViewById(R.id.returnBtn5);
        board.readyCheck2 = true;
        context = getApplicationContext();
        client = (Client) getIntent().getSerializableExtra("clientKey");
        if (client == null) {
            setResult(RESULT_CANCELED, getIntent());
            finish();
        }
        client.setConnectProgress(0);
        gameState = new GameState();
        board.setOnTouchListener(new OnSwipeListener(this, gameState));
        gamingThread = new Thread(new GamingRunnable());
        gamingThread.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    while (!board.readycheck) {
                    }
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                    }
                    board.drawGame();

                }
            }
        }).start();

        returnBtn.setVisibility(View.GONE);
        returnBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                endGame();
            }
        });

    }

    private Thread gamingThread;

    private class GamingRunnable implements Runnable {
        @Override
        public void run() {
            runGame();
        }
    }

    private void updateText1(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoText1.setText(msg);
                infoText1.invalidate();
            }
        });
    }

    private void updateText2(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                infoText2.setText(msg);
                infoText2.invalidate();
            }
        });
    }

    private void runGame() {
        // Log.i("TEST_A", "before init + " + client.getError());
        gameState.initStep(client);
        board.setGameState(gameState);
        // Log.i("TEST_A", "after init");

        while (client.getError() == Client.ErrorType.NONE) {
            gameState.processStep(client);
            if (!gameState.isGameStarted()) {

                if(gameState.isTimeout()){
                    client.setErrorType(Client.ErrorType.GAME_INIT);
                    break;
                }
                int sec = gameState.millisBeforeStart;///1000+1;
                updateText1("Game starts in: " + sec + "s");
            } else {
                updateText1("Game started!");
                if (client.getPlayerNumber() == 0) {
                    updateText2("You are BLUE.");
                } else {
                    updateText2("You are RED.");
                }
            }
        }
        gameState.endStep(client);

        if (client.getError() != Client.ErrorType.GAME_END) {
            endGameFail();
        }

        if (gameState.winnerCode == 3) {
            updateText1("Game ended: TIE.");
        }
        if (gameState.winnerCode == 2) {
            updateText1("Game ended: RED wins.");
        }
        if (gameState.winnerCode == 1) {
            updateText1("Game ended: BLUE wins.");
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                returnBtn.setVisibility(View.VISIBLE);
                returnBtn.invalidate();
            }
        });
    }

    private void endGameFail(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                setResult(100, intent);
                finish();
            }
        });
    }

    private void endGame() {
        Intent intent = getIntent();
        //intent.putExtra("key", value);
        setResult(RESULT_OK, intent);
        finish();
    }

}
