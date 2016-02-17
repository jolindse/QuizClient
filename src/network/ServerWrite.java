package network;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import logic.ClientController;

public class ServerWrite implements Runnable {

	private Socket server;
	private ClientController controller;
	private BufferedWriter out;

	public ServerWrite(Socket server, ClientController controller) {
		this.controller = controller;
		this.server = server;
		out = null;
	}

	public void send(String text) {
		try {
			out.write(text);
			out.flush();
		} catch (IOException e) {
			controller.outputText("Error sending to server!");
		}
	}

	@Override
	public void run() {
		try {
			out = new BufferedWriter(new PrintWriter(server.getOutputStream()));
			while (true) {
			}
		} catch (IOException e) {
			controller.outputText("Error sending to server!");
		}
	}

}
