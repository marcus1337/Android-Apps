package test.gameapp.CONTROLLER.SERVER;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import test.gameapp.MODEL.CLIENT.ClientModelGameInfo;
import test.gameapp.MODEL.DATA.PlayerAction;
import test.gameapp.MODEL.SERVER.ITEM.Cannonball;
import test.gameapp.MODEL.SERVER.ITEM.Tower;
import test.gameapp.MODEL.SERVER.ServerModel;


public class Server {

    private ServerModel servermodel;
    private ServerUDPListener udpserver;

    private InetAddress clientaddress;
    private InetAddress serveraddress;

    private int udp_send_port;
    private ArrayList<TowerAIThread> towerAIThreads;

    public Server(String clientaddress, int udp_listen_port, int udp_send_port, ServerModel servermodel) throws UnknownHostException, SocketException {
        this.servermodel = servermodel;
        this.clientaddress = InetAddress.getByName(clientaddress);
        this.serveraddress = InetAddress.getByName("127.0.0.1");
        clientTimeout = false;
        udpserver = new ServerUDPListener(this, udp_listen_port, servermodel, this.clientaddress);
        udpserver.start();
        this.udp_send_port = udp_send_port;
        datagramSendSocket = new DatagramSocket();
        byteArrayOutputStream = new ByteArrayOutputStream(200);

        towerAIThreads = new ArrayList<TowerAIThread>();
        ArrayList<Tower> towers = servermodel.getTowers();
        ArrayList<Cannonball> cannonballs = servermodel.getCannonballs();
        for (int i = 0; i < towers.size(); i++) {
            if (i < cannonballs.size())
                towerAIThreads.add(new TowerAIThread(this, servermodel, towers.get(i), cannonballs.get(i), servermodel.getPlayer(1)));
        }
        for (int i = 0; i < towerAIThreads.size(); i++)
            towerAIThreads.get(i).start();
    }

    private DatagramSocket datagramSendSocket;
    private ByteArrayOutputStream byteArrayOutputStream;
    private boolean clientTimeout;

    public boolean isClientTimeout(){
        return clientTimeout;
    }

    public void stopAll() {
        if (datagramSendSocket != null)
            datagramSendSocket.close();
        udpserver.interrupt();
        for (int i = 0; i < towerAIThreads.size(); i++) {
            towerAIThreads.get(i).interrupt();
        }
    }

    public void sendObjectOverUDPToClients(Object obj) {
        try {
            byteArrayOutputStream.reset();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(byteArrayOutputStream));
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();

            byte[] sendBuffer = byteArrayOutputStream.toByteArray();
            datagramSendSocket.send(new DatagramPacket(sendBuffer, sendBuffer.length, clientaddress, udp_send_port));
            datagramSendSocket.send(new DatagramPacket(sendBuffer, sendBuffer.length, serveraddress, udp_send_port));
            objectOutputStream.close();
        } catch (Exception e) {
            Log.i("Server", "sendObjectOverUDP ERROR");
            e.printStackTrace();
        }

        if (obj instanceof ClientModelGameInfo)
            if (((ClientModelGameInfo) obj).getGameover())
                new EndGameThread(5000).start();
    }

    /*
     * Listens for incomming UDP Packets on a port.
     * The UDP packets sent from a client contains pressed buttons.
     * If a UDP packet is received the button pressed will be forwarded
     * to the servermodel. Both clients will then be notified of the changes
     * that has been made in the model.
     */
    private class ServerUDPListener extends Thread {

        private ServerModel servermodel;
        private Server server;
        private InetAddress clientaddress;
        private int port;
        private PlayerAction tmpData;
        private boolean check;
        private DatagramSocket datagramServerSocket;
        private Timer timer;
        private boolean receivedAPacket;


        public ServerUDPListener(Server server, int port, ServerModel servermodel, InetAddress clientaddress) {
            this.server = server;
            this.port = port;
            this.servermodel = servermodel;
            this.clientaddress = clientaddress;
            receivedAPacket = false;
            tmpData = new PlayerAction();
            check = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (receivedAPacket) {
                        receivedAPacket = false;
                        return;
                    }
                    clientTimeout = true;

                }
            }, 10000, 7000);
        }

        @Override
        public void interrupt() {
            super.interrupt();
            check = false;
            if (datagramServerSocket != null)
                datagramServerSocket.close();
        }

        public void run() {
            try {
                datagramServerSocket = new DatagramSocket(port);
                byte[] receiveBuffer = new byte[1];
                DatagramPacket datagramPacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                while (check) {
                    try {
                        datagramServerSocket.receive(datagramPacket);
                        tmpData.setValues(datagramPacket.getData());
                        int playernr = 0;
                        if (datagramPacket.getAddress().equals(clientaddress)) {
                            //Klienten finns kvar
                            receivedAPacket = true;
                            playernr = 1;
                        }
                        ArrayList<Object> objects = servermodel.playerAction(playernr, tmpData);
                        for (int i = 0; i < objects.size(); i++)
                            server.sendObjectOverUDPToClients(objects.get(i));
                    } catch (Exception ee) {
                        Log.i("Server", "error when receiving udp packet");
                    }
                }
            } catch (Exception e) {
                Log.i("ServerUDPListener", "crashed");
            }
        }
    }//private class

    private class EndGameThread extends Thread {
        private int sleep;

        public EndGameThread(int sleep) {
            this.sleep = sleep;
        }

        public void run() {
            try {
                Thread.sleep(sleep);
            } catch (Exception e) {

            }
            sendObjectOverUDPToClients(new ClientModelGameInfo(true, false, -1, 0, -1));
        }
    }
}//public class
