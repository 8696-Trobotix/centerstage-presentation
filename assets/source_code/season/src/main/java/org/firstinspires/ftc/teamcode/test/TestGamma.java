// package org.firstinspires.ftc.teamcode.test;

// import org.firstinspires.ftc.teamcode.squid.gamma.SquidWare;

// import org.firstinspires.ftc.teamcode.mollusc.drivetrain.*;
// import org.firstinspires.ftc.teamcode.mollusc.utility.*;
// import org.firstinspires.ftc.teamcode.mollusc.Mollusc;

// import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
// import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

// @TeleOp(name="TestGamma", group="Test")

// public class TestGamma extends LinearOpMode {

//     private Configuration config;

//     private Drivetrain drivetrain;
//     private SquidWare squidWare;

//     private double drive, strafe, turn;

//     private UpDownState intakeState = UpDownState.UP;
//     private SystemState spinningState = SystemState.OFF;
//     private UpDownState releaseState = UpDownState.UP;
//     private SystemState intakeMotorState = SystemState.OFF;
//     private SystemState conveyorMotorState = SystemState.OFF;
    
//     @Override
//     public void runOpMode() {
//         telemetry.setAutoClear(false);

//         Mollusc.init(this);

//         boolean fieldCentric = true;
//         try {
//             config = new Configuration("gamma.txt");
//             fieldCentric = Configuration.inputBoolean("Field Centric", "On", "Off", fieldCentric);
//             squidWare = new SquidWare(config, false);
//             if (fieldCentric) {
//                 drivetrain = new MecanumFieldCentric(squidWare.base, squidWare.imu);
//             } else { // Robot centric.
//                 drivetrain = new MecanumRobotCentric(squidWare.base);
//             }
//         } catch (Exception e) {
//             telemetry.log().add(e.getMessage());
//             telemetry.log().add("Exception occurred. See above. Press (A) to terminate.");
//             while (!gamepad1.a && !isStopRequested()) {
//                 idle();
//             }
//             requestOpModeStop();
//         }

//         telemetry.setAutoClear(true);

//         waitForStart();

//         if (!startTest("Configuration")) {
//             return;
//         }
//         telemetry.log().add("Key-value pairs.");
//         for (String key : config.getConfigData().keySet()) {
//             telemetry.log().add(key + " [" + config.getConfigData().get(key) + "]");
//             sleep(250);
//         }
//         telemetry.log().add("Test ended.");

//         while (opModeIsActive()) {

//             PID.bulkMode();
//             telemetry.log().add("Press right bumper to stop.");
//             if (!startTest("Dead Wheels")) {
//                 return;
//             }
//             while (!gamepad1.right_bumper && opModeIsActive()) {
//                 double[] values = {
//                         squidWare.deadWheels.left.getDisplacement(),
//                         squidWare.deadWheels.right.getDisplacement(),
//                         squidWare.deadWheels.center.getDisplacement()
//                 };
//                 telemetry.log().add(String.format("left = %.2f, right = %.2f, center = %.2f", values[0], values[1], values[2]));
//             }
//             telemetry.log().add("Test ended.");

//             if (!startTest("Driving")) {
//                 return;
//             }
//             telemetry.log().add("Press right bumper to stop.");
//             while (!gamepad1.right_bumper && opModeIsActive()) {
//                 // Quadratic controller sensitivity.
//                 drive  = Controls.quadratic(-gamepad1.left_stick_y);
//                 strafe = Controls.quadratic(gamepad1.left_stick_x);
//                 turn   = Controls.quadratic(gamepad1.right_stick_x);

//                 drivetrain.drive(drive, strafe, turn);

//                 telemetry.log().add(squidWare.deadWheels.pose.toString());
//                 squidWare.deadWheels.update();
//             }
//             telemetry.log().add("Test ended.");

//             if (!startTest("Intake system.")) {
//                 return;
//             }
//             telemetry.log().add("Press right bumper to stop.");
//             while (!gamepad1.right_bumper && opModeIsActive()) {
//                 // Intake system.
//                 if (Controls.singlePress("intake servo", gamepad1.a)) {
//                     if (intakeState == UpDownState.UP) {
//                         telemetry.log().add("Setting up.");
//                         intakeState = UpDownState.DOWN;
//                         squidWare.intake.presetDown();
//                     } else {
//                         telemetry.log().add("Setting down.");
//                         intakeState = UpDownState.UP;
//                         squidWare.intake.presetUp();
//                     }
//                 } else if (Controls.spacedHold("intake servo up", gamepad1.dpad_up, 0.05)) {
//                     telemetry.log().add("Space up.");
//                     squidWare.intake.up();
//                 } else if (Controls.spacedHold("intake servo down", gamepad1.dpad_down, 0.05)) {
//                     telemetry.log().add("Space down.");
//                     squidWare.intake.down();
//                 }
//                 if (Controls.singlePress("intake motor", gamepad1.b)) {
//                     if (intakeMotorState == SystemState.OFF) {
//                         telemetry.log().add("ON");
//                         intakeMotorState = SystemState.ON;
//                         squidWare.intake.power(squidWare.intake.PRESET_POWER);
//                     } else {
//                         telemetry.log().add("OFF");
//                         intakeMotorState = SystemState.OFF;
//                         squidWare.intake.power(0);
//                     }
//                 } else if (intakeMotorState == SystemState.OFF) {
//                     squidWare.intake.power(gamepad1.right_stick_x);
//                 }
//             }
//             telemetry.log().add("Test ended.");

//             if (!startTest("Internal transfer system.")) {
//                 return;
//             }
//             telemetry.log().add("Press right bumper to stop.");
//             while (!gamepad1.right_bumper && opModeIsActive()) {
//                 // Internal transfer system.
//                 if (Controls.singlePress("conveyor motor", gamepad1.y)) {
//                     if (conveyorMotorState == SystemState.OFF) {
//                         conveyorMotorState = SystemState.ON;
//                         squidWare.conveyor.power(squidWare.conveyor.PRESET_POWER);
//                     } else {
//                         conveyorMotorState = SystemState.OFF;
//                         squidWare.conveyor.power(0);
//                     }
//                 } else if (conveyorMotorState == SystemState.OFF) {
//                     squidWare.conveyor.power(-gamepad1.right_stick_y);
//                 }
//             }
//             telemetry.log().add("Test ended.");

//             if (!startTest("Scoring system slide and spinners.")) {
//                 return;
//             }
//             telemetry.log().add("Press right bumper to stop.");
//             while (!gamepad1.right_bumper && opModeIsActive()) {
//                 // Scoring system.
//                 // Linear slide.
//                 squidWare.scoring.slidePower(gamepad1.left_stick_y);
//                 // Spinning servos.
//                 if (Controls.singlePress("spinning", gamepad1.x)) {
//                     if (spinningState == SystemState.ON) {
//                         spinningState = SystemState.OFF;
//                         squidWare.scoring.spinningOff();
//                     } else {
//                         spinningState = SystemState.ON;
//                         squidWare.scoring.spinningOn();
//                     }
//                 }
//             }
//             telemetry.log().add("Test ended.");

//             if (!startTest("Scoring system angler and release.")) {
//                 return;
//             }
//             telemetry.log().add("Press right bumper to stop.");
//             while (!gamepad1.right_bumper && opModeIsActive()) {
//                 // Scoring system.
//                 // Angler.
//                 // if (Controls.singlePress("angler up", gamepad1.dpad_up)) {
//                 //     squidWare.scoring.anglerUp();
//                 // } else if (Controls.singlePress("angler down", gamepad1.dpad_down)) {
//                 //     squidWare.scoring.anglerDown();
//                 // }
//                 // Release.
//                 if (Controls.singlePress("release", gamepad1.a)) {
//                     if (releaseState == UpDownState.UP) {
//                         releaseState = UpDownState.DOWN;
//                         squidWare.scoring.releaseReset();
//                     } else {
//                         releaseState = UpDownState.UP;
//                         squidWare.scoring.releasePixel();
//                     }
//                 }
//             }
//             telemetry.log().add("Test ended.");
//         }
//         Mollusc.deinit();
//     }

//     private enum UpDownState {
//         UP, DOWN
//     }

//     private enum SystemState {
//         ON, OFF
//     }

//     private boolean startTest(String name) {
//         telemetry.log().add("TEST: " + name + " | Press left bumper to begin.");
//         while (!gamepad1.left_bumper && !isStopRequested()) {
//             idle();
//         }
//         while (gamepad1.left_bumper) {
//             idle();
//         }
//         sleep(250);
//         if (isStopRequested()) {
//             return false;
//         }
//         return true;
//     }
// }
