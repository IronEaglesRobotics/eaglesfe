package eaglesfe.roverruckus.opmodes.autonomous;
import android.graphics.Point;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.internal.opengl.models.Geometry;

import eaglesfe.common.BirdseyeServer;
import eaglesfe.common.FieldPosition;
import eaglesfe.roverruckus.IronEaglesRobotRoverRuckus;
import eaglesfe.roverruckus.opmodes.RoverRuckusBirdseyeAutonomous;

@Autonomous(name="AutonomousDepotDead", group ="Competition")
public class AutonomousDepotDead extends LinearOpMode {

    IronEaglesRobotRoverRuckus robot;

    @Override
    public void runOpMode() {

        robot = new IronEaglesRobotRoverRuckus(hardwareMap);

        FieldPosition currentPosition;
        waitForStart();

        moveTowardHeightTime(.3,0, 1200);
        moveAway(.3, 0, 300);
        moveAway(0, -.3, 6780);
        rotateAngle(.3, -33);
        moveAway(-.3,0,1000);
        moveTowardHeightTime(0, .75, 200);
        moveAway(-.8, 0, 1000);
        moveAway(-.3,0, 1000);
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


