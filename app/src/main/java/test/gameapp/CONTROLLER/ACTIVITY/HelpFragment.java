package test.gameapp.CONTROLLER.ACTIVITY;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import test.gameapp.R;

/**
 * Created by Marcus on 2017-01-07.
 */

public class HelpFragment extends Fragment {

    public static final String FRAGMENT_TAG = "SPECIAL_FRAGMENT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final HelpFragment tmpFrag = (HelpFragment) getActivity().getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        View view = inflater.inflate(R.layout.howto_fragment, container, false);
        final GameActivity gameActivity = (GameActivity) getActivity();

        TextView infoText = (TextView) view.findViewById(R.id.goalText);
        Button button = (Button) view.findViewById(R.id.buttonDone);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                gameActivity.setOverLayVisible();
                getActivity().getFragmentManager().beginTransaction().remove(tmpFrag).disallowAddToBackStack().commit();
            }
        });

        Bundle extras = getArguments();
        int type = extras.getInt("player_typeInt", -1);
        if(type == 0)
            infoText.setText(defenderInfo);
        if(type == 1)
            infoText.setText(attackerInfo);

        return view;
    }

    private static final String attackerInfo = "Your goal is to either kill the defender or steal his gold. Befare of his mighty defence." +
            "The defender has bombardment towers shooting cannonballs at you and a great wall around his base. You can" +
            "get into the defenders base by attacking the orange wall repeatedly." +
            "\n" +
            "The defenders gold is stored in his secret chest. By opening it and then running" +
            "away from his base back to where you started will result in you winning the game." +
            "\n" +
            "To steal gold from a chest press the blue B-button. To attack press the red A-button. To move touch the D-pad.";

    private static final String defenderInfo = "Your goal is to prevent the attacker from stealing your gold and running " +
            "away with it to outside of your base. You must kill the attacker in order to win the game." +
            "If the attacker steals your gold you will be notified by a 'GOLD' text splash screen. You become much faster" +
            "when the attacker steals your gold. You must use your superspeed in order to catch up with the attacker and kill " +
            "him."+
            "\n" +
            "To steal gold from a chest press the blue B-button. To attack press the red A-button. To move touch the D-pad.";

}
