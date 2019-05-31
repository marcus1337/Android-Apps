package MAIN;

import STATES.CState;
import STATES.ConnectState;

public class Client {
	
	
	public void setUdpPort(int udpPort){
		this.udpPort = udpPort;
	}
	public int getUdpPort(){
		return udpPort;
	}
	
	public void setErrorType(ErrorType errorType){
		this.errorType = errorType;
	}
	
	public ErrorType getError(){
		return errorType;
	}
	
	public String getServerAddress() {
		return serverAddress;
	}
	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
	
	private String serverAddress;
	private int udpPort;
	private ErrorType errorType;
	
	public enum ErrorType{
		NONE, INIT_TCP, HANDSHAKE_TCP, GETUDPPORT_TCP, GAME_INIT;
	}
	
	public Client(){
		udpPort = -1;
		errorType = ErrorType.NONE;
	}
	
	public void start(){
		
		CState state = new ConnectState();
		state.initStep(this);
		
		while(errorType == ErrorType.NONE){
			CState tmpState = null;
			tmpState = state.processStep(this);
			if(tmpState != null){
				state.endStep(this);
				state = tmpState;
				state.initStep(this);
			}
			
		}
		
		
	}

}
