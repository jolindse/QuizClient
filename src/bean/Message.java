package bean;

/**
 * Message bean used to store and access values from messages sent/recieved from
 * server.
 * 
 * @author Johan Lindström (jolindse@hotmail.com)
 *
 */
public class Message {
	private String cmd, cmdData, optionalData;
	private String splitChars = ",@";

	/**
	 * Splits the input string and loads the bean values.
	 * 
	 * @param inputString
	 */
	public Message(String inputString) {
		String[] splitString = inputString.split(splitChars, -1);
		cmd = splitString[0];
		cmdData = splitString[1];
		optionalData = splitString[2];

	}

	/**
	 * Alternate constructor for internal messagebuilding use.
	 * 
	 * @param cmd
	 * @param cmdData
	 * @param optionalData
	 */
	public Message(String cmd, String cmdData, String optionalData) {
		this.cmd = cmd;
		this.cmdData = cmdData;
		this.optionalData = optionalData;
	}

	// Getters & Setters
	public String getCmd() {
		return cmd;
	}

	public String getCmdData() {
		return cmdData;
	}

	public String getOptionalData() {
		return optionalData;
	}

	/**
	 * Returns a message formated for network transmission
	 * 
	 * @return
	 */
	public String getSendString() {
		return cmd + splitChars + cmdData + splitChars + optionalData;
	}
}
