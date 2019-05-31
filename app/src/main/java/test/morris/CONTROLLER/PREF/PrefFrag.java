package test.morris.CONTROLLER.PREF;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.Toast;

import test.morris.R;

public class PrefFrag extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SwitchPreference swi1, swi2, swi3;
    private Preference button, switch1, switch2, switch3;
    private boolean switch_level_1, switch_level_2, switch_level_3;
    private int oldSelectedLevel;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencescreen);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        oldSelectedLevel = preferences.getInt("currentUsedLevel", 1);
        switch_level_1 = switch_level_2 = switch_level_3 = false;
        button = findPreference("prefBtn");

        switch1 = findPreference("swi1");
        switch2 = findPreference("swi2");
        switch3 = findPreference("swi3");

        swi1 = (SwitchPreference) switch1;
        swi2 = (SwitchPreference) switch2;
        swi3 = (SwitchPreference) switch3;

        if(oldSelectedLevel == 1){
            swi1.setChecked(true);
            swi2.setChecked(false);
            swi3.setChecked(false);
            setSummaries();
        }
        else if(oldSelectedLevel == 2){
            swi1.setChecked(false);
            swi2.setChecked(true);
            swi3.setChecked(false);
            setSummaries();
        }
        else if(oldSelectedLevel == 3){
            swi1.setChecked(false);
            swi2.setChecked(false);
            swi3.setChecked(true);
            setSummaries();
        }

        swi1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                swi2.setChecked(false);
                swi3.setChecked(false);
                return false;
            }
        });

        swi2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                swi1.setChecked(false);
                swi3.setChecked(false);
                return false;
            }
        });

        swi3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                swi1.setChecked(false);
                swi2.setChecked(false);
                return false;
            }
        });


        SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences2.edit();

        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                switch_level_1 = preferences.getBoolean("swi1", false);
                switch_level_2 = preferences.getBoolean("swi2", false);
                switch_level_3 = preferences.getBoolean("swi3", false);

                int level = 0;
                if(switch_level_1){
                    level = 1;
                }
                if(switch_level_2){
                    level = 2;
                }
                if(switch_level_3){
                    level = 3;
                }

                if(level == 0){
                    Toast.makeText(getContext(), "CHOOSE A LEVEL, PLEASE", Toast.LENGTH_SHORT).show();
                }
                else{
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("level_key", level);
                    editor.commit();
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
                return false;
            }
        });


    }

    private void setSummaries(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        switch_level_1 = preferences.getBoolean("swi1", false);
        switch_level_2 = preferences.getBoolean("swi2", false);
        switch_level_3 = preferences.getBoolean("swi3", false);

        swi1.setSummary(switch_level_1 ? "nothing fancy about this level, normal rules apply" : "");
        swi2.setSummary(switch_level_2 ? "level 2 lets players move diagonally within the inner field" : "");
        swi3.setSummary(switch_level_3 ? "level 3 lets players move diagonally from all corner positions" : "");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setSummaries();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}