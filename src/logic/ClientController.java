package logic;

import java.net.Socket;

import gui.ClientWindow;
import network.ServerConnection;
import network.ServerRead;
import network.ServerWrite;

public class ClientController {

	private ClientWindow view;
	private Socket server;
	private ServerWrite sw;
	
	
	private boolean serverConnected;
	
	public ClientController(){
		serverConnected = false;
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
	
	public void setServerSocket(Socket server){
		
		this.server = server;
		serverConnected = true;
		
		// Initializing read/write threads
		
		sw = new ServerWrite(server, this);
		Thread sendTh = new Thread(sw);
		Thread readTh = new Thread(new ServerRead(server, this));
		
		sendTh.start();
		readTh.start();
		
	}
	
	public void send(String text){
		if (serverConnected){
			sw.send(text);
		}
	}
}
