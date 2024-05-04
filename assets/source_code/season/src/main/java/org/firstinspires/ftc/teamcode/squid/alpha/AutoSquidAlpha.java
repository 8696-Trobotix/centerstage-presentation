/*
AutoSquidAlpha

Simple.
*/

package org.firstinspires.ftc.teamcode.squid.alpha;

import org.firstinspires.ftc.teamcode.squid.alpha.mollusc.drivetrain.MecanumRobotCentric;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

@Autonomous(name="AutoSquidAlpha XI-III", group="Squid")

public class AutoSquidAlpha extends LinearOpMode {

    private MecanumRobotCentric drivetrain;
    private SquidWareAlpha      squidWare;

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        drivetrain = new MecanumRobotCentric(
            hardwareMap, telemetry, 
            "frontLeft", DcMotorEx.Direction.REVERSE,
            "frontRight", DcMotorEx.Direction.FORWARD, 
            "rearLeft", DcMotorEx.Direction.REVERSE,
            "rearRight", DcMotorEx.Direction.FORWARD
        );
        drivetrain.setDriveParams(1, 0.9, 1.1);
        drivetrain.base.zeroEncoders();

        squidWare = new SquidWareAlpha(hardwareMap, "hook");

        // Primitive Configuration
        telemetry.log().add("Press (A) for Red Alliance, (B) for Blue Alliance.");
        boolean alliance = getConfig();
        telemetry.log().add("> Alliance: " + (alliance ? "Red" : "Blue"));
        telemetry.log().add("Press (A) for short route park, (B) for long route park.");
        boolean route = getConfig();
        telemetry.log().add("> Route: " + (route ? "Short" : "Long"));

        waitForStart();

        if (alliance && route) { // Red & Short
            driveTime(0.5, 0, 0, 1.5, 1);
        } else if (alliance && !route) { // Red & Long
            driveTime(0, -0.5, 0, 1.9, 1);
            driveTime(0.5, 0, 0, 2.75, 1);
        } else if (!alliance && route) { // Blue & Short
            driveTime(0.5, 0, 0, 1.5, 1);
        } else if (!alliance && !route) {
            driveTime(0, 0.5, 0, 1.9, 1);
            driveTime(0.5, 0, 0, 2.75, 1);
        }
    }

    public void driveTime(double drive, double strafe, double turn, double t, long delay) {
        drivetrain.drive(drive, strafe, turn);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < t)) {
            idle();
        }
        drivetrain.drive(0, 0, 0);
        sleep(delay);
    }

    public boolean getConfig() {
        boolean b = false;
        while (!isStopRequested()) {
            if (gamepad1.a) {
                b = true;
                break;
            } else if (gamepad1.b) {
                b = false;
                break;
            }
            idle();
        }
        while (gamepad1.a || gamepad1.b) {
            idle();
        }
        sleep(250);
        return b;
    }
}
