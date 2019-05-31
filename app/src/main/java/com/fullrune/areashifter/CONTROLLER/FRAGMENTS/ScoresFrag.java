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

import com.fullrune.areashifter.CONTROLLER.Event;
import com.fullrune.areashifter.CONTROLLER.FileObjects.HighScore;
import com.fullrune.areashifter.CONTROLLER.ACTIVITIES.GameActivity;
import com.fullrune.areashifter.CONTROLLER.IOManager;
import com.fullrune.areashifter.R;

/**
 * Created by Marcus on 2017-05-09.
 */

public class ScoresFrag extends Fragment {

    public static final String FRAGMENT_TAG = "SCORES_FRAGMENT";
    private Event.EV event;

    public void setEvent(Event event){
        this.event = event.getEvent();
    }

    private IOManager ioManager;
    private HighScore highScore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final ScoresFrag tmpFrag = (ScoresFrag) getActivity().getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        View view = inflater.inflate(R.layout.scoresfragment, container, false);
        final GameActivity gameActivity = (GameActivity) getActivity();

        TextView infoText = (TextView) view.findViewById(R.id.scoredTxt);
        TextView scoreText = (TextView) view.findViewById(R.id.scorelista);

        ImageButton button = (ImageButton) view.findViewById(R.id.continuescoresbtn);



        ioManager = new IOManager(gameActivity);
        highScore = ioManager.loadHighScore();

        String scrTxt = "";
        if(highScore != null)
        for(int i = 0; i < 5; i++){
            scrTxt += (i+1) +". " + highScore.getName(i) + ", " + highScore.getScore(i) + "\n\n";
        }
        scoreText.setText(scrTxt);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //Your animation
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);

                //You can use AnimationListener, MagicAnimationListener is simply a class extending it.
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //This is the key, when the animation is finished, remove the fragment.
                        try {
                            // gameActivity.setOverLayVisible();
                            gameActivity.exitToMain();
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

        return view;
    }
}
