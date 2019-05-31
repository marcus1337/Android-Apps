package STATES;

import java.io.Serializable;
import java.util.concurrent.Semaphore;

public class Client implements Serializable {


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
    private int connectProgress;
    private Semaphore progressSem;
    private int playerNumber;

    public void setPlayerNumber(int playerNumber){
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }

    public int getConnectProgress(){
        try {
            progressSem.acquire();
            return connectProgress;
        } catch (InterruptedException e) {
        }
        finally{
            progressSem.release();
        }
        return 0;
    }

    public void setConnectProgress(int connectProgress){
        try {
            progressSem.acquire();
            this.connectProgress = connectProgress;
        } catch (InterruptedException e) {
        }
        finally{
            progressSem.release();
        }
    }


    public enum ErrorType{
        NONE, INIT_TCP, HANDSHAKE_TCP, GETUDPPORT_TCP, GAME_INIT, GAME_END;
    }

    public Client(){
        playerNumber = 0;
        progressSem = new Semaphore(1, true);
        connectProgress = 0;
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