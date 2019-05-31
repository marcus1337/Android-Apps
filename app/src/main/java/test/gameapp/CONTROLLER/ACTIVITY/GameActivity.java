package test.gameapp.CONTROLLER.ACTIVITY;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.Timer;

import test.gameapp.CONTROLLER.IO.IOManager;
import test.gameapp.MODEL.DATA.PlayerAction;
import test.gameapp.MODEL.CLIENT.ClientModel;
import test.gameapp.MODEL.CLIENT.ClientModelGameInfo;
import test.gameapp.MODEL.SERVER.ServerModel;
import test.gameapp.R;
import test.gameapp.VIEW.GAME.GameSurfaceView;
import test.gameapp.CONTROLLER.CLIENT.Client;
import test.gameapp.CONTROLLER.SERVER.Server;

/**
 * Created by Marcus on 2016-12-13.
 */

public class GameActivity extends Activity {

    private GameSurfaceView mGLView;
    private GameActivity gameActivity;
    private Button aBtn, bBtn, settingsBtn;

    private PlayerAction playerAction;
    private ImageButton padBtn;

    private ClientModel clientmodel;

    private Server server;
    private Client client;
    private SendPlayerActionThread sendPlayerActionThread;

    private int playerNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameActivity = this;
        playerNumber = getIntent().getIntExtra("player-type", -1);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (playerNumber == 0) {//Start the server
                try {
                    ServerModel servermodel = (ServerModel) b.getSerializable("servermodel");
                    clientmodel = servermodel.getClientModel();
                    server = new Server(getIntent().getStringExtra("clientaddress"), 55440, 55442, servermodel);
                    client = new Client(this, "127.0.0.1", 55440, 55442);
                    sendPlayerActionThread = new SendPlayerActionThread();
                    sendPlayerActionThread.start();
                } catch (Exception e) {
                    Log.i("GameActivity", "Error when starting server or client.");
                    return;
                }
            } else {
                try {
                    clientmodel = (ClientModel) b.getSerializable("clientmodel");
                    client = new Client(this, getIntent().getStringExtra("serveraddress"), 55440, 55442);
                    sendPlayerActionThread = new SendPlayerActionThread();
                    sendPlayerActionThread.start();
                } catch (Exception e) {
                    Log.i("GameActivity", "Error when starting client.");
                    return;
                }
            }
        } else {
            Log.i("GameActivity", "BUNDLE IS NULL");
            //Throw Exception that Bundle is model is missing
            return;
        }
        playerAction = new PlayerAction();

        mGLView = new GameSurfaceView(this, clientmodel, playerNumber);
        setContentView(mGLView);
        setOverLayout();

    }

    public void updateClientModel(Object obj) {


        if(obj instanceof ClientModelGameInfo){
            Log.i("GameActivity","GAMEINFO!!!");
            long gold = ((ClientModelGameInfo) obj).goldForMe(playerNumber);
            if(gold!=0){
                try {
                    Log.i("GameActivity", "Add " + gold + " to PlayerInfo");
                    new IOManager(this).addSteps(gold);
                }catch(Exception e){
                    Log.i("GameActivity","Error when adding gold");
                }
            }
            if(((ClientModelGameInfo) obj).getServerclosed()){
                endEverything("Game over.");
            }
        }
        clientmodel.updateModel(obj);
        mGLView.updateEvent();
    }

    private void setOverLayout() {
        LayoutInflater inflater = LayoutInflater.from(gameActivity);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.gamelay, null, false);
        LinearLayout ll = new LinearLayout(this);
        ll.addView(layout);
        this.addContentView(ll, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));

        padBtn = (ImageButton) findViewById(R.id.imageButton5);
        padBtn.setBackgroundResource(R.drawable.dpadabsolute);
        padBtn.setOnTouchListener(new DPadListener());
        aBtn = (Button) findViewById(R.id.button1);
        aBtn.setOnTouchListener(new AButtonListen());
        bBtn = (Button) findViewById(R.id.button2);
        bBtn.setOnTouchListener(new BButtonListen());
        settingsBtn = (Button) findViewById(R.id.buttonSettings);
        settingsBtn.setOnClickListener(new SettingsButtonListener());

    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            mGLView.onPause();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mGLView.onResume();
        } catch (Exception e) {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        endEverything("onDestroy - no result will be returned to other activities as it is now.");
    }

    private class DPadListener implements View.OnTouchListener {

        private static final int tresh = 50;

        private int calculateDirection(int x, int y, int midX, int midY) { //1 = upp, 2 = ner, 3 = vänster, 4 = höger

            if (x < midX) {
                if (y > midY - tresh && y < midY + tresh) {
                    return 3;
                } else if (y > midY - tresh) {
                    return 2;
                } else {
                    return 1;
                }

            } else if (x > midX) {
                if (y > midY - tresh && y < midY + tresh) {
                    return 4;
                } else if (y > midY - tresh) {
                    return 2;
                } else {
                    return 1;
                }
            }

            return 0;
        }

        private void pressDirection(int direction) { //1 = upp, 2 = ner, 3 = vänster, 4 = höger

            switch (direction) {
                case 1:
                    playerAction.setUp(true);
                    break;
                case 2:
                    playerAction.setDown(true);
                    break;
                case 3:
                    playerAction.setLeft(true);
                    break;
                case 4:
                    playerAction.setRight(true);
                    break;
                default:
                    break;
            }

        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int maxX = padBtn.getLayoutParams().width;
            int maxY = padBtn.getLayoutParams().height;
            int midX = maxX / 2;
            int midY = maxY / 2;
            int x = (int) event.getX();
            int y = (int) event.getY();
            int direction = calculateDirection(x, y, midX, midY);

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressDirection(direction);
                    break;
                case MotionEvent.ACTION_UP:
                    playerAction.setNoDirection();
                    break;
            }

            return false;
        }
    }

    private class AButtonListen implements View.OnTouchListener {
        private boolean a = true;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if(a) {
                        playerAction.setButtonAttack(true);
                        a = false;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    playerAction.setButtonAttack(false);
                    a = true;
                    break;
            }

            return false;
        }
    }

    private class BButtonListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    playerAction.setButtonUse(true);
                    break;
                case MotionEvent.ACTION_UP:
                    playerAction.setButtonUse(false);
                    break;
            }

            return false;
        }
    }

    public void setOverLayVisible(){
        padBtn.setVisibility(View.VISIBLE);
        settingsBtn.setVisibility(View.VISIBLE);
        aBtn.setVisibility(View.VISIBLE);
        bBtn.setVisibility(View.VISIBLE);
    }

    private class SettingsButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            padBtn.setVisibility(View.INVISIBLE);
            settingsBtn.setVisibility(View.INVISIBLE);
            aBtn.setVisibility(View.INVISIBLE);
            bBtn.setVisibility(View.INVISIBLE);
            Bundle data = new Bundle();
            data.putInt("player_typeInt", playerNumber);

            //gameActivity.getFragmentManager().addOnBackStackChangedListener(backStackListener);
            Fragment tmpFrag = new HelpFragment();
            tmpFrag.setArguments(data);

            gameActivity.getFragmentManager()
                    .beginTransaction()
                    .add(R.id.sometmpframe, tmpFrag, HelpFragment.FRAGMENT_TAG)
                    .disallowAddToBackStack()
                    .commit();
        }
    }

    private void endEverything(String result) { //kalla på denna för att avsluta spelet
        if (server != null) {
            server.stopAll(); //klar
        }
        if (client != null) {
            client.stopAll(); //klar
        }
        if (sendPlayerActionThread != null) {
            sendPlayerActionThread.interrupt(); //klar
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", result);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private class SendPlayerActionThread extends Thread {
        private int counter;
        public void run() {
            counter = 0;
            while (true) {
                counter++;
                try {
                    if (Thread.interrupted())
                        return;
                    if(client.isServerAlive()){
                        endEverything("server timed out");
                    }
                    if(server != null && server.isClientTimeout()){
                        endEverything("opponent lost connection");
                    }

                    Thread.sleep(33);
                    if (playerAction.isUp() || playerAction.isButtonAttack() || playerAction.isButtonUse() || playerAction.isDown() || playerAction.isLeft() || playerAction.isRight()){
                        client.sendPlayerActionOverUDP(playerAction);
                        counter = 0;
                    }
                    else if(counter > 50){
                        counter = 0;
                        client.sendPlayerActionOverUDP(playerAction);
                    }

                    playerAction.setButtonAttack(false);
                } catch (Exception e) {

                }
            }
        }//public void
    }//private class


}