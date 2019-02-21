package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			// load the FXML resource
 			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FDT.fxml"));
			BorderPane root = (BorderPane) loader.load();
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Face Detection & Tracking");
			primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream( "AI.png" )));
			primaryStage.setScene(scene);
			primaryStage.show();

			FDTController controller = loader.getController();
			controller.init();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
