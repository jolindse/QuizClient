package logic;

import bean.Message;
import gui.ClientWindow;
import network.ServerConnection;

public class ClientController {

	private ClientWindow view;
	private ServerConnection server;

	// SERVER DEFAULT VALUES
	private String serverAdress = "127.0.0.1";
	private int serverPort = 55500;

	private boolean serverConnected;

	public ClientController() {
		serverConnected = false;
	}

	public void registerView(ClientWindow view) {
		this.view = view;
	}

	// METHODS TO MANIPULATE VIEW

	public void outputText(String inLine) {
		Message currMessage = new Message(inLine);
		view.addOutput(currMessage);
	}

	public void outputText(String cmd, String cmdData, String optionalData) {
		Message currMessage = new Message(cmd, cmdData, optionalData);
		view.addOutput(currMessage);
	}

	// METHODS TO HANDLE CONNECTION TO SERVER

	public void setServerConnected(){
		serverConnected = true;
	}
	
	public void connect(String name) {
		if (!serverConnected) {
			server = new ServerConnection(this, name, serverAdress, serverPort);
			Thread serverConnection = new Thread(server);
			serverConnection.start();
		}
	}

	public synchronized void send(String cmd, String cmdData, String optionalData) {
		if (serverConnected) {
			Message currMessage = new Message(cmd, cmdData, optionalData);
			server.send(currMessage);
		}
	}

	public void exitAll() {
		if (serverConnected){
		send("DISCONNECT",server.getName(),"");
		}
		System.exit(0);
	}
}
