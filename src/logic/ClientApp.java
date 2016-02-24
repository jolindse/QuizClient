package logic;

import gui.ClientWindow;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Entry point class.
 * 
 * @author Johan Lindström (jolindse@hotmail.com)
 *
 */
public class ClientApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Starts JavaFX thread and controller. Registers view with controller after
	 * view init.
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		ClientController controller = new ClientController();
		ClientWindow view = new ClientWindow(primaryStage, controller);
		controller.registerView(view);

	}

}
