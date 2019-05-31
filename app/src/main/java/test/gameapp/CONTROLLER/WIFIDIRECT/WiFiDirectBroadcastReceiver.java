package test.gameapp.CONTROLLER.WIFIDIRECT;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import java.util.ArrayList;
import java.util.List;

import test.gameapp.CONTROLLER.ACTIVITY.WiFiActivity;

/***
 * Denna klass har kod tagen från https://developer.android.com/guide/topics/connectivity/wifip2p.html
 *
 *
 *
 ****/
public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager manager;
    private Channel channel;
    private WiFiActivity activity;

    private List peers = new ArrayList();

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, Channel channel, WiFiActivity activity) {
        super();
        this.manager = manager;
        this.channel = channel;
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            if (intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1) == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
                activity.setIsWifiP2pEnabled(true);
            else
                activity.setIsWifiP2pEnabled(false);
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (manager != null)
                manager.requestPeers(channel, peerListListener);
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (manager != null){
                NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected())
                    activity.connectedToDevice();
                 else
                    activity.disconnectedFromDevice();
            }
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            WifiP2pDevice device = (WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE);
            //device = this device
        }
    }//public void

    public WifiP2pDevice getDeviceFromList(int nr){
        if(nr>=peers.size())
            return null;
        return (WifiP2pDevice) peers.get(nr);
    }

    private WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            peers.clear();
            peers.addAll(peerList.getDeviceList());
            if (peers.size() > 0)
                activity.populatePeerListView(peers);
        }
    };
}//public class
