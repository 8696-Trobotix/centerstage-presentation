package org.firstinspires.ftc.teamcode.squid.gamma.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Conveyor {

    public DcMotorEx motor;

    public double PRESET_POWER;

    public void power(double power) {
        motor.setPower(power);
    }
}
