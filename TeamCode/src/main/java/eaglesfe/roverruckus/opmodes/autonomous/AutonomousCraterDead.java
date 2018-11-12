package eaglesfe.roverruckus.opmodes.autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import eaglesfe.common.FieldPosition;
import eaglesfe.roverruckus.IronEaglesRobotRoverRuckus;

@Autonomous(name="AutonomousCraterDead", group ="Competition")
public class AutonomousCraterDead extends LinearOpMode {

    IronEaglesRobotRoverRuckus robot;

    @Override
    public void runOpMode() {

        robot = new IronEaglesRobotRoverRuckus(hardwareMap);

        FieldPosition currentPosition;
        waitForStart();

        moveTowardHeightTime(.3,0, 1200);
        moveAway(.3, 0, 300);
        moveAway(0, -.3, 4100);

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


