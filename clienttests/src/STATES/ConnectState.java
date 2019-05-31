package STATES;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import MAIN.Client;
import MAIN.Client.ErrorType;

public class ConnectState implements CState {
	
	private static final String ACK0 = "Welcome!";
	private static final String ACK1 = "r u sure?";
	private static final String ACK2 = "OK start UDP";
	private static final String RESPONSE1 = "connect this\n";
	private static final String RESPONSE2 = "yep, transform\n";
	
	private String serverAddress;
	private Socket clientSocket;
	private DataOutputStream outToServer;
	private BufferedReader inFromServer;
	private int udpPort;
	

	
	public ConnectState(){
	}

	@Override
	public void initStep(Client client) {

	}

	@Override
	public CState processStep(Client client) {
		boolean stepOk;
		
		stepOk = init();
		if(!stepOk){
			client.setErrorType(ErrorType.INIT_TCP);
			return null;
		}
		
		stepOk = handShake();
		if(!stepOk){
			client.setErrorType(ErrorType.HANDSHAKE_TCP);
			return null;
		}
		
		stepOk = getUdpPort();
		if(!stepOk){
			client.setErrorType(ErrorType.GETUDPPORT_TCP);
			return null;
		}
		

		return new GameState();
	}

	@Override
	public void endStep(Client client) {
		client.setServerAddress(serverAddress);
		client.setUdpPort(udpPort);
		endConnection();
	}
	
	public void endConnection(){
		try {
			if(clientSocket != null)
				clientSocket.close();
		} catch (IOException e) {
		}
	}
	
	public boolean init(){
		try {
			serverAddress = InetAddress.getByName("www.fullrune.com").getHostAddress();
			clientSocket = new Socket(serverAddress, 12225);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			return true;
		} catch (IOException e) {
		}
		return false;
	}
	
	public boolean handShake(){
		String serverMsg;
		
		try {
			serverMsg = inFromServer.readLine();
			System.out.println("FROM SERVER: " + serverMsg + " " + serverMsg.equals(ACK0));
			outToServer.writeBytes(RESPONSE1);
			outToServer.flush();
			serverMsg = inFromServer.readLine();
			System.out.println("FROM SERVER: " + serverMsg + " " + serverMsg.equals(ACK1));
			outToServer.writeBytes(RESPONSE2);
			outToServer.flush();
			serverMsg = inFromServer.readLine();
			System.out.println("FROM SERVER: " + serverMsg + " " + serverMsg.equals(ACK2));
			return true;
		} catch (IOException e) {

		}
		return false;
	}
	
	public boolean getUdpPort(){
		String serverMsg;
		
		try {
			serverMsg = inFromServer.readLine();
			System.out.println("FROM SERVER: " + serverMsg);
			String portStr = serverMsg.replaceAll("\\D+", "");
			udpPort = Integer.parseInt(portStr);
			System.out.println("test: " + udpPort);
			return true;
		} catch (IOException e) {
		}

		
		return false;
	}

}
