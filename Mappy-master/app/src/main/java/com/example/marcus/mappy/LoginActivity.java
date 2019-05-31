package com.example.marcus.mappy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Marcus on 2016-01-17.
 */
public class LoginActivity extends Activity {

    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        if(new FileManager().checkFileExist(getApplicationContext())){
            Intent i = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(i);

        }

        editText = (EditText) findViewById(R.id.editName2);
        button = (Button) findViewById(R.id.myLogin);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().length() > 0) {
                    Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                    String loginName = editText.getText().toString();
                    i.putExtra("login_variable_name", loginName);
                    startActivity(i);
                }

            }
        });



    }


}
