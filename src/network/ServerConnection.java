package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import bean.Message;
import logic.ClientController;

/**
 * Communications class.
 * 
 * Handles the socket connected to server.
 * 
 * @author Johan Lindström (jolindse@hotmail.com)
 *
 */
public class ServerConnection implements Runnable {

	private Socket server;
	private ClientController controller;
	private PrintWriter out;
	private String name;
	private boolean running;

	/**
	 * Connects to server with the address and port. Establishes writer and
	 * socket for I/O operations.
	 * 
	 * @param controller
	 * @param name
	 * @param serverAdress
	 * @param serverPort
	 */
	public ServerConnection(ClientController controller, String name, String serverAdress, int serverPort) {
		this.controller = controller;
		this.name = name;
		try {
			server = new Socket(serverAdress, serverPort);
			out = new PrintWriter(server.getOutputStream(), true);
			running = true;
			controller.setServerConnected();
		} catch (IOException e) {
			controller.errorDialog("Connection error","Unable to connect to gameserver","The server was not found, Make sure server is running and that you have provided the proper IP.");
		}
	}

	/**
	 * Sends current message to server.
	 * 
	 * @param currMessage
	 */
	public void send(Message currMessage) {
		out.println(currMessage.getSendString());
	}

	/**
	 * Thread loop that starts with registering name on server and runs waiting
	 * for input from server.
	 */
	@Override
	public void run() {
		if (server != null) {
			controller.outputText("INFORMATION", "",
					"Connected to :" + server.getInetAddress() + " on port: " + server.getPort());
			controller.send("CONNECT", name, "");
			Scanner sc;
			try {
				sc = new Scanner(server.getInputStream());
				while (sc.hasNextLine() && running) {
					controller.outputText(sc.nextLine());
				}
			} catch (IOException e) {
				controller.errorDialog("Communication error", "Problem with connection to gameserver", "There was a problem with the network connection to gameserver. Please restart application.");
			}
		}
	}

	/**
	 * Disconnect method
	 * @return
	 */
	public boolean disconnect() {
		boolean result = false;
		try {
			controller.send("DISCONNECT", name, "");
			server.close();
			running = false;
			result = true;
		} catch (IOException e) {
			controller.errorDialog("Connection error", "Unable to disconnect from gameserver", "Please wait a moment and try again.");
		}
		return result;
	}
	

	// Getters
	
	public boolean getRunning(){
		return running;
	}
	
	public String getName() {
		return name;
	}

	
}
