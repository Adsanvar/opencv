package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class DTFController {
	 //static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

@FXML
ImageView transformedImage;

@FXML
ImageView originalImage;

@FXML
ImageView antitransformedImage;

@FXML
Button transformButton;

@FXML
Button antitransformButton;

//the main stage
	private Stage stage;
	// the JavaFX file chooser
	private FileChooser fileChooser = new FileChooser();
		// support variables
		private Mat image;
		private List<Mat> planes;
		// the final complex image
		private Mat complexImage;

	@FXML
	private void transformImage(ActionEvent event){

	}

	@FXML
	private void antitransformImage(ActionEvent event){

	}

	@FXML
	private void loadImage(ActionEvent event)
	{
		File file = new File("./resources/");
		this.fileChooser.setInitialDirectory(file);
		file = this.fileChooser.showOpenDialog(stage);
		if(file != null){
			//Image img = new Image(file.toURI().toString());
			//this.originalImage.setImage(img);
			System.out.println(file.getAbsolutePath());
			this.image = Imgcodecs.imread(file.getAbsolutePath());
			//this.originalImage.setImage(Utils.mat2Image(this.image));

		}

	}

	private void updateImageView(ImageView view, Image image)
	{
		Utils.onFXThread(view.imageProperty(), image);
	}

}
