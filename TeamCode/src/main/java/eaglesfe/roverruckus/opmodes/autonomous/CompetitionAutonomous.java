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

//        int currentTarget = 0;
//        int[][] target = {
//                {0,0},
//                {10,10}
//        };

        robot = new IronEaglesRobotRoverRuckus(hardwareMap);

        FieldPosition currentPosition;
        waitForStart();

        //moveTowardHeightTime(.25, 500);
        final Point TARGET = new Point(0, 36);

//        while(opModeIsActive() && currentTarget < target.length){
        long start = System.currentTimeMillis();

        while(opModeIsActive()){
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

                moveTowardPosition(x *.5 , y*.5);

            this.addPositionToTelemetry();
            telemetry.addData("rise:", rise);
            telemetry.addData("run", run);
            telemetry.update();

//            if ((currentPosition.x - target[currentTarget][0] < 1) && (currentPosition.y - target[currentTarget][1] < 1)) {currentTarget++;}
        }

        try {
            birdseye.stop(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void moveTowardPosition(double x, double y){
        robot.get_drive().updateMotors(-x, -y, 0);
    }

    //public void moveTowardHeight(float z) { robot.get_Arms().updateArms(z,-z,0, 0); }

    public void moveTowardHeightTime(double z, int millis) { robot.get_Arms().updateArmsTime(z, 0,0,0, millis);}
}


