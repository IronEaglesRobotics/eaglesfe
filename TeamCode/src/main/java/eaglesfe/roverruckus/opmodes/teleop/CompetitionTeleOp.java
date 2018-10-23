package eaglesfe.roverruckus.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import eaglesfe.roverruckus.opmodes.RoverRuckusBirdseyeTeleop;

@TeleOp(name="TeleOp", group ="Competition")
public class CompetitionTeleOp extends RoverRuckusBirdseyeTeleop {

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
