package logic;

import gui.ClientWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientController controller = new ClientController();
		ClientWindow view = new ClientWindow(primaryStage, controller);
		controller.registerView(view);
	}

}
