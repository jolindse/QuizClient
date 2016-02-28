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
 * @author Johan Lindstr�m (jolindse@hotmail.com)
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
			controller.connectionErrorDialog();
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
				// MAKE PROPER ERROR LINE
				e.printStackTrace();
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
