package eaglesfe.roverruckus.opmodes.autonomous;
import eaglesfe.roverruckus.opmodes.RoverRuckusBirdseyeAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Autonomous", group ="Competition")
public class CompetitionAutonomous extends RoverRuckusBirdseyeAutonomous {

    @Override
    public void runOpMode() {
        super.runOpMode();

        waitForStart();
        while(opModeIsActive()){
            /* Do robot stuff here. */
            /* Use this.getPosition() to determine your position on the field. */

            this.addPositionToTelemetry();
            telemetry.update();
        }
    }
}
