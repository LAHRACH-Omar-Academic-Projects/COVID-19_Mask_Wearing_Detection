package app.controllers.views.components.camera;

import app.controllers.db.AlarmController;
import app.controllers.db.PersonController;
import app.models.Alarm;
import app.models.Camera;
import app.models.Person;
import app.utils.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CameraDisplayController {
    public StackPane camerasDisplayPanel;
    public GridPane camerasGridPanel;
    public ImageView currentFrame;
    public Button startButton;
    public AnchorPane infoBox;
    public ImageView connected;
    public ImageView disconnected;
    private ScheduledExecutorService timer;
    private VideoCapture capture;
    private boolean cameraActive = false;
    private CascadeClassifier faceCascade;
    private int absoluteFaceSize;
    private String cameraName;
    private String IpAddress;
    private int port;
    private double cameraWidth, cameraHeight;
    private double displayPanelWidth;
    private double displayPanelHeight;
    private AlarmController alarmController;
    private Alarm alarm;
    private PersonController personController;
    private MediaPlayer mediaPlayer;

    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    protected void init() {
        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.faceCascade.load("C:\\Users\\Lahrach Omar\\Downloads\\Work space\\Education\\S4\\PFA\\Je-te-surveille\\src\\app\\resources\\haarcascades\\haarcascade_frontalface_alt.xml");
        this.absoluteFaceSize = 0;
        currentFrame.setPreserveRatio(true);
        this.alarmController = new AlarmController();
        this.alarm = alarmController.getSelectedAlarm();
        this.personController = new PersonController();
        if(alarm != null) {
            Media sound = new Media(new File(alarm.getPath()).toURI().toString());
            mediaPlayer = new MediaPlayer(sound);
        }
    }

    public void loadCamera(StackPane camerasDisplayPanel, GridPane camerasGridPanel, int rowIndex, int colIndex) {
        setCamerasGridPanel(camerasGridPanel);
        setCamerasDisplayPanel(camerasDisplayPanel);
        FXMLLoader loader = new FXMLLoader();
        try {
            loader.setLocation(new URL("file:///C:\\Users\\Lahrach Omar\\Downloads\\Work space\\Education\\S4\\PFA\\Je-te-surveille\\src\\app\\views\\components\\camera\\cameraDisplay.fxml"));
            BorderPane item = loader.<BorderPane>load();
            item.getStylesheets().add(getClass().getResource("../../../../assets/styles/components/camera/cameraDisplay.css").toExternalForm());
            camerasGridPanel.add(item, colIndex, rowIndex);
            currentFrame = (ImageView) loader.getNamespace().get("currentFrame");
            startButton = (Button) loader.getNamespace().get("startButton");
            connected = (ImageView) loader.getNamespace().get("connected");
            disconnected = (ImageView) loader.getNamespace().get("disconnected");
            infoBox = (AnchorPane) loader.getNamespace().get("infoBox");
            int rows_count = camerasGridPanel.getRowCount();
            int cols_count = camerasGridPanel.getColumnCount();
            setDisplayPanelWidth(camerasDisplayPanel.getWidth() - ((cols_count - 1) * 10 + 30));
            setDisplayPanelHeight(camerasDisplayPanel.getHeight() - (rows_count - 1) * 10);
            setCameraWidth(displayPanelWidth / cols_count);
            setCameraHeight(displayPanelHeight / rows_count - 0.2 * displayPanelHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCamera(Camera camera, StackPane camerasDisplayPanel, GridPane camerasGridPanel, int rowIndex, int colIndex) {
        loadCamera(camerasDisplayPanel, camerasGridPanel, rowIndex, colIndex);
        setCameraInfo(camera);
        startButton.setOnAction(actionEvent -> {
            if (!this.cameraActive) {
                startCamera();
            }
            else {
                stopCamera();
            }
        });
        Label cameraName = new Label(camera.getName());
        AnchorPane.setTopAnchor(cameraName, 10d);
        AnchorPane.setBottomAnchor(cameraName, 10d);
        AnchorPane.setLeftAnchor(cameraName, 30d);
        infoBox.getChildren().add(1, cameraName);
    }

    public void setCameraInfo(Camera camera) {
        setCameraName(camera.getName());
        setIpAddress(camera.getIpAddress());
        setPort(camera.getPort());
    }

    public void startCamera() {
        init();
        if(port == 0) {
            this.capture.open(0);
        }
        else {
            this.capture.open("http://" + getIpAddress() + ":" + getPort() + "/video?dummy=param.mjpg");
        }
        if (this.capture.isOpened()) {
            this.cameraActive = true;
            Runnable frameGrabber = () -> {
                Mat frame = elaborateFrame();
                Size size = new Size(cameraWidth, cameraHeight);
                Imgproc.resize(frame, frame, size);
                Image imageToShow = Utils.mat2Image(frame);
                updateImageView(currentFrame, imageToShow);
            };
            this.timer = Executors.newSingleThreadScheduledExecutor();
            this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
            this.startButton.setText("Stop Camera");
            disconnected.setVisible(false);
            connected.setVisible(true);
        }
        else {
            System.err.println("Impossible to open the camera connection...");
        }
    }

    private Mat elaborateFrame() {
        Mat frame = new Mat();
        try {
            this.capture.read(frame);
            if (!frame.empty()) {
                this.detectAndDisplay(frame);
            }
        }
        catch (Exception e) {
            System.err.println("Exception during the image elaboration: " + e);
        }
        return frame;
    }

    private void detectAndDisplay(Mat frame) throws IOException {
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();

        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);

        if (this.absoluteFaceSize == 0)
        {
            int height = grayFrame.rows();
            if (Math.round(height * 0.2f) > 0)
            {
                this.absoluteFaceSize = Math.round(height * 0.2f);
            }
        }

        this.faceCascade.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(this.absoluteFaceSize, this.absoluteFaceSize), new Size());
        // each rectangle in faces is a face: draw them!
        Rect[] facesArray = faces.toArray();
        Point P1=new Point(0,0);
        Point P2=new Point(0,0);
        Point p3=new Point(0,0);
        Point p4=new Point(0,0);
        Point p5=new Point(0,0);
        String filename = "Ouput.jpg";
        Mat desImaMat=new Mat();
        Imgproc.cvtColor(frame, desImaMat, Imgproc.COLOR_RGB2HSV);
        Scalar lowerbScalar = new Scalar(250,0,0); // Set the lowest range
        Scalar highbScalar = new Scalar(256, 10, 10); // Set the highest range
        // Get a binary image. The result is saved to desImgMag
        Core.inRange(frame, lowerbScalar, highbScalar, desImaMat);
        HighGui.imshow("image",desImaMat);
        Mat blurredImage = new Mat();
        Mat hsvImage = new Mat();
        // remove some noise
        Imgproc.blur(frame, blurredImage, new Size(7, 7));
        // convert the frame to HSV
        Imgproc.cvtColor(blurredImage, hsvImage, Imgproc.COLOR_BGR2HSV);
        int clr, red, green, blue, sumred = 0, sumgreen = 0, sumblue = 0;
        MatOfByte mem = new MatOfByte();
        MatOfByte mem2=new MatOfByte();
        Scalar greenn = new Scalar(0, 128, 0);
        Scalar redd= new Scalar(0, 0, 255);
        Rect rectCrop=null;
        for (Rect rect : faces.toArray()) {
            Point startingPoint = new Point(rect.tl().x-1,rect.br().y);
            Point endingPoint = new Point(rect.br().x,rect.br().y+25);
            double fontScale = (rect.br().x/rect.tl().x)/4;
            p3.x=rect.x+rect.width/4;
            p3.y=rect.y;
            P1.x=rect.x+rect.width/4;
            double d=rect.y;
            p4.x=rect.x+rect.width/2; p4.y= rect.y+rect.height/2;
            p5.x=rect.x+rect.width; p5.y= rect.y+rect.height;
            P1.y=rect.y+rect.height/2;
            P2.x=rect.x+rect.width/2;
            P2.y=rect.y+rect.height;
            Imgproc.rectangle(frame,rect.tl(),rect.br(),redd,3);
            Imgproc.rectangle(frame,startingPoint,endingPoint,redd,-1);
            Imgproc.putText(frame,"Portez votre masque !",new Point(rect.tl().x+5,rect.br().y+18),Core.Formatter_FMT_CSV,fontScale,new Scalar(255, 255, 255),1);
            rectCrop = new Rect(rect.x+rect.width/4, rect.y,rect.width/4,rect.height/4);
            Rect rectCrop2 = new Rect(rect.x+rect.width/4, rect.y + rect.height / 2, rect.width/4, rect.height / 4);
            Rect rectCrop3 = new Rect(rect.x, rect.y, rect.width, rect.height);
            Mat personPhoto = frame.submat(rectCrop3);

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
          /*  String path = "C:\\Users\\Lahrach Omar\\Downloads\\Work space\\Education\\S4\\PFA\\Je-te-surveille\\src\\app\\photos\\" + formatter.format(date)  + "photo.jpg";
            Imgcodecs.imwrite(path, personPhoto);
            Person person = new Person();
            person.setNumberOfDetections(1);
            person.setPhoto(path);
            personController.addPerson(person);*/

            Mat faceImage =frame.submat(rectCrop);
            Mat faceImage2=frame.submat(rectCrop2);
            Mat resizeimage = new Mat();
            Mat resizeimage2=new Mat();
            Size sz = new Size(rectCrop.width, rectCrop.height);
            Imgproc.resize(faceImage, resizeimage, sz);
            Imgproc.resize(faceImage2, resizeimage2, sz);
            Imgcodecs.imwrite("D:\\" + "yy" + ".jpg", resizeimage);
            Imgcodecs.imwrite("D:\\" + "xx" + ".jpg", resizeimage2);
            Imgcodecs.imencode(".bmp", resizeimage, mem);
            Imgcodecs.imencode(".bmp", resizeimage2, mem2);
            BufferedImage im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
            BufferedImage im2 = ImageIO.read(new ByteArrayInputStream(mem2.toArray()));
            Mat gray = new Mat(resizeimage.rows(), resizeimage.cols(), resizeimage.type());
            Imgproc.cvtColor(resizeimage, gray, Imgproc.COLOR_BGR2GRAY);
            Mat binary = new Mat(resizeimage.rows(), resizeimage.cols(), resizeimage.type(), new Scalar(0));
            Imgproc.threshold(gray, binary, 100, 255, Imgproc.THRESH_BINARY_INV);
            //Finding Contours
            //System.out.println("rgb color of top");
            displayMeanColor(im, true);
            //System.out.println("rgb color of buttom");
            displayMeanColor(im2, true);
            Imgproc.putText(frame,"",P2, Core.Formatter_FMT_CSV, 1, new Scalar(255, 255, 255));
            Imgproc.putText(frame,"",P1, Core.Formatter_FMT_CSV, 1, new Scalar(255, 255, 255));
            int w1 = im.getWidth();
            int w2 = im2.getWidth();
            int h1 = im.getHeight();
            int h2 = im2.getHeight();
            if ((w1!=w2)||(h1!=h2)) {
                //System.out.println("Both images should have same dimwnsions");
            } else {
                long diff = 0;
                for (int j = 0; j < h1; j++) {
                    for (int i = 0; i < w1; i++) {
                        //Getting the RGB values of a pixel
                        int pixel1 = im.getRGB(i, j);

                        Color color1 = new Color(pixel1, true);
                        int r1 = color1.getRed();
                        int g1 = color1.getGreen();
                        int b1 = color1.getBlue();
                        int pixel2 = im2.getRGB(i, j);
                        Color color2 = new Color(pixel2, true);
                        int r2 = color2.getRed();
                        int g2 = color2.getGreen();
                        int b2= color2.getBlue();
                        //sum of differences of RGB values of the two images
                        long data = Math.abs(r1-r2)+Math.abs(g1-g2)+ Math.abs(b1-b2);
                        diff = diff+data;
                    }
                }
                double avg = diff/(w1*h1*3);
                double percentage = (avg/255)*100;
                System.out.println("Difference: "+percentage);
                if(percentage > 20){
                    Imgproc.rectangle(frame,rect.tl(),rect.br(),greenn,3);
                    Imgproc.rectangle(frame,startingPoint,endingPoint,greenn,-1);
                    Imgproc.putText(frame,"Bien ! ",new Point(rect.tl().x+5,rect.br().y+18),Core.Formatter_FMT_CSV,fontScale,new Scalar(255, 255, 255),1);
                    if(alarm != null) {
                        alarm.stop(mediaPlayer);
                    }
                }
                else {
                    String path = "C:\\Users\\Lahrach Omar\\Downloads\\Work space\\Education\\S4\\PFA\\Je-te-surveille\\src\\app\\photos\\" + formatter.format(date)  + "photo.jpg";
                    Imgcodecs.imwrite(path, personPhoto);
                    Person person = new Person();
                    person.setNumberOfDetections(1);
                    person.setPhoto(path);
                    personController.addPerson(person);
                    if (alarm != null) {
                        alarm.play(mediaPlayer);
                    }
                }
            }
        }
    }
    public void displayMeanColor(BufferedImage im, boolean topArea) {
        double meanRed = 0;
        double meanGreen = 0;
        double meanBlue = 0;

        double numberOfPixels = 0;

        double red_sum = 0;
        double green_sum = 0;
        double blue_sum = 0;

        for (int y = 0; y < im.getHeight(); y++) {
            for (int x = 0; x < im.getWidth(); x++) {
                numberOfPixels++;
                //Retrieving contents of a pixel
                int pixel = im.getRGB(x,y);

                //Creating a Color object from pixel value
                Color color = new Color(pixel, true);
                //Retrieving the R G B values
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                red_sum += red;
                green_sum += green;
                blue_sum += blue;
            }
        }
        meanRed =  red_sum / numberOfPixels;
        meanGreen =  green_sum / numberOfPixels;
        meanBlue =  blue_sum / numberOfPixels;

        //System.out.println("(" + meanRed + "," + meanGreen + "," + meanBlue + ")");
    }

    private void updateImageView(ImageView view, Image image) {
        Utils.onFXThread(view.imageProperty(), image);
    }

    public void stopCamera() {
        this.cameraActive = false;
        this.startButton.setText("Start camera");
        connected.setVisible(false);
        disconnected.setVisible(true);
        this.stopAcquisition();
        if(alarm != null) {
            alarm.stop(mediaPlayer);
        }
    }

    private void stopAcquisition() {
        if (this.timer!=null && !this.timer.isShutdown()) {
            this.timer.shutdown();
        }
        if (this.capture.isOpened()) {
            this.capture.release();
        }
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getIpAddress() {
        return IpAddress;
    }

    public void setIpAddress(String ipAddress) {
        IpAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setCameraWidth(double cameraWidth) {
        this.cameraWidth = cameraWidth;
    }

    public void setDisplayPanelWidth(double displayPanelWidth) {
        this.displayPanelWidth = displayPanelWidth;
    }

    public void setDisplayPanelHeight(double displayPanelHeight) {
        this.displayPanelHeight = displayPanelHeight;
    }

    public void setCameraHeight(double cameraHeight) {
        this.cameraHeight = cameraHeight;
    }

    public void setCamerasDisplayPanel(StackPane camerasDisplayPanel) {
        this.camerasDisplayPanel = camerasDisplayPanel;
    }

    public void setCamerasGridPanel(GridPane camerasGridPanel) {
        this.camerasGridPanel = camerasGridPanel;
    }
}
