package eaglesfe.roverruckus.opmodes.autonomous;
import android.graphics.Point;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.internal.opengl.models.Geometry;

import eaglesfe.common.BirdseyeServer;
import eaglesfe.common.FieldPosition;
import eaglesfe.roverruckus.IronEaglesRobotRoverRuckus;
import eaglesfe.roverruckus.opmodes.RoverRuckusBirdseyeAutonomous;

@Autonomous(name="Autonomous", group ="Competition")
public class AutonomousDepotDead extends RoverRuckusBirdseyeAutonomous {
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

        int currentTarget = 0;
        int[][] target = {
                {-48,48}
        };

        robot = new IronEaglesRobotRoverRuckus(hardwareMap);

        FieldPosition currentPosition;
        waitForStart();

        moveTowardHeightTime(.25, 1200);
        moveAway(.25, 0, 300);
        moveAway(0,-.25,800);

        final Point TARGET =  new Point(target[currentTarget][0], target[currentTarget][1]);

        long start = System.currentTimeMillis();

        while(opModeIsActive() && currentTarget < target.length){
            telemetry.addData("","opmodeactive");
            currentPosition = getPosition();

            if (System.currentTimeMillis() - start > 500) {
                birdseye.broadcast(currentPosition.asJSONObject());
                start = System.currentTimeMillis();
            }

                double rise = TARGET.y - currentPosition.y;
                double run = TARGET.x - currentPosition.x;
                double ratio = rise/run;
                double t = Math.atan(ratio);
                double angle = Math.toDegrees(t);
                angle -= currentPosition.heading;
                angle = Math.toRadians(angle);
                double newRatio = Math.tan(angle);
                double nrise = newRatio;
                double nrun = 1;
                double x = nrise;
                double y = nrun;
                double max = Math.max(Math.abs(x), Math.abs(y));
                x /= max;
                y /= max;

                moveTowardPosition(-x*.25, -y*.25);

            this.addPositionToTelemetry();
            telemetry.addData("Color:RGB",robot.get_Arms().getSample());
            telemetry.addData("rise:", rise);
            telemetry.addData("run", run);
            telemetry.update();

            if ((target[currentTarget][0] - currentPosition.x < .5) && (target[currentTarget][1] - currentPosition.y) < .5) {currentTarget++;}
        }

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



