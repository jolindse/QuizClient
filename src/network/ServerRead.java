package network;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import logic.ClientController;

public class ServerRead implements Runnable {

	private Socket server;
	private ClientController controller;

	public ServerRead(Socket server, ClientController controller) {
		this.controller = controller;
		this.server = server;
	}

	@Override
	public void run() {
		try (Scanner sc = new Scanner(server.getInputStream())) {
			while (sc.hasNextLine()) {
				controller.outputText(sc.nextLine());
			}
		} catch (IOException e) {
			controller.outputText("Error getting input from server!");
		}
	}

}
