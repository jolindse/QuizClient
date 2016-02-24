package gui;

import java.net.URL;

import bean.Message;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

	private Stage stage;
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

		/*
		 * Depending on what amount of info is submitted will connect to server
		 * on submitted IP or default IP with given name.
		 */
		btnConnect.setOnAction((e) -> {
			//
			if (nameField.getText().length() > 0 && ipField.getText().length() > 0) {
				controller.connect(nameField.getText(), ipField.getText());
			} else if (nameField.getText().length() > 0) {
				controller.connect(nameField.getText());
			} else {
				controller.outputInfoText("Name field can not be empty!");
			}
		});
		topPanel.getChildren().addAll(nameField, ipField, btnConnect);

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
		rootPane.setBottom(input);

		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				controller.exitAll();
			}
		});

		Scene scene = new Scene(rootPane, 600, 500);
		stage.setTitle("QuizClient v0.7");
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
		Platform.runLater(() -> {
			outputView.getEngine().executeScript("output('" + currMessage.getCmd() + "','" + currMessage.getCmdData()
					+ "','" + currMessage.getOptionalData() + "')");
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
}
