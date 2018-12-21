package eaglesfe.roverruckus.opmodes.autonomous;

import android.graphics.Point;

import com.eaglesfe.birdseye.BirdseyeServer;
import com.eaglesfe.birdseye.roverruckus.MineralSample;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eaglesfe.common.MathHelpers;
import eaglesfe.roverruckus.Robot;

@Autonomous(name="Autonomous", group ="Competition")
public class CompetitionAutonomous extends LinearOpMode {

    @Override
    public void runOpMode() {
        final BirdseyeServer    server  =   new BirdseyeServer(3708, telemetry);
        final Robot             robot   =   new Robot(hardwareMap);
        robot.setVisionEnabled(true);

        ArrayList<State> states = new ArrayList<>(Arrays.asList(

                /* ============================================================================== */
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
                    public void leave() {
                    }
                },
                /* ============================================================================== */
                new State() {
                    public String getDescription() {
                        return "Scoot away from hook...";
                    }

                    @Override
                    public void enter() {
                        robot.moveBackward(5.0, 0.3);
                    }

                    @Override
                    public boolean isFinished() {
                        return !robot.isDriveBusy();
                    }

                    @Override
                    public void leave() {
                        robot.setDriveInput(0, 0, 0);
                    }
                },
                /* ============================================================================== */
                new State() {
                    public String getDescription() {
                        return "Strafe toward minerals...";
                    }

                    @Override
                    public void enter() {
                        robot.resetGyroHeading();
                        robot.useSideCamera();
                        robot.setDriveInput(-0.15, 0, 0);
                    }

                    @Override
                    public boolean isFinished() {
                        MineralSample sample = robot.getMineralSample();
                        double x, z = 0;
                        float heading = robot.getGyroHeading180();
                        if (heading >= 1) {
                            z = 0.1;
                        } else if (heading <= -1) {
                            z = -0.1;
                        }

                        if (sample.sampleSize > 0) {
                            double top = sample.silverSampleSize > 0
                                    ? sample.silverMineralLocations.get(0).y
                                    : sample.goldMineralLocations.get(0).y;

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

                    @Override
                    public void leave() {
                        robot.setDriveInput(0, 0, 0);
                    }

                    @Override
                    public int getTimeout() {
                        return 2500;
                    }
                },
                /* ============================================================================== */
                new State() {
                    public String getDescription() {
                        return "Move to far right of minerals...";
                    }

                    @Override
                    public void enter() {
                        robot.moveForward(18, 0.4);
                    }

                    @Override
                    public boolean isFinished() {
                        return !robot.isDriveBusy();
                    }

                    @Override
                    public void leave() {
                        robot.setDriveInput(0, 0, 0);
                    }
                },
                /* ============================================================================== */
                new State() {
                    public String getDescription() {
                        return "Scan for gold mineral...";
                    }

                    @Override
                    public void enter() {
                        robot.useSideCamera();
                        robot.setDriveInput(0, -0.15, 0);
                    }

                    @Override
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

                    @Override
                    public void leave() {
                        robot.setDriveInput(0, 0, 0);
                    }
                },
                /* ============================================================================== */
                new State() {
                    public String getDescription() {
                        return "Position center with mineral...";
                    }

                    @Override
                    public void enter() {
                    }

                    @Override
                    public boolean isFinished() {
                        MineralSample sample = robot.getMineralSample();
                        if (sample.goldSampleSize == 1 && sample.goldMineralLocations.get(0).x < 35) {
                            return true;
                        }
                        robot.setDriveInput(0, 0.15, 0);
                        return false;
                    }

                    @Override
                    public void leave() {
                        robot.setDriveInput(0, 0, 0);
                    }
                },
                /* ============================================================================== */
                new TimedState(250) {
                    @Override
                    public String getDescription() {
                        return "Pause to let motion settle...";
                    }
                },
                /* ============================================================================== */
                new TimedState(1000) {

                    @Override
                    public String getDescription() {
                        return "Dislodge gold mineral...";
                    }

                    @Override
                    public void enter() {
                        super.enter();
                        robot.setDriveInput(-0.5, 0, 0);
                    }

                    @Override
                    public void leave() {
                        super.leave();
                        robot.setDriveInput(0, 0, 0);
                    }
                },
                new TimedState(250) {
                    @Override
                    public String getDescription() {
                        return "Pause to let motion settle...";
                    }
                },
                new TimedState(1000) {

                    @Override
                    public String getDescription() {
                        return "Return to previous position...";
                    }

                    @Override
                    public void enter () {
                        super.enter();
                        robot.setDriveInput(0.5,0,0);
                    }

                    @Override
                    public void leave () {
                        super.leave();
                        robot.setDriveInput(0,0,0);
                    }
                },
                new State() {
                    public String getDescription() {
                        return "Reset lift...";
                    }

                    @Override
                    public void enter() {
                        robot.setLiftPosition(0.05, 1.0);
                    }

                    @Override
                    public boolean isFinished() {
                        return !robot.isLiftBusy();
                    }

                    @Override
                    public void leave() {}
                }
                /* ============================================================================== */
            )
        );

        // =========================================================================================

        robot.useSideCamera();
        while (!isStarted()) {
            telemetry.addData("GYRO", robot.getGyroHeading180());
            telemetry.update();
        }
        waitForStart();

        for (State state : states) {
            if (!state.isEnabled()) {
                continue;
            }

            long timeout = System.currentTimeMillis() + state.getTimeout();
            state.enter();

            telemetry.addData("STATE", state.getDescription());
            telemetry.update();

            while(state.isEnabled()
                    && !state.isFinished()
                    && opModeIsActive()
                    && System.currentTimeMillis() < timeout) {

            }

            state.leave();
        }

        // =========================================================================================

        while (opModeIsActive()) {}
        robot.stopAllMotors();
    }
}

abstract class State {
    public abstract String getDescription();
    public boolean isEnabled() { return true; }
    public abstract void enter();
    public abstract boolean isFinished();
    public abstract void  leave();

    public int getTimeout() {
        return 10000;
    }
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
