package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			// load the FXML resource
						FXMLLoader loader = new FXMLLoader(getClass().getResource("/ObjRecognition.fxml"));
						// store the root element so that the controllers can use it
						BorderPane root = (BorderPane) loader.load();
						// create and style a scene
						Scene scene = new Scene(root, 800, 600);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						// create the stage with the given title and the previously created
						// scene
						primaryStage.setTitle("Object Recognition");
						primaryStage.setScene(scene);
						// show the GUI
						primaryStage.show();
						// set the proper behavior on closing the application
						ObjRecognitionController controller = loader.getController();
						primaryStage.setOnCloseRequest((new EventHandler<WindowEvent>() {
							public void handle(WindowEvent we)
							{
								controller.setClosed();
							}
						}));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
