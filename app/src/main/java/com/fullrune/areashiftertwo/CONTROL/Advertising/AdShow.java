package com.fullrune.areashiftertwo.CONTROL.Advertising;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AdShow {
    private Context context;

    private InterstitialAd mInterstitialAd;

    private long adTimerStart = 0;
    public void setAdTimerStart(long someTime) {
        adTimerStart = someTime;
    }

    public InterstitialAd getAd() {
        if(System.currentTimeMillis() - adTimerStart > 10000)
            return mInterstitialAd;
        return null;
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    public AdShow(Context context){
        this.context = context;
        // init();
    }

    private static boolean hasInitializedMob = false;

    public void  init(){

        if(!hasInitializedMob)
            MobileAds.initialize(context, "ca-app-pub-2455620344756383~8088681112");
        hasInitializedMob = true;

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId("ca-app-pub-2455620344756383/4859882544");
        requestNewInterstitial();

        mInterstitialAd.setAdListener(new OnExitAd());
        setAdTimerStart(System.currentTimeMillis());
    }

    private class OnExitAd extends AdListener{
        @Override
        public void onAdClosed() {
            requestNewInterstitial();
            adTimerStart = System.currentTimeMillis();
        }
    }

    public void showAd(){
        InterstitialAd ad = getAd();
        if(ad != null && ad.isLoaded())
            ad.show();
    }

}
