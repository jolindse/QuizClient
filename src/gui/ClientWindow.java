package gui;


import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import logic.ClientController;

public class ClientWindow {

		private String htmlHead ="<html><head><title></title></head><body>";
		private String htmlFoot ="</body></html>";
		private 
		private ClientController controller;
		private WebView outputView;
		private WebEngine output;
	
		public ClientWindow(Stage stage, ClientController controller){
			this.controller = controller;
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
			outputView.setFontSmoothingType(FontSmoothingType.LCD);
			output = outputView.getEngine();
			
			
			TextField input = new TextField();
			input.setOnAction((e) ->{
					controller.send(input.getText(),"CHAT","");
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
		
		public void addOutput(String text){
			output.(text+"\n");
		}
}
