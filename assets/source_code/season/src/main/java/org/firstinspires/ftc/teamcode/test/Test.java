package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.mollusc.drivetrain.*;
import org.firstinspires.ftc.teamcode.mollusc.wrapper.*;
import org.firstinspires.ftc.teamcode.mollusc.utility.*;
import org.firstinspires.ftc.teamcode.mollusc.Mollusc;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Test", group="Test")

public class Test extends LinearOpMode {

    private MecanumRobotCentric drivetrain;

    private double drive, strafe, turn;

    @Override
    public void runOpMode() {

        Mollusc.init(this);

        drivetrain = new MecanumRobotCentric(
            new DrivetrainBaseFourWheel(
                Make.motor("frontLeft", DcMotorEx.Direction.REVERSE),
                Make.motor("frontRight", DcMotorEx.Direction.FORWARD), 
                Make.motor("rearLeft", DcMotorEx.Direction.REVERSE), 
                Make.motor("rearRight", DcMotorEx.Direction.FORWARD)
            )
        );
        drivetrain.setDriveParams(1, 0.9, 1.1);
        drivetrain.base.zeroEncoders();

        waitForStart();

        while (opModeIsActive()) {

            // Quadratic controller sensitivity.
            drive  = Controls.quadratic(-gamepad1.left_stick_y);
            strafe = Controls.quadratic(gamepad1.left_stick_x);
            turn   = Controls.quadratic(gamepad1.right_stick_x);

            drivetrain.drive(drive, strafe, turn);

            int[] encoderCounts = drivetrain.base.getEncoderCounts();
            telemetry.addData(
                "Wheel Encoder Values", "%d %d %d %d", 
                encoderCounts[0], encoderCounts[1], encoderCounts[2], encoderCounts[3]
            );
            telemetry.update();
        }
    }
}
