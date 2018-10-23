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
        boolean lu = gamepad1.dpad_up;
        boolean ld = gamepad1.dpad_down;
        boolean cu = gamepad1.dpad_right;
        boolean cd = gamepad1.dpad_left;

        robot.get_drive().updateMotors(x, y, z);
        robot.get_Arms() .updateArms(lu, ld, cu, cd);

        addPositionToTelemetry();
        telemetry.update();
    }
}
