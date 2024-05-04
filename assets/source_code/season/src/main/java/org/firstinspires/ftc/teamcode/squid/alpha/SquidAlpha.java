/*
SquidAlpha

Chassis with a hook attached to the end.
*/

package org.firstinspires.ftc.teamcode.squid.alpha;

 import org.firstinspires.ftc.teamcode.squid.alpha.mollusc.drivetrain.MecanumFieldCentric;
 import org.firstinspires.ftc.teamcode.squid.alpha.mollusc.drivetrain.MecanumRobotCentric;
//import org.firstinspires.ftc.teamcode.squid.alpha.mollusc.drivetrain.MecanumFieldCentric;
import org.firstinspires.ftc.teamcode.squid.alpha.mollusc.utilities.Controls;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

@TeleOp(name="SquidAlpha XI-IX", group="Squid")

public class SquidAlpha extends LinearOpMode {

    private MecanumRobotCentric drivetrain;
    private SquidWareAlpha      squidWare;
    private SquidWareAlpha     squidWare2;

    private double drive, strafe, turn;

    @Override
    public void runOpMode() {

        drivetrain = new MecanumRobotCentric(
            hardwareMap, telemetry, 
            "frontLeft", DcMotorEx.Direction.REVERSE,
            "frontRight", DcMotorEx.Direction.FORWARD, 
            "rearLeft", DcMotorEx.Direction.REVERSE,
            "rearRight", DcMotorEx.Direction.FORWARD// ,
//            "imu",
//                RevHubOrientationOnRobot.LogoFacingDirection.UP,
//                RevHubOrientationOnRobot.UsbFacingDirection.LEFT
        );
        drivetrain.setDriveParams(1, 0.9, 1.1);
        drivetrain.base.zeroEncoders();

        squidWare = new SquidWareAlpha(hardwareMap, "hook");
        squidWare2 = new SquidWareAlpha(hardwareMap, "hook2");

        telemetry.log().add("Amount of offset to apply to field centric drive +/- 90 deg. Press (A) to increase and (B) to decrease.");
        int offset = getAmountConfig();
//        drivetrain.imuOffset = offset * Math.PI / 2;

        waitForStart();

        while (opModeIsActive()) {

            // Quadratic controller sensitivity.
            drive  = Controls.quadratic(-gamepad1.left_stick_y);
            strafe = Controls.quadratic(gamepad1.left_stick_x);
            turn   = Controls.quadratic(gamepad1.right_stick_x);

            drivetrain.drive(drive, strafe, turn);

            if (gamepad1.a) {
                squidWare.hookUp();
            } else if (gamepad1.b) {
                squidWare.hookDown();
            }
            if (gamepad1.x) {
                squidWare2.hookUp();
            } else if (gamepad1.y) {
                squidWare2.hookDown();
            }

            int[] encoderCounts = drivetrain.base.getEncoderCounts();
            telemetry.addData(
                "Wheel Encoder Values", "%d %d %d %d", 
                encoderCounts[0], encoderCounts[1], encoderCounts[2], encoderCounts[3]
            );
            telemetry.update();
        }
    }

    public int getAmountConfig() {
        int value = 0;
        while (!isStopRequested()) {
            if (gamepad1.a) {
                ++value;
                sleep(250);
            } else if (gamepad1.b) {
                --value;
                sleep(250);
            } else if (gamepad1.x) {
                break;
            }
            telemetry.addData("Amount", value);
            idle();
        }
        sleep(250);
        return value;
    }
}
