package STATES;

import android.util.Log;

import com.fullrune.snakebattle.HelpFunc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import STATES.Client.ErrorType;

public class GameState implements CState {
	
	private byte[] sendData, receiveData;
	private DatagramSocket socket;
	private DatagramPacket sndPkt, recPkt;
	private SendThread sendthread;
	private boolean gameStarted;
	public int winnerCode;

	public boolean isGameStarted(){
		return gameStarted;
	}

	public int millisBeforeStart;
	public Player player1, player2;

	private Semaphore dirSema, playerSema;
	private int myDir;

	public Player getPlayer(int playnum){
		Player player = null;
		try {
			playerSema.acquire();
			if(playnum == 0){
				player = player1;
			}
			if(playnum == 1){
				player = player2;
			}
		} catch (InterruptedException e) {
		}finally {
			playerSema.release();
		}

		return player;
	}

	public int getDir(){
		try {
			dirSema.acquire();
			return myDir;
		} catch (InterruptedException e) {
		}finally{
			dirSema.release();
		}
		return 1;
	}

	public void setDir(int _dir){
		try {
			dirSema.acquire();
			myDir = _dir;
		} catch (InterruptedException e) {
		}finally{
			dirSema.release();
		}
	}

	public GameState(){
		recSomething = new AtomicBoolean(false);
		myDir = 1;
		dirSema = new Semaphore(1, true);
		playerSema = new Semaphore(1, true);
		winnerCode = -1;
		player1 = new Player(0,0);
		player2 = new Player(0,0);
		millisBeforeStart = 0;
		gameStarted = false;
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
					//System.out.println("TEST: "  + getDir());
					HelpFunc.setByteData(sendData, 0, 1, (byte) 1);
					HelpFunc.setByteData(sendData, 2, 7, (byte) getDir());
					socket.send(sndPkt);
					Thread.sleep(250);
				
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
			HelpFunc.setByteData(sendData, 0, 1, (byte) 1);
			sndPkt = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(client.getServerAddress()), client.getUdpPort());
			recPkt = new DatagramPacket(receiveData, receiveData.length);
			new Thread(sendthread).start();
		} catch (SocketException | UnknownHostException e) {
			client.setErrorType(ErrorType.GAME_INIT);
		}
	}

	private long independentTimer;
	private AtomicBoolean recSomething;

	public boolean isTimeout(){
		if(independentTimer == 0){
			independentTimer = System.currentTimeMillis();
		}
		if(System.currentTimeMillis() - independentTimer > 1000){
			if(recSomething.get()){
				recSomething.set(false);
				independentTimer = System.currentTimeMillis();
			}else{
				return true;
			}
		}
		return false;
	}

	@Override
	public CState processStep(Client client) {
       // System.out.println();
		try {
			socket.setSoTimeout(2000);
			socket.receive(recPkt);
			recSomething.set(true);
		} catch (IOException e) {
		//	Log.i("TEST_TAG2", "socket fail?");
			//System.out.println("NOPE: " + e.getMessage());
			return null;
		}

		//Log.i("TEST_TAG", "after socket?");
		byte[] tmpData = recPkt.getData();
		int msgType = HelpFunc.getByteData(tmpData, 0, 1);
		if(msgType == 2){
			gameStarted = true;
			int x1 = HelpFunc.getByteData(tmpData, 2, 8);
			int y1 = HelpFunc.getByteData(tmpData, 9, 15);
			int x2 = HelpFunc.getByteData(tmpData, 16, 22);
			int y2 = HelpFunc.getByteData(tmpData, 23, 29);
			ArrayList<Player.Move> recent1 = new ArrayList<Player.Move>();
			ArrayList<Player.Move> recent2 = new ArrayList<Player.Move>();
			for(int i = 0; i < 3; i++){
				int r = HelpFunc.getByteData(tmpData, 30+i*3, 32+i*3);
				if(r != 0){
					recent1.add(HelpFunc.intToMove(r));
				}
			}
			for(int i = 0; i < 3; i++){
				int r = HelpFunc.getByteData(tmpData, 39+i*3, 41+i*3);
				if(r != 0){
					recent2.add(HelpFunc.intToMove(r));
				}
			}
			try {
				playerSema.acquire();
				player1.recentMoves = recent1;
				player1.x = x1;
				player1.y = y1;
				player2.recentMoves = recent2;
				player2.x = x2;
				player2.y = y2;

			} catch (InterruptedException e) {
			}finally {
				playerSema.release();
			}

		}else if(msgType == 1){
			int playernum = HelpFunc.getByteData(tmpData, 46, 46);
			client.setPlayerNumber(playernum); //* 0 = player1, 1 = player2**//
			millisBeforeStart = HelpFunc.getByteData(tmpData, 30, 37);

		//	Log.i("TEST_RAND", "num: " + playernum + "  milli: " + millisBeforeStart);
		}else if((msgType == 3 && !gameStarted) || (msgType == 3 && HelpFunc.getByteData(tmpData, 10, 15) == 5)){
			client.setErrorType(ErrorType.GAME_INIT);
			return null;
		}else if(msgType == 3){
			winnerCode = HelpFunc.getByteData(tmpData, 40, 47);
			client.setErrorType(ErrorType.GAME_END);
			return null;
		}

		//System.out.println("TYPE: " + msgType);
		
		
		return null;
	}

	@Override
	public void endStep(Client client) {

		if(sendthread != null)
			sendthread.stop.set(true);
	}

	
	
}
