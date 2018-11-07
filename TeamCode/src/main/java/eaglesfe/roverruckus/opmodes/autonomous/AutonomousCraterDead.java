package eaglesfe.roverruckus.opmodes.autonomous;
import android.graphics.Point;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.internal.opengl.models.Geometry;

import eaglesfe.common.BirdseyeServer;
import eaglesfe.common.FieldPosition;
import eaglesfe.roverruckus.IronEaglesRobotRoverRuckus;
import eaglesfe.roverruckus.opmodes.RoverRuckusBirdseyeAutonomous;

@Autonomous(name="Autonomous", group ="Competition")
public class AutonomousCraterDead extends RoverRuckusBirdseyeAutonomous {
    protected BirdseyeServer birdseye;
    IronEaglesRobotRoverRuckus robot;
    @Override
    protected Geometry.Point3 getCameraPositionOnRobot() {
        return new Geometry.Point3(0.4f,8.0f,5.25f);
    }
    @Override
    public void runOpMode() {
        super.runOpMode();

        birdseye = BirdseyeServer.GetInstance(3708, this.telemetry);
        birdseye.start();

        robot = new IronEaglesRobotRoverRuckus(hardwareMap);

        FieldPosition currentPosition;
        waitForStart();

        moveTowardHeightTime(.25, 1200);
        moveAway(.25, 0, 300);
        moveAway(0,-.25,800);
        rotate(.5, 100);

        try {
            birdseye.stop(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void moveTowardPosition(double x, double y){
        robot.get_drive().updateMotors(-x, -y, 0);
    }

    public void moveTowardHeightTime(double z, long millis)
    { robot.get_Arms().updateArmsTime(-z, 0,0,0, millis, System.currentTimeMillis());}

    public void moveAway(double x,double y, long millis) {
        robot.get_drive().updateDriveTime(y, x,0, millis,System.currentTimeMillis());
    }

    public void rotate(double z, long millis) {
        robot.get_drive().updateDriveTime(0,0, z, millis,System.currentTimeMillis());
    }
}



