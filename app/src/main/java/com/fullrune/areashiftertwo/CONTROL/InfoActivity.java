package com.fullrune.areashiftertwo.CONTROL;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fullrune.areashiftertwo.MODEL.SAVING.MiscInfo;
import com.fullrune.areashiftertwo.R;

import org.w3c.dom.Text;

public class InfoActivity extends Activity {

    private Button backbutton;
    private TextView timeText;
    private MiscInfo miscInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        backbutton = findViewById(R.id.backbuttoninfo);
        backbutton.setOnClickListener(new BackButtonListener());
        timeText = findViewById(R.id.textView2);
        IOHandler ioHandler = new IOHandler(this);
        miscInfo = ioHandler.loadMiscInfo();
        if(miscInfo != null){
            String tmpText = "";
            tmpText = "Total time: \t" + getHourMinSec(miscInfo.getGamePlayMilli());
            tmpText += "\n\n\n";
            for(int i = 0; i < 12; i++){
                tmpText += "Time " + String.valueOf(i+1) + ": \t" + getHourMinSec(miscInfo.getLevelTime(i+1));
                tmpText += "\n";
            }

            timeText.setText(tmpText);
        }else{
            timeText.setText("No data yet.");
        }



    }

    private String getHourMinSec(long durmillis){
        long millis = durmillis % 1000;
        long second = (durmillis / 1000) % 60;
        long minute = (durmillis / (1000 * 60)) % 60;
        long hour = (durmillis / (1000 * 60 * 60)) % 24;
        String timestr = String.format("%02d:%02d:%02d.%d", hour, minute, second, millis);
        return timestr;
    }

    private class BackButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            finish();
        }
    }

}
