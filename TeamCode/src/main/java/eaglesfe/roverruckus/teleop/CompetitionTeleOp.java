package eaglesfe.roverruckus.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import eaglesfe.common.PositionAwareTeleOp;

@TeleOp(name="TeleOp", group ="Competition")
public class CompetitionTeleOp extends PositionAwareTeleOp {

    @Override
    public void init() {
        super.init();

        /* Do hardware initialization stuff here */
    }

    @Override
    public void loop() {
        super.loop();

        /* Handle gamepad / driver input here */
        /* Use 'this.position' to determine your position on the field */
    }
}
