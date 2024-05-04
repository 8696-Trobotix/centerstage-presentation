package org.firstinspires.ftc.teamcode.squid.delta.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Lift {

    public DcMotorEx motor;

    public double POWER;

    public void power(double power) {
        motor.setPower(power);
    }
}
