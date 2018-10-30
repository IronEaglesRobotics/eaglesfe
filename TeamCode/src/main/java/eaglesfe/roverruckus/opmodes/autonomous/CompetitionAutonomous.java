package eaglesfe.roverruckus.opmodes.autonomous;
import android.graphics.Point;

import eaglesfe.common.FieldPosition;
import eaglesfe.roverruckus.IronEaglesRobotRoverRuckus;
import eaglesfe.roverruckus.opmodes.RoverRuckusBirdseyeAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Autonomous", group ="Competition")
public class CompetitionAutonomous extends RoverRuckusBirdseyeAutonomous {

    IronEaglesRobotRoverRuckus robot;

    @Override
    public void runOpMode() {
        super.runOpMode();

        robot = new IronEaglesRobotRoverRuckus(hardwareMap);

        FieldPosition currentPosition;
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
