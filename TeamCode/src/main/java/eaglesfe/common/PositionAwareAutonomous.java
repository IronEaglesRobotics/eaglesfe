package eaglesfe.common;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.internal.opengl.models.Geometry;

//   |----------------|
// B |       X+       | R
// L |                | E
// U | Y+          Y- | D
// E |       X-       |
//   |________________|

public abstract class PositionAwareAutonomous extends LinearOpMode {
    private RoverRuckusRobotPositionEstimator positionEstimator;

    /**
     * Gets whether the back-facing camera of the Robot Controller (false) or if
     * a connected UVC webcam should be used for tracking (true).
     */
    protected boolean shouldUseWebcam() {
        return true;
    }
    /**
     * Sets whether to display a preview of the camera image on the Robot Controller.
     */
    protected boolean shouldShowCameraPreview(){
        return true;
    }

    /**
     * Sets the XYZ position of the tracking camera relative to the center point of the bottom of the robot.
     */
    protected Geometry.Point3 getCameraPositionOnRobot() {
        return new Geometry.Point3(0,0,0);
    }

    // Make sure you call super.runOpMode() in your derived class.
    @Override
    public void runOpMode() {
        Geometry.Point3 cameraPosition = getCameraPositionOnRobot();
        positionEstimator = new RoverRuckusRobotPositionEstimator(cameraPosition.y, cameraPosition.x, cameraPosition.z);
        positionEstimator.initialize(hardwareMap, shouldUseWebcam(), shouldShowCameraPreview());
        positionEstimator.start();
    }

    protected RobotPosition getPosition(){
        return positionEstimator.getCurrentOrLastKnownPosition();
    }

    /**
     * Adds the current XYZ and PRH information to the telemetry. Does not call telemetry.update().
     */
    protected final void addPositionToTelemetry(){
        RobotPosition position = getPosition();
        position.addToTelemetry(telemetry, false);
    }
}
