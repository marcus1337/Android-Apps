package com.fullrune.halfrobot.ACTIVITY;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fullrune.halfrobot.R;

public class MainActivity extends AppCompatActivity {

    Button startbtn, loadbtn, helpbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startbtn = (Button) findViewById(R.id.startbtn);
        loadbtn = (Button) findViewById(R.id.loadbtn);

        startbtn.setOnClickListener(new StartBtnListener());
        loadbtn.setOnClickListener(new LoadBtnListener());

    }

    private class LoadBtnListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, LevelActivity.class);
            myIntent.putExtra("key", "loadgame");
            MainActivity.this.startActivity(myIntent);
        }
    }

    private class StartBtnListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MainActivity.this, LevelActivity.class);
            myIntent.putExtra("key", "newgame");
            MainActivity.this.startActivity(myIntent);
        }
    }

}
