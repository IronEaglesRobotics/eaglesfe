package eaglesfe.common;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;

//   |----------------|
// B |       X+       | R
// L |                | E
// U | Y+          Y- | D
// E |       X-       |
//   |________________|

public abstract class PositionAwareTeleOp extends OpMode {

    private RoverRuckusRobotPositionEstimator positionEstimator;
    protected RobotPosition position = RobotPosition.UNKNOWN;

    // Make sure you call super.init() in your derived class.
    @Override
    public void init() {
        positionEstimator = new RoverRuckusRobotPositionEstimator();
        positionEstimator.initialize(this.hardwareMap, CameraDirection.BACK, true);
        positionEstimator.start();
    }

    // Make sure you call super.loop() in your derived class.
    @Override
    public void loop() {
        position = positionEstimator.getCurrentOrLastKnownPosition();
    }

    public void addPositionToTelemetry(){
        position.addToTelemetry(telemetry, false);
    }
}
