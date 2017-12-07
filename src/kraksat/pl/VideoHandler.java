package kraksat.pl;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


class VideoHandler {

    private Scalar minPixVal;
    private Scalar maxPixVal;
    private static ScheduledExecutorService timer;
    private boolean cameraActive;
    private boolean binarizationFlag;
    private boolean setCenterOfRotationFlag;
    private VideoCapture capture;
    private String conversionMated;
    private SpeedCalculator speedCalculator;
    private FrameDrawer frameDrawer;
    private boolean trackingFlag;
    private static int period =10;

    VideoHandler() {
        capture = new VideoCapture();
        binarizationFlag = false;
        minPixVal = new Scalar(0, 0, 0);
        maxPixVal = new Scalar(255, 255, 255);
        conversionMated = "BGR";
        speedCalculator = new SpeedCalculator(period);
        setCenterOfRotationFlag = false;
        frameDrawer = new FrameDrawer();
        trackingFlag = false;
    }

    boolean isCameraActive() {
        return cameraActive;
    }

    void setMinPixVal(int v1, int v2, int v3) {
        minPixVal = new Scalar(v1, v2, v3);
    }

    void setMaxPixVal(int v1, int v2, int v3) {
        maxPixVal = new Scalar(v1, v2, v3);
    }

    void handleVideo(ImageView currentFrame, ImageView modifyCurrentFrame, Label centerPosText, Label objectPosText, Label objectDegPostText, Label objectRotSpeedText) {
        int cameraId = 0;
        capture.open(cameraId);
        if (capture.isOpened()) {
            this.cameraActive = true;
            Runnable frameGrabber = () -> {
                Mat frame = grabFrame(capture);
                if (!frame.empty()) {
                    frame = new VideoConverter().convertFrameUsingMetod(frame, conversionMated);
                    Mat modifyFrame = new VideoBinarer().makeFrameBinary(frame, minPixVal, maxPixVal);
                    if (binarizationFlag) {
                        speedCalculator.setObject(new MassCenterHandler().getMassCenter(modifyFrame));
                        updateTextsOnFront(objectPosText, speedCalculator.getObject().x +" , "+speedCalculator.getObject().y);
                        frame = frameDrawer.addCross(frame, speedCalculator.getObject() ,new Scalar(0,255,0));
                    }
                    Point centerOfRotation = speedCalculator.getCenterOfRotation();
                    if (setCenterOfRotationFlag) {
                        speedCalculator.setCenterOfRotation(speedCalculator.getObject());
                        updateTextsOnFront(centerPosText,centerOfRotation.x+" , "+centerOfRotation.y);
                        setCenterOfRotationFlag=false;
                    }
                    if(centerOfRotation!=null) {
                        frame = frameDrawer.addCross(frame, centerOfRotation, new Scalar(255, 0, 0));
                        updateTextsOnFront(objectDegPostText, String.valueOf(speedCalculator.getDegreeObjectCenter()));
                        updateTextsOnFront(objectRotSpeedText,"dupa "+String.valueOf(speedCalculator.getObjectRotationSpeed()));
                    }
                    Image image2ToShow = mat2Image(modifyFrame);
                    updateVideosOnFront(modifyCurrentFrame, image2ToShow);
                }
                Image imageToShow = mat2Image(frame);
                updateVideosOnFront(currentFrame, imageToShow);

            };
            timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(frameGrabber, 0, period, TimeUnit.MILLISECONDS);
        } else {
            System.out.println("Error during camera connection");
        }
    }

    private static Image mat2Image(Mat frame) {
        try {
            return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
        } catch (Exception e) {
            System.out.println("Cannot convert the Mat obejct: " + e);
            return null;
        }
    }

    private static BufferedImage matToBufferedImage(Mat original) {
        BufferedImage image;
        int width = original.width();
        int height = original.height();
        int channels = original.channels();
        byte[] sourcePixels;
        sourcePixels = new byte[width * height * channels];
        original.get(0, 0, sourcePixels);

        if (original.channels() > 1)
            image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        else
            image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(sourcePixels, 0, targetPixels, 0, sourcePixels.length);

        return image;
    }

    void stopAcquisition() {
        if (timer != null && !timer.isShutdown()) {
            try {
                timer.shutdown();
                timer.awaitTermination(period, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                System.out.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }
        if (capture.isOpened()) {
            capture.release();
        }
        cameraActive = false;
    }

    private void updateVideosOnFront(ImageView view, Image image) {
        Platform.runLater(() -> view.imageProperty().set(image));
    }

    private void updateTextsOnFront(Label label,String text){
        Platform.runLater(() -> label.textProperty().set(text));
    }

    private Mat grabFrame(VideoCapture capture) {
        Mat frame = new Mat();
        if (capture.isOpened()) {
            try {
                capture.read(frame);
            } catch (Exception e) {
                System.out.println("Exception during the image grabing: " + e);
            }
        }
        return frame;
    }

    void setBinarizationFlag(boolean binarizationFlag) {
        this.binarizationFlag = binarizationFlag;
    }

    boolean isBinarizationFlag() {
        return binarizationFlag;
    }

    void setConversionMated(String metod){
        conversionMated = metod;
    }

    public void setSetCenterOfRotationFlag(boolean setCenterOfRotationFlag) {
        this.setCenterOfRotationFlag = setCenterOfRotationFlag;
    }

    public void setTrackingFlag(boolean trackingFlag) {
        this.trackingFlag = trackingFlag;
    }

    public boolean isTrackingFlag() {
        return trackingFlag;
    }
}
