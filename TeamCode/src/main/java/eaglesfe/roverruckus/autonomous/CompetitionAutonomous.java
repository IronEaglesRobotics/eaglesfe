package eaglesfe.roverruckus.autonomous;
import eaglesfe.common.PositionAwareAutononomous;
import eaglesfe.common.RobotPosition;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="Autonomous", group ="Competition")
public class CompetitionAutonomous extends PositionAwareAutononomous {

    @Override
    public void runOpMode() {
        super.runOpMode();

        /* Do robot stuff here. */
        /* Use this.getPosition() to determine your position on the field. */
    }
}
