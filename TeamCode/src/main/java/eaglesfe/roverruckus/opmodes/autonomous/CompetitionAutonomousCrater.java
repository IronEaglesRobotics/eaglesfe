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
import java.util.HashMap;
import java.util.Map;

import eaglesfe.common.SleepStep;
import eaglesfe.common.Step;
import eaglesfe.common.Steps;
import eaglesfe.roverruckus.Robot;
import eaglesfe.roverruckus.opmodes.OpModeHelpers;

@Autonomous(name="Autonomous Crater", group ="Competition")
public class CompetitionAutonomousCrater extends LinearOpMode {

    @Override
    public void runOpMode() {
        final BirdseyeServer    server  =   new BirdseyeServer(3708, telemetry);
        final Robot             robot   =   new Robot(hardwareMap);
        robot.setVisionEnabled(true);

        Map<String, Step> steps = new HashMap<>();

        steps.put("start", new Step("Descending from lander...") {
            public void enter() { robot.setLiftPosition(1.0, 1.0); }
            public boolean isFinished() { return !robot.isLiftBusy(); }
            public String leave() { return "scoot_away_from_hook"; }
        });

        steps.put("scoot_away_from_hook", new Step("Scoot away from hook...", 1000) {
            public void enter() { robot.moveBackward(5.0, 0.3); }
            public boolean isFinished() { return !robot.isDriveBusy(); }
            public String leave() {
                robot.setDriveInput(0, 0, 0);
                return "scoot_toward_minerals";
            }
        });

        steps.put("scoot_toward_minerals", new Step("Quick scoot toward minerals...", 1000) {
            public void enter() { robot.setDriveInput(-0.4, 0, 0);}
            public boolean isFinished() { return false; }
            public String leave() {
                robot.stopAllMotors();
                return "pause_for_sample";
            }
        });

        steps.put("pause_for_sample", new Step("Pause until sample size is greater than 0...", 2000) {
            public void enter() { robot.stopAllMotors(); }
            public boolean isFinished() { return robot.getMineralSample().sampleSize > 0; }
            public String leave() {
                robot.stopAllMotors();
                return "strafe_toward_minerals";
            }
        });

        steps.put("strafe_toward_minerals", new Step("Strafe toward minerals...", 2500) {
            public void enter() {
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
                    double center = sample.boundingBox.top + (sample.boundingBox.height() / 2);

                    if (center < 10) {
                        x = -0.1;
                    } else if (center > 50) {
                        x = 0.1;
                    } else {
                        return true;
                    }
                    robot.setDriveInput(x, 0, z);
                }
                return false;
            }

            public String leave() {
                robot.setDriveInput(0, 0, 0);
                return "move_to_right_of_minerals";
            }
        });

        steps.put("move_to_right_of_minerals", new Step("Move to far right of minerals...") {
            public void enter() { robot.moveForward(22, 0.4); }
            public boolean isFinished() { return !robot.isDriveBusy(); }
            public String leave() {
                robot.setDriveInput(0, 0, 0);
                return "scan_for_gold";
            }
        });

        steps.put("scan_for_gold", new Step("Scan for gold mineral...") {
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

            public String leave() {
                robot.setDriveInput(0, 0, 0);
                return "sleep_to_settle_before_sample";
            }
        });

        steps.put("sleep_to_settle_before_sample", new SleepStep("Pause to let motion settle...",
                250, "dislodge_gold_mineral"));

        steps.put("dislodge_gold_mineral", new Step("Dislodge gold mineral...", 750) {
            public void enter() { robot.setDriveInput(-0.5, 0, 0); }
            public boolean isFinished() { return false; }
            public String leave() {
                robot.setDriveInput(0, 0, 0);
                return "sleep_to_settle_after_sample";
            }
        });

        steps.put("sleep_to_settle_after_sample", new SleepStep("Pause to let motion settle...",
                250, "return_to_previous_position"));

        steps.put("return_to_previous_position", new Step("Return to previous position...", 600) {
            public void enter () { robot.setDriveInput(0.5,0,0); }
            public boolean isFinished() { return false; }
            public String leave () {
                robot.setDriveInput(0,0,0);
                return "scan_for_vuforia_targets";
            }
        });

        steps.put("scan_for_vuforia_targets", new Step("Scan for vuforia targets...", 60000) {

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

            public String leave () {
                robot.stopAllMotors();
                return "move_to_wall";
            }
        });

        steps.put("move_to_wall", new Step("Move to wall...", 10000) {

            private double distance = Double.MIN_VALUE;
            public void enter() { tryMove(); }

            private void tryMove() {
                FieldPosition position = robot.getPosition();

                if (position != null) {
                    int x = (int)position.getX();
                    int y = (int)position.getY();
                    Point target = OpModeHelpers.getTurnaroundPointForAutonomous(position);
                    this.distance = MathHelpers.getDistanceBetweenTwoPoints(new Point(target.x, target.y), new Point(x, y));
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

            public String leave() {
                robot.stopAllMotors();
                return "turn_to_face_depot";
            }
        });

        steps.put("turn_to_face_depot", new Step("Turn to face the depot...", 10000) {

            public void enter() { robot.resetGyroHeading(); }

            public boolean isFinished() {
                if (robot.getGyroHeading180() > 40) {
                    return true;
                }

                robot.setDriveInput(0,0, -0.4);
                return false;
            }

            public String leave() {
                robot.stopAllMotors();
                return "strafe_toward_wall";
            }
        });

        steps.put("strafe_toward_wall", new Step("Strafe toward wall to square up...", 1000) {
            public void enter() { robot.setDriveInput(-0.4, 0, 0);}
            public boolean isFinished() { return false; }
            public String leave() {
                robot.stopAllMotors();
                return "skedaddle";
            }
        });

        steps.put("skedaddle", new Step("Skedaddle post haste to the depot...") {
            public void enter() {
                robot.setArmPosition(Robot.Constants.TEAM_MARKER_DEPLOY, 1.0);
                robot.moveBackward(28, 0.6);
            }
            public boolean isFinished() { return !robot.isDriveBusy() && !robot.isArmBusy(); }
            public String leave() {
                robot.stopAllMotors();
                return "crater";
            }
        });

        steps.put("crater", new Step("CRATER!!!") {
            public void enter() {
                robot.setArmPosition(0.25, 1.0);
                robot.moveForward(50, 0.6);
                robot.setExtedPosition(-4000, 1.0);
            }
            public boolean isFinished() { return !robot.isDriveBusy()
                    && !robot.isArmBusy()
                    && !robot.isExtendBusy(); }
            public String leave() {
                robot.stopAllMotors();
                return null;
            }
        });

        robot.useSideCamera();

        telemetry.addData("Ready...", null);
        telemetry.update();

        waitForStart();

        Steps stepsRunner = new Steps(steps, this);
        stepsRunner.runStepsMap();

        // =========================================================================================

        robot.stopAllMotors();
    }
}