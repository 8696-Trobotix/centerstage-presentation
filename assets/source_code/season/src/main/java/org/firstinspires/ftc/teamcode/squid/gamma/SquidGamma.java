/*
SquidGamma

Main robot.
*/

package org.firstinspires.ftc.teamcode.squid.gamma;

import org.firstinspires.ftc.teamcode.squid.gamma.mollusc.drivetrain.*;
import org.firstinspires.ftc.teamcode.squid.gamma.mollusc.utility.*;
import org.firstinspires.ftc.teamcode.squid.gamma.mollusc.wrapper.*;
import org.firstinspires.ftc.teamcode.squid.gamma.mollusc.Mollusc;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="SquidGamma XII-XX", group="Squid")

public class SquidGamma extends MolluscLinearOpMode {

    private Configuration config;

    private Drivetrain drivetrain;
    private SquidWare squidWare;

    private MecanumFieldCentric fieldCentricDrive;
    private MecanumRobotCentric robotCentricDrive;
    private double maxDrive, maxStrafe, maxTurn;
    private double maxDriveReduced, maxStrafeReduced, maxTurnReduced;
    private double drive, strafe, turn;

    private UpDownState intakeState = UpDownState.UP;
    private SystemState intakeMotorState = SystemState.OFF;
    private SystemState conveyorMotorState = SystemState.OFF;
    private SystemState spinningState = SystemState.OFF;
    private UpDownState launchState = UpDownState.DOWN;
    private SystemState gState = SystemState.OFF;
    private SystemState reducedDrivePowerState = SystemState.OFF;

    @Override
    public void molluscRunOpMode() {
        telemetry.setAutoClear(false);

        Configuration.handle(() -> {
            config = new Configuration("gamma.txt");
            boolean fieldCentric = config.getBoolean("field centric on");
            maxDrive = config.getDouble("drive power max");
            maxStrafe = config.getDouble("strafe power max");
            maxTurn = config.getDouble("turn power max");
            maxDriveReduced = config.getDouble("drive power max reduced");
            maxStrafeReduced = config.getDouble("strafe power max reduced");
            maxTurnReduced = config.getDouble("turn power max reduced");

            squidWare = new SquidWare(config, false);

            fieldCentricDrive = new MecanumFieldCentric(squidWare.base, squidWare.imu);
            fieldCentricDrive.setDriveParams(maxDrive, maxStrafe, maxTurn, Math.PI);
            robotCentricDrive = new MecanumRobotCentric(squidWare.base) {
                @Override
                public void drive(double drive, double strafe, double turn) {
                    super.drive(-drive, -strafe, turn); // Because it starts backwards.
                }
            };
            robotCentricDrive.setDriveParams(maxDrive, maxStrafe, maxTurn);
            if (fieldCentric) {
                drivetrain = fieldCentricDrive;
                telemetry.speak("Driving field centric.");
            } else {
                drivetrain = robotCentricDrive;
                telemetry.speak("Driving robot centric.");
            }
        });

        telemetry.setAutoClear(true);

        waitForStart();

        squidWare.intake.presetUp();
        squidWare.scoring.anglerPosition(squidWare.scoring.anglerPos);

        while (opModeIsActive()) {

            // Quadratic controller sensitivity.
            drive  = Controls.quadratic(-gamepad1.left_stick_y);
            strafe = Controls.quadratic(gamepad1.left_stick_x);
            turn   = Controls.quadratic(gamepad1.right_stick_x);

            drivetrain.drive(drive, strafe, turn);

            if (Controls.singlePress("toggle drive", gamepad1.x)) {
                if (drivetrain instanceof MecanumFieldCentric) {
                    drivetrain = robotCentricDrive;
                } else {
                    drivetrain = fieldCentricDrive;
                }
            }
            if (Controls.singlePress("toggle reduce", gamepad1.b)) {
                if (reducedDrivePowerState == SystemState.OFF) {
                    reducedDrivePowerState = SystemState.ON;
                    fieldCentricDrive.setDriveParams(maxDriveReduced, maxStrafeReduced, maxTurnReduced, Math.PI);
                    robotCentricDrive.setDriveParams(maxDriveReduced, maxStrafeReduced, maxTurnReduced);
                } else {
                    reducedDrivePowerState = SystemState.OFF;
                    fieldCentricDrive.setDriveParams(maxDrive, maxStrafe, maxTurn, Math.PI);
                    robotCentricDrive.setDriveParams(maxDrive, maxStrafe, maxTurn);
                }
            }

            // Intake system.
            if (Controls.singlePress("intake servo", gamepad2.b)) {
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
            if (Controls.singlePress("intake motor", gamepad2.y)) {
                if (intakeMotorState == SystemState.OFF) {
                    intakeMotorState = SystemState.ON;
                    squidWare.intake.power(squidWare.intake.PRESET_POWER);
                } else {
                    intakeMotorState = SystemState.OFF;
                    squidWare.intake.power(0);
                }
            } else if (intakeMotorState == SystemState.OFF || Math.abs(gamepad2.right_stick_y) > 0) {
                intakeMotorState = SystemState.OFF;
                squidWare.intake.power(-gamepad2.right_stick_y);
            }

            // Internal transfer system.
            if (Controls.singlePress("conveyor motor out", gamepad2.dpad_left)) {
                if (conveyorMotorState == SystemState.OFF) {
                    conveyorMotorState = SystemState.ON;
                    squidWare.conveyor.power(-squidWare.conveyor.PRESET_POWER);
                } else {
                    conveyorMotorState = SystemState.OFF;
                    squidWare.conveyor.power(0);
                }
            } else if (Controls.singlePress("conveyor motor in", gamepad2.dpad_right)) {
                if (conveyorMotorState == SystemState.OFF) {
                    conveyorMotorState = SystemState.ON;
                    squidWare.conveyor.power(squidWare.conveyor.PRESET_POWER);
                } else {
                    conveyorMotorState = SystemState.OFF;
                    squidWare.conveyor.power(0);
                }
            }
            if (conveyorMotorState == SystemState.OFF || Math.abs(gamepad2.right_stick_x) > 0) {
                conveyorMotorState = SystemState.OFF;
                squidWare.conveyor.power(gamepad2.right_stick_x);
            }

            // Scoring system.
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
            // Linear slide.
            squidWare.scoring.slidePower(-gamepad2.left_stick_y);
            // Angler.
            if (Controls.spacedHold("angler servo up", gamepad2.right_trigger > 0, 0.01)) {
                squidWare.scoring.anglerUp();
            } else if (Controls.spacedHold("angler servo down", gamepad2.left_trigger > 0, 0.01)) {
                squidWare.scoring.anglerDown();
            }
            // Release.
            if (gamepad1.left_bumper) {
                squidWare.scoring.releaseReset();
            } else if (gamepad1.right_bumper) {
                squidWare.scoring.releasePixel();
            }
            // Launch.
            if (Controls.singlePress("launcher", gamepad1.y)) {
                if (launchState == UpDownState.DOWN) {
                    launchState = UpDownState.UP;
                    squidWare.launcher.fire();
                } else {
                    launchState = UpDownState.DOWN;
                    squidWare.launcher.stop();
                }
            }

            // Combo.
            if (Controls.singlePress("g state", gamepad2.a)) {
                if (gState == SystemState.OFF) {
                    gState = SystemState.ON;
                    intakeState = UpDownState.DOWN;
                    intakeMotorState = SystemState.ON;
                    conveyorMotorState = SystemState.ON;
                    squidWare.intake.presetDown();
                    squidWare.intake.power(squidWare.intake.PRESET_POWER);
                    squidWare.conveyor.power(squidWare.conveyor.PRESET_POWER);
                } else {
                    gState = SystemState.OFF;
                    intakeState = UpDownState.UP;
                    intakeMotorState = SystemState.OFF;
                    conveyorMotorState = SystemState.OFF;
                    squidWare.intake.presetUp();
                    squidWare.intake.power(0);
                    squidWare.conveyor.power(0);
                }
            }
        }
    }

    private enum UpDownState {
        UP, DOWN
    }

    private enum SystemState {
        ON, OFF
    }
}
