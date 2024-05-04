package org.firstinspires.ftc.teamcode.squid.gamma.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {

    public Servo servo1, servo2;
    public DcMotorEx motor;

    public double UP_1, UP_2;
    public double DOWN_1, DOWN_2;
    public double DELTA;
    public double MAX_DOWN_1, MAX_DOWN_2;
    public double PRESET_POWER;
    public double intakePos1, intakePos2;

    public void presetUp() {
        position(UP_1, UP_2);
    }

    public void presetDown() {
        position(DOWN_1, DOWN_2);
    }

    public void up() {
        position(intakePos1 - DELTA, intakePos2 + DELTA);
    }

    public void down() {
        position(intakePos1 + DELTA, intakePos2 - DELTA);
    }

    public void position(double pos1, double pos2) {
        if (pos1 >= UP_1 && pos1 <= MAX_DOWN_1) {
            intakePos1 = pos1;
            servo1.setPosition(intakePos1);
        }
        if (pos2 <= UP_2 && pos2 >= MAX_DOWN_2) {
            intakePos2 = pos2;
            servo2.setPosition(intakePos2);
        }
    }

    public void power(double power) {
        motor.setPower(power);
    }
}
