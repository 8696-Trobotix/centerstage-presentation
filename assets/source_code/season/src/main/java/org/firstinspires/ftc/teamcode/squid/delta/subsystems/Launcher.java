package org.firstinspires.ftc.teamcode.squid.delta.subsystems;

import com.qualcomm.robotcore.hardware.CRServo;

public class Launcher {

    public CRServo servo;

    public double FIRE;

    public void fire() {
        servo.setPower(FIRE);
    }
    public void stop() {
        servo.setPower(0);
    }
}
