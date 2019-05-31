package test.gameapp.CONTROLLER.WIFIDIRECT;

import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import test.gameapp.CONTROLLER.IO.IOManager;
import test.gameapp.MODEL.DATA.PlayerInfo;
import test.gameapp.MODEL.CLIENT.ClientModel;
import test.gameapp.MODEL.SERVER.ServerModel;
import test.gameapp.CONTROLLER.ACTIVITY.WiFiActivity;

/**
 * Created by Andreas on 2016-12-21.
 */

public class WiFiDirectConnectionInfoListener implements WifiP2pManager.ConnectionInfoListener {

    private WiFiActivity activity;
    private WifiP2pInfo info;

    private TCPListenAndAccept tcpListenAndAccept;
    private TCPConnectToSocket tcpConnectToSocket;

    private ServerModel servermodel;
    private ClientModel clientmodel;


    private boolean running;
    private boolean timeout;
    private Timer timer;
    public WiFiDirectConnectionInfoListener(WiFiActivity activity) {
        this.activity = activity;
        running = false;
        servermodel = null;
        clientmodel = null;
        timeout = false;
        timer = new Timer();
    }

    public void endByTimeout() {
        Log.i("timeoutTest1", "testMIDDLE");
        timeout = true;
        if(tcpListenAndAccept != null)
            tcpListenAndAccept.interrupt();
        if(tcpConnectToSocket != null)
            tcpConnectToSocket.interrupt();
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        if (!running) {
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    Log.i("timeoutTest1", "testbefore");
                    endByTimeout();
                    Log.i("timeoutTest1", "testafter");
                }
            }, 7000);

            if (info.groupOwnerAddress == null)
                return;
            // Log.i("ConnectionInfoListener", "onConnectionInfoAvailable inside if");
            this.info = info;

            if (info.isGroupOwner) {
                Log.i("Groupowner", "start tcpListenAndAccept");
                tcpListenAndAccept = new TCPListenAndAccept(25553);
                tcpListenAndAccept.start();
            } else {
                Log.i("NOT Groupowner", "start tcpConnectToSocket");
                tcpConnectToSocket = new TCPConnectToSocket(info.groupOwnerAddress.getHostAddress(), 25553);
                tcpConnectToSocket.start();
            }
            running = true;
        }
    }


    private class TCPListenAndAccept extends Thread {

        private int port;
        private Socket listenSocket;
        private ServerSocket serverSocket;

        public TCPListenAndAccept(int port) {
            this.port = port;
            listenSocket = null;
        }

        @Override
        public void interrupt(){
            try {
                if(serverSocket != null){
                    serverSocket.close();
                    Log.i("timeoutTest1", "testClose1server");
                }
                if(listenSocket != null){
                    listenSocket.close();
                    Log.i("timeoutTest1", "testClose1");
                }

            } catch (IOException e) {
            }
        }

        public void run() {
            boolean success = false;
            String failmessage = null, remoteaddress = null;

            DataInputStream is = null;
            DataOutputStream os = null;
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            try {
                serverSocket = new ServerSocket(port);
                listenSocket = serverSocket.accept();
                remoteaddress = listenSocket.getRemoteSocketAddress().toString().split(":")[0].substring(1);

                if (isPlayersOfDifferentTypes(os = new DataOutputStream(listenSocket.getOutputStream()), is = new DataInputStream(listenSocket.getInputStream()))) {
                    sendDataOverTCP(oos = new ObjectOutputStream(listenSocket.getOutputStream()),ois = new ObjectInputStream(listenSocket.getInputStream()));
                    success = true;
                } else {
                    failmessage = "Players can't be same type.";
                    success = false;
                }
            } catch (Exception e) {
                failmessage = "Error when sending data to client.";
                success = false;
            } finally {
                try {
                    if (serverSocket != null) serverSocket.close();
                } catch (Exception ee) {
                }
                try {
                    if (os != null) os.close();
                } catch (Exception ee) {
                }
                try {
                    if (is != null) is.close();
                } catch (Exception ee) {
                }
                try {
                    if (oos != null) oos.close();
                } catch (Exception ee) {
                }
                try {
                    if (ois != null) ois.close();
                } catch (Exception ee) {
                }
                try {
                    listenSocket.close();
                } catch (Exception ee) {
                }
            }
            if (success) {
                if (activity.getPlayertype().equals("Defender"))
                    activity.startAsDefender(remoteaddress, servermodel);
                else
                    activity.startAsAttacker(remoteaddress, clientmodel);
            } else {
                if(timeout)
                    activity.resetActivity("timeout");
                else
                    activity.resetActivity(failmessage);
            }
        }

        private boolean isPlayersOfDifferentTypes(DataOutputStream os, DataInputStream is) throws IOException {
            String otherplayertype = is.readUTF();//Receive other players playertype.
            os.writeUTF(activity.getPlayertype());//Send out playertype to other player.
            if (otherplayertype.equals(activity.getPlayertype()))
                return false;
            return true;
        }
    }//private class


    private class TCPConnectToSocket extends Thread {

        private String host;
        private int port;
        private Socket connectSocket;

        public TCPConnectToSocket(String host, int port) {
            this.host = host;
            this.port = port;
            connectSocket = null;
        }

        @Override
        public void interrupt(){
            try {
                if(connectSocket != null){
                    connectSocket.close();
                    Log.i("timeoutTest1", "testClose2");
                }

            } catch (IOException e) {
            }
        }

        public void run() {
            boolean success;
            String failmessage = null, remoteaddress = null;

            DataOutputStream os = null;
            DataInputStream is = null;
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;
            try {
                connectSocket = new Socket(host, port);
                remoteaddress = connectSocket.getRemoteSocketAddress().toString().split(":")[0].substring(1);

                if (isPlayersOfDifferentTypes(os = new DataOutputStream(connectSocket.getOutputStream()), is = new DataInputStream(connectSocket.getInputStream()))) {
                    sendDataOverTCP(oos = new ObjectOutputStream(connectSocket.getOutputStream()),ois = new ObjectInputStream(connectSocket.getInputStream()));
                    success = true;
                } else {
                    success = false;
                    failmessage = "Players can't be same type.";
                }

            } catch (Exception e) {
                success = false;
                failmessage = "Error receiving data from server.";
            } finally {
                try {
                    if (os != null) os.close();
                } catch (Exception ee) {
                }
                try {
                    if (is != null) is.close();
                } catch (Exception ee) {
                }
                try {
                    if (oos != null) oos.close();
                } catch (Exception ee) {
                }
                try {
                    if (ois != null) ois.close();
                } catch (Exception ee) {
                }
                try {
                    connectSocket.close();
                } catch (Exception ee) {
                }
            }

            if (success) {
                if (activity.getPlayertype().equals("Defender"))
                    activity.startAsDefender(remoteaddress, servermodel);
                else
                    activity.startAsAttacker(remoteaddress, clientmodel);
            } else {
                if(timeout)
                    activity.resetActivity("timeout");
                else
                    activity.resetActivity(failmessage);
            }
        }

        private boolean isPlayersOfDifferentTypes(DataOutputStream os, DataInputStream is) throws IOException {
            os.writeUTF(activity.getPlayertype());
            String otherplayertype = is.readUTF();
            if (otherplayertype.equals(activity.getPlayertype()))
                return false;
            return true;
        }
    }//private class


    private void sendDataOverTCP(ObjectOutputStream oos,ObjectInputStream ois) throws IOException, ClassNotFoundException {
        Log.i("SendDataOverTCP","1");
        if (activity.getPlayertype().equals("Defender")) {    //Server
            Log.i("SendDataOverTCP","2");
            sendClientModel(oos,ois);
            Log.i("SendDataOverTCP","3");
        } else {                                              //Client
            Log.i("SendDataOverTCP","4");
            clientmodel = receiveClientModel(oos,ois);
            Log.i("SendDataOverTCP","5");
        }
    }

    private void sendClientModel(ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        PlayerInfo attackerInfo = (PlayerInfo) ois.readObject();
        servermodel = new ServerModel(new IOManager(activity).load(),attackerInfo);
        oos.writeObject(servermodel.getClientModel());
        timer.cancel();
        timer.purge();
    }

    private ClientModel receiveClientModel(ObjectOutputStream oos, ObjectInputStream ois) throws IOException, ClassNotFoundException {
        oos.writeObject(new IOManager(activity).load());
        ClientModel clientmodel = (ClientModel) ois.readObject();
        timer.cancel();
        timer.purge();
        return clientmodel;
    }

}//public class