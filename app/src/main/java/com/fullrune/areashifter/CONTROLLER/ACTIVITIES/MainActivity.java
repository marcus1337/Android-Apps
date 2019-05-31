package com.fullrune.areashifter.CONTROLLER.ACTIVITIES;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fullrune.areashifter.CONTROLLER.IOManager;
import com.fullrune.areashifter.R;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private Button resumeBtn, startBtn, highscoresBtn, storeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.textView);

        resumeBtn = (Button) findViewById(R.id.resumeButton);
        startBtn = (Button) findViewById(R.id.newgameButton);
        highscoresBtn = (Button) findViewById(R.id.highscoreButton);
        storeBtn = (Button) findViewById(R.id.storeButton);
        startBtn.setOnClickListener(new StartButtonListener());
        resumeBtn.setOnClickListener(new ResumeButtonListener());
        storeBtn.setOnClickListener(new StoreButtonListener());
        highscoresBtn.setOnClickListener(new HighscoreButtonListener());

        if(new IOManager(this).loadModel() == null)
            resumeBtn.setVisibility(View.GONE);

    }

    private class HighscoreButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, HighscoreActivity.class);
            MainActivity.this.startActivity(myIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(new IOManager(this).loadModel() != null)
                resumeBtn.setVisibility(View.VISIBLE);
        }
    }//onActivityResult

    private class StoreButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, ShopActivity.class);
            MainActivity.this.startActivity(myIntent);
        }
    }

    private class ResumeButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
            myIntent.putExtra("startCondition", "resumegame"); //Optional parameters
            MainActivity.this.startActivityForResult(myIntent, 1);
        }
    }

    private class StartButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
            myIntent.putExtra("startCondition", "newgame"); //Optional parameters
            MainActivity.this.startActivityForResult(myIntent, 1);
        }
    }
}
