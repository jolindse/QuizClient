package network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import logic.ClientController;

public class ServerConnection implements Runnable {

	private String serverAdress = "127.0.0.1";
	private int serverPort = 55500;
	private ClientController controller;

	public ServerConnection(ClientController controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		try (Socket connection = new Socket(serverAdress, serverPort)) {
			controller.setServerSocket(connection);
			controller.outputText("Connected to :" + connection.getInetAddress() + " on port: " + connection.getPort());
			while (true) {
				// Keep connection alive.
			}
		} catch (UnknownHostException e) {
			controller.outputText("Host unknown. Please check hostname and reconnect.");
		} catch (IOException e) {
			controller.outputText("Network connection problem. Please try to reconnect");
		}

	}

}
