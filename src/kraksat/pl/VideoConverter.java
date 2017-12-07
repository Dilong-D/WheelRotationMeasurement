package kraksat.pl;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class VideoConverter {
    Mat convertFrameUsingMetod(Mat frame , String metod){
        switch (metod){
            case "YCrCb":
                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2YCrCb);
                break;
            case "HSV":
                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2HSV);
                break;
            case "BGR":
                break;
            default:
                Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB);
                break;
        }
        return frame;
    }
}
