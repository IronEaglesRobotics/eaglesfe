package eaglesfe.common;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;

//   |----------------|
// B |       X+       | R
// L |                | E
// U | Y+          Y- | D
// E |       X-       |
//   |________________|

public abstract class PositionAwareAutononomous extends LinearOpMode {
    private RoverRuckusRobotPositionEstimator positionEstimator;

    // Make sure you call super.runOpMode() in your derived class.
    @Override
    public void runOpMode() {
        positionEstimator = new RoverRuckusRobotPositionEstimator();
        positionEstimator.initialize(hardwareMap, CameraDirection.BACK, true);
        positionEstimator.start();
    }

    protected RobotPosition getPosition(){
        return positionEstimator.getCurrentOrLastKnownPosition();
    }

    public void addPositionToTelemetry(){
        RobotPosition position = getPosition();
        position.addToTelemetry(telemetry, false);
    }
}
