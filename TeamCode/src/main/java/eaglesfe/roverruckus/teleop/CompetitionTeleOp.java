package eaglesfe.roverruckus.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import eaglesfe.common.PositionAwareTeleOp;
import eaglesfe.roverruckus.IronEaglesRobotRoverRuckus;

@TeleOp(name="TeleOp", group ="Competition")
public class CompetitionTeleOp extends PositionAwareTeleOp {

    IronEaglesRobotRoverRuckus robot;

    @Override
    public void init() {
        super.init();

        robot = new IronEaglesRobotRoverRuckus(hardwareMap);
        /* Do hardware initialization stuff here */
    }

    @Override
    public void loop() {
        super.loop();

        /* Handle gamepad / driver input here */
        /* Use 'this.position' to determine your position on the field */

        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double z = gamepad1.right_stick_x;
        float liftUp = gamepad1.right_trigger;
        float liftDown = gamepad1.left_trigger;
        float extendOut = gamepad2.right_trigger;
        float extendIn = gamepad2.left_trigger;
        boolean collectorUp = gamepad2.dpad_up;
        boolean collectorDown = gamepad2.dpad_down;
        boolean collectorLeft = gamepad2.x;
        boolean collectorRight = gamepad2.b;

        robot.get_drive().updateMotors(x, y, z);
        robot.get_Arms() .updateArms(liftUp, liftDown, collectorUp, collectorDown, extendOut, extendIn, collectorLeft, collectorRight);

        addPositionToTelemetry();
        telemetry.addData("Left:", robot.get_Arms().getCollectorLeft());
        telemetry.addData("Right", robot.get_Arms().getCollectorRight());
        telemetry.update();
    }
}
