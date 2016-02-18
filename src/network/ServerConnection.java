package network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import logic.ClientController;

public class ServerConnection implements Runnable {

	private String serverAdress = "127.0.0.1";
	private int serverPort = 55500;
	private ClientController controller;
	private PrintWriter out;
	private String name;

	public ServerConnection(ClientController controller, String rawName) {
		this.controller = controller;
		name = rawName;
	}

	@Override
	public void run() {
		Socket connection = null;
		try {
			connection = new Socket(serverAdress, serverPort);
			controller.setServerSocket(connection);
			controller.outputText("Connected to :" + connection.getInetAddress() + " on port: " + connection.getPort());
			out = new PrintWriter(connection.getOutputStream(),true);
			controller.setWriter(out);
			controller.send("", "CONNECT", name);
			Scanner sc = new Scanner(connection.getInputStream());
			while(sc.hasNextLine()){
				controller.outputText(sc.nextLine());
			}
		} catch (UnknownHostException e) {
			controller.outputText("Host unknown. Please check hostname and reconnect.");
		} catch (IOException e) {
			controller.outputText("Network connection problem. Please try to reconnect");
		} finally {
			try {
				connection.close();
			} catch (IOException e) {
				controller.outputText("Error closing socket.");
			}
		}

	}

}
