package network;

import java.io.BufferedWriter;
import java.io.IOException;

import logic.ClientController;

public class ServerWrite implements Runnable {

	private ClientController controller;
	private BufferedWriter out;
	private String text;

	public ServerWrite(ClientController controller, BufferedWriter out, String rawText, String command) {		
		this.out = out;
		text = parseText(rawText, command);
		this.controller = controller;
	}
	
	private String parseText(String in,String command){
		String parsed;
		switch(command){
		case "DISCONNECT":
			parsed = "DISCONNECT,@,@\n";
			break;
		case "CHAT":
			parsed = "CHAT,@,@"+in+"\n";
			break;
		default:
			parsed = "CHAT,@,@"+in+"\n";
			break;
		}
		return parsed;
	}

	@Override
	public void run() {
			try {
				out.write(text);
				out.flush();
			} catch (IOException e) {
				controller.outputText("Error sending to server!");
			}
		}

}
