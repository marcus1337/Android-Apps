package com.fullrune.halfrobot.ACTIVITY;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fullrune.halfrobot.MISC.IOManager;
import com.fullrune.halfrobot.R;
import com.fullrune.halfrobot.MISC.SaveFile;

/**
 * Created by Marcus on 2017-09-21.
 */

public class LevelActivity extends Activity {

    private SaveFile saveFile;
    private Context context;

    private static final int[] idArray = {R.id.lvl1btn, R.id.lvl2btn, R.id.lvl3btn,
            R.id.lvl4btn, R.id.lvl5btn, R.id.lvl6btn, R.id.lvl7btn, R.id.lvl8btn};

    private Button[] lvlBtn = new Button[idArray.length];
    private Button saveBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        context = getApplicationContext();

        for(int i = 0; i < 8; i++){
            final int b = i;
            lvlBtn [b] = (Button)findViewById(idArray[b]);
            lvlBtn [b].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(LevelActivity.this, GameActivity.class);
                    myIntent.putExtra("gamestatus", saveFile);
                    myIntent.putExtra("map_id", b);
                    startActivityForResult(myIntent, 1);
                }
            });
        }

        saveBtn = (Button) findViewById(R.id.savebtn);
        backBtn = (Button) findViewById(R.id.backbtn);
        saveBtn.setOnClickListener(new SaveBtnListener());
        backBtn.setOnClickListener(new BackBtnListener());

        Intent intent = getIntent();
        String value = intent.getStringExtra("key");

        if(value.equals("newgame"))
            newGame();
        else if (value.equals("loadgame"))
            loadGame();

    }

    private class BackBtnListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    private class SaveBtnListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            IOManager.save(context, saveFile);
            Toast.makeText(context, "DATA SAVED", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                int mapid  = data.getIntExtra("map_id", -1);
                SaveFile tmpSave = (SaveFile) data.getSerializableExtra("gamestatus");
                if(mapid >= 0 && tmpSave != null){
                    saveFile = tmpSave;
                }
                else{
                    finish();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private void loadGame(){

    }

    private void newGame(){
        saveFile = new SaveFile();
    }

}
