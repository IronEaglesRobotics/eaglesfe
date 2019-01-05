package eaglesfe.roverruckus.opmodes.autonomous;

import com.eaglesfe.birdseye.BirdseyeServer;
import com.eaglesfe.birdseye.roverruckus.MineralSample;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;
import java.util.Arrays;

import eaglesfe.roverruckus.Robot;

@Autonomous(name="Autonomous", group ="Competition")
public class CompetitionAutonomous extends LinearOpMode {

    @Override
    public void runOpMode() {
        final BirdseyeServer    server  =   new BirdseyeServer(3708, telemetry);
        final Robot             robot   =   new Robot(hardwareMap);
        robot.setVisionEnabled(true);

        ArrayList<Step> steps = new ArrayList<>(Arrays.asList(
                /* ============================================================================== */
                new Step("Descending from lander...") {
                    public void enter() { robot.setLiftPosition(1.0, 1.0); }
                    public boolean isFinished() { return !robot.isLiftBusy(); }
                    public void leave() {}
                },
                /* ============================================================================== */
                new Step("Scoot away from hook...") {
                    public void enter() { robot.moveBackward(5.0, 0.3); }
                    public boolean isFinished() { return !robot.isDriveBusy(); }
                    public void leave() { robot.setDriveInput(0, 0, 0); }
                },
                /* ============================================================================== */
                new Step("Strafe toward minerals...", 2500) {
                    public void enter() {
                        robot.resetGyroHeading();
                        robot.useSideCamera();
                        robot.setDriveInput(-0.15, 0, 0);
                    }

                    public boolean isFinished() {
                        MineralSample sample = robot.getMineralSample();
                        float heading = robot.getGyroHeading180();

                        double x, z = 0;
                        if (heading >= 1) {
                            z = 0.1;
                        } else if (heading <= -1) {
                            z = -0.1;
                        }

                        if (sample.sampleSize > 0) {
                            double top = sample.boundingBox.top;

                            if (top < 20) {
                                x = -0.15;
                            } else if (top > 50) {
                                x = 0.15;
                            } else {
                                return true;
                            }
                            robot.setDriveInput(x, 0, z);
                        }
                        return false;
                    }

                    public void leave() { robot.setDriveInput(0, 0, 0); }
                },
                /* ============================================================================== */
                new Step("Move to far right of minerals...") {
                    public void enter() { robot.moveForward(18, 0.4); }
                    public boolean isFinished() { return !robot.isDriveBusy(); }
                    public void leave() { robot.setDriveInput(0, 0, 0); }
                },
                /* ============================================================================== */
                new Step("Scan for gold mineral...") {
                    public void enter() {
                        robot.useSideCamera();
                        robot.setDriveInput(0, -0.15, 0);
                    }

                    public boolean isFinished() {
                        float heading = robot.getGyroHeading180();
                        if (heading >= 1) {
                            robot.setDriveInputZ(0.1);
                        } else if (heading <= -1) {
                            robot.setDriveInputZ(-0.1);
                        } else {
                            robot.setDriveInputZ(0);
                        }

                        return robot.getMineralSample().goldSampleSize > 0;
                    }

                    public void leave() { robot.setDriveInput(0, 0, 0); }
                },
                /* ============================================================================== */
                new Step("Position center with mineral...") {
                    public void enter() { }

                    public boolean isFinished() {
                        MineralSample sample = robot.getMineralSample();
                        if (sample.goldSampleSize == 1 && sample.goldMineralLocations.get(0).x < 35) {
                            return true;
                        }
                        robot.setDriveInput(0, 0.15, 0);
                        return false;
                    }

                    public void leave() { robot.setDriveInput(0, 0, 0); }
                },
                /* ============================================================================== */
                new SleepStep("Pause to let motion settle...", 250),
                /* ============================================================================== */
                new Step("Dislodge gold mineral...", 1000) {
                    public void enter() { robot.setDriveInput(-0.5, 0, 0); }
                    public boolean isFinished() { return false; }
                    public void leave() { robot.setDriveInput(0, 0, 0); }
                },
                /* ============================================================================== */
                new SleepStep("Pause to let motion settle...", 250),
                /* ============================================================================== */
                new Step("Return to previous position...", 1000) {

                    public void enter () { robot.setDriveInput(0.5,0,0); }
                    public boolean isFinished() { return false; }
                    public void leave () { robot.setDriveInput(0,0,0); }
                },
                /* ============================================================================== */
                new Step("Reset lift...") {
                    public void enter() { robot.setLiftPosition(0.05, 1.0); }
                    public boolean isFinished() { return !robot.isLiftBusy(); }
                    public void leave() {}
                }
                /* ============================================================================== */
            )
        );

        // =========================================================================================

        robot.useSideCamera();

        waitForStart();

        for (Step step : steps) {
            if (!step.isEnabled()) {
                continue;
            }

            long timeout = System.currentTimeMillis() + step.getTimeout();
            step.enter();

            telemetry.addData("STATE", step.getDescription());
            telemetry.update();

            while(step.isEnabled()
                    && !step.isFinished()
                    && opModeIsActive()
                    && System.currentTimeMillis() < timeout) {

            }

            step.leave();

            if (!opModeIsActive()) {
                break;
            }
        }

        // =========================================================================================

        robot.stopAllMotors();
    }
}

abstract class Step {
    private final String description;
    private final int timeout;

    public Step (String description, int timeout) {
        this.description = description;
        this.timeout = timeout;
    }

    public Step (String description) {
        this.description = description;
        this.timeout = 10000;
    }

    public boolean isEnabled() { return true; }
    public abstract void enter();
    public abstract boolean isFinished();
    public abstract void  leave();

    public String getDescription() { return this.description; }
    public int getTimeout() { return this.timeout; }
}

class SleepStep extends Step {
    public SleepStep(String description, int timeout) {
        super(description, timeout);
    }

    public void enter() { }

    public boolean isFinished() { return false; }

    public void leave() { }
}