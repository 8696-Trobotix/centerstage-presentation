package org.firstinspires.ftc.teamcode.squid.delta.pipelines;

import org.firstinspires.ftc.teamcode.mollusc.Mollusc;
import org.firstinspires.ftc.teamcode.mollusc.vision.*;

import java.util.List;

import org.openftc.easyopencv.OpenCvPipeline;

import org.opencv.core.Scalar;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Mat;

public class TotemPipeline extends OpenCvPipeline {

    // Note: Red hues wrap around the hue color wheel.
    public Scalar lowerRed = new Scalar(0, 95, 95);
    public Scalar upperRed = new Scalar(10, 255, 255);
    public Scalar lowerRed2 = new Scalar(160, 95, 95);
    public Scalar upperRed2 = new Scalar(180, 255, 255);
    public Scalar lowerBlue = new Scalar(100, 80, 80);
    public Scalar upperBlue = new Scalar(130, 255, 255);

    private ColorRange redColorRange = new ColorRange(lowerRed, upperRed);
    private ColorRange redColorRange2 = new ColorRange(lowerRed2, upperRed2);
    private ColorRange blueColorRange = new ColorRange(lowerBlue, upperBlue);

    private Point p1 = new Point(0, 0), p2 = new Point(110, 240);
    private Point p3 = new Point(110, 0), p4 = new Point(320, 240);
    private Rect r1 = new Rect (p1, p2);
    private Rect r2 = new Rect (p3, p4);

    private ColorRange[] focusRange;
    private Alliance alliance;
    private List<VisionObject> objs = null;

    public double threshold = 0.025;
    public boolean calibrate = true;

    public TotemPipeline(Alliance alliance) {
        this.alliance = alliance;
        if (alliance == Alliance.RED) {
            focusRange = new ColorRange[] {redColorRange, redColorRange2};
        } else {
            focusRange = new ColorRange[] {blueColorRange};
        }
    }

    public void setBound(Scalar bound, int idx, double value) {
        bound.val[idx] = value;
    }

    @Override
    public void init(Mat firstFrame) {
    }

    @Override
    public Mat processFrame(Mat inputFrame) {
        objs = ObjectDetector.coloredObjectCoordinates(
            inputFrame, 
            threshold, 
            0.0, 
            calibrate, 
            focusRange
        );
        if (calibrate) {
            ObjectDetector.rectangle(
                inputFrame, 
                (int)p1.x, 
                (int)p1.y, 
                (int)p2.x, 
                (int)p2.y, 
                ObjectDetector.BLUE_COLOR, 2
            );
        }
        return inputFrame;
    }

    public TotemZone getZone() {
        if (objs == null || objs.isEmpty()) {
            return TotemZone.RIGHT;
        } else { // This may help mitigate the exceedingly rare edge case where the image processing thread clears `objs` after the prior check.
            VisionObject largest = objs.get(0);
            for (VisionObject obj : objs) {
                if (obj.pixelTotality > largest.pixelTotality) {
                    largest = obj;
                }
            }
            if (largest.x > p2.x) {
                return TotemZone.CENTER;
            }
            return TotemZone.LEFT;
        }
    }

    public enum Alliance {
        RED, BLUE
    }
    public enum TotemZone {
        LEFT, CENTER, RIGHT
    }
}
