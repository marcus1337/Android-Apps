package MAIN;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import CLIENT.GameRoom;
import CLIENT.HandShaker;

public class Server {

	private Semaphore semaphore;

	private ServerSocket server;
	private ArrayList<Socket> clients;
	private Thread acceptClientThread;
	private HandShaker handshaker;
	private List<ReadyItem> readyList;
	
	// assume byte array is "cleared" >>> right << left
	public static void setByteData(byte[] data, int begin, int end, byte value) {
		for (int i = begin; i <= end; i++) {
			int nowByte = i / 8;
			int nowBit = i - nowByte * 8;

			if (Server.getBit((end - i), value) == 1)
				data[nowByte] = (byte) (data[nowByte] | (1 << nowBit));
			else
				data[nowByte] = (byte) (data[nowByte] & ~(1 << nowBit));

		}

	}

	public static int getByteData(byte[] data, int begin, int end) {
		int result = 0;
		for (int i = begin; i <= end; i++) {
			int nowByte = i / 8;
			int nowBit = i - nowByte * 8;
			int powBy = (end - i);

			if (Server.getBit(nowBit, data[nowByte]) != 0)
				result += Math.pow(2, powBy);
		}
		return result;
	}
	
	public static int getBit(int position, byte ID)
	{
	   return (byte) ((ID >> position) & 1);
	}

	public Server() {
		runningGames = 0;
		deleteStack = new Stack<Integer>();
		deleteStack2 = new Stack<Integer>();
		readyList = new ArrayList<ReadyItem>();
		semaphore = new Semaphore(1, true);
		clients = new ArrayList<Socket>();
		handshaker = new HandShaker();
		acceptClientThread = new Thread() {
			public void run() {
				try {
					while (true) {
						welcomeClients();
						Thread.sleep(100);
					}
				} catch (InterruptedException v) {
					return;
				}
			}
		};

	}

	public void start() {
		try {
			server = new ServerSocket(12225);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		acceptClientThread.start();

		while (true) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
			}

			try {
				semaphore.acquire(1);
				processClients();
			} catch (InterruptedException e) {
			} finally {
				semaphore.release();
			}
		}
	}

	public void welcomeClients() {
		try {
			Socket client = server.accept();

			semaphore.acquire(1);
			if (clients.size() <= 100) {
				clients.add(client);
			}
		} catch (IOException | InterruptedException e) {
			System.out.println("TEST: " + e.getMessage());
		} finally {
			semaphore.release();
		}
	}

	private Stack<Integer> deleteStack;
	private Stack<Integer> deleteStack2;
	private int runningGames;
	
	private class ReadyItem{
		public Socket client;
		public long millis;
		public ReadyItem(Socket sock){
			this.client = sock;
			millis = System.currentTimeMillis();
		}
		public boolean isExpired(){
			return (System.currentTimeMillis() - millis) > 20000;
		}
		
	}

	public void processClients() {
		
		for (int i = 0; i < clients.size(); i++) {
			Socket client = clients.get(i);
			if (runningGames < 100 && handshaker.shakeHand(client)) {
				readyList.add(new ReadyItem(client));
				deleteStack2.push(i);
			} else {
				deleteStack.push(i);
			}
		}

		while (!deleteStack2.isEmpty()) {
			int i = deleteStack2.pop();
			clients.remove(i);
		}
		while (!deleteStack.isEmpty()) {
			int i = deleteStack.pop();
			Socket tmp = clients.get(i);
			if (tmp != null) {
				try {
					tmp.close();
				} catch (IOException e) {
				}
			}
			clients.remove(i);
		}

		while (readyList.size() >= 2) {
			ReadyItem p1 = readyList.get(0);
			ReadyItem p2 = readyList.get(1);
			
			if(p1.isExpired()){
				readyList.remove(0);
				continue;
			}
			
			if(p2.isExpired()){
				readyList.remove(1);
				continue;
			}
			
			int port1 = handshaker.getPort();
			if (port1 == -1)
				return;
			int port2 = handshaker.getPort();
			if (port2 == -1){
				handshaker.setPort(port1);
				return;
			}
			
			readyList.remove(0);
			readyList.remove(0);
			runningGames++;
			
			new Thread(new GameThread(port1 + 9000, port2 + 9000, p1.client, p2.client)).start();

		}

	}

	private class GameThread implements Runnable {

		private AtomicBoolean sendNewData;

		private class UDPHandlerThread implements Runnable {

			private byte[] tmpData1, tmpData2;
			private DatagramPacket tmpPacket1, tmpPacket2;

			public UDPHandlerThread() {
				tmpData1 = new byte[1];
				tmpData2 = new byte[1];
				tmpPacket1 = new DatagramPacket(tmpData1, tmpData1.length);
				tmpPacket2 = new DatagramPacket(tmpData2, tmpData2.length);
			}

			@Override
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					try {
						if (sendNewData.get()) {
							gsocket1.send(sndPkt1);
							gsocket2.send(sndPkt2);
							sendNewData.set(false);
						}
					} catch (IOException e) {}
						
						try {
							gsocket1.setSoTimeout(50);
							gsocket1.receive(tmpPacket1);
							extPort1 = tmpPacket1.getPort();
							//System.out.println("REC_1: " + tmpPacket1.getPort() + " " + tmpPacket1.getAddress());
							try {
								udpSem.acquire();
								inTimer1 = System.currentTimeMillis();
								inPack1 = tmpData1;
								//System.out.println("1: " + inPack1[0] + " _ " + extPort1);
							} catch (InterruptedException e) {
							} finally {
								udpSem.release();
							}
						} catch (SocketException e1) {
						} catch (IOException e) {
						}
						
						try {
							gsocket2.setSoTimeout(50);
							gsocket2.receive(tmpPacket2);
							extPort2 = tmpPacket2.getPort();
							//System.out.println("REC_2: " + tmpPacket2.getPort() + " " + tmpPacket2.getAddress());
							try {
								udpSem.acquire();
								inTimer2 = System.currentTimeMillis();
								inPack2 = tmpData2;
							//	System.out.println("2: " + inPack2[0] + " _ " + extPort2);
							} catch (InterruptedException e) {
							} finally {
								udpSem.release();
							}
						} catch (SocketException e1) {
						} catch (IOException e) {
						}
										
				}

			}

		}

		private GameRoom room;
		private int port1, port2;
		private int extPort1, extPort2;
		private Socket p1TCP, p2TCP;
		private InetAddress p1Address, p2Address;
		private DatagramSocket gsocket1, gsocket2;

		private byte[] inPack1, inPack2, outPack;

		private Semaphore udpSem;
		private DatagramPacket sndPkt1, sndPkt2;
		private UDPHandlerThread udphandlerthread;
		private Thread UDPthread;
		
		private long inTimer1;
		private long inTimer2;
		private long independentTimer;

		public GameThread(int port1, int port2, Socket p1TCP, Socket p2TCP) {
			extPort1 = extPort2 = 25000;
			udphandlerthread = new UDPHandlerThread();
			UDPthread = new Thread(udphandlerthread);
			sendNewData = new AtomicBoolean(false);
			udpSem = new Semaphore(1, true);
			this.port1 = port1;
			this.port2 = port2;
			this.p1TCP = p1TCP;
			this.p2TCP = p2TCP;
			p1Address = p1TCP.getInetAddress();
			p2Address = p2TCP.getInetAddress();
			inPack1 = new byte[1];
			inPack2 = new byte[1];
			outPack = new byte[6];
			sndPkt1 = new DatagramPacket(outPack, outPack.length, p1Address, port1);
			sndPkt2 = new DatagramPacket(outPack, outPack.length, p2Address, port2);

			room = new GameRoom();
			try {
				gsocket1 = new DatagramSocket(port1);
				gsocket2 = new DatagramSocket(port2);
			} catch (SocketException e) {
				System.out.println("critical udp fail!");
			}

		}

		private boolean sendInitInfo() {

			try {
				DataOutputStream outToClient1 = new DataOutputStream(p1TCP.getOutputStream());
				DataOutputStream outToClient2 = new DataOutputStream(p2TCP.getOutputStream());
				outToClient1.writeBytes("UDP PORT:" + Integer.toString(port1));
				outToClient1.flush();
				outToClient2.writeBytes("UDP PORT:" + Integer.toString(port2));
				outToClient2.flush();
			} catch (IOException e) {
				System.out.println("FEL: " + e.getMessage());
				return false;
			} finally {
				try {
					p1TCP.close();
					p2TCP.close();
				} catch (IOException e) {
					System.out.println("FEL2: " + e.getMessage());
					return false;
				}
			}

			p1TCP = p2TCP = null;
			return true;
		}
		
		private boolean isConnectionLost(){
			
			if(independentTimer == 0){
				independentTimer = System.currentTimeMillis();
			}
			
			if(System.currentTimeMillis() - independentTimer < 1000){
				return false;
			}
			
			long diff = System.currentTimeMillis() - inTimer1;
			if(diff >= 1000){
				return true;
			}
			diff = System.currentTimeMillis() - inTimer2;
			if(diff >= 1000){
				return true;
			}
			return false;
		}

		@Override
		public void run() {

			boolean timeoutError = false;
			boolean result = sendInitInfo();
			//System.out.println("LEL: " + result);
			 if (result) {
			//if (true) {
				UDPthread.start();
				while (!room.isDone()) {
					
					try {
						udpSem.acquire();

						if(isConnectionLost())
						{
							timeoutError = true;
							break;
						}
						
						if(inPack1[0] != 0 || inPack2[0] != 0){
							room.updatePlayerCommands(inPack1, inPack2);

						}
						inPack1[0] = inPack2[0] = 0;
					} catch (InterruptedException e) {
					}
					finally{
						udpSem.release();
					}

					room.process();

					if (!sendNewData.get()) {
						outPack = room.getGameData();
						if(!room.isGameStarted()){
							Server.setByteData(outPack, 46, 46, (byte) 0);
							sndPkt1 = new DatagramPacket(outPack, outPack.length, p1Address, extPort1);
							byte[] tmpPack = new byte[outPack.length];
							for(int i = 0; i < outPack.length; i++)
								tmpPack[i] = outPack[i];
							Server.setByteData(tmpPack, 46, 46, (byte) 1);
							sndPkt2 = new DatagramPacket(tmpPack, tmpPack.length, p2Address, extPort2);
						}else{
							sndPkt1 = new DatagramPacket(outPack, outPack.length, p1Address, extPort1);
							sndPkt2 = new DatagramPacket(outPack, outPack.length, p2Address, extPort2);
						}
						//System.out.println("TEST: " + Server.getByteData(outPack, 0, 1));
						sendNewData.set(true);
					}
					
					try {
						Thread.sleep(room.sleepDelay);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			}
			 
			////SEND GOODBYE MESSAGE
			try {
				Thread.sleep(30);
				sendNewData.set(false);
				Server.setByteData(outPack, 0, 1, (byte) 3);
				if(timeoutError){
					Server.setByteData(outPack, 10, 15, (byte) 5);
				}
				
				for(int i = 0; i < 30; i++){
					gsocket1.send(sndPkt1);
					gsocket2.send(sndPkt2);
					Thread.sleep(10);
				}
			} catch (InterruptedException | IOException e1) {}
			///////

			try {
				handshaker.setPort(port1-9000);
				handshaker.setPort(port2-9000);
				semaphore.acquire(1);
				runningGames--;
				UDPthread.interrupt();
			} catch (InterruptedException e) {
			} finally {
				semaphore.release();
				if (gsocket1 != null) {
					gsocket1.close();
				}
				if (gsocket2 != null) {
					gsocket2.close();
				}
				UDPthread = null;
			}

		}

	}

}
