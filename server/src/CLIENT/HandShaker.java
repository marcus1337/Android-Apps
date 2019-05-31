package CLIENT;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Stack;
import java.util.concurrent.Semaphore;

public class HandShaker {

	private static final String RESPONSE0 = "Welcome!\n";
	private static final String RESPONSE1 = "r u sure?\n";
	private static final String RESPONSE2 = "OK start UDP\n";
	private static final String ACK1 = "connect this";
	private static final String ACK2 = "yep, transform";

	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private Stack<Integer> freePorts;
	private Semaphore semaphore;

	public HandShaker() {
		semaphore = new Semaphore(1, true);
		freePorts = new Stack<Integer>();
		for (int i = 0; i < 200; i++) {
			freePorts.push(i);
		}
	}
	
	public int getPort(){
		try {
			semaphore.acquire(1);
			if(freePorts.isEmpty())
				return -1;
			return freePorts.pop();
		} catch (InterruptedException e) {
			return -1;
		}
		finally{
			semaphore.release();
		}
	}
	
	public void setPort(int port){
		try {
			semaphore.acquire(1);
			freePorts.push(port);
			
		} catch (InterruptedException e) {
		}
		finally{
			semaphore.release();
		}
	}

	private boolean handleStrMsg(Socket socket) throws IOException {
		outToClient.writeBytes(RESPONSE0);
		outToClient.flush();
		String msg = inFromClient.readLine();
		if (msg.equals(ACK1)) {
			outToClient.writeBytes(RESPONSE1);
			outToClient.flush();
			msg = inFromClient.readLine();
			if (msg.equals(ACK2)) {
				outToClient.writeBytes(RESPONSE2);
				outToClient.flush();
				return true;
			}
		}
		return false;
	}

	public boolean shakeHand(Socket socket) {
        try {
        	socket.setSoTimeout(3000);
    		inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    	    outToClient = new DataOutputStream(socket.getOutputStream());
        	return handleStrMsg(socket);
		} catch (IOException e) {
			System.out.println("excpetion: " + e.getMessage());
			return false;
		}
	}

}
