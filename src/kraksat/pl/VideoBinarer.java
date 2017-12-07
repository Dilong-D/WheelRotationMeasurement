package kraksat.pl;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

class VideoBinarer {

    public Mat makeFrameBinary(Mat frame, Scalar minPixVal, Scalar maxPixVal) {
        Mat modifyFrame = new Mat();
        Core.inRange(frame, minPixVal, maxPixVal, modifyFrame);
        Imgproc.medianBlur(modifyFrame, modifyFrame, 5);
        return modifyFrame;
    }
}
