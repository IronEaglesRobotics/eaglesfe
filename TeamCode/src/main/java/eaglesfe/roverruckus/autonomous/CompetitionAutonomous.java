package eaglesfe.roverruckus.autonomous;
import eaglesfe.common.PositionAwareAutonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Autonomous", group ="Competition")
public class CompetitionAutonomous extends PositionAwareAutonomous {

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
