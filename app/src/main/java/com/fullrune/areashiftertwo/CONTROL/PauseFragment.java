package com.fullrune.areashiftertwo.CONTROL;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fullrune.areashiftertwo.R;
import com.fullrune.areashiftertwo.VIEW.GameOverView;

public class PauseFragment extends Fragment {

    public static final String FRAGMENT_TAG = "PAUSE_FRAGMENT";
    private Button continueGameBtn;
    private PauseFragment pauseFragment;
    private GameActivity gameActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        pauseFragment = (PauseFragment) getActivity().getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        View view = inflater.inflate(R.layout.pause_fragment, container, false);
        gameActivity = (GameActivity) getActivity();

        continueGameBtn = (Button) view.findViewById(R.id.continueGameBtn1);
        continueGameBtn.setOnClickListener(new BackButtonClickListener());

        return view;
    }

    private class BackButtonClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            gameActivity.unHideEveryThing();
            getActivity().getFragmentManager().beginTransaction()
                    .remove(pauseFragment).disallowAddToBackStack().commit();
        }
    }

}
