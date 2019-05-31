package com.fullrune.areashifter.CONTROLLER.ACTIVITIES;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.fullrune.areashifter.CONTROLLER.COMMANDS.CommandManager;
import com.fullrune.areashifter.CONTROLLER.Event;
import com.fullrune.areashifter.CONTROLLER.FRAGMENTS.EndFrag;
import com.fullrune.areashifter.CONTROLLER.FRAGMENTS.LvlFrag;
import com.fullrune.areashifter.CONTROLLER.FRAGMENTS.PauseFrag;
import com.fullrune.areashifter.CONTROLLER.FRAGMENTS.ScoresFrag;
import com.fullrune.areashifter.CONTROLLER.FileObjects.Purchases;
import com.fullrune.areashifter.CONTROLLER.GameLoopThread;
import com.fullrune.areashifter.CONTROLLER.IOManager;
import com.fullrune.areashifter.CONTROLLER.OnSwipeTouchListener;
import com.fullrune.areashifter.MODEL.Model;
import com.fullrune.areashifter.R;
import com.fullrune.areashifter.VIEW.Board;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Marcus on 2017-04-24.
 */

public class GameActivity extends Activity {

    private Board board;
    private GameLoopThread gameThread;
    private Model model;
    private CommandManager commandManager;
    private GameActivity gameActivity;
    private String condition;
    private ImageButton imageButton;

    private InterstitialAd mInterstitialAd;

    private long adTimerStart = 0;
    public void setAdTimerStart(long someTime){
        adTimerStart = someTime;
    }

    public InterstitialAd getAd() {
        if(System.currentTimeMillis() - adTimerStart > 10000)
            return mInterstitialAd;
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameactivity);

        mInterstitialAd = new InterstitialAd(this);

        mInterstitialAd.setAdUnitId("ca-app-pub-2455620344756383/5287342359");


        requestNewInterstitial();

        imageButton = (ImageButton) findViewById(R.id.pauseimagebtn);
        imageButton.setOnTouchListener(new PausBtnListener());

        gameActivity = this;
        board = (Board) findViewById(R.id.gameBoard);
        commandManager = new CommandManager();
        board.setOnTouchListener(new OnSwipeTouchListener(this, commandManager));
        Intent intent = getIntent();
        condition = intent.getStringExtra("startCondition");
        if (condition.equals("resumegame")) {
            Model m = new IOManager(gameActivity).loadModel();
            if (m != null) {
                m.setPaused(false);
                board.setModel(m);
                board.setCanDraw(true);
            }
            else {
                newGame();
            }
        } else {
            newGame();
        }

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                if(gameThread != null)
                    gameThread.setRunning(false);
                gameThread = new GameLoopThread(board, commandManager, gameActivity);
                gameThread.start();

            }
        });

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest);
    }

    private void newGame() {
        Model m = new Model();
        m.init(500, 250);
        Purchases purchases = new IOManager(gameActivity).loadPurchases();
        if (purchases != null && purchases.isExtraLives()) {
            m.getPlayer().setLives(m.getPlayer().getLives() + 5);
        }
        board.setCanDraw(false);
        board.setModel(m);

    }

    public void restartLvl() {
        board.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.VISIBLE);
        board.setCanDraw(true);
        board.getModel().setPaused(false);
        board.notify(new Event(Event.EV.RESTART));
        gameThread.setPaused(false);
    }

    public void resumeLvl() {
        board.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.VISIBLE);
        board.setCanDraw(true);
        // board.notify(new Event(Event.EV.RESUME));
        board.getModel().setPaused(false);
        gameThread.setPaused(false);
    }

    private class PausBtnListener implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            switch (e.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.mennuu2);
                    bm = Bitmap.createScaledBitmap(bm, imageButton.getWidth(), imageButton.getHeight(), true);
                    imageButton.setImageBitmap(bm);
                    return true;
                case MotionEvent.ACTION_UP:
                    Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.mennuu);
                    bm2 = Bitmap.createScaledBitmap(bm2, imageButton.getWidth(), imageButton.getHeight(), true);
                    imageButton.setImageBitmap(bm2);
                    if (e.getX() >= 0 && e.getY() >= 0 && e.getX() < imageButton.getWidth() && e.getY() < imageButton.getHeight()) {

                        board.getModel().setPaused(true);
                    }

                    return true;
            }
            return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (gameThread == null) {
           // Log.i("WOLOLOOO", "WWLLWLROLRW");
            gameThread = new GameLoopThread(board, commandManager, this);
            if (condition != null && condition.equals("resumegame")) {
                gameThread.notify(new Event(Event.EV.LOADGAME));
            }
            gameThread.start();
        }
        else{
            gameThread.notify(new Event(Event.EV.RESUME));
        }
    }

    public void continueBtn() {
        board.setVisibility(View.VISIBLE);
        imageButton.setVisibility(View.VISIBLE);
        board.setCanDraw(true);
        board.notify(new Event(Event.EV.NYLEVEL));
        board.getModel().setPaused(false);
        gameThread.setPaused(false);
    }

    public void showScoreScreen() {
        gameThread.setPaused(true);
        board.setVisibility(View.GONE);
        imageButton.setVisibility(View.GONE);
        ScoresFrag tmpFrag = new ScoresFrag();
        gameActivity.getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                .add(R.id.sometmpframe, tmpFrag, ScoresFrag.FRAGMENT_TAG)
                .disallowAddToBackStack()
                .commitAllowingStateLoss();
    }

    public void exitToMain() {
        finish();
    }

    public int getLevel() {
        return board.getModel().getLevel();
    }

    public long getScore() {
        return board.getModel().getTotalScore();
    }

    public void notify(final Event event) {
        gameActivity.runOnUiThread(new Runnable() {
            public void run() {

                if (event.getEvent() == Event.EV.NYLEVEL) {

                    gameThread.setPaused(true);
                    board.setVisibility(View.GONE);
                    imageButton.setVisibility(View.GONE);
                    Fragment tmpFrag = new LvlFrag();
                    gameActivity.getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                            .add(R.id.sometmpframe, tmpFrag, LvlFrag.FRAGMENT_TAG)
                            .disallowAddToBackStack()
                            .commitAllowingStateLoss();
                    gameActivity.getFragmentManager().executePendingTransactions();
                } else if (event.getEvent() == Event.EV.WINNER) {
                    gameThread.setPaused(true);
                    board.setVisibility(View.GONE);
                    imageButton.setVisibility(View.GONE);
                    EndFrag tmpFrag = new EndFrag();
                    tmpFrag.setEvent(event);
                    gameActivity.getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                            .add(R.id.sometmpframe, tmpFrag, EndFrag.FRAGMENT_TAG)
                            .disallowAddToBackStack()
                            .commitAllowingStateLoss();
                    gameActivity.getFragmentManager().executePendingTransactions();
                } else if (event.getEvent() == Event.EV.GAMEOVER) {
                    gameThread.setPaused(true);
                    board.setVisibility(View.GONE);
                    imageButton.setVisibility(View.GONE);
                    EndFrag tmpFrag = new EndFrag();
                    tmpFrag.setEvent(event);
                    gameActivity.getFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                            .add(R.id.sometmpframe, tmpFrag, EndFrag.FRAGMENT_TAG)
                            .disallowAddToBackStack()
                            .commitAllowingStateLoss();
                    gameActivity.getFragmentManager().executePendingTransactions();
                }
                else if(event.getEvent() == Event.EV.PAUSE){
                    gameThread.setPaused(true);
                    board.setVisibility(View.GONE);
                    imageButton.setVisibility(View.GONE);
                    Fragment tmpFrag = new PauseFrag();
                    gameActivity.getFragmentManager()
                            .beginTransaction()
                            .add(R.id.sometmpframe, tmpFrag, PauseFrag.FRAGMENT_TAG)
                            .disallowAddToBackStack()
                            .commitAllowingStateLoss();
                    gameActivity.getFragmentManager().executePendingTransactions();
                }


                //Toast.makeText(gameActivity, "Test: " + event.getEvent(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        super.onSaveInstanceState(outState);
    }

    public void saveAndExit() {
        if (board != null) {
            Model m = board.getModel();
            if (m != null) {
                new IOManager(getApplicationContext()).saveModel(m);
            }
        }
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();

        FragmentManager f = gameActivity.getFragmentManager();
        Fragment pane1 = f.findFragmentByTag(EndFrag.FRAGMENT_TAG);
        Fragment pane2 = f.findFragmentByTag(LvlFrag.FRAGMENT_TAG);
        Fragment pane3 = f.findFragmentByTag(PauseFrag.FRAGMENT_TAG);
        Fragment pane4 = f.findFragmentByTag(ScoresFrag.FRAGMENT_TAG);
        if (pane1 != null)
            f.beginTransaction().remove(pane1).commit();
        if (pane2 != null)
            f.beginTransaction().remove(pane2).commit();
        if (pane3 != null)
            f.beginTransaction().remove(pane3).commit();
        if (pane4 != null){
            f.beginTransaction().remove(pane4).commit();
            if (gameThread != null)
                gameThread.setRunning(false);
            finish();
        }


        if (gameThread != null) {
            gameThread.setRunning(false);
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        gameThread = null;
    }

}
