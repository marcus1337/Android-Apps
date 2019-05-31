package com.fullrune.areashifter.CONTROLLER.FRAGMENTS;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.Fragment;

import com.fullrune.areashifter.CONTROLLER.ACTIVITIES.GameActivity;
import com.fullrune.areashifter.R;

/**
 * Created by Marcus on 2017-05-10.
 */

public class PauseFrag extends Fragment {

    public static final String FRAGMENT_TAG = "PAUSE_FRAGMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final PauseFrag tmpFrag = (PauseFrag) getActivity().getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        View view = inflater.inflate(R.layout.pausefragment, container, false);
        final GameActivity gameActivity = (GameActivity) getActivity();

        Button resumebtn = (Button) view.findViewById(R.id.resumbtn);
        Button savebtn = (Button) view.findViewById(R.id.savebtn);
        Button restartbtn = (Button) view.findViewById(R.id.restartlvlbtn);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameActivity.saveAndExit();
                getActivity().getFragmentManager().beginTransaction()
                        .remove(tmpFrag).disallowAddToBackStack().commit();
            }
        });

        resumebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameActivity.resumeLvl();
                getActivity().getFragmentManager().beginTransaction()
                        .remove(tmpFrag).disallowAddToBackStack().commit();
            }
        });

        restartbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameActivity.restartLvl();
                getActivity().getFragmentManager().beginTransaction()
                        .remove(tmpFrag).disallowAddToBackStack().commit();
            }
        });

        return view;
    }

}