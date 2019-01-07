package eaglesfe.roverruckus.opmodes.autonomous;

import android.graphics.Point;

import com.eaglesfe.birdseye.BirdseyeServer;
import com.eaglesfe.birdseye.FieldPosition;
import com.eaglesfe.birdseye.roverruckus.MineralSample;
import com.eaglesfe.birdseye.util.MathHelpers;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;
import java.util.Arrays;

import eaglesfe.roverruckus.Robot;
import eaglesfe.roverruckus.opmodes.OpModeHelpers;

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
                    public void enter() { robot.moveForward(5.0, 0.3); }
                    public boolean isFinished() { return !robot.isDriveBusy(); }
                    public void leave() { robot.setDriveInput(0, 0, 0); }
                },
                /* ============================================================================== */
                new Step("Quick scoot toward minerals...", 1000) {
                    public void enter() { robot.setDriveInput(-0.4, 0, 0);}
                    public boolean isFinished() { return false; }
                    public void leave() { robot.stopAllMotors(); }
                },
                new Step("Pause until sample size is greater than 0...", 3000) {
                    public void enter() { robot.stopAllMotors(); }
                    public boolean isFinished() { return robot.getMineralSample().sampleSize > 0; }
                    public void leave() { robot.stopAllMotors(); }
                },
                /* ============================================================================== */
                new Step("Strafe toward minerals...", 2500) {
                    public void enter() {
                        robot.resetGyroHeading();
                        robot.useSideCamera();
                        robot.setDriveInput(-0.1, 0, 0);
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
                                x = -0.1;
                            } else if (top > 50) {
                                x = 0.1;
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
                    public void enter() { robot.moveForward(8, 0.4); }
                    public boolean isFinished() { return !robot.isDriveBusy(); }
                    public void leave() { robot.setDriveInput(0, 0, 0); }
                },
                /* ============================================================================== */
                new Step("Scan for gold mineral...") {
                    public void enter() {
                        robot.useSideCamera();
                        robot.setDriveInput(0, -0.1, 0);
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
                new SleepStep("Pause to let motion settle...", 250),
                /* ============================================================================== */
                new Step("Dislodge gold mineral...", 750) {
                    public void enter() { robot.setDriveInput(-0.5, 0, 0); }
                    public boolean isFinished() { return false; }
                    public void leave() { robot.setDriveInput(0, 0, 0); }
                },
                /* ============================================================================== */
                new SleepStep("Pause to let motion settle...", 250),
                /* ============================================================================== */
                new Step("Return to previous position...", 675) {
                    public void enter () { robot.setDriveInput(0.5,0,0); }
                    public boolean isFinished() { return false; }
                    public void leave () { robot.setDriveInput(0,0,0); }
                },
                /* ============================================================================== */
                new Step("Scan for vuforia targets...", 60000) {

                    public void enter () {
                        robot.useRearCamera();

                        robot.setDriveInput(0,-.1,0);
                    }

                    public boolean isFinished() {
                        FieldPosition position = robot.getPosition();
                        if (position != null) {
                            return true;
                        } else {
                            float heading = robot.getGyroHeading180();
                            if (heading >= 1) {
                                robot.setDriveInputZ(0.1);
                            } else if (heading <= -1) {
                                robot.setDriveInputZ(-0.1);
                            } else {
                                robot.setDriveInputZ(0);
                            }
                        }
                        telemetry.update();
                        return false;
                    }

                    public void leave () {
                        robot.stopAllMotors();
                    }
                },
                /* ============================================================================== */
                new Step("Move to wall...", 10000) {

                    private double distance = Double.MIN_VALUE;
                    public void enter() { tryMove(); }

                    private void tryMove() {
                        FieldPosition position = robot.getPosition();

                        if (position != null) {
                            int x = (int)position.getX();
                            int y = (int)position.getY();
                            Point target = OpModeHelpers.getTurnaroundPointForAutonomous(position);
                            this.distance = MathHelpers.getDistanceBetweenTwoPoints(new Point(x, y), new Point(target.x, target.y));
                            robot.moveBackward(this.distance, 0.3);
                        }
                    }

                    public boolean isFinished() {
                        if (distance == Double.MIN_VALUE) {
                            tryMove();
                        } else {
                            return !robot.isDriveBusy();
                        }
                        return false;
                    }

                    public void leave() {
                        robot.stopAllMotors();
                    }
                },
                /* ============================================================================== */
                new Step("Turn to face the depot...", 10000) {

                    public void enter() { robot.resetGyroHeading(); }

                    public boolean isFinished() {
                        if (robot.getGyroHeading180() < -110) {
                            return true;
                        }

                        robot.setDriveInput(0,0, 0.4);
                        return false;
                    }

                    public void leave() {
                        robot.stopAllMotors();
                    }
                },
                new Step("Strafe toward wall to square up...", 1000) {
                    public void enter() { robot.setDriveInput(0.4, 0, 0);}
                    public boolean isFinished() { return false; }
                    public void leave() { robot.stopAllMotors(); }
                },
                /* ============================================================================== */
                new Step("Sceedadle post haste to the depot...") {
                    public void enter() {
                        robot.setArmPosition(Robot.Constants.TEAM_MARKER_DEPLOY, 1.0);
                        robot.moveBackward(39, 0.6);
                    }
                    public boolean isFinished() { return !robot.isDriveBusy() && !robot.isArmBusy(); }
                    public void leave() { robot.stopAllMotors();}
                },
                /* ============================================================================== */
                new Step("CRATER!!!") {
                    public void enter() {
                        robot.setArmPosition(0.25, 1.0);
                        robot.moveForward(48, 0.6);
                        robot.setExtedPosition(-4000, 1.0);
                    }
                    public boolean isFinished() { return !robot.isDriveBusy()
                                                      && !robot.isArmBusy()
                                                      && !robot.isExtendBusy(); }
                    public void leave() { robot.stopAllMotors();}
                }
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