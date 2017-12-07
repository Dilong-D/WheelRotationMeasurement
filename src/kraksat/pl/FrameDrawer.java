package kraksat.pl;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

class FrameDrawer {

    Mat addCross(Mat frame, Point center, Scalar color) {
        org.opencv.core.Point p1 = new org.opencv.core.Point(center.x, center.y+10);
        org.opencv.core.Point p2 = new org.opencv.core.Point(center.x, center.y-10);
        Imgproc.line(frame, p1, p2, color, 3);
        org.opencv.core.Point p3 = new org.opencv.core.Point(center.x+10, center.y);
        org.opencv.core.Point p4 = new org.opencv.core.Point(center.x-10, center.y);
        Imgproc.line(frame, p3, p4, color, 3);
        return frame;
    }
}
