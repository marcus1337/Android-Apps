package com.fullrune.areashiftertwo.CONTROL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fullrune.areashiftertwo.CONTROL.Advertising.AdShow;
import com.fullrune.areashiftertwo.MODEL.SAVING.Progress;
import com.fullrune.areashiftertwo.R;

import java.util.ArrayList;

public class LevelActivity extends Activity {

    private ArrayList<Button> buttons;
    private Progress progress;
    private Context context;

    private Button backButton, infoButton;
    private AdShow adShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levelselect);

        adShow = new AdShow(this);

        backButton = findViewById(R.id.button19);
        infoButton = findViewById(R.id.button20);

        backButton.setOnClickListener(new BackButtonListener());
        infoButton.setOnClickListener(new InfoButtonListener());
        
        context = this;
        buttons = new ArrayList<Button>();
        String btnID = "lvl";
        for (int i = 1; i <= 12; i++) {
            int resID = getResources().getIdentifier(btnID + Integer.toString(i), "id", getPackageName());
            Button button = findViewById(resID);
            buttons.add(button);
        }
        iterateButtons();

        adShow.init();

    }

    private void iterateButtons(){
        IOHandler ioHandler = new IOHandler(this);
        progress = ioHandler.loadProgress();
        if (progress == null) {
            progress = new Progress();
            progress.setLevelUnlocked(1);
            ioHandler.saveProgress(progress);
        }
        ArrayList<Integer> unlockedlvls = progress.getUnlockedLevels();
        ArrayList<Integer> levelsDone = progress.getLevelsDone();

        for(int i = 1 ; i <= 12; i++){
            final int finalI = i;
            Button button = buttons.get(i-1);
            if (unlockedlvls.contains(i)) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(LevelActivity.this, GameActivity.class);
                        myIntent.putExtra("level", finalI); //Optional parameters
                        LevelActivity.this.startActivityForResult(myIntent, 1);
                    }
                });
                button.setBackgroundResource(R.drawable.roundedbutton2);
            }else{
                button.setBackgroundResource(R.drawable.roundedbutton_locked);
                button.setTextColor(Color.BLACK);
            }
            if(levelsDone.contains(i)){
                button.setBackgroundResource(R.drawable.roundedbutton3);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                iterateButtons();
                adShow.showAd();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
               finish();
            }
        }
    }//onActivityResult

    private class InfoButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent myIntent = new Intent(LevelActivity.this, InfoActivity.class);
            LevelActivity.this.startActivity(myIntent);
        }
    }

    private class BackButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }

}
