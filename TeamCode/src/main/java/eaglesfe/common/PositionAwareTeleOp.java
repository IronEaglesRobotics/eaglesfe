package eaglesfe.common;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.internal.opengl.models.Geometry;

//   |----------------|
// B |       X+       | R
// L |                | E
// U | Y+          Y- | D
// E |       X-       |
//   |________________|

public abstract class PositionAwareTeleOp extends OpMode {

    private RoverRuckusRobotPositionEstimator positionEstimator;
    protected RobotPosition position = RobotPosition.UNKNOWN;

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

    /**
     * Sets the Z angle of the tracking camera. 0 degrees = RIGHT, 90 = FORWARD, etc.
     */
    protected int getCameraAngle() { return 90; }

    // Make sure you call super.init() in your derived class.
    @Override
    public void init() {
        Geometry.Point3 cameraPosition = getCameraPositionOnRobot();
        positionEstimator = new RoverRuckusRobotPositionEstimator(cameraPosition.y, cameraPosition.x, cameraPosition.z, getCameraAngle());
        positionEstimator.initialize(this.hardwareMap, shouldUseWebcam(), shouldShowCameraPreview());
        positionEstimator.start();
    }

    // Make sure you call super.loop() in your derived class.
    @Override
    public void loop() {
        position = positionEstimator.getCurrentOrLastKnownPosition();
    }

    /**
     * Adds the current XYZ and PRH information to the telemetry. Does not call telemetry.update().
     */
    public void addPositionToTelemetry(){
        position.addToTelemetry(telemetry, false);

    }

}
