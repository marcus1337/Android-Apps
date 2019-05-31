package test.morris;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class HelpFragment extends Fragment {

    public static final String FRAGMENT_TAG = "SPECIAL_FRAGMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final HelpFragment tmpFrag = (HelpFragment) getActivity().getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        View view = inflater.inflate(R.layout.howto_fragment, container, false);
        final MainActivity gameActivity = (MainActivity) getActivity();

        gameActivity.hideMain();
        TextView infoText = (TextView) view.findViewById(R.id.goalText);
        Button button = (Button) view.findViewById(R.id.buttonDone);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gameActivity.setOverLayVisible();
                getActivity().getFragmentManager().beginTransaction()
                        .remove(tmpFrag).disallowAddToBackStack().commit();


            }
        });


        infoText.setText(someInfo);

        return view;
    }

    private static final String someInfo = "Your goal is to remove your opponents pieces or to make it impossible for the other player to move." +
            "\n" + "To move a piece click on it and when it has a circle around it click on an adjacent yellow square."
            + "The game begins by letting each player put down 9 pieces on the board, when that is done the players may start moving."
            + "You can NOT remove pieces that are aligned by 3 in a row."
            +"\n\n" + "Have fun and dont forget to visit the website www.FullRune.com";


}