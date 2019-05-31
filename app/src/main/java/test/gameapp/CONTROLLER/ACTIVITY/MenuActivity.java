package test.gameapp.CONTROLLER.ACTIVITY;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import test.gameapp.CONTROLLER.IO.StepCounter;
import test.gameapp.R;
import test.gameapp.VIEW.MENU.MenuSurfaceView;

public class MenuActivity extends Activity {

    private MenuSurfaceView menuGLView;
    private MenuActivity menuActivity;
    private Button serverBtn, settingsBtn, playerTypeBtn;
    private boolean checkIfHost;
    private StepCounter stepCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkIfHost = false;
        menuActivity = this;
        setContentView(R.layout.activity_main);

        menuGLView = new MenuSurfaceView(this);
        setContentView(menuGLView);
        setOverLayout();
        stepCounter = new StepCounter(this);

    }

    //Tryckbara knappar läggs ovanpå den befintliga grafiken
    private void setOverLayout() {

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(0, 200, 0, 0);

        serverBtn = new Button(this);
        settingsBtn = new Button(this);
        playerTypeBtn = new Button(this);

        settingsBtn.setText("Settings");
        playerTypeBtn.setText("Defender");
        serverBtn.setText("Connect");

        DisplayMetrics metrics = menuActivity.getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        settingsBtn.setHeight((int) (height*0.08));
        playerTypeBtn.setHeight((int) (height*0.08));
        serverBtn.setHeight((int) (height*0.08));
        settingsBtn.setWidth((int) (width * 0.7));
        playerTypeBtn.setWidth((int) (width * 0.7));
        serverBtn.setWidth((int) (width * 0.7));

        LinearLayout.LayoutParams paramsButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        settingsBtn.setLayoutParams(paramsButton);
        playerTypeBtn.setLayoutParams(paramsButton);
        serverBtn.setLayoutParams(paramsButton);

        layout.addView(serverBtn);
        layout.addView(settingsBtn);
        layout.addView(playerTypeBtn);
        layout.setGravity(Gravity.CENTER);

        addContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        serverBtn.setOnClickListener(new ConnectButtonListen());
        playerTypeBtn.setOnClickListener(new PlayerTypeButtonListen());
        settingsBtn.setOnClickListener(new SettingsButtonListen());

    }

    @Override
    protected void onPause() {
        super.onPause();
        menuGLView.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        menuGLView.onResume();
        stepCounter.resume();
    }

    //wifiactivity eller settingsactivity är klara
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) { //settingsactivity
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
        else if(requestCode == 2){ //wifiactivity
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                if(result != null && result.length() > 0)
                    showToast(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    private class ConnectButtonListen implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MenuActivity.this, WiFiActivity.class);
            myIntent.putExtra("player-type", playerTypeBtn.getText().toString());
            startActivityForResult(myIntent,2);
        }
    }

    private class PlayerTypeButtonListen implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (playerTypeBtn.getText().equals("Attacker")) {
                playerTypeBtn.setText("Defender");
            } else {
                playerTypeBtn.setText("Attacker");
            }
        }
    }

    private void showToast(String msg){
        Toast.makeText(menuActivity, msg, Toast.LENGTH_SHORT).show();
    }

    private class SettingsButtonListen implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(MenuActivity.this, SettingsActivity.class);
            startActivityForResult(myIntent, 1);
        }
    }


}
