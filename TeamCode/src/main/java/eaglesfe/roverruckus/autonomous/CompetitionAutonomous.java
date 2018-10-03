package eaglesfe.roverruckus.autonomous;
import android.graphics.Point;

import eaglesfe.common.MathHelpers;
import eaglesfe.common.MecanumDrive;
import eaglesfe.common.PositionAwareAutonomous;
import eaglesfe.common.RobotPosition;
import eaglesfe.roverruckus.IronEaglesRobotRoverRuckus;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name="Autonomous", group ="Competition")
public class CompetitionAutonomous extends PositionAwareAutonomous {

    IronEaglesRobotRoverRuckus robot;

    @Override
    public void runOpMode() {
        super.runOpMode();

        robot = new IronEaglesRobotRoverRuckus(hardwareMap);

        RobotPosition currentPosition;
        final Point TARGET = new Point(-50, 0); // for testing

        waitForStart();
        while(opModeIsActive()){
            /* Do robot stuff here. */
            /* Use this.getPosition() to determine your position on the field. */
            currentPosition = getPosition();

            double rise = TARGET.y - currentPosition.y;
            double run = TARGET.x - currentPosition.x;
            moveTowardPosition(run, rise);

            this.addPositionToTelemetry();
            telemetry.update();
        }
    }

    public void moveTowardPosition(double x, double y){
        robot.get_drive().updateMotors(x, y, 0);
    }
}
