package test.gameapp.CONTROLLER.ACTIVITY;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import test.gameapp.MODEL.CLIENT.ClientModel;
import test.gameapp.MODEL.SERVER.ServerModel;
import test.gameapp.R;
import test.gameapp.CONTROLLER.WIFIDIRECT.WiFiDirectConnectionInfoListener;
import test.gameapp.CONTROLLER.WIFIDIRECT.WiFiDirectBroadcastReceiver;

/***
 * Denna klass har kod tagen från https://developer.android.com/guide/topics/connectivity/wifip2p.html
 *
 ****/
public class WiFiActivity extends AppCompatActivity {

    private ListView main_listView_peers;
    private final IntentFilter intentFilter = new IntentFilter();

    //private WifiManager wifiManager;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private WiFiDirectBroadcastReceiver receiver = null;
    private WiFiDirectConnectionInfoListener conninfolistener;

    private WiFiActivity wiFiActivity;
    private String playertype;
    private Toast toast;

    private boolean shouldToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        wiFiActivity = this;
        shouldToast = true;

        playertype = getIntent().getStringExtra("player-type");

        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        disconnect();//TODO: FIXA!!!

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        conninfolistener = new WiFiDirectConnectionInfoListener(this);

        main_listView_peers = (ListView) findViewById(R.id.listView1); //holds all the discovered peers.

        //When a peer is selected in the main_listView_peers, a connection towards that peer will be made.
        main_listView_peers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                WifiP2pDevice device = receiver.getDeviceFromList(arg2);
                WifiP2pConfig config = new WifiP2pConfig();

                try {
                    config.deviceAddress = device.deviceAddress;
                } catch (NullPointerException e) {
                    resetActivity(e.toString());
                    return;
                }

                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        showToast("Connecting to peer...");
                    }

                    @Override
                    public void onFailure(int reason) {
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        shouldToast = true;
        receiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        shouldToast = false;
        // toast = Toast.makeText(wiFiActivity, "never_use", Toast.LENGTH_SHORT);
    }

    public void resetActivity(final String failmessage) {
        //KILL THE CONNECTION BETWEEN THE DEVICES!!!!!!!!!!!
        Handler mainHandler = new Handler(wiFiActivity.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                showToast(failmessage);
            } // This is your code
        };
        mainHandler.post(myRunnable);
        disconnect();
        Runnable myRunnable2 = new Runnable() {
            @Override
            public void run() {
                recreate();
            }
        };
        mainHandler.post(myRunnable2);
    }

    public void startAsDefender(String clientaddress, ServerModel sm) {
        Intent myIntent = new Intent(WiFiActivity.this, GameActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("servermodel", sm);
        myIntent.putExtras(b);
        myIntent.putExtra("clientaddress", clientaddress);
        myIntent.putExtra("player-type", 0);
        startActivityForResult(myIntent, 1);
    }

    public void startAsAttacker(String serveraddress, ClientModel cm) {
        Intent myIntent = new Intent(WiFiActivity.this, GameActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("clientmodel", cm);
        myIntent.putExtras(b);
        myIntent.putExtra("serveraddress", serveraddress);
        myIntent.putExtra("player-type", 1);
        startActivityForResult(myIntent, 1);
    }

    /**
     * If the boolean p2pEnabled is set to FALSE the TextView textview_nearby_wifi_direct_devices
     * visibility will be set to invisible and the TextView textview_wifi_disabled will be set to
     * visible.
     * <p>
     * If the boolean p2pEnabled is set to TRUE a discovery for peers will be activated and the
     * TextView textview_wifi_disabled visibility will be set to invisible and the TextView
     * textview_nearby_wifi_direct_devices will be set to visible.
     */
    public void setIsWifiP2pEnabled(boolean p2pEnabled) {
        if (p2pEnabled) { //WiFi is enabled
            mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    showToast("Searching for peers...");
                }

                @Override
                public void onFailure(int reasonCode) {
                }
            });
        } else {        //WiFi is disabled
            showToast("Please turn on WiFi");
            //  wifiManager.setWifiEnabled(true);//Enable WiFi
        }
    }

    /**
     * Populates the ListView main_listView_peers with the List peers
     */
    public void populatePeerListView(List peers) {
        List peerList = new ArrayList();
        for (int i = 0; i < peers.size(); i++) {
            peerList.add(peers.get(i).toString().split("primary type:")[0]);
        }
        main_listView_peers.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, peerList));
    }

    public void connectedToDevice() {
        mManager.requestConnectionInfo(mChannel, conninfolistener);
    }

    public void disconnectedFromDevice() {
    }

    public String getPlayertype() {
        return playertype;
    }

    private void showToast(String text) {
        if (toast != null)
            toast.cancel();

        if(!shouldToast)
            return;

        toast = Toast.makeText(this.getApplicationContext(), text, Toast.LENGTH_LONG);
        toast.show();
    }

    /*
     * If there is an ongoing WiFi p2p connection then disconnect from it.
     */
    private void disconnect() {
        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onFailure(int reasonCode) {
                Log.d("theWifiTag", "Disconnect failed. Reason :" + reasonCode);
            }

            @Override
            public void onSuccess() {
                recreate();
                Log.d("theWifiTag", "succes I guesss.. ");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (toast != null)
            toast.cancel();

        if (requestCode == 1) {
            String result = "";
            if (resultCode == Activity.RESULT_OK) {
                result = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                result = "Cancelled the game.";
            }

            if(result.length() < 1 && data != null && data.hasExtra("result"))
                result = data.getStringExtra("result");

            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", result);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }//onActivityResult


}//public class