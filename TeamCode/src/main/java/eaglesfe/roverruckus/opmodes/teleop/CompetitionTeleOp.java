package eaglesfe.roverruckus.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import eaglesfe.roverruckus.IronEaglesRobotRoverRuckus;
import eaglesfe.roverruckus.opmodes.RoverRuckusBirdseyeTeleop;

@TeleOp(name="TeleOp", group ="Competition")
public class CompetitionTeleOp extends RoverRuckusBirdseyeTeleop {

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
        float collectorUp = gamepad2.left_stick_y;
        boolean collectorLeft = gamepad2.x;
        boolean collectorRight = gamepad2.b;

        robot.get_drive().updateMotors(x, y, z);
        robot.get_Arms().updateArms(liftUp, liftDown, extendOut, extendIn);
        robot.get_Arms().updateCollector(collectorUp, collectorLeft, collectorRight);

        telemetry.addData("Left:", robot.get_Arms().getCollectorLeft());
        telemetry.addData("Right", robot.get_Arms().getCollectorRight());
        telemetry.update();
    }
}
