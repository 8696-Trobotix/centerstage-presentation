package org.firstinspires.ftc.teamcode.squid.alpha;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class SquidWareAlpha {

    public final double HOOK_UP = 0, HOOK_DOWN = 0.25;

    public Servo hook;

    public SquidWareAlpha(HardwareMap hardwareMap, String hookName) {
        hook = hardwareMap.get(Servo.class, hookName);
    }

    public void hookUp() {
        hook.setPosition(HOOK_UP);
    }

    public void hookDown() {
        hook.setPosition(HOOK_DOWN);
    }
}
