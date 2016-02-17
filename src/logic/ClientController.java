package logic;

import gui.ClientWindow;
import network.ServerConnection;

public class ClientController {

	private ClientWindow view;
	
	public ClientController(){
		
	}
	
	public void registerView(ClientWindow view){
		this.view = view;
	}
	
	// METHODS TO MANIPULATE VIEW
	
	public void outputText(String text){
		view.addOutput(text);
	}
	
	
	// METHODS TO HANDLE CONNECTION TO SERVER
	
	public void connect(){
		ServerConnection sc = new ServerConnection(this);
		Thread connection = new Thread(sc);
		connection.start();
	}
}
