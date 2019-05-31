package test.morris.CONTROLLER.LOADCONTROLLER;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import test.morris.MODELLEN.GameState;
import test.morris.MODELLEN.NineMenMorrisRules;
import test.morris.R;

/**
 * Created by Marcus on 2016-11-27.
 */

public class LoadGameActivity extends Activity {

    private Button cancelBtn;
    private ListView dataListView;
    private GameState gameState;
    private ListAdapter listAdapter;
    private ArrayList<NineMenMorrisRules> values;
    private NineMenMorrisRules unlistedCurrentGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_old_games);
        cancelBtn = (Button) findViewById(R.id.cancelButton);
        dataListView = (ListView) findViewById(R.id.loadListView);
        cancelBtn.setOnClickListener(new CancelListener());

        Intent intent = getIntent();
        if (intent != null) {
            gameState = (GameState) intent.getSerializableExtra("MyGameState");
        }

        if (gameState != null) {
            values = gameState.getAllGames();
            unlistedCurrentGame = values.get(0);
            values.remove(0);
            listAdapter = new ListAdapter(this, R.layout.item_list_row, values);
            dataListView.setAdapter(listAdapter);
        }
        dataListView.setOnItemClickListener(new ListaListener());
        listAdapter.notifyDataSetChanged();

    }

    @Override
    public void onPause() {
        super.onPause();
        values.add(0, unlistedCurrentGame);
    }


    //ladda ett sparat spel
    private class ListaListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent returnIntent = new Intent();
            values.add(0, unlistedCurrentGame);
            gameState.setAllGames(values);
            gameState.selectNewGame(position + 1);
            returnIntent.putExtra("TMP_MODELL_KEY1337", gameState);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    //strunta i att ladda och gå tillbaka till mainActivity
    private class CancelListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent returnIntent = new Intent();
            values.add(0, unlistedCurrentGame);
            gameState.setAllGames(values);
            returnIntent.putExtra("modell-changes", gameState);
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        }
    }

}
