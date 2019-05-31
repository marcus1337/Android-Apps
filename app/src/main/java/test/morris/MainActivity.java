package test.morris;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;


import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import test.morris.CONTROLLER.BOARDCONTROLLER.CustomBoard;
import test.morris.CONTROLLER.BOARDCONTROLLER.PositionClick;
import test.morris.CONTROLLER.IO_STUFF.IOManager;
import test.morris.CONTROLLER.LOADCONTROLLER.LoadGameActivity;
import test.morris.MODELLEN.GameState;
import test.morris.MODELLEN.NineMenMorrisRules;
import test.morris.CONTROLLER.PREF.OptionPrefActivity;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private CustomBoard boardView;
    private MainActivity mainActivity;
    private int choosenmarker;
    private Toolbar toolbar;
    private IOManager ioManager;
    private GameState gameState;
    private TextView toolText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gameState = new GameState();
        ioManager = new IOManager(this);

        boardView = (CustomBoard) findViewById(R.id.custom_board_view);
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolText = getToolbarTitleView(toolbar);
        toolText.setTextSize(16);

        mainActivity = this;
        context = getApplicationContext();
        choosenmarker = -1;
        boardView.setOnTouchListener(new BoardClickListener());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            getSupportActionBar().hide();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            if (reqCode == 2) { //loadGame
                GameState tmpGameState = (GameState) data.getSerializableExtra("TMP_MODELL_KEY1337");
                if (tmpGameState == null)
                    return;
                gameState = tmpGameState;
                NineMenMorrisRules model = gameState.getGame();
                boardView.updateMarks(model.getBoard(), model.getBlueUnPlacedMarkers(), model.getRedUnPlacedMarkers(), model.getLevel());
                userInfo();
            } else if (reqCode == 1) { //preferences
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                int chosenLevel = sharedPrefs.getInt("level_key", 0);
                gameState.setLevel(chosenLevel, 0);
                NineMenMorrisRules model = gameState.getGame();
                boardView.updateMarks(model.getBoard(), model.getBlueUnPlacedMarkers(), model.getRedUnPlacedMarkers(), model.getLevel());
            }

        if (resultCode == Activity.RESULT_CANCELED && reqCode == 2) { //inga förändringar, återuppta tidigare spel
            GameState tmpGameState = (GameState) data.getSerializableExtra("modell-changes");
            if (tmpGameState == null)
                return;
            gameState = tmpGameState;
        }


    }


    /**
     * DENNA FUNKTION "getToolbarTitleView" ÄR TAGEN FRÅN STACKOVERFLOW__________________________
     * ________________________returnerar toolbarens text som en textview_____________________
     */
    public TextView getToolbarTitleView(Toolbar toolbar) {
        ActionBar actionBar = getSupportActionBar();
        CharSequence actionbarTitle = null;
        if (actionBar != null)
            actionbarTitle = actionBar.getTitle();
        actionbarTitle = TextUtils.isEmpty(actionbarTitle) ? toolbar.getTitle() : actionbarTitle;
        if (TextUtils.isEmpty(actionbarTitle)) return null;
        // can't find if title not set
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View v = toolbar.getChildAt(i);
            if (v != null && v instanceof TextView) {
                TextView t = (TextView) v;
                CharSequence title = t.getText();
                if (!TextUtils.isEmpty(title) && actionbarTitle.equals(title) && t.getId() == View.NO_ID) {
                    //Toolbar does not assign id to views with layout params SYSTEM, hence getId() == View.NO_ID
                    //in same manner subtitle TextView can be obtained.
                    return t;
                }
            }
        }
        return null;
    }

    public void hideMain(){
        getSupportActionBar().hide();
        boardView.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_game:
                showYesNoDialog();
                return true;
            case R.id.help:


                Fragment tmpFrag = new HelpFragment();
                mainActivity.getFragmentManager()
                        .beginTransaction()
                        .add(R.id.sometmpframe, tmpFrag, HelpFragment.FRAGMENT_TAG)
                        .disallowAddToBackStack()
                        .commit();
                //showHelp();
                return true;
            case R.id.load_game:
                Intent i = new Intent(this, LoadGameActivity.class);
                GameState tmp = gameState;
                i.putExtra("MyGameState", tmp);
                startActivityForResult(i, 2);
                return true;
            case R.id.preferences:
                Intent ii = new Intent(this, OptionPrefActivity.class);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("currentUsedLevel", gameState.getGame().getLevel());
                editor.commit();
                startActivityForResult(ii, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void setOverLayVisible(){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            getSupportActionBar().show();
        }
        boardView.setVisibility(View.VISIBLE);
    }




    @Override //anpassa för landscape och vertical orienteringar
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        HelpFragment tmpfrag = (HelpFragment) getFragmentManager().findFragmentByTag(HelpFragment.FRAGMENT_TAG);
        if (tmpfrag != null && tmpfrag.isVisible()) {
            getSupportActionBar().hide();
            return;
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportActionBar().hide();
            } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().show();
        }
    }

    private void setTabText(String str) { //instruktioner för användarna + ändra färger
        if (str == null)
            return;
        int posBlue1 = str.indexOf("BLUE");
        int posBlue2 = str.indexOf("BLUE", 2);
        int posRed1 = str.indexOf("RED");
        int posRed2 = str.indexOf("RED", 2);

        SpannableString span = new SpannableString(str.toString());
        if (posBlue1 != -1) {
            span.setSpan(new ForegroundColorSpan(Color.BLUE), posBlue1, posBlue1 + 4,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (posBlue2 != -1) {
            span.setSpan(new ForegroundColorSpan(Color.BLUE), posBlue2, posBlue2 + 4,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (posRed1 != -1) {
            span.setSpan(new ForegroundColorSpan(Color.RED), posRed1, posRed1 + 4,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        if (posRed2 != -1) {
            span.setSpan(new ForegroundColorSpan(Color.RED), posRed2, posRed2 + 4,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        toolText.setText(span);

    }

    public void restartGame() {
        gameState.startNewGame(new NineMenMorrisRules(1));
        NineMenMorrisRules model = gameState.getGame();
        boardView.updateMarks(model.getBoard(), model.getBlueUnPlacedMarkers(), model.getRedUnPlacedMarkers(), model.getLevel());
        choosenmarker = -1;
        boardView.setSelected(-1);
        setTabText("RED PLAYER PLACE ONE RED MARK!");
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ioManager == null)
            return;

        ioManager.saveData(gameState);
    }

    @Override
    public void onResume() {
        super.onResume();
       // Toast.makeText(context,"TEST: " + gameState.size(),Toast.LENGTH_SHORT).show();
        if (gameState.size() == 0) {

            new LoadDataThread().start();
        }
    }

    private class LoadDataThread extends Thread {
        @Override
        public void run() {
            GameState tmp = ioManager.loadData();
            if (tmp != null) {
                gameState = tmp;
                NineMenMorrisRules tmpModel = gameState.getGame();
                if (tmpModel != null) {
                    final NineMenMorrisRules model = tmpModel;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            boardView.updateMarks(model.getBoard(), model.getBlueUnPlacedMarkers(), model.getRedUnPlacedMarkers(), model.getLevel());
                            userInfo();
                        }
                    });
                }
                else restartGame();
            }
            else restartGame();
        }
    }

    private class BoardClickListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if(gameState.getGame() == null)
                    return false;

                PositionClick positionClick = new PositionClick(boardView.getWidth(), boardView.getHeight());
                int area = positionClick.handleTouch(event.getX(), event.getY(), mainActivity, boardView.getMarks());

                if (area < 0)
                    return false;

                NineMenMorrisRules model = gameState.getGame();

                if (!model.loss(1) && !model.loss(2)) {//Ingen har vunnit
                    if (model.getRemove()) {//SCENARIO 1: En marker ska tas bort
                        model.remove(area, (model.getTurn() * 2) - ((model.getTurn() - 1) * 3)); //om turn är 1 blir värdet 2 och vice versa
                    } else {
                        if (model.allMarksPlaced()) {//SCENARIO 2: En marker ska flyttas
                            if (choosenmarker > -1) {
                                int turn = model.getTurn();
                                if (model.legalMove(area, choosenmarker, model.getTurn())) {
                                    boardView.setTurn(turn);
                                    boardView.moveToAnimation(area);
                                }
                                choosenmarker = -1;
                                boardView.setSelected(-1);
                            } else if (choosenmarker == -1 && model.board(area) == model.getTurn() + 3) {
                                choosenmarker = area;
                                boardView.setSelected(area);
                            }
                        } else {//SCENARIO 3: En marker ska läggas till
                            model.legalMove(area, 0, model.getTurn());
                        }
                    }

                    userInfo();
                    boardView.updateMarks(model.getBoard(), model.getBlueUnPlacedMarkers(), model.getRedUnPlacedMarkers(), model.getLevel());
                }
                gameState.addOrUpdateModel(model);
            }
            return false;
        }
    }

    private void userInfo() {
        //INSTRUCTIONS FOR THE USER.
        NineMenMorrisRules model = gameState.getGame();
        if (model.getTurn() == 1) {
            if (model.getRemove())
                setTabText("BLUE PLAYER REMOVE ONE RED MARK!");
            else if (model.allMarksPlaced())
                setTabText("BLUE PLAYER MOVE ONE BLUE MARK!");
            else
                setTabText("BLUE PLAYER PLACE ONE BLUE MARK!");
        } else {
            if (model.getRemove())
                setTabText("RED PLAYER REMOVE ONE BLUE MARK!");
            else if (model.allMarksPlaced())
                setTabText("RED PLAYER MOVE ONE RED MARK!");
            else
                setTabText("RED PLAYER PLACE ONE RED MARK!");
        }
        if (model.loss(1))
            showVictoryDialog("RED PLAYER WON");
        if (model.loss(2))
            showVictoryDialog("BLUE PLAYER WON");
    }

    private void showVictoryDialog(String victoryMsg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(victoryMsg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        gameState.deleteGame(0);
                        restartGame();
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //spara spelet eller inte?
    private void showYesNoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Save current game?");
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gameState.deleteGame(0);
                restartGame();
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                restartGame();
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

}



