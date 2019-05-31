package test.morris.CONTROLLER.PREF;

import android.os.Bundle;
import android.preference.PreferenceActivity;


/**
 * Created by Marcus on 2016-11-24.
 */

public class OptionPrefActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefFrag()).commit();


    }

}
