package com.fullrune.snakebattle;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    private Button multiplayerBtn, singleplayerBtn;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, "ca-app-pub-2455620344756383~7591895934");
        mAdView = findViewById(R.id.adView);
        //String android_id = Settings.Secure.getString(this.getContentResolver(),
       //         Settings.Secure.ANDROID_ID);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        multiplayerBtn = findViewById(R.id.multiplayerbtn);
        singleplayerBtn = findViewById(R.id.singleplayerbtn);

        multiplayerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, ConnectActivity.class);
                //myIntent.putExtra("key", value); //Optional parameters
                //CurrentActivity.this.startActivity(myIntent);
                MainActivity.this.startActivity(myIntent);
            }
        });

        multiplayerBtn.setVisibility(View.GONE);

        singleplayerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, SingleGameActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }
}
