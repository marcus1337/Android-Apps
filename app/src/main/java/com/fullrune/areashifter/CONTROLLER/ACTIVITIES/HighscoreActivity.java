package com.fullrune.areashifter.CONTROLLER.ACTIVITIES;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fullrune.areashifter.CONTROLLER.FileObjects.HighScore;
import com.fullrune.areashifter.CONTROLLER.IOManager;
import com.fullrune.areashifter.R;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by Marcus on 2017-05-11.
 */

public class HighscoreActivity extends Activity {

    private ImageButton backBtn;
    private IOManager ioManager;
    private HighScore highScore;
    private HighscoreActivity highscoreActivity;
    private TextView[] scoreTxts;
    private Date[] dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtity_highscore);
        highscoreActivity = this;
        scoreTxts = new TextView[5];
        dates = new Date[5];

        scoreTxts[0] = (TextView) findViewById(R.id.ascore1);
        scoreTxts[1] = (TextView) findViewById(R.id.ascore2);
        scoreTxts[2] = (TextView) findViewById(R.id.ascore3);
        scoreTxts[3] = (TextView) findViewById(R.id.ascore4);
        scoreTxts[4] = (TextView) findViewById(R.id.ascore5);

        for(int i = 0; i < 5; i++){
            final int finalI = i;
            scoreTxts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Date date = dates[finalI];
                    if(date != null){
                        android.text.format.DateFormat df = new android.text.format.DateFormat();
                        String reportDate  = df.format("yyyy-MM-dd hh:mm a", date).toString();
                        Toast.makeText(highscoreActivity, reportDate, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }

        backBtn = (ImageButton) findViewById(R.id.continuescoresbtn2);

        ioManager = new IOManager(highscoreActivity);
        highScore = ioManager.loadHighScore();

        if(highScore != null)
            for(int i = 0; i < 5; i++){
                String scrTxt = "";
                scrTxt += (i+1) +". " + highScore.getName(i) + ", " + highScore.getScore(i);
                scoreTxts[i].setText(scrTxt);
                dates[i] = highScore.getDate(i);
            }

        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
