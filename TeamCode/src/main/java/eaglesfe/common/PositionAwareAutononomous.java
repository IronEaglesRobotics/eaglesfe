package eaglesfe.common;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.robot.Robot;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
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
        int offset = (int)(9 * VuforiaBase.MM_PER_INCH);
        positionEstimator = new RoverRuckusRobotPositionEstimator(offset, 0,0);
        positionEstimator.initialize(hardwareMap, CameraDirection.BACK, true);
        positionEstimator.start();
    }

    protected RobotPosition getPosition(){
        return positionEstimator.getCurrentOrLastKnownPosition();
    }

    protected final void addPositionToTelemetry(){
        RobotPosition position = getPosition();
        position.addToTelemetry(telemetry, false);
    }
}
