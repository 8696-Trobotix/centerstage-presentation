/*
SquidGamma

Main robot.
*/

package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.mollusc.drivetrain.*;
import org.firstinspires.ftc.teamcode.mollusc.utility.*;
import org.firstinspires.ftc.teamcode.mollusc.Mollusc;
import org.firstinspires.ftc.teamcode.squid.delta.SquidWare;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="Dead Wheel Values", group="Test")

public class DeadWheelValues extends LinearOpMode {

    private Configuration config;

    private Drivetrain drivetrain;
    private SquidWare squidWare;

    private double drive, strafe, turn;

    private UpDownState intakeState = UpDownState.UP;
    private SystemState intakeMotorState = SystemState.OFF;
    private SystemState conveyorMotorState = SystemState.OFF;
    private SystemState spinningState = SystemState.OFF;
    private UpDownState releaseState = UpDownState.UP;
    private UpDownState launchState = UpDownState.DOWN;

    @Override
    public void runOpMode() {
        telemetry.setAutoClear(false);

        Mollusc.init(this);

        try {
            config = new Configuration("gamma.txt");
            boolean fieldCentric = config.getBoolean("field centric on");
            squidWare = new SquidWare(config, false);
            if (fieldCentric) {
                MecanumFieldCentric fieldCentricDrive = new MecanumFieldCentric(squidWare.base, squidWare.imu);
                fieldCentricDrive.setDriveParams(1.0, 1.1, 1, Math.PI);
                drivetrain = fieldCentricDrive;
            } else { // Robot centric.
                drivetrain = new MecanumRobotCentric(squidWare.base);
            }
        } catch (Exception e) {
            telemetry.log().add(e.getMessage());
            telemetry.log().add("Exception occurred. See above. Press (A) to terminate.");
            while (!gamepad1.a && !isStopRequested()) {
                idle();
            }
            requestOpModeStop();
        }

        telemetry.setAutoClear(true);

        waitForStart();

        while (opModeIsActive()) {

            // Quadratic controller sensitivity.
            drive  = Controls.quadratic(-gamepad1.left_stick_y);
            strafe = Controls.quadratic(gamepad1.left_stick_x);
            turn   = Controls.quadratic(gamepad1.right_stick_x);

            drivetrain.drive(drive, strafe, turn);

            // Intake system.
            if (Controls.singlePress("intake servo", gamepad2.a)) {
                if (intakeState == UpDownState.UP) {
                    intakeState = UpDownState.DOWN;
                    squidWare.intake.presetDown();
                } else {
                    intakeState = UpDownState.UP;
                    squidWare.intake.presetUp();
                }
            } else if (Controls.spacedHold("intake servo up", gamepad2.dpad_up, 0.05)) {
                squidWare.intake.up();
            } else if (Controls.spacedHold("intake servo down", gamepad2.dpad_down, 0.05)) {
                squidWare.intake.down();
            }
            if (Controls.singlePress("intake motor", gamepad2.b)) {
                if (intakeMotorState == SystemState.OFF) {
                    intakeMotorState = SystemState.ON;
                    squidWare.intake.power(squidWare.intake.PRESET_POWER);
                } else {
                    intakeMotorState = SystemState.OFF;
                    squidWare.intake.power(0);
                }
            } else if (intakeMotorState == SystemState.OFF) {
                squidWare.intake.power(gamepad2.right_stick_x);
            }

            // Internal transfer system.
            if (Controls.singlePress("conveyor motor", gamepad2.y)) {
                if (conveyorMotorState == SystemState.OFF) {
                    conveyorMotorState = SystemState.ON;
                    squidWare.conveyor.power(squidWare.conveyor.PRESET_POWER);
                } else {
                    conveyorMotorState = SystemState.OFF;
                    squidWare.conveyor.power(0);
                }
            } else if (conveyorMotorState == SystemState.OFF) {
                squidWare.conveyor.power(-gamepad2.right_stick_y);
            }

            // Scoring system.
            // Linear slide.
            squidWare.scoring.slidePower(gamepad2.left_stick_y);
            // Spinning servos.
            if (Controls.singlePress("spinning", gamepad2.x)) {
                if (spinningState == SystemState.ON) {
                    spinningState = SystemState.OFF;
                    squidWare.scoring.spinningOff();
                } else {
                    spinningState = SystemState.ON;
                    squidWare.scoring.spinningOn();
                }
            }
            // // Angler.
            // if (Controls.singlePress("angler up", gamepad1.dpad_up)) {
            //     squidWare.scoring.anglerUp();
            // } else if (Controls.singlePress("angler down", gamepad1.dpad_down)) {
            //     squidWare.scoring.anglerDown();
            // }
            // Release.
            if (Controls.singlePress("release", gamepad1.a)) {
                if (releaseState == UpDownState.UP) {
                    releaseState = UpDownState.DOWN;
                    squidWare.scoring.releaseReset();
                } else {
                    releaseState = UpDownState.UP;
                    squidWare.scoring.releasePixel();
                }
            }
            // Launch.
            if (Controls.singlePress("launcher", gamepad1.b)) {
                if (launchState == UpDownState.DOWN) {
                    launchState = UpDownState.UP;
                    squidWare.launcher.fire();
                } else {
                    launchState = UpDownState.DOWN;
                    squidWare.launcher.stop();
                }
            }

            telemetry.addData("Position", squidWare.deadWheels.pose);
            telemetry.update();
            squidWare.deadWheels.update();
        }
        Mollusc.deinit();
    }

    private enum UpDownState {
        UP, DOWN
    }

    private enum SystemState {
        ON, OFF
    }
}
