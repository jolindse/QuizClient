package logic;

import bean.Message;
import gui.ClientWindow;
import network.ServerConnection;

/**
 * Client logic class.
 * 
 * Handles the flow of the program and communication between network and view.
 * 
 * @author Johan Lindström (jolindse@hotmail.com)
 *
 */
public class ClientController {

	private ClientWindow view;
	private ServerConnection server;

	// SERVER DEFAULT VALUES
	private String serverAdress = "127.0.0.1";
	private int serverPort = 55500;

	private boolean serverConnected;

	/**
	 * Constructor. Simply sets the controller status to not connected.
	 */
	public ClientController() {
		serverConnected = false;
	}

	/**
	 * Used to register the view reference from entry class.
	 * 
	 * @param view
	 */
	public void registerView(ClientWindow view) {
		this.view = view;
	}

	// METHODS TO MANIPULATE VIEW

	/*
	 * Outputs text to view but doesn't send it to server.
	 */
	public void outputText(String inLine) {
		Message currMessage = new Message(inLine);
		view.addOutput(currMessage);
	}

	/**
	 * Method to output information text only to view.
	 * 
	 * @param infoText
	 */
	public void outputInfoText(String infoText) {
		view.addOutput(new Message("INFORMATION", "", infoText));
	}

	/**
	 * Outputs message to both view and send it to server.
	 * 
	 * @param cmd
	 * @param cmdData
	 * @param optionalData
	 */
	public void outputText(String cmd, String cmdData, String optionalData) {
		Message currMessage = new Message(cmd, cmdData, optionalData);
		view.addOutput(currMessage);
	}

	// METHODS TO HANDLE CONNECTION TO SERVER

	/**
	 * Registers that the client is connected to server.
	 */
	public void setServerConnected() {
		serverConnected = true;
		view.setConnected(serverAdress, Integer.toString(serverPort));
	}

	/**
	 * Connects to the server and sets the client name.
	 * 
	 * @param name
	 */
	public void connect(String name) {
		if (!serverConnected) {
			server = new ServerConnection(this, name, serverAdress, serverPort);
			Thread serverConnection = new Thread(server);
			serverConnection.start();
		}
	}

	public void connect(String name, String serverAdress) {
		if (!serverConnected) {
			this.serverAdress = serverAdress;
			server = new ServerConnection(this, name, serverAdress, serverPort);
			Thread serverConnection = new Thread(server);
			serverConnection.start();
		}
	}

	/**
	 * Sends a constructed message to server
	 * 
	 * @param cmd
	 * @param cmdData
	 * @param optionalData
	 */
	public synchronized void send(String cmd, String cmdData, String optionalData) {
		if (serverConnected) {
			server.send(new Message(cmd, cmdData, optionalData));
		}
	}

	/**
	 * Exits gracefully and registers the disconnection with the server.
	 */
	public void exitAll() {
		if (serverConnected) {
			send("DISCONNECT", server.getName(), "");
		}
		System.exit(0);
	}
}
