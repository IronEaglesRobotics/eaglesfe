package eaglesfe.roverruckus.opmodes.autonomous;

import com.eaglesfe.birdseye.BirdseyeServer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;
import java.util.Arrays;

import eaglesfe.common.MathHelpers;
import eaglesfe.roverruckus.Robot;

@Autonomous(name="Autonomous", group ="Competition")
public class CompetitionAutonomous extends LinearOpMode {

    @Override
    public void runOpMode() {
        BirdseyeServer      server  =   new BirdseyeServer(3708, telemetry);
        final Robot         robot   =   new Robot(hardwareMap);
        robot.setVisionEnabled(true);

        ArrayList<State> states = new ArrayList<>(Arrays.asList(

                /* Descend from the lander ====================================================== */
                new State() {
                    public String getDescription() {
                       return "Descending from lander...";
                    }

                    @Override
                    public void enter() {
                        robot.setLiftPosition(1.0, 1.0);
                    }

                    @Override
                    public boolean isFinished() {
                        return !robot.isLiftBusy();
                    }

                    @Override
                    public void leave() {}
                },
                /* ============================================================================== */

                /* Scoot ======================================================================== */
                new TimedState(1000) {
                    public String getDescription() {
                        return "Scoot away from hook...";
                    }

                    @Override
                    public void enter() {
                        super.enter();
                        robot.setDriveInput(0.0, -0.25, 0);
                    }

                    @Override
                    public boolean isFinished() {
                        return super.isFinished();
                    }

                    @Override
                    public void leave() {
                        robot.setDriveInput(0,0,0);
                    }
                },
                /* Strafe toward minerals ======================================================= */
                new State() {
                    public String getDescription() {
                        return "Strafe toward minerals...";
                    }

                    @Override
                    public void enter() {
                        robot.useSideCamera();
                        robot.setDriveInput(-0.2, 0, 0);
                    }

                    @Override
                    public boolean isFinished() {
                        return robot.getMineralSample().sampleSize > 0;
                    }

                    @Override
                    public void leave() {
                        robot.setDriveInput(0, 0, 0);
                    }
                }
                /* ============================================================================== */

            )
        );

        // =========================================================================================

        waitForStart();

        for (State state : states) {
            state.enter();

            telemetry.addData("STATE", state.getDescription());
            telemetry.update();

            while(state.isEnabled() && !state.isFinished() && opModeIsActive()) {

            }

            state.leave();
        }

        // =========================================================================================

        while (opModeIsActive()) {}
        robot.disable();
    }
}

abstract class State {
    public abstract String getDescription();
    public boolean isEnabled() { return true; }
    public abstract void enter();
    public abstract boolean isFinished();
    public abstract void  leave();
}

abstract class TimedState extends State {
    private long runUntil;
    private int duration;

    public TimedState(int duration){
        this.duration = duration;
    }

    @Override
    public void enter() {
        this.runUntil = System.currentTimeMillis() + this.duration;
    }

    @Override
    public boolean isFinished() {
        return System.currentTimeMillis() >= runUntil;
    }

    @Override
    public void leave() {}
}

abstract class TurningState extends State {
    private Robot robot;
    private float targetAngle;
    private float angleDelta;

    public TurningState(float angleDelta, Robot robot){
        this.angleDelta = angleDelta;
        this.robot = robot;
    }

    @Override
    public void enter() {
        targetAngle = MathHelpers.piTo2Pi(robot.getGyroHeading() + angleDelta);
    }

    @Override
    public boolean isFinished() {
        float current = robot.getGyroHeading();

        boolean finished = MathHelpers.isInRange2pi(current, this.targetAngle, 2);
        if (finished) {
            return true;
        }

        float phi = Math.abs(this.targetAngle - current) % 360;
        float error = phi > 180
                ? (360 - phi) * -1
                : Math.copySign(phi, targetAngle - current);
        robot.setDriveInput(0,0, 0);
        return true;
    }

    @Override
    public void leave() {}
}
