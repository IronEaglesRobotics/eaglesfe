package eaglesfe.roverruckus.opmodes.autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import eaglesfe.common.FieldPosition;
import eaglesfe.roverruckus.IronEaglesRobotRoverRuckus;

@Autonomous(name="AutonomousCraterDeadClaim", group ="Competition")
public class AutonomousCraterDeadClaim extends LinearOpMode {

    IronEaglesRobotRoverRuckus robot;

    @Override
    public void runOpMode() {

        robot = new IronEaglesRobotRoverRuckus(hardwareMap);

        FieldPosition currentPosition;
        waitForStart();

        moveTowardHeightTime(.3,0, 1200);
        moveAway(.3, 0, 300);
        moveAway(0, -.3, 3800);
        moveAway(0,.3,1700);
        moveAway(.3, 0, 2500);
        rotateAngle(-.3, 40);
        moveAway(0,-.3, 600);
        moveAway(.5, 0, 1300);
        moveTowardHeightTime(0, .75, 200);
        moveAway(-.5,0,2250);

    }

    public void moveTowardPosition(double x, double y) {
        robot.get_drive().updateMotors(-x, -y, 0);
    }

    public void moveTowardHeightTime(double z,double a, long millis) {
        robot.get_Arms().updateArmsTime(-z, 0, 0, 0, a, millis, System.currentTimeMillis());
    }

    public void moveAway(double x, double y, long millis) {
        robot.get_drive().updateDriveTime(y, x, 0, millis, System.currentTimeMillis());
    }

    public void rotateAngle(double z, float rTarget){
        robot.get_drive().updateDriveGyro(z,robot.get_drive().getCurrentAngle(), rTarget, this);
    }

//    public void rotateTime(double z, long millis) {
//        robot.get_drive().updateDriveTime(0,0, z, millis,System.currentTimeMillis());
//    }
}


