package org.firstinspires.ftc.teamcode.squid.delta.subsystems;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.mollusc.Mollusc;

public class Conveyor {

    public DcMotorEx motor;
    public DistanceSensor sensor;
    public ElapsedTime timer = new ElapsedTime();

    public double PRESET_POWER;
    // PIXEL_TIME --> time until sensor begins searching for pixels again.
    // CARRY_TIME --> time until the conveyor is switched to manual / stop mode after detecting the second pixel.
    public double DISTANCE_THRESHOLD, PIXEL_TIME, CARRY_TIME;

    public DetectionState detectionState = DetectionState.NONE;

    public void power(double power) {
        motor.setPower(power);
    }

    public boolean pixelOverflow() {
        boolean sensorState = sensor.getDistance(DistanceUnit.CM) <= DISTANCE_THRESHOLD;

        switch (detectionState) {
            case NONE:
                if (sensorState && timer.seconds() >= PIXEL_TIME) {
                    detectionState = DetectionState.FIRST_PIXEL;
                    timer.reset();
                }
                break;
            case FIRST_PIXEL:
                if (sensorState && timer.seconds() >= PIXEL_TIME) {
                    detectionState = DetectionState.SECOND_PIXEL;
                    timer.reset();
                }
                break;
            case SECOND_PIXEL:
                if (timer.seconds() >= CARRY_TIME) {
                    return true;
                }
                break;
        }
        return false;
    }

    public void resetState() {
        detectionState = DetectionState.NONE;
        timer.reset();
    }

    public enum DetectionState {
        NONE, FIRST_PIXEL, SECOND_PIXEL
    }
}
