package test.gameapp.CONTROLLER.CLIENT;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import test.gameapp.CONTROLLER.ACTIVITY.GameActivity;
import test.gameapp.MODEL.DATA.PlayerAction;

/**
 * Created by Andreas on 2016-12-28.
 */

public class Client{

    private ClientUDPListener clientUDPListener;

    private GameActivity activity;
    private InetAddress serveraddress;
    private int udp_send_port;
    private int udp_listen_port;

    private DatagramSocket datagramSendSocket;
    private ByteArrayOutputStream byteArrayOutputStream;
    private int counterAlive;

    public Client(GameActivity activity,String serveraddress, int udp_send_port,int udp_listen_port) throws SocketException, UnknownHostException {
        this.activity = activity;
        counterAlive = 0;
        this.serveraddress = InetAddress.getByName(serveraddress);
        this.udp_send_port = udp_send_port;
        this.udp_listen_port = udp_listen_port;
        clientUDPListener = new ClientUDPListener(activity,udp_listen_port);
        clientUDPListener.start();
        datagramSendSocket = new DatagramSocket();
        byteArrayOutputStream = new ByteArrayOutputStream(112);
    }

    /**
     * Send a message to the server over UDP
     */
    public void sendPlayerActionOverUDP(PlayerAction obj){
        try{
            counterAlive++;
            byteArrayOutputStream.reset();
            datagramSendSocket.send(new DatagramPacket(obj.getByte(), 1, serveraddress,udp_send_port));
        }catch(Exception e){
            Log.i("Client","sendPlayerActionOverUDP ERROR");
        }
    }

    public boolean isServerAlive(){
        return counterAlive > 100;
    }

    public void stopAll(){
        clientUDPListener.interrupt();
        if(datagramSendSocket != null)
            datagramSendSocket.close();
    }

    private class ClientUDPListener extends Thread {
        private GameActivity activity;
        private int udp_listen_port;

        public ClientUDPListener(GameActivity activity, int udp_listen_port){
            this.activity = activity;
            this.udp_listen_port = udp_listen_port;
            check = true;
        }

        private DatagramSocket datagramListenSocket;
        private boolean check;
        @Override
        public void interrupt(){
            super.interrupt();
            check = false;
            if(datagramListenSocket != null)
                datagramListenSocket.close();
        }

        public void run() {
            try {
                datagramListenSocket = new DatagramSocket(udp_listen_port);
                byte[] receiveBuffer = new byte[200];
                DatagramPacket datagramPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                while(check){
                    try{
                        datagramListenSocket.receive(datagramPacket);
                        counterAlive = 0;
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(receiveBuffer);
                        ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(byteArrayInputStream));
                        Object obj = objectInputStream.readObject();
                        activity.updateClientModel(obj);
                    }catch(Exception ee){
                        Log.i("Client","error when receiving udp packet");
                    }
                }
            } catch (Exception e) {
                Log.i("Client", "Error when receiving object");
                e.printStackTrace();
            }
        }//public void run
    }//private class

}//public class
