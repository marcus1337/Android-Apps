package STATES;

public interface CState {

    public void initStep(Client client);
    public CState processStep(Client client);
    public void endStep(Client client);

}