package kraksat.pl;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;


class MassCenterHandler {



    public Mat addMassCenterMarker(Mat frame, Mat binaryFrame) {
        Point center = getMassCenter(binaryFrame);
        return new FrameDrawer().addCross(frame, center, new Scalar(0, 255, 0));
    }



    public Point getMassCenter(Mat map) {
        Moments moments = Imgproc.moments(map);
        Point centroid = new Point();
        centroid.x = (int) (moments.get_m10() / moments.get_m00());
        centroid.y = (int) (moments.get_m01() / moments.get_m00());
        return centroid;
    }
}
