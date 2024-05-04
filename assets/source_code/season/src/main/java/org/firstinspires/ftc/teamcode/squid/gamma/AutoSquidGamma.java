/*
AutoSquidGamma

Main robot.
*/

package org.firstinspires.ftc.teamcode.squid.gamma;

import org.firstinspires.ftc.teamcode.squid.gamma.pipelines.TotemPipeline;

import org.firstinspires.ftc.teamcode.squid.gamma.mollusc.utility.*;
import org.firstinspires.ftc.teamcode.squid.gamma.mollusc.wrapper.*;
import org.firstinspires.ftc.teamcode.squid.gamma.mollusc.Mollusc;
import org.firstinspires.ftc.teamcode.squid.gamma.mollusc.auto.*;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCamera;

@Autonomous(name="AutoSquidGamma I-II", group="Squid")

public class AutoSquidGamma extends MolluscLinearOpMode {

    private Configuration config;

    private Auto auto;
    private SquidWare squidWare;
    private Interpreter interpreter;
    private TotemPipeline totemPipeline;

    private boolean alliance = true, route = true;

    @Override
    public void molluscRunOpMode() {
        telemetry.setAutoClear(false);

        Configuration.handle(() -> {
            config = new Configuration("gamma.txt");
            alliance = Configuration.inputBoolean("Alliance", "Red", "Blue", alliance);
            route = Configuration.inputBoolean("Route", "Short", "Long", route);
            squidWare = new SquidWare(config, true);
            interpreter = new Interpreter(new Asset("script_gamma.txt"));

            register(); // Important.

            double maxDrivePower = config.getDouble("autonomous drive power");
            auto = new MecanumAutoII(
                squidWare.base, 
                squidWare.deadWheels, 
                interpreter, 
                new PID(0.005, 0, 0, 0.15, 1),
                new PID(0.009, 0, 0, 0.2, 1),
                new PID(1.4, 0.00, 0.00, 0.15, 1),
                maxDrivePower, 
                30,
                6
            );
            auto.register();
        });

        telemetry.setAutoClear(true);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        OpenCvCamera webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode) {
            }
        });
        totemPipeline = new TotemPipeline(alliance ? TotemPipeline.Alliance.RED : TotemPipeline.Alliance.BLUE);
        webcam.setPipeline(totemPipeline);

        PID.bulkMode();

        // Pre-start manual image processing calibrations.
        while (!gamepad1.dpad_up && !isStopRequested()) {
            telemetry.addLine("Press dpad up to confirm.");
            telemetry.addLine("(A) --> Configure threshold.");
            telemetry.addLine("(X) --> Configure Red 1.");
            telemetry.addLine("(Y) --> Configure Red 2.");
            telemetry.addLine("(B) --> Configure Blue.");
            displayTotemZone();
            telemetry.update();

            if (gamepad1.a) {
                configDoubleNonBlocking(totemPipeline.threshold, 0.001, (double value) -> {
                    totemPipeline.threshold = value;
                    telemetry.addData("Threshold", value);
                });
            } else if (gamepad1.x) {
                Configuration.handle(() -> {
                    boolean ub = Configuration.inputBoolean("Bound", "Upper", "Lower", true);
                    int idx = Configuration.inputInteger("Value", 0, 0.25, 1, new String[] {"Hue", "Saturation", "Brightness"});
                    if (ub) {
                        configDoubleNonBlocking(totemPipeline.upperRed.val[idx], 1, (double value) -> {
                            totemPipeline.setBound(totemPipeline.upperRed, idx, value);
                            telemetry.addData("Upper Red Color Bound", totemPipeline.upperRed);
                        });
                    } else {
                        configDoubleNonBlocking(totemPipeline.lowerRed.val[idx], 1, (double value) -> {
                            totemPipeline.setBound(totemPipeline.lowerRed, idx, value);
                            telemetry.addData("Lower Red Color Bound", totemPipeline.lowerRed);
                        });
                    }
                });
            } else if (gamepad1.y) {
                Configuration.handle(() -> {
                    boolean ub = Configuration.inputBoolean("Bound", "Upper", "Lower", true);
                    int idx = Configuration.inputInteger("Value", 0, 0.25, 1, new String[] {"Hue", "Saturation", "Brightness"});
                    if (ub) {
                        configDoubleNonBlocking(totemPipeline.upperRed2.val[idx], 1, (double value) -> {
                            totemPipeline.setBound(totemPipeline.upperRed2, idx, value);
                            telemetry.addData("Upper Red Color Bound 2", totemPipeline.upperRed2);
                        });
                    } else {
                        configDoubleNonBlocking(totemPipeline.lowerRed2.val[idx], 1, (double value) -> {
                            totemPipeline.setBound(totemPipeline.lowerRed2, idx, value);
                            telemetry.addData("Lower Red Color Bound 2", totemPipeline.lowerRed2);
                        });
                    }
                });
            } else if (gamepad1.b) {
                Configuration.handle(() -> {
                    boolean ub = Configuration.inputBoolean("Bound", "Upper", "Lower", true);
                    int idx = Configuration.inputInteger("Value", 0, 0.25, 1, new String[] {"Hue", "Saturation", "Brightness"});
                    if (ub) {
                        configDoubleNonBlocking(totemPipeline.upperBlue.val[idx], 1, (double value) -> {
                            totemPipeline.setBound(totemPipeline.upperBlue, idx, value);
                            telemetry.addData("Upper Blue Color Bound", totemPipeline.upperBlue);
                        });
                    } else {
                        configDoubleNonBlocking(totemPipeline.lowerBlue.val[idx], 1, (double value) -> {
                            totemPipeline.setBound(totemPipeline.lowerBlue, idx, value);
                            telemetry.addData("Lower Blue Color Bound", totemPipeline.lowerBlue);
                        });
                    }
                });
            }

            idle();
        }

        totemPipeline.calibrate = false;
        telemetry.addLine("Waiting for start.");
        telemetry.update();

        waitForStart();

        webcam.pauseViewport();

        Configuration.handle(() -> {
            interpreter.run(true);
        });
    }

    private void register() {
        interpreter.register("GoToTotemSection", (Object[] args) -> {
            String delimiter = alliance ? "Red" : "Blue";
            switch (totemPipeline.getZone()) {
                case CENTER:
                    interpreter.advanceTo("SectionTotemCenter" + delimiter);
                    break;
                case LEFT:
                    interpreter.advanceTo("SectionTotemLeft" + delimiter);
                    break;
                case RIGHT:
                    interpreter.advanceTo("SectionTotemRight" + delimiter);
                    break;
            }
        });
        interpreter.register("SectionTotemCenterRed", Interpreter.noneAction);
        interpreter.register("SectionTotemLeftRed", Interpreter.noneAction);
        interpreter.register("SectionTotemRightRed", Interpreter.noneAction);
        interpreter.register("SectionTotemCenterBlue", Interpreter.noneAction);
        interpreter.register("SectionTotemLeftBlue", Interpreter.noneAction);
        interpreter.register("SectionTotemRightBlue", Interpreter.noneAction);

        interpreter.register("GoToShortOrLongRoute", (Object[] args) -> {
            if (route) {
                interpreter.advanceTo("ShortRoute");
            } else {
                interpreter.advanceTo("LongRoute");
            }
        });
        interpreter.register("ShortRoute", Interpreter.noneAction);
        interpreter.register("LongRoute", Interpreter.noneAction);

        interpreter.register("intake", (Object[] args) -> {
            switch ((String)args[0]) {
                case "up":
                    squidWare.intake.presetUp();
                    break;
                case "down":
                    squidWare.intake.presetDown();
                    break;
                case "on":
                    squidWare.intake.power(squidWare.intake.PRESET_POWER);
                    break;
                case "reverse":
                    squidWare.intake.power(-squidWare.intake.PRESET_POWER);
                    break;
                case "off":
                    squidWare.intake.power(0);
                    break;
            }
        }, String.class);
        
        interpreter.register("itsPower", (Object[] args) -> {
            squidWare.conveyor.power((Double)args[0]);
        }, Double.class);
        interpreter.register("slidePower", (Object[] args) -> {
            squidWare.scoring.slidePower((Double)args[0]);
            interpreter.getActions().get("wait_seconds-f").execute(new Object[] {args[1]});
            squidWare.scoring.slidePower(0);
        }, Double.class, Double.class);

        interpreter.register("setAnglerPos", (Object[] args) -> {
            squidWare.scoring.anglerPosition((Double)args[0]);
        }, Double.class);

        interpreter.register("scoring", (Object[] args) -> {
            switch ((String)args[0]) {
                case "release":
                    squidWare.scoring.releasePixel();
                    break;
                case "close":
                    squidWare.scoring.releaseReset();
                    break;
            }
        }, String.class);

        interpreter.register("pause", (Object[] args) -> {
            while (!gamepad1.a && !isStopRequested()) {
                idle();
            }
        });

        interpreter.register("GoToEnd", (Object[] args) -> {
            interpreter.advanceTo("END");
        });
        interpreter.register("END", Interpreter.noneAction);
    }

    public void displayTotemZone() {
        telemetry.addData("Totem Zone", totemPipeline.getZone());
    }

    private void configDoubleNonBlocking(double value, double delta, Synchronous synchronous) {
        while (gamepad1.a && !isStopRequested()) {
            idle();
        }
        while (!gamepad1.a && !isStopRequested()) {
            if (gamepad1.dpad_up) {
                value += delta;
                sleep(250);
            } else if (gamepad1.dpad_down) {
                value -= delta;
                sleep(250);
            } else if (gamepad1.dpad_left) {
                delta /= 10;
                sleep(250);
            } else if (gamepad1.dpad_right) {
                delta *= 10;
                sleep(250);
            }
            synchronous.process(value);
            displayTotemZone();
            telemetry.addLine("Press (A) to confirm.");
            telemetry.addData("Value", value);
            telemetry.addData("Delta", delta);
            telemetry.update();
            idle();
        }
        while (gamepad1.a && !isStopRequested()) {
            idle();
        }
    }

    @FunctionalInterface
    private interface Synchronous {
        void process(double value);
    }
}
