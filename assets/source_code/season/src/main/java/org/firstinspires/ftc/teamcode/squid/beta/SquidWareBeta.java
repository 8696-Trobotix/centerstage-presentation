package org.firstinspires.ftc.teamcode.squid.beta;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class SquidWareBeta {

    public final double HOOK_UP = 0, HOOK_DOWN = 1;
    public final double ARM_UP = 0.3, ARM_DOWN = 0.9;
    public final double CLAW_OPEN = 0.3, CLAW_CLOSE = 0.65;
    public final double LAUNCHER_FIRE = 1, LAUNCHER_RESET = 0;
    public final double TOTEM_THRESHOLD = 10;

    public Servo hook, arm, claw, launcher;
    public DistanceSensor sensor1, sensor2;

    public SquidWareBeta(HardwareMap hardwareMap, String hookName, String armName, String clawName, String launcherName, String sensor1Name, String sensor2Name) {
        hook = hardwareMap.get(Servo.class, hookName);
        arm = hardwareMap.get(Servo.class, armName);
        claw = hardwareMap.get(Servo.class, clawName);
        launcher = hardwareMap.get(Servo.class, launcherName);
        sensor1 = hardwareMap.get(DistanceSensor.class, sensor1Name);
        sensor2 = hardwareMap.get(DistanceSensor.class, sensor2Name);
    }

    public void hookUp() {
        hook.setPosition(HOOK_UP);
    }
    public void hookDown() {
        hook.setPosition(HOOK_DOWN);
    }

    public void armUp() {
        arm.setPosition(ARM_UP);
    }
    public void armDown() {
        arm.setPosition(ARM_DOWN);
    }

    public void clawOpen() {
        claw.setPosition(CLAW_OPEN);
    }
    public void clawClose() {
        claw.setPosition(CLAW_CLOSE);
    }

    public void launcherFire() {
        launcher.setPosition(LAUNCHER_FIRE);
    }
    public void launcherReset() {
        launcher.setPosition(LAUNCHER_RESET);
    }

    public int getTotemLocation() {
        double d1 = sensor1.getDistance(DistanceUnit.CM);
        double d2 = sensor2.getDistance(DistanceUnit.CM);

        if (d1 > TOTEM_THRESHOLD && d2 > TOTEM_THRESHOLD) {
            return 0;
        }
        if (d1 < TOTEM_THRESHOLD) {
            return 1;
        }
        if (d2 < TOTEM_THRESHOLD) {
            return 2;
        }
        return 0;
    }
}
