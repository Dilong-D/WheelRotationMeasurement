package kraksat.pl;

import org.opencv.core.Point;

import static java.lang.Math.*;

public class SpeedCalculator {
    private Point object;
    private Point centerOfRotation;
    private double degreeObjectCenter;
    private double preDegreeObjectCenter;
    private double objectRotationSpeed;
    private int deltaTime;

    SpeedCalculator(int deltaTime){
        this.deltaTime=deltaTime;
        this.object=new Point(0,0);
        this.centerOfRotation=new Point(0,0);
        this.objectRotationSpeed=0;
        this.deltaTime=deltaTime*1000;
        this.degreeObjectCenter=0;
        this.preDegreeObjectCenter=0;
    }

    public Point getObject() {
        return object;
    }

    public void setObject(Point object) {
        this.object = object;
        this.degreeObjectCenter = asin((-(object.y-centerOfRotation.y))/sqrt((object.x-centerOfRotation.x)*(object.x-centerOfRotation.x)+(object.y-centerOfRotation.y)*(object.y-centerOfRotation.y)));
        if(centerOfRotation.x>object.x)
            degreeObjectCenter=PI - degreeObjectCenter;
        if(centerOfRotation.y<object.y && centerOfRotation.x<object.x)
            degreeObjectCenter = degreeObjectCenter+ 2*PI;
        degreeObjectCenter=degreeObjectCenter*180/PI;

        double deltaDegree = degreeObjectCenter-preDegreeObjectCenter;
        if(deltaDegree<(-PI))
            deltaDegree=deltaDegree+2*PI;
        if(deltaDegree>(PI))
            deltaDegree=deltaDegree-2*PI;
        preDegreeObjectCenter=degreeObjectCenter;
        objectRotationSpeed=deltaDegree/deltaTime;
    }

    public void setCenterOfRotation(Point centerOfRotation) {
        this.centerOfRotation = centerOfRotation;
    }

    public Point getCenterOfRotation(){
        return centerOfRotation;
    }

    public double getDegreeObjectCenter() {
        return degreeObjectCenter;
    }

    public double getObjectRotationSpeed() {
        return objectRotationSpeed;
    }
}
