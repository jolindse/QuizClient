package gui;

import java.net.URL;
import java.util.List;

import bean.Message;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.ClientController;

/**
 * Client windows class
 * 
 * Uses standard JavaFX controlls for input and interaction and a WebView for
 * displaying the messages from network. The latter in order to have "richtext"
 * features since FX doesn't have a textarea with thouse capabilites.
 * 
 * HTML. CSS and JavaScript that handles formatting and output resides in
 * resources/render.htm.
 * 
 * @author Johan Lindström (jolindse@hotmail.com)
 *
 */
public class ClientWindow {

	private final String titleString = "QuizClient 0.7";
	private Stage stage;
	private ListView<String> users;
	private List<String> usersConnected;
	private ListProperty<String> userListProperty;
	private final WebView outputView;

	public ClientWindow(Stage stage, ClientController controller) {

		/*
		 * Makes an URL to the HTML file that renders the chat and quiz output
		 * in the WebView.
		 */
		this.stage = stage;

		URL content = getClass().getResource("/resources/render.htm");

		/*
		 * Panes, buttons and listeners
		 */
		BorderPane rootPane = new BorderPane();
		HBox topPanel = new HBox();

		TextField nameField = new TextField();
		nameField.setPrefColumnCount(15);
		nameField.setPromptText("Input name");

		TextField ipField = new TextField();
		ipField.setPrefColumnCount(15);
		ipField.setPromptText("Server ip-adress");

		Button btnConnect = new Button("Connect to server");
		Button btnDisconnect = new Button("Disconnect");

		/*
		 * Depending on what amount of info is submitted will connect to server
		 * on submitted IP or default IP with given name.
		 */
		btnConnect.setOnAction((e) -> {
			if (nameField.getText().length() > 0 && ipField.getText().length() > 0) {
				controller.connect(nameField.getText(), ipField.getText());
			} else if (nameField.getText().length() > 0) {
				controller.connect(nameField.getText());
			} else {
				controller.outputInfoText("Name field can not be empty!");
			}
		});

		/*
		 * Disconnects the client from server
		 */
		btnDisconnect.setOnAction((e) -> {
			controller.disconnect();
		});

		topPanel.getChildren().addAll(nameField, ipField, btnConnect, btnDisconnect);

		userListProperty = new SimpleListProperty<>();
		users = new ListView<String>();
		users.itemsProperty().bind(userListProperty);
		users.setPrefWidth(120);
		users.setVisible(false);

		outputView = new WebView();
		outputView.setFontSmoothingType(FontSmoothingType.GRAY);
		outputView.getEngine().load(content.toExternalForm());

		TextField input = new TextField();
		input.setOnAction((e) -> {
			controller.send("CHAT", "", input.getText());
			input.setText("");
		});

		// Sets initial focus on input field. For some reason only works if ran
		// with runLater.
		Platform.runLater(() -> {
			input.requestFocus();
		});

		rootPane.setTop(topPanel);
		rootPane.setCenter(outputView);
		rootPane.setRight(users);
		rootPane.setBottom(input);

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				controller.exitAll();
			}
		});

		Scene scene = new Scene(rootPane, 690, 500);
		stage.setTitle(titleString);
		stage.setScene(scene);
		stage.show();

	}

	/**
	 * Takes a message and sends it to WebView via the JavaScrip function
	 * output() in resources/render.htm.
	 * 
	 * In order to avoid concurrency issues has to run in JavaFX thread.
	 * 
	 * @param currMessage
	 */
	public void addOutput(Message currMessage) {
		if (currMessage.getCmd().equals("QUIZ")){
			if (currMessage.getCmdData().equals("START")){
				quizStarted();
			}
			if (currMessage.getCmdData().equals("ENDED")){
				quizEnded();
			}
		}
		Platform.runLater(() -> {
			outputView.getEngine().executeScript("output('" + currMessage.getCmd() + "','" + currMessage.getCmdData() + "','" + currMessage.getOptionalData() + "')");
		});
	}

	/**
	 * Error dialog method called when theres an error connecting to server.
	 */
	public void displayAlert(){
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Connection error");
			alert.setHeaderText("Unable to connect to gameserver");
			alert.setContentText("The server was not found, Make sure server is running and that you have provided the proper IP.");
			alert.showAndWait();
		});
	}
	
	
	/**
	 * Sets window title when connected to server. Called from controller.
	 * 
	 * @param ip
	 * @param port
	 */
	public void setConnected(String ip, String port) {
		Platform.runLater(() -> {
			stage.setTitle("Connected to " + ip + ":" + port);
		});
	}
	
	/**
	 * Sets window title to default value when not connected.
	 */
	public void setDisconnected(){
		Platform.runLater(() -> {
			stage.setTitle(titleString);
		});
	}

	/**
	 * Feeds the status list to listview 
	 * 
	 * @param name
	 */
	public void updateUserStats(List<String> currStats) {
		usersConnected = currStats;
		Platform.runLater(() -> {
			userListProperty.set(FXCollections.observableArrayList(usersConnected));
		});

	}
	
	/**
	 *  Show status list when a game is running
	 */
	private void quizStarted(){
		Platform.runLater(()->{
			users.setVisible(true);
		});
	}
	
	/**
	 * Hide status list when a game ends.
	 */
	private void quizEnded(){
		Platform.runLater(()->{
			users.setVisible(false);
		});
	}
}
