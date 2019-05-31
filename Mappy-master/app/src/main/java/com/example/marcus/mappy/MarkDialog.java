package com.example.marcus.mappy;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Marcus on 2016-01-14.
 */
public class MarkDialog extends Dialog {

    private String descChanges;
    private String markDesc;
    private TextView t;
    private EditText ed;

    public MarkDialog(Context context, String markerInfo) {
        super(context);
        markDesc = markerInfo;
        descChanges = "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        setContentView(R.layout.markerbox);

        t = (TextView) findViewById(R.id.markerName);
        ed = (EditText) findViewById(R.id.editName2);
        t.setText(markDesc);

        Button btn = (Button) findViewById(R.id.dialogexitbutton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.renamebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                descChanges = ed.getText().toString();
                dismiss();
            }
        });

    }

    public String getDescChanges(){
        return descChanges;
    }

}
