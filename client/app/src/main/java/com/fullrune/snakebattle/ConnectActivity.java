package com.fullrune.snakebattle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

import STATES.Client;
import STATES.ConnectState;

public class ConnectActivity extends AppCompatActivity {

    public static int REQUEST_CODE = 1;

    private ProgressBar progressBar;
    private Thread progressThread;
    private ConnectThread progressRunnable;
    private Client client;
    private Context context;

    private boolean timerStarted;
    private long timeoutStart;
    private TextView timeoutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting);
        progressBar = findViewById(R.id.progressBar2);
        timeoutText = findViewById(R.id.textTimeout);
        timeoutText.setVisibility(View.INVISIBLE);
        timerStarted = false;
        timeoutStart = -1;

        client = new Client();
        context = this.getApplicationContext();
        progressRunnable = new ConnectThread();
        progressThread = new Thread(progressRunnable);
        progressThread.start();
        Thread t = new Thread(new ConnectRunnable());
        t.start();
    }

    private class ConnectRunnable implements Runnable{
        @Override
        public void run() {
            connectToServer();
        }
    }

    private void showTimer(){
        if(client.getConnectProgress() != 0){
            if(timerStarted == false){
                timerStarted = true;
                timeoutStart = System.currentTimeMillis();
            }
            final long secLeft = (9500 - ((System.currentTimeMillis() - timeoutStart)))/1000+1;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timeoutText.setVisibility(View.VISIBLE);
                    if(secLeft >= 0)
                        timeoutText.setText("Timeout: " + Long.toString(secLeft) + " seconds.");
                    if(secLeft <= 0){
                        makeToast("Connection timeout.");
                        finish();
                    }
                }
            });
        }
    }

    private void connectToServer(){
        ConnectState connectState = new ConnectState();
        connectState.initStep(client);
        connectState.processStep(client);

        if(client.getConnectProgress() == 100){
            startGame();
        }else{
            makeToast("Connecting to server failed.");
            finish();
        }

    }

    private class ConnectThread implements Runnable{
    public AtomicBoolean runThread;
    public ConnectThread(){
        runThread = new AtomicBoolean(true);
    }

        @Override
        public void run() {
            progressBar.setScaleY(5f);
            while(runThread.get()){
                int progressValue = client.getConnectProgress();
                progressBar.setProgress(progressValue);
                showTimer();

                if(progressValue == 100)
                    return;
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                }
            }

        }
    }

    private void stopThreads(){
        if(progressRunnable != null){
            progressRunnable.runThread.set(false);
            try {
                if(progressThread != null)
                    progressThread.join();
            } catch (InterruptedException e) {}
        }
    }

    private void startGame() {
        stopThreads();
        Intent i = new Intent(context, GameActivity.class);
        i.putExtra("clientKey", client);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
               // String requiredValue = data.getStringExtra("Key");
                finish();
            }
            else if(resultCode == 100){
                makeToast("Error occurred. Retrying...");
                recreate();
            }else if(resultCode == RESULT_CANCELED){
                finish();
            }

        } catch (Exception ex) {
            makeToast(ex.toString());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopThreads();
    }

    private void makeToast(final String msg){
        Handler mHandler = new Handler(context.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
