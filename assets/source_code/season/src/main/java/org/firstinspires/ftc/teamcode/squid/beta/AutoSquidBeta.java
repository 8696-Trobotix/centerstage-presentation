/*
AutoSquidAlpha

Simple.
*/

package org.firstinspires.ftc.teamcode.squid.beta;

// Something is broken with the default RUN_TO_POSITION and we can't resolve it!

import org.firstinspires.ftc.teamcode.squid.beta.mollusc.drivetrain.MecanumRobotCentric;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(name="AutoSquidBeta XII-XII", group="Squid")

public class AutoSquidBeta extends LinearOpMode {

    private MecanumRobotCentric drivetrain;
    private SquidWareBeta      squidWare;

    private ElapsedTime runtime = new ElapsedTime();

    private final double AUTO_DRIVE_POWER = 0.25, AUTO_DRIVE_DELAY = 0.25;

    public final long DOUBLE_DELAY = 250;
    private double offset = 0.0;

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

        squidWare = new SquidWareBeta(hardwareMap, "hook", "arm", "claw", "launcher", "front", "right");

        // Primitive Configuration
        telemetry.log().add("Press (A) for Red Alliance, (B) for Blue Alliance.");
        boolean alliance = getConfig();
        telemetry.log().add("> Alliance: " + (alliance ? "Red" : "Blue"));
        telemetry.log().add("Press (A) for short route park, (B) for long route park.");
        boolean route = getConfig();
        telemetry.log().add("> Route: " + (route ? "Short" : "Long"));
        offset = float64(0.0, 0.05, 2, "Global Time Offset");

        waitForStart();

        squidWare.hookDown();
        squidWare.clawClose();
        sleep(500);
        squidWare.armDown();
        sleep(500);

        int totemLocation = driveTimeTotemMode(AUTO_DRIVE_POWER, 0, 0, 2.3, AUTO_DRIVE_DELAY);
        if (totemLocation == 1) { // Special moves for if totem is in center.
            squidWare.hookUp();
            sleep(500);
            driveTime(-AUTO_DRIVE_POWER, 0, 0, 0.167, AUTO_DRIVE_DELAY);
        }
        driveTime(-AUTO_DRIVE_POWER, 0, 0, 0.533, AUTO_DRIVE_DELAY);
        if (alliance) { // Red
            if (route) { // Short Route
                switch (totemLocation) {
                    case 0: // Left
                        driveTime(0, 0, -AUTO_DRIVE_POWER, 1.8, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 0.433, AUTO_DRIVE_DELAY);
                        driveTime(0, AUTO_DRIVE_POWER, 0, 0.2, AUTO_DRIVE_POWER);
                        squidWare.hookUp();
                        driveTime(-AUTO_DRIVE_POWER, 0, 0, 2.47, AUTO_DRIVE_DELAY);
                        driveTime(0, -AUTO_DRIVE_POWER, 0, 1.5, 0);
                        break;
                    case 1: // Center
                        driveTime(0, 0, AUTO_DRIVE_POWER, 1.78, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 2.7, AUTO_DRIVE_DELAY);
                        squidWare.clawOpen();
                        squidWare.armUp();
                        driveTime(0, AUTO_DRIVE_POWER, 0, 1.05, AUTO_DRIVE_DELAY);
                        break;
                    case 2: // Right
                        driveTime(0, 0, AUTO_DRIVE_POWER, 1.7, AUTO_DRIVE_DELAY);
                        driveTime(0, -AUTO_DRIVE_POWER, 0, 0.5, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 0.25, AUTO_DRIVE_DELAY);
                        squidWare.hookUp();
                        driveTime(-AUTO_DRIVE_POWER, 0, 0, 0.33, AUTO_DRIVE_DELAY);
                        driveTime(0, AUTO_DRIVE_POWER, 0, 1.8, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 2.3, 0); // Used to be 2.3.
                        break;
                }
            } else { // Long Route
                switch (totemLocation) {
                    case 0: // Left
                        driveTime(0, 0, -AUTO_DRIVE_POWER, 1.8, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 0.433, AUTO_DRIVE_DELAY);
                        driveTime(0, AUTO_DRIVE_POWER, 0, 0.2, AUTO_DRIVE_POWER);
                        squidWare.hookUp();
                        driveTime(-AUTO_DRIVE_POWER,0,0,0.18,AUTO_DRIVE_DELAY);
                        driveTime(0,-AUTO_DRIVE_POWER,0,2,AUTO_DRIVE_DELAY);
                        driveTime(-AUTO_DRIVE_POWER, 0, 0, 6, AUTO_DRIVE_DELAY);
                        break;
                    case 1: // Center
                        driveTime(-AUTO_DRIVE_POWER,0,0,1.7,AUTO_DRIVE_DELAY);
                        driveTime(0, 0, AUTO_DRIVE_POWER, 1.78, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 6, AUTO_DRIVE_DELAY);
                        squidWare.clawOpen();
                        squidWare.armUp();
                        break;
                    case 2: // Right
                        driveTime(0, 0, AUTO_DRIVE_POWER, 1.65, AUTO_DRIVE_DELAY);
                        driveTime(0, -AUTO_DRIVE_POWER, 0, 0.45, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 0.25, AUTO_DRIVE_DELAY);
                        squidWare.hookUp();
                        driveTime(-AUTO_DRIVE_POWER, 0, 0, 0.43, AUTO_DRIVE_DELAY);
                        driveTime(0, AUTO_DRIVE_POWER, 0, 1.9, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 6, 0); // Used to be 2.3.
                        break;
                }

            }
        } else { // Blue
            if (route) { // Short Route
                switch (totemLocation) {
                    case 0: // Left
                        driveTime(0, 0, -AUTO_DRIVE_POWER, 1.8, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 0.35, AUTO_DRIVE_DELAY);
                        driveTime(0, AUTO_DRIVE_POWER, 0, 0.17, AUTO_DRIVE_DELAY);
                        sleep(250);
                        squidWare.hookUp();
                        sleep(250);
                        driveTime(-AUTO_DRIVE_POWER, 0, 0, 0.317, AUTO_DRIVE_DELAY);
                        driveTime(0, -AUTO_DRIVE_POWER, 0, 1.67, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 2.5, 0); // Used to be 2.5.
                        break;
                    case 1: // Center
                        driveTime(0, 0, -AUTO_DRIVE_POWER, 1.8, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 2.637, AUTO_DRIVE_DELAY);
                        driveTime(0, -AUTO_DRIVE_POWER, 0, 1.05, 0);
                        break;
                    case 2: // Right
                        driveTime(-AUTO_DRIVE_POWER,0,0,0.5,AUTO_DRIVE_DELAY);
                        driveTime(0, 0, AUTO_DRIVE_POWER, 0.6, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 0.68, AUTO_DRIVE_DELAY);
                        //driveTime(0, -AUTO_DRIVE_POWER, 0, 0.2, AUTO_DRIVE_POWER);
                        squidWare.hookUp();
                        driveTime(-AUTO_DRIVE_POWER, 0, 0, 0.68, AUTO_DRIVE_POWER);
                        driveTime(0, 0, AUTO_DRIVE_POWER, 1.2, AUTO_DRIVE_DELAY);
                        driveTime(-AUTO_DRIVE_POWER, 0, 0, 2.47, AUTO_DRIVE_DELAY);
                        driveTime(0, AUTO_DRIVE_POWER, 0, 1.1, AUTO_DRIVE_DELAY);
                        driveTime(-AUTO_DRIVE_POWER, 0, 0, 0.4, AUTO_DRIVE_DELAY);
                        break;
                }
            } else { // Long Route
                switch (totemLocation) {
                    case 0: // Left
                        driveTime(0, 0, -AUTO_DRIVE_POWER, 1.8, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 0.35, AUTO_DRIVE_DELAY);
                        driveTime(0, AUTO_DRIVE_POWER, 0, 0.17, AUTO_DRIVE_DELAY);
                        sleep(250);
                        squidWare.hookUp();
                        sleep(250);
                        driveTime(-AUTO_DRIVE_POWER, 0, 0, 0.317, AUTO_DRIVE_DELAY);
                        driveTime(0, -AUTO_DRIVE_POWER, 0, 2, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 5, 0); // Used to be 2.5.
                        break;
                    case 1: // Center
                        driveTime(-AUTO_DRIVE_POWER,0,0,1.8,AUTO_DRIVE_DELAY);
                        driveTime(0, 0, -AUTO_DRIVE_POWER, 1.7, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 5.7, AUTO_DRIVE_DELAY);
                        break;
                    case 2: // Right
                        driveTime(-AUTO_DRIVE_POWER,0,0,0.5,AUTO_DRIVE_DELAY);
                        driveTime(0, 0, AUTO_DRIVE_POWER, 0.6, AUTO_DRIVE_DELAY);
                        driveTime(AUTO_DRIVE_POWER, 0, 0, 0.68, AUTO_DRIVE_DELAY);
                        squidWare.hookUp();
                        driveTime(-AUTO_DRIVE_POWER, 0, 0, 0.68, AUTO_DRIVE_POWER);
                        driveTime(0, 0, AUTO_DRIVE_POWER, 1.05, AUTO_DRIVE_DELAY);
                        driveTime(0,AUTO_DRIVE_POWER,0,2,AUTO_DRIVE_DELAY);
                        driveTime(-AUTO_DRIVE_POWER, 0, 0, 5, AUTO_DRIVE_DELAY);
                        break;
                }
            }
        }

//        int totemLocation = driveEncoderTotemMode(-1499, -1522, -1516, -1507);
//        if (totemLocation == 1) {
//            driveEncoder(-1385, -1407, -1401, -1393);
//        }
//        driveEncoder(-1022, -1038, -1033, -1028);
//        if (alliance) { // Red
//            switch (totemLocation) {
//                case 0: // Left
//                    driveEncoder(-2032, 31, -1918, -146);
//                    driveEncoder(-2327, -269, -2217, -443);
//                    squidWare.hookUp();
//                    driveEncoder(-646, 1438, -517, 1247);
//                    driveTime(0, -1, 0, 1.5, 0);
//                    break;
//                case 1: // Center
//                    squidWare.hookUp();
//                    driveEncoder(9, -2121, -269, -1966);
//                    driveEncoder(-1785, -3943, -2084, -3770);
//                    driveEncoder(-3076, -2634, -941, -4965);
//                    break;
//                case 2: // Right
//                    driveEncoder(72, -2164, -195, -2004);
//                    driveEncoder(4, -2233, -264, -2073);
//                    squidWare.hookUp();
//                    driveEncoder(186, -2049, -80, -1890);
//                    driveTime(0, 1, 0, 1.5, AUTO_DRIVE_DELAY);
//                    driveTime(1, 0, 0, 2, 0);
//                    break;
//            }
//        } else { // Blue
//            switch (totemLocation) {
//                case 0: // Left
//                    driveEncoder(-2116, 88, -1871, -52);
//                    driveEncoder(-2184, 19, -1940, -121);
//                    squidWare.hookUp();
//                    driveEncoder(-2002, 203, -1756, 62);
//                    driveTime(0, -1, 0, 1.5, AUTO_DRIVE_DELAY);
//                    driveTime(1, 0, 0, 2, 0);
//                    break;
//                case 1: // Center
//                    squidWare.hookUp();
//                    driveEncoder(-2013, -18, -1931, -87);
//                    driveEncoder(-3807, -1840, -3746, -1891);
//                    driveTime(0, -1, 0, 1.5, 0); // Discrepancy move.
//                    break;
//                case 2: // Right
//                    driveEncoder(-31, -2058, -135, -1969);
//                    driveEncoder(-326, -2358, -434, -2266);
//                    squidWare.hookUp();
//                    driveEncoder(1355, -652, 1266, -576);
//                    driveTime(0, 1, 0, 1.5, 0);
//                    break;
//            }
//        }

        telemetry.addData("Totem Location", totemLocation);
        telemetry.update();
    }

    public void driveTime(double drive, double strafe, double turn, double t, double delay) {
        t += offset;
        drivetrain.drive(drive, strafe, turn);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < t)) {
            idle();
        }
        drivetrain.drive(0, 0, 0);
        sleep((long)(delay * 1000));
    }

    public int driveTimeTotemMode(double drive, double strafe, double turn, double t, double delay) {
        t += offset;
        drivetrain.drive(drive, strafe, turn);
        runtime.reset();
        int totemLocation = 0;
        while (opModeIsActive() && (runtime.seconds() < t)) {
            totemLocation = squidWare.getTotemLocation();
        }
        drivetrain.drive(0, 0, 0);
        sleep((long)(delay * 1000));
        return totemLocation;
    }

    public void driveEncoder(int fl, int fr, int rl, int rr) {
        telemetry.addData("Running To", "%d %d %d %d", fl, fr, rl, rr);
        telemetry.update();

        // I don't have time to implement a better design at the moment.

        DcMotorEx[] motors = {
            drivetrain.base.frontLeft, 
            drivetrain.base.frontRight, 
            drivetrain.base.rearLeft, 
            drivetrain.base.rearRight
        };

//        int[] encoderValues = drivetrain.base.getEncoderCounts();
//        int[] targets = {fl, fr, rl, rr};
//        int[] signs = new int [4];
//        for (int i = 0; i < 4; ++i) {
//            int diff = targets[i] - encoderValues[i];
//            signs[i] = diff > 0 ? 1 : -1;
//            motors[i].setPower(-signs[i] * AUTO_DRIVE_POWER);
//        }
//        boolean[] stopped = new boolean[4];
//        while (opModeIsActive() && !(stopped[0] && stopped[1] && stopped[2] && stopped[3])) {
//            for (int i = 0; i < 4; ++i) {
//                if (signs[i] == -1 && motors[i].getCurrentPosition() < targets[i]) {
//                    motors[i].setPower(0);
//                    stopped[i] = true;
//                } else if (signs[i] == 1 && motors[i].getCurrentPosition() > targets[i]) {
//                    motors[i].setPower(0);
//                    stopped[i] = true;
//                }
//            }
//            encoderPosTelemetry();
//        }

//        motors[0].setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
//        motors[0].setTargetPosition(1000);
//        motors[0].setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
//        motors[0].setPower(0.25);
//        while (motors[0].isBusy()) {
//            encoderPosTelemetry();
//        }
//        motors[0].setPower(0);
//        motors[0].setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        sleep((long)(AUTO_DRIVE_DELAY * 1000));
    }

    public int driveEncoderTotemMode(int fl, int fr, int rl, int rr) {
        telemetry.addData("Running To", "%d %d %d %d", fl, fr, rl, rr);
        telemetry.update();

        // I don't have time to implement a better design at the moment.

        DcMotorEx[] motors = {
            drivetrain.base.frontLeft, 
            drivetrain.base.frontRight, 
            drivetrain.base.rearLeft, 
            drivetrain.base.rearRight
        };

        int totemLocation = 0;
//        int[] encoderValues = drivetrain.base.getEncoderCounts();
//        int[] targets = {fl, fr, rl, rr};
//        int[] signs = new int [4];
//        for (int i = 0; i < 4; ++i) {
//            int diff = targets[i] - encoderValues[i];
//            signs[i] = diff > 0 ? 1 : -1;
//            motors[i].setPower(-signs[i] * AUTO_DRIVE_POWER);
//        }
//        boolean[] stopped = new boolean[4];
//        while (opModeIsActive() && !(stopped[0] && stopped[1] && stopped[2] && stopped[3])) {
//            for (int i = 0; i < 4; ++i) {
//                if (signs[i] == -1 && motors[i].getCurrentPosition() < targets[i]) {
//                    motors[i].setPower(0);
//                    stopped[i] = true;
//                } else if (signs[i] == 1 && motors[i].getCurrentPosition() > targets[i]) {
//                    motors[i].setPower(0);
//                    stopped[i] = true;
//                }
//            }
//            encoderPosTelemetry();
//            totemLocation = squidWare.getTotemLocation();
//        }

        sleep((long)(AUTO_DRIVE_DELAY * 1000));

        return totemLocation;
    }

    private void encoderPosTelemetry() {
        int[] temp = drivetrain.base.getEncoderCounts();
        telemetry.log().add("Initial Encoder Values: %d %d %d %d", temp[0], temp[1], temp[2], temp[3]);
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

    public double float64(double defaultValue, double var, int precision, String caption) {
        boolean autoClear = telemetry.isAutoClear();
        if (autoClear) telemetry.setAutoClear(false);
        double num = defaultValue;
        Telemetry.Item telItem = telemetry.addData(caption, (long) (num * Math.pow(10, precision)) / Math.pow(10, precision));
        while (!gamepad1.a && !isStopRequested()) {
            telItem.setValue((long) (num * Math.pow(10, precision)) / Math.pow(10, precision));
            telemetry.update();
            if (gamepad1.dpad_up) num += var;
            else if (gamepad1.dpad_down) num -= var;
            sleep(DOUBLE_DELAY);
        }
        while (gamepad1.a);
        telItem.setCaption(caption.toUpperCase());
        telemetry.update();
        if (autoClear) telemetry.setAutoClear(true);
        return num;
    }
}
