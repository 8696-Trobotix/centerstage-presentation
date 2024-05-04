package org.firstinspires.ftc.teamcode.squid.delta.subsystems;

import org.firstinspires.ftc.teamcode.mollusc.wrapper.Encoder;

// import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;

public class Scoring {

    public Servo angler;
    public DcMotorEx slide;
    public Encoder slideEncoder;
    // public TouchSensor limitSwitch;
    public CRServo spinner1, spinner2;
    public Servo stopper;

    public double SPINNER_POWER;
    public double RELEASE_PIXEL, RELEASE_RESET;
    public int SLIDE_MIN, SLIDE_MAX;
    public double ANGLER_DELTA;

    public double anglerPos = 1.0;
    
    public void slidePower(double power) {
        if (slide.isOverCurrent()
            || (power > 0 && slideEncoder.getTicks() > SLIDE_MAX)
            || (power < 0 && slideEncoder.getTicks() < SLIDE_MIN)
            /*|| (power < 0 && limitSwitch.isPressed())*/) {
            slide.setPower(0);
            return;
        }
        slide.setPower(power);
    }

    public void spinningOn() {
        spinner1.setPower(-SPINNER_POWER);
        spinner2.setPower(-SPINNER_POWER);
    }

    public void spinningOff() {
        spinner1.setPower(0);
        spinner2.setPower(0);
    }

    public void spinningPanic() {
        spinner1.setPower(SPINNER_POWER);
        spinner2.setPower(SPINNER_POWER);
    }

    public void anglerUp() {
        anglerPosition(anglerPos + ANGLER_DELTA);
    }
    public void anglerDown() {
        anglerPosition(anglerPos - ANGLER_DELTA);
    }
    public void anglerPosition(double pos) {
        if (0 <= pos && pos <= 1) {
            anglerPos = pos;
            angler.setPosition(anglerPos);
        }
    }

    public void releasePixel() {
        stopper.setPosition(RELEASE_PIXEL);
    }

    public void releaseReset() {
        stopper.setPosition(RELEASE_RESET);
    }
}
