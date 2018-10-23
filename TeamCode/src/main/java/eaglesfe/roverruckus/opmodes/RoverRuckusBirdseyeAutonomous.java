package eaglesfe.roverruckus.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.internal.opengl.models.Geometry;

import eaglesfe.common.VisionBasedRobotPosition;
import eaglesfe.roverruckus.util.RoverRuckusBirdseyeTracker;

//   |----------------|
// B |       X+       | R
// L |                | E
// U | Y+          Y- | D
// E |       X-       |
//   |________________|

public abstract class RoverRuckusBirdseyeAutonomous extends LinearOpMode {
    private RoverRuckusBirdseyeTracker tracker;

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

    // Make sure you call super.runOpMode() in your derived class.
    @Override
    public void runOpMode() {
        Geometry.Point3 cameraPosition = getCameraPositionOnRobot();
        tracker = new RoverRuckusBirdseyeTracker(cameraPosition.y, cameraPosition.x, cameraPosition.z, getCameraAngle());
        tracker.initialize(hardwareMap, shouldUseWebcam(), shouldShowCameraPreview());
        tracker.start();
    }

    protected VisionBasedRobotPosition getPosition(){
        return tracker.getCurrentOrLastKnownPosition();
    }

    /**
     * Adds the current XYZ and PRH information to the telemetry. Does not call telemetry.update().
     */
    protected final void addPositionToTelemetry(){
        VisionBasedRobotPosition position = getPosition();
        position.addToTelemetry(telemetry, false);
    }
}
