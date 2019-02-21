package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class FXController {
	 static{System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}
	// a timer for acquiring the video stream
    private ScheduledExecutorService timer;
    // the OpenCV object that realizes the video capture
    private VideoCapture capture = new VideoCapture();
    // a flag to change the button behavior
    private boolean cameraActive = false;
    // the id of the camera to be used
    private static int cameraId = 0;

    private Mat logo;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
/**
    @FXML
    private ImageView cameraImageView;

    @FXML
    private Button startCameraBtn;**/

    @FXML
    private Button button;

    @FXML
    private ImageView currentFrame;

    @FXML
    private ImageView histogram;

    @FXML
    private CheckBox grayscale;

    @FXML
    private CheckBox logoCheckBox;

   /**
    @FXML
    void StartCamera(ActionEvent event) {
    	if (!this.cameraActive)
		{
			// start the video capture
			this.capture.open(cameraId);

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
						updateImageView(cameraImageView, imageToShow);
					}
				};

				this.timer = Executors.newSingleThreadScheduledExecutor();
				this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

				// update the button content
				this.startCameraBtn.setText("Stop Camera");
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
			this.startCameraBtn.setText("Start Camera");

			// stop the timer
			this.stopAcquisition();
		}
    }

    @FXML
    void initialize() {
        assert cameraImageView != null : "fx:id=\"cameraImageView\" was not injected: check your FXML file 'FirstJFX.fxml'.";
        assert startCameraBtn != null : "fx:id=\"startCameraBtn\" was not injected: check your FXML file 'FirstJFX.fxml'.";


    }**/


//new
    @FXML
    void loadLogo(){
    		if(logoCheckBox.isSelected()){
    			//Reads the Logo
    			//this.logo = Imgcodecs.imread("resources/images.png");

    		}
    }

    public void initialize()
	{
		this.capture = new VideoCapture();
		this.cameraActive = false;
	}

    @FXML
    void startCamera(){
    	// set a fixed width for the frame
    			this.currentFrame.setFitWidth(600);
    			// preserve image ratio
    			this.currentFrame.setPreserveRatio(true);

    			if (!this.cameraActive)
    			{
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
    							updateImageView(currentFrame, imageToShow);
    						}
    					};

    					this.timer = Executors.newSingleThreadScheduledExecutor();
    					this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);

    					// update the button content
    					this.button.setText("Stop Camera");
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
    				this.button.setText("Start Camera");

    				// stop the timer
    				this.stopAcquisition();
    			}
    }

    //New
    void showHistogram(Mat frame, boolean grayScale){
    	//split the frames in multiple images
    	List<Mat> images = new ArrayList<Mat>();
    	Core.split(frame, images);

    	//set the number of bins at 256
    	MatOfInt histSize = new MatOfInt(256);
    	//only one channel
    	MatOfInt channels = new MatOfInt(0);
    	//set the ranges
    	MatOfFloat histRange = new MatOfFloat(0, 256);


    	//compute the histograms for the B, G and R components
    	Mat hist_b = new Mat();
    	Mat hist_g = new Mat();
    	Mat hist_r = new Mat();

    	//B component or gray image
    	Imgproc.calcHist(images.subList(0,1), channels, new Mat(), hist_b, histSize, histRange, false);

    	//G and R components (if the image is not in gray scale)
    	if(!grayScale){
			Imgproc.calcHist(images.subList(1, 2), channels, new Mat(), hist_g, histSize, histRange, false);
			Imgproc.calcHist(images.subList(2, 3), channels, new Mat(), hist_r, histSize, histRange, false);

    	}

    	//draw the histogram
    	int hist_w = 150; //width of the histogram image
    	int hist_h = 150; //height of the histogram image
    	int bin_w = (int) Math.round(hist_w / histSize.get(0,0)[0]);


    	Mat histImage = new Mat(hist_h, hist_w, CvType.CV_8UC3, new Scalar(0,0,0));
    	//normalize the result to [0, histImage.rows()]
    	Core.normalize(hist_b, hist_b, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());

    	//for G and R components
    	if(!grayScale){
    		Core.normalize(hist_g, hist_g, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());
    		Core.normalize(hist_r, hist_r, 0, histImage.rows(), Core.NORM_MINMAX, -1, new Mat());

    	}

    	//effectively draw the histogram(s)
    	for(int i=1; i < histSize.get(0,0)[0]; i++)
    	{
    		// B component or gray image
    			Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_b.get(i - 1, 0)[0])),
    				new Point(bin_w * (i), hist_h - Math.round(hist_b.get(i, 0)[0])), new Scalar(255, 0, 0), 2, 8, 0);

    		// G and R components (if the image is not in gray scale)
    			if (!grayScale)
    			{
    				Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_g.get(i - 1, 0)[0])),
    					new Point(bin_w * (i), hist_h - Math.round(hist_g.get(i, 0)[0])), new Scalar(0, 255, 0), 2, 8, 0);

    				Imgproc.line(histImage, new Point(bin_w * (i - 1), hist_h - Math.round(hist_r.get(i - 1, 0)[0])),
    					new Point(bin_w * (i), hist_h - Math.round(hist_r.get(i, 0)[0])), new Scalar(0, 0, 255), 2, 8, 0);
    			}
    	}

    	//display the histogram
    	Image histImg = Utils.mat2Image(histImage);
    	updateImageView(histogram, histImg);


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

					if(logoCheckBox.isSelected() && this.logo !=null) //new
					{
						Rect roi = new Rect(frame.cols() - logo.cols(), frame.rows() - logo.rows(), logo.cols(), logo.rows());

						Mat imageROI = frame.submat(roi);
						//add the logo: method ##1
						Core.addWeighted(imageROI, 1.0, logo, 0.8, 0.0, imageROI);

						//add the logo: method #2
						//logo.copyTo(imageROI, logo);

					}

					if(grayscale.isSelected()){ //new
						Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY); //old
					}
					//show the histogram
					this.showHistogram(frame, grayscale.isSelected());
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

    private void updateImageView(ImageView view, Image image)
	{
		Utils.onFXThread(view.imageProperty(), image);
	}
    protected void setClosed()
	{
		this.stopAcquisition();
	}
}
