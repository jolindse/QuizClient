package gui;


import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import logic.ClientController;

public class ClientWindow {

		private ClientController controller;
		private TextArea output;
	
		public ClientWindow(Stage stage, ClientController controller){
			this.controller = controller;
			BorderPane rootPane = new BorderPane();
			
			
			HBox topPanel = new HBox();
			Button btnConnect = new Button("Connect to server");
			btnConnect.setOnAction((e) -> {
				controller.connect();
			});
			topPanel.getChildren().add(btnConnect);
			
			output = new TextArea();
			output.setEditable(false);
			
			TextField input = new TextField();
			
			rootPane.setTop(topPanel);
			rootPane.setCenter(output);
			rootPane.setBottom(input);
			
			Scene scene = new Scene(rootPane,600,800);
			stage.setTitle("QuizClient v0.1");
			stage.setScene(scene);
			stage.show();
			
		}
		
		public void addOutput(String text){
			String out = output.getText()+"\n"+text;
			output.setText(out);
		}
}
