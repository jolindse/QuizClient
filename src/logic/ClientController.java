package logic;

import java.io.PrintWriter;
import java.net.Socket;

import gui.ClientWindow;
import network.ServerConnection;
import network.ServerWrite;

public class ClientController {

	private ClientWindow view;
	private Socket server;
	private ServerWrite sw;
	private PrintWriter out;

	private boolean serverConnected;

	public ClientController() {
		serverConnected = false;
	}

	public void registerView(ClientWindow view) {
		this.view = view;
	}

	// METHODS TO MANIPULATE VIEW

	public void outputText(String text) {
		view.addOutput(text);
	}

	// METHODS TO HANDLE CONNECTION TO SERVER

	public void connect(String name) {
		if (!serverConnected) {
			ServerConnection sc = new ServerConnection(this, name);
			Thread connection = new Thread(sc);
			connection.start();
		}
	}

	public void setWriter(PrintWriter out) {
		this.out = out;
	}

	public void setServerSocket(Socket server) {

		this.server = server;
		serverConnected = true;
	}

	public void send(String text, String command, String commandData) {
		if (serverConnected) {
			ServerWrite write = new ServerWrite(this, out, text, command, commandData);
			Thread writeTh = new Thread(write);
			writeTh.start();
		} else {
			outputText("No connection to server!");
		}
	}

	public void send(String text) {
		if (serverConnected) {
			ServerWrite write = new ServerWrite(this, out, text, "CHAT", "");
			Thread writeTh = new Thread(write);
			writeTh.start();
		} else {
			outputText("No connection to server!");
		}
	}

	public void exitAll() {
		send("", "DISCONNECT", "");
		System.exit(0);
	}
}
