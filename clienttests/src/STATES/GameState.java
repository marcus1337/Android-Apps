package STATES;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import MAIN.Client;
import MAIN.Client.ErrorType;
import MAIN.Main;

public class GameState implements CState {
	
	private byte[] sendData, receiveData;
	private DatagramSocket socket;
	private DatagramPacket sndPkt, recPkt;
	private SendThread sendthread;
	public GameState(){
		sendData = new byte[1];
		receiveData = new byte[6];
		sendthread = new SendThread();
	}
	
	private class SendThread implements Runnable{
		public AtomicBoolean stop;
		public SendThread(){
			stop = new AtomicBoolean(false);
		}
		
		@Override
		public void run() {
			while(!stop.get()){
				
				try {
					socket.send(sndPkt);
					Thread.sleep(500);
				
				} catch (IOException | InterruptedException e) {
					System.out.println(e.getMessage());
				}
			}
			
		}
		
	}

	@Override
	public void initStep(Client client) {
		try {
			socket = new DatagramSocket(client.getUdpPort());
			Main.setByteData(sendData, 0, 1, (byte) 3);
			sndPkt = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(client.getServerAddress()), client.getUdpPort());
		
			recPkt = new DatagramPacket(receiveData, receiveData.length);
			new Thread(sendthread).start();
		} catch (SocketException | UnknownHostException e) {
			client.setErrorType(ErrorType.GAME_INIT);
		} catch (IOException e) {
			client.setErrorType(ErrorType.GAME_INIT);
		}
	}

	@Override
	public CState processStep(Client client) {

		try {
			socket.setSoTimeout(2000);
			socket.receive(recPkt);
		} catch (IOException e) {
			System.out.println("NOPE: " + e.getMessage());
			return null;
		}
		
		byte[] test = recPkt.getData();
		int msgType = Main.getByteData(test, 0, 1);
		System.out.println("TYPE: " + msgType);
		
		
		return null;
	}

	@Override
	public void endStep(Client client) {
		// TODO Auto-generated method stub
		
	}

	
	
}
