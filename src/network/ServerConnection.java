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

	/**
	 * Connects to server with the adress and port. Establishes writer and
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
		} catch (IOException e) {
			// MAKE INFO MESSAGE AND DISPLAY
			e.printStackTrace();
		}
		controller.setServerConnected();
	}

	public String getName() {
		return name;
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
		controller.outputText("INFORMATION", "",
				"Connected to :" + server.getInetAddress() + " on port: " + server.getPort());
		controller.send("CONNECT", name, "");
		Scanner sc;
		try {
			sc = new Scanner(server.getInputStream());
			while (sc.hasNextLine()) {
				controller.outputText(sc.nextLine());
			}
		} catch (IOException e) {
			// MAKE PROPER ERROR LINE
			e.printStackTrace();
		}
	}
}
