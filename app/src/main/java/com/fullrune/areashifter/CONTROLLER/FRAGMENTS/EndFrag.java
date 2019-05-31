package com.fullrune.areashifter.CONTROLLER.FRAGMENTS;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fullrune.areashifter.CONTROLLER.FileObjects.HighScore;
import com.fullrune.areashifter.CONTROLLER.ACTIVITIES.GameActivity;
import com.fullrune.areashifter.CONTROLLER.IOManager;
import com.fullrune.areashifter.R;
import com.fullrune.areashifter.CONTROLLER.Event;

/**
 * Created by Marcus on 2017-05-08.
 */

public class EndFrag extends Fragment {

    public static final String FRAGMENT_TAG = "END_FRAGMENT";
    private Event.EV event;

    public void setEvent(Event event){
        this.event = event.getEvent();
    }
    private boolean mustEnterName;
    private IOManager ioManager;
    private HighScore highScore;
    private long score;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final EndFrag tmpFrag = (EndFrag) getActivity().getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        View view = inflater.inflate(R.layout.endgamefragment, container, false);
        final GameActivity gameActivity = (GameActivity) getActivity();

        TextView infoText = (TextView) view.findViewById(R.id.endresultText2);
        TextView scoreText = (TextView) view.findViewById(R.id.scoredTxt);
        TextView recordTxt = (TextView) view.findViewById(R.id.newRecordTxt);
        final EditText nameinput = (EditText) view.findViewById(R.id.nameinputTxt);

        ImageButton button = (ImageButton) view.findViewById(R.id.continueEndBtn2);
        if(event == Event.EV.WINNER){
            infoText.setText("Congratulations!" + '\n' + "You Won The Game");
        }else{
            infoText.setText("Game Over");
        }

        mustEnterName = false;
        score = gameActivity.getScore();
        scoreText.setText("Score: " + score);
        ioManager = new IOManager(gameActivity);
        highScore = ioManager.loadHighScore();
        if(highScore == null)
            highScore = new HighScore();

        if(highScore.isNewScore(score)){
            mustEnterName = true;
            recordTxt.setVisibility(View.VISIBLE);
            nameinput.setVisibility(View.VISIBLE);
        }else{
            recordTxt.setVisibility(View.GONE);
            nameinput.setVisibility(View.GONE);
        }


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(mustEnterName){
                    String name = nameinput.getText().toString();
                    if(name == null || name.length() == 0){
                        Toast.makeText(gameActivity, "Input Score Name.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    highScore.insertScore(score, name);
                    ioManager.saveHighScore(highScore);
                }

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
                            gameActivity.showScoreScreen();
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