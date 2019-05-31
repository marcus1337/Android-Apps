package STATES;

import MAIN.Client;

public interface CState {
	
	public void initStep(Client client);
	public CState processStep(Client client);
	public void endStep(Client client);

}
