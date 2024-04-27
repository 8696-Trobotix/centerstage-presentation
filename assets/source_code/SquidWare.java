package org.firstinspires.ftc.teamcode.squid.delta;

import org.firstinspires.ftc.teamcode.squid.delta.subsystems.*;
import org.firstinspires.ftc.teamcode.mollusc.auto.odometry.*;
import org.firstinspires.ftc.teamcode.mollusc.drivetrain.*;
import org.firstinspires.ftc.teamcode.mollusc.wrapper.*;
import org.firstinspires.ftc.teamcode.mollusc.utility.*;
import org.firstinspires.ftc.teamcode.mollusc.Mollusc;

// import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.IMU;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

public class SquidWare {

    public Configuration config;

    public DrivetrainBaseFourWheel base;

    // Deadwheels.
    public DeadWheels deadWheels;

    public Intake intake = new Intake();
    public Conveyor conveyor = new Conveyor();
    public Scoring scoring = new Scoring();
    public Launcher launcher = new Launcher();
    public Lift lift = new Lift();

    public IMU imu;

    public SquidWare(Configuration config, boolean resetIMU) throws Exception {
        this.config = config;

        intake.UP_1 = intake.intakePos1 = config.getDouble("intake servo up 1");
        intake.UP_2 = intake.intakePos2 = config.getDouble("intake servo up 2");
        intake.DOWN_1 = config.getDouble("intake servo down 1");
        intake.DOWN_2 = config.getDouble("intake servo down 2");
        intake.MAX_DOWN_1 = config.getDouble("intake servo 1 max down");
        intake.MAX_DOWN_2 = config.getDouble("intake servo 2 max down");
        intake.DELTA = config.getDouble("intake servo delta");
        intake.PRESET_POWER = config.getDouble("intake power");

        conveyor.PRESET_POWER = config.getDouble("conveyor power");
        conveyor.DISTANCE_THRESHOLD = config.getDouble("pixel distance threshold cm");
        conveyor.PIXEL_TIME = config.getDouble("pixel time seconds");
        conveyor.CARRY_TIME = config.getDouble("pixel carry time seconds");
        
        scoring.SPINNER_POWER = config.getDouble("spinning servo powers");
        scoring.SLIDE_MIN = config.getInteger("slide min");
        scoring.SLIDE_MAX = config.getInteger("slide max");
        scoring.RELEASE_PIXEL = config.getDouble("release go");
        scoring.RELEASE_RESET = config.getDouble("release reset");
        scoring.ANGLER_DELTA = config.getDouble("angling servo delta");

        launcher.FIRE = config.getDouble("launcher fire");

        lift.POWER = config.getDouble("lift power");

        Make make = new Make();

        imu = make.imu(
            "imu",
            RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
            RevHubOrientationOnRobot.UsbFacingDirection.BACKWARD,
            resetIMU
        );

        base = new DrivetrainBaseFourWheel(
            make.motor("frontLeft", getStringConfigDirection("frontLeft direction").motor), 
            make.motor("frontRight", getStringConfigDirection("frontRight direction").motor), 
            make.motor("rearLeft", getStringConfigDirection("rearLeft direction").motor), 
            make.motor("rearRight", getStringConfigDirection("rearRight direction").motor)
        );

        intake.servo1 = make.servo("intakeServo1", getStringConfigDirection("intake servo 1 direction").servo);
        intake.servo2 = make.servo("intakeServo2", getStringConfigDirection("intake servo 2 direction").servo);
        intake.motor = make.motor("intake", getStringConfigDirection("intake motor direction").motor);
        
        conveyor.motor = make.motor("conveyor", getStringConfigDirection("conveyor motor direction").motor);
        conveyor.sensor = Mollusc.instance().hardwareMap.get(DistanceSensor.class, "pixelSensor");

        scoring.angler = make.servo("angler", getStringConfigDirection("angling servo direction").servo);
        scoring.slide = make.motor("slide", getStringConfigDirection("slide motor direction").motor);
        scoring.slideEncoder = new Encoder(scoring.slide, 1, 1, 1);
        // scoring.limitSwitch = Mollusc.opMode.hardwareMap.get(TouchSensor.class, "limitSwitch");
        scoring.spinner1 = make.crservo("spinner1", getStringConfigDirection("spinner servo 1 direction").crservo);
        scoring.spinner2 = make.crservo("spinner2", getStringConfigDirection("spinner servo 2 direction").crservo);
        scoring.stopper = make.servo("release", getStringConfigDirection("release servo direction").servo);

        launcher.servo = make.crservo("launcher", CRServo.Direction.FORWARD);

        int TPR = config.getInteger("TPR");
        DcMotorEx leftEncoderMotor = make.motor("leftEncoder", DcMotorEx.Direction.FORWARD);
        Encoder leftEncoder = new Encoder(leftEncoderMotor, -1.0, TPR, 35);
        Encoder rightEncoder = new Encoder(intake.motor, 1.0, TPR, 35);
        Encoder centerEncoder = new Encoder(conveyor.motor, 1.0, TPR, 35);
        deadWheels = new DeadWheels(
            new Pose(0, 0, Math.PI),
            leftEncoder, rightEncoder, centerEncoder, 
            -352.435, 30
        );

        // lift.motor = make.motor("lift", DcMotorEx.Direction.FORWARD);
        lift.motor = leftEncoderMotor;
    }

    public DirectionGroup getStringConfigDirection(String key) throws Exception {
        String direction = config.getString(key);
        return new DirectionGroup(direction);
    }

    public class DirectionGroup {
        public DcMotorEx.Direction motor;
        public Servo.Direction servo;
        public CRServo.Direction crservo;

        public DirectionGroup(String direction) throws Exception {
            if (!direction.equals("forward") && !direction.equals("reverse")) {
                throw new Exception("Invalid motor direction value: " + direction);
            }
            if (direction.equals("forward")) {
                this.motor = DcMotorEx.Direction.FORWARD;
                this.servo = Servo.Direction.FORWARD;
                this.crservo = CRServo.Direction.FORWARD;
            } else {
                this.motor = DcMotorEx.Direction.REVERSE;
                this.servo = Servo.Direction.REVERSE;
                this.crservo = CRServo.Direction.REVERSE;
            }
        }
    }
}
