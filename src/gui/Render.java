package gui;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import bean.Message;

public class Render {

	private int bufferSize = 150;
	
	private String htmlHead;
	private String htmlFoot = "</body></html>";

	
	private Deque<String> output;

	public Render() {
		setHead();
		output = new ArrayDeque<>(bufferSize);
	}

	public String getContent(Message inMessage) {
		String currLine = formatInput(inMessage);
		if (output.size() < bufferSize ) {
		output.offer(currLine);
		} else {
			output.remove();
			output.offer(currLine);
		}
		
		String renderedPage = htmlHead; 
		for(String currBuffer: output) {
			renderedPage +=currBuffer+"\n";
		}
		renderedPage += htmlFoot;
		return renderedPage;
	}

	public String getEmpty(){
		return htmlHead + htmlFoot;
	}
	
	private String formatInput(Message inMessage) {
		String currLine = null;
		switch (inMessage.getCmd()) {
		case "NAME":
			currLine = "<p class=\"name\">" + inMessage.getOptionalData() + " changed name to " + inMessage.getCmdData()+ "</p>";
			break;
		case "CONNECT":
			currLine =  "<p class=\"connect\">"+ inMessage.getCmdData() + " connected to gameserver!</p>";
			break;
		case "DISCONNECT":
			currLine = "<p class=\"disconnect\">" + inMessage.getCmdData() + " disconnected from gameserver!</p>";
			break;
		case "CHAT":
			currLine = "<p class=\"chat\">" + inMessage.getCmdData() + " says: " + inMessage.getOptionalData() + "</p>";
			break;
		case "ERROR":
			currLine = "<p class=\"error\">" + inMessage.getOptionalData()+"</p>";
			break;
		case "INFORMATION":
			currLine = "<p class=\"chat\">" + inMessage.getOptionalData()+"</p>";
			break;
		}
		return currLine;
	}
	
	private void setHead(){
		try {
			List<String> head = Files.readAllLines(Paths.get("resources/htmlhead.html"),StandardCharsets.UTF_8);
			for (String currLine: head){
				htmlHead += currLine+"\n";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
