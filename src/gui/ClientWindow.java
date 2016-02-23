package gui;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

public class ClientWindow {

		
		private final WebView outputView;
		
		public ClientWindow(Stage stage, ClientController controller){
		
			/*
			 * Have to load the html file with script and css into a string 
			 * in order to feed it to the WebView engine.
			 */
			String content = " ";
			// Load html as string for webview
			try(BufferedReader reader = new BufferedReader(new FileReader(new File("resources/render.htm")))){
				while(reader.ready()){
					content += reader.readLine()+"\n";
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
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
			outputView.setFontSmoothingType(FontSmoothingType.GRAY);
			outputView.getEngine().loadContent(content);		
			
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
			Platform.runLater(() ->{
				outputView.getEngine().executeScript("appendChat('"+currMessage.getCmd()+"','"+currMessage.getCmdData()+"','"+currMessage.getOptionalData()+"')");
			});
		}
}
