package network;

import java.io.PrintWriter;

import logic.ClientController;

public class ServerWrite implements Runnable {

	private ClientController controller;
	private PrintWriter out;
	private String text;

	public ServerWrite(ClientController controller, PrintWriter out, String rawText, String command,
			String commandData) {
		this.out = out;
		text = parseText(rawText, command, commandData);
		this.controller = controller;
	}

	private String parseText(String in, String command, String commandData) {
		String parsed;
		switch (command) {
		case "CONNECT":
			parsed = "CONNECT,@" + commandData + ",@";
			break;
		case "DISCONNECT":
			parsed = "DISCONNECT,@,@";
			break;
		case "CHAT":
			parsed = "CHAT,@,@" + in;
			break;
		default:
			parsed = "CHAT,@,@" + in;
			break;
		}
		return parsed;
	}

	@Override
	public void run() {
		out.println(text);
	}

}
