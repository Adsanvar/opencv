package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FDTController {

	 static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	    private Mat logo;
		// FXML buttons
		@FXML
		private Button cameraButton;
		// the FXML area for showing the current frame
		@FXML
		private ImageView originalFrame;
		// checkboxes for enabling/disabling a classifier
		@FXML
		private CheckBox haarClassifier;
		@FXML
		private CheckBox lbpClassifier;

		// a timer for acquiring the video stream
		private ScheduledExecutorService timer;
		// the OpenCV object that performs the video capture
		private VideoCapture capture;
		// a flag to change the button behavior
		private boolean cameraActive;

		// face cascade classifier
		private CascadeClassifier faceCascade;
		private int absoluteFaceSize;


	protected void init(){

		this.capture = new VideoCapture();
		this.faceCascade = new CascadeClassifier();
		this.absoluteFaceSize = 0;

		// set a fixed width for the frame
		originalFrame.setFitWidth(600);
		// preserve image ratio
		originalFrame.setPreserveRatio(true);
	}

	 @FXML
	 void haarSelected(ActionEvent event){
		// check whether the lpb checkbox is selected and deselect it
			if (this.lbpClassifier.isSelected())
				this.lbpClassifier.setSelected(false);

			this.checkboxSelection("resources/haarcascades/haarcascade_frontalface_alt.xml");
	 }

	 @FXML
	 void lbpSelected(ActionEvent event){
			// check whether the haar checkbox is selected and deselect it
			if (this.haarClassifier.isSelected())
				this.haarClassifier.setSelected(false);

			this.checkboxSelection("resources/lbpcascades/lbpcascade_frontalface.xml");
	 }
	private void checkboxSelection(String classifierPath)
	{
			// load the classifier(s)
			this.faceCascade.load(classifierPath);

			// now the video capture can start
			this.cameraButton.setDisable(false);
	}

	 @FXML
	    void startCamera(){

	    			if (!this.cameraActive)
	    			{
	    				this.haarClassifier.setDisable(true);
	    				this.lbpClassifier.setDisable(true);
	    				// start the video capture
	    				this.capture.open(0);

	    				// is the video stream available?
	    				if (this.capture.isOpened())
	    				{
	    					this.cameraActive = true;

	    					// grab a frame every 33 ms (30 frames/sec)
	    					Runnable frameGrabber = new Runnable() {

	    						@Override
	    						public void run()
	    						{
	    							// effectively grab and process a single frame
	    							Mat frame = grabFrame();
	    							// convert and show the frame
	    							Image imageToShow = Utils.mat2Image(frame);
	    							updateImageView(originalFrame, imageToShow);
	    						}
	    					};

	    					this.timer = Executors.newSingleThreadScheduledExecutor();
	    					this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

	    					// update the button content
	    					this.cameraButton.setText("Stop Camera");
	    				}
	    				else
	    				{
	    					// log the error
	    					System.err.println("Impossible to open the camera connection...");
	    				}
	    			}
	    			else
	    			{
	    				// the camera is not active at this point
	    				this.cameraActive = false;
	    				// update again the button content
	    				this.cameraButton.setText("Start Camera");
	    				//enable classifiers checkboxes
	    				this.haarClassifier.setDisable(false);
	    				this.lbpClassifier.setDisable(false);

	    				// stop the timer
	    				this.stopAcquisition();
	    			}
	    }


	    private Mat grabFrame()
		{
			// init everything
			Mat frame = new Mat(); //old

			// check if the capture is open
			if (this.capture.isOpened())
			{
				try
				{
					// read the current frame
					this.capture.read(frame);//old

					// if the frame is not empty, process it
					if (!frame.empty()) //old
					{

						this.detectAndDisplay(frame);
					}

				}
				catch (Exception e)
				{
					// log the error
					System.err.println("Exception during the image elaboration: " + e);
				}
			}

			return frame;
		}

	    /**
	     *
	     * @param frame
	     */
	    private void detectAndDisplay(Mat frame)
	    {
	    	MatOfRect faces = new MatOfRect();
	    	Mat grayFrame = new Mat();

	    	Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
	    	Imgproc.equalizeHist(grayFrame, grayFrame);

	    	//compute minimun face size (20% of the frame height, in our case)
	    	if(this.absoluteFaceSize ==0)
	    	{
	    		int height = grayFrame.rows();
	    		if(Math.round(height *0.2f) > 0){
	    			this.absoluteFaceSize = Math.round(height * 0.2f);
	    		}
	    	}

	    	//detect faces
	    	this.faceCascade.detectMultiScale(grayFrame, faces, 1.1,2,0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());

	    	//each rectangle in faces is a face: draw them!
	    	Rect[] facesArray = faces.toArray();
	    	for(int i=0; i < facesArray.length; i++)
	    	Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0,255,0), 3);

	    }

	    private void updateImageView(ImageView view, Image image)
		{
			Utils.onFXThread(view.imageProperty(), image);
		}

	    private void stopAcquisition()
		{
			if (this.timer!=null && !this.timer.isShutdown())
			{
				try
				{
					// stop the timer
					this.timer.shutdown();
					this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
				}
				catch (InterruptedException e)
				{
					// log any exception
					System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
				}
			}

			if (this.capture.isOpened())
			{
				// release the camera
				this.capture.release();
			}
		}

	  protected void setClosed()
		{
			this.stopAcquisition();
		}

}
