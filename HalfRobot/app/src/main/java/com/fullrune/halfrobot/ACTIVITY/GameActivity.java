package com.fullrune.halfrobot.ACTIVITY;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.fullrune.halfrobot.GameThread;
import com.fullrune.halfrobot.MISC.SaveFile;
import com.fullrune.halfrobot.R;
import com.fullrune.halfrobot.Window;

/**
 * Created by Marcus on 2017-09-21.
 */

public class GameActivity extends Activity {

    private GameThread gameThread;

    ImageButton abtn,bbtn, upbtn, downbtn, leftbtn, rightbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Window window = (Window) findViewById(R.id.windowlay1);

        Intent intent = getIntent();
        SaveFile saveFile = (SaveFile) intent.getSerializableExtra("gamestatus");

        gameThread = new GameThread(window);

        abtn = (ImageButton) findViewById(R.id.abtn);
        bbtn = (ImageButton) findViewById(R.id.bbtn);
        upbtn = (ImageButton) findViewById(R.id.keyup);
        downbtn = (ImageButton) findViewById(R.id.keydown);
        leftbtn = (ImageButton) findViewById(R.id.keyleft);
        rightbtn = (ImageButton) findViewById(R.id.keyright);

        abtn.setOnClickListener(new AButtonListener());


        gameThread.start();


    }

    private class AButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }

    private class BButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }

    private class UpButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }

    private class DownButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }

    private class LeftButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }

    private class RightButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {

        }
    }

}
