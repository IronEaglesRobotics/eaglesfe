package eaglesfe.roverruckus.opmodes.autonomous;
import android.graphics.Point;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import eaglesfe.common.BirdseyeServer;
import eaglesfe.common.FieldPosition;
import eaglesfe.common.MathHelpers;
import eaglesfe.roverruckus.IronEaglesRobotRoverRuckus;
import eaglesfe.roverruckus.opmodes.RoverRuckusBirdseyeAutonomous;

@Autonomous(name="Autonomous", group ="Competition")
public class CompetitionAutonomous extends RoverRuckusBirdseyeAutonomous {
    protected BirdseyeServer birdseye;
    IronEaglesRobotRoverRuckus robot;
    @Override
    protected boolean shouldShowCameraPreview() {return false;}

    @Override
    public void runOpMode() {
        super.runOpMode();

        birdseye = BirdseyeServer.GetInstance(3708, this.telemetry);
        birdseye.start();

        int currentTarget = 0;
        int[][] target = {
                {0,0},
                {10,10}
        };

        robot = new IronEaglesRobotRoverRuckus(hardwareMap);

        FieldPosition currentPosition;
        waitForStart();

        moveTowardHeightTime(.5, 500);

        while(opModeIsActive()){
            final Point TARGET = new Point(target[currentTarget][0], target[currentTarget][1]);
            currentPosition = getPosition();

            if (currentTarget <= target.length) {

                birdseye.broadcast(currentPosition.asJSONObject());

                double rise = TARGET.y - currentPosition.y;
                double run = TARGET.x - currentPosition.x;

                moveTowardPosition(run, rise);
            }

            this.addPositionToTelemetry();
            telemetry.update();

            if ((currentPosition.x - target[currentTarget][0] < 10) && (currentPosition.y - target[currentTarget][1] < 10)) {currentTarget++;}
        }

        try {
            birdseye.stop(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void moveTowardPosition(double x, double y){
        robot.get_drive().updateMotors(x, y, 0);
    }

    //public void moveTowardHeight(float z) { robot.get_Arms().updateArms(z,-z,0, 0); }

    public void moveTowardHeightTime(double z, int millis) { robot.get_Arms().updateArmsTime(z, 0,0,0, millis);}

}


