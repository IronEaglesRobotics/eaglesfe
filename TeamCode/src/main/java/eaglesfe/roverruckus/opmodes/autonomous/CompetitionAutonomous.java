package eaglesfe.roverruckus.opmodes.autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import eaglesfe.common.BirdseyeServer;
import eaglesfe.common.FieldPosition;
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

        robot = new IronEaglesRobotRoverRuckus(hardwareMap);

        FieldPosition currentPosition;
        waitForStart();
        while(opModeIsActive()){
            currentPosition = getPosition();
            birdseye.broadcast(currentPosition.asJSONObject());

            this.addPositionToTelemetry();
            telemetry.update();

            this.sleep(100);
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
}
