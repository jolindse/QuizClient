package gui;



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

public class ClientWindow {

		private Render render;
		private final WebView outputView;
		
		public ClientWindow(Stage stage, ClientController controller){
			render = new Render();
			BorderPane rootPane = new BorderPane();
			HBox topPanel = new HBox();
			TextField nameField = new TextField();
			Button btnConnect = new Button("Connect to server");
			btnConnect.setOnAction((e) -> {
				if (nameField.getText().length() > 0){
					controller.connect(nameField.getText());
				}
			});
			topPanel.getChildren().addAll(nameField,btnConnect);
			
			outputView = new WebView();
			
			/*
			 * Trying to implement auto scroll;
			 */
			
			 
			outputView.setFontSmoothingType(FontSmoothingType.GRAY);
			outputView.getEngine().loadContent(render.getEmpty());
			
			
			TextField input = new TextField();
			input.setOnAction((e) ->{
					controller.send("CHAT","",input.getText());
					input.setText("");
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
			
			Scene scene = new Scene(rootPane,600,800);
			stage.setTitle("QuizClient v0.1");
			stage.setScene(scene);
			stage.show();
			
		}
		
		public void addOutput(Message currMessage){
			System.out.println("VIEW; Recieved output: "+currMessage.getSendString()); // TEST
			// RENDER INPUT
			String htmlContent = render.getContent(currMessage);
			// DO UPDATE IN FX THREAD
			Platform.runLater(() ->{
				outputView.getEngine().loadContent(htmlContent);
			});
		}
}
