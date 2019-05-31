package com.fullrune.areashifter.CONTROLLER.FRAGMENTS;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fullrune.areashifter.CONTROLLER.FileObjects.Purchases;
import com.fullrune.areashifter.CONTROLLER.ACTIVITIES.GameActivity;
import com.fullrune.areashifter.CONTROLLER.IOManager;
import com.fullrune.areashifter.R;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Marcus on 2017-05-08.
 */

public class LvlFrag extends Fragment {

    public static final String FRAGMENT_TAG = "LVL_FRAGMENT";
    private boolean doOnce;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final LvlFrag tmpFrag = (LvlFrag) getActivity().getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        View view = inflater.inflate(R.layout.lvlfragment, container, false);
        final GameActivity gameActivity = (GameActivity) getActivity();
        doOnce = false;

        TextView infoText = (TextView) view.findViewById(R.id.lvlHeaderTxt);
        ImageButton button = (ImageButton) view.findViewById(R.id.continueLvlBtn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (doOnce)
                    return;
                //Your animation
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);

                //You can use AnimationListener, MagicAnimationListener is simply a class extending it.
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        doOnce = true;
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //This is the key, when the animation is finished, remove the fragment.
                        try {
                            // gameActivity.setOverLayVisible();
                            gameActivity.continueBtn();
                            getActivity().getFragmentManager().beginTransaction()
                                    .remove(tmpFrag).disallowAddToBackStack().commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                //Start the animation.
                getView().startAnimation(animation);


            }
        });


        infoText.setText(someInfo + gameActivity.getLevel());

        if(gameActivity.getLevel() % 2 == 0 && gameActivity.getLevel() != 2){
            Purchases purchases = new IOManager(gameActivity).loadPurchases();
            if(purchases == null || !purchases.isPremium()){
                InterstitialAd ad = gameActivity.getAd();
                if(ad != null && ad.isLoaded())
                    ad.show();
            }
            gameActivity.setAdTimerStart(System.currentTimeMillis());
        }


        return view;
    }

    private static final String someInfo = "Level: ";

}