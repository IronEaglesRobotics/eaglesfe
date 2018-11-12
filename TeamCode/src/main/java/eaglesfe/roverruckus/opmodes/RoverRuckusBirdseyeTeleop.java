package eaglesfe.roverruckus.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.internal.opengl.models.Geometry;

import eaglesfe.common.BirdseyeServer;
import eaglesfe.common.FieldPosition;
import eaglesfe.roverruckus.util.RoverRuckusBirdseyeTracker;

//   |----------------|
// B |       X+       | R
// L |                | E
// U | Y+          Y- | D
// E |       X-       |
//   |________________|

public abstract class RoverRuckusBirdseyeTeleop extends OpMode {

    protected BirdseyeServer birdseye;
    private RoverRuckusBirdseyeTracker tracker;
    protected FieldPosition position = FieldPosition.UNKNOWN;

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

    public RoverRuckusBirdseyeTeleop() {
        this.msStuckDetectInit = 10000;
        this.msStuckDetectStart = 10000;
    }

    // Make sure you call super.init() in your derived class.
    @Override
    public void init() {
        birdseye = BirdseyeServer.GetInstance(3708, this.telemetry);
        birdseye.start();
        Geometry.Point3 cameraPosition = getCameraPositionOnRobot();
        tracker = new RoverRuckusBirdseyeTracker(cameraPosition.y, cameraPosition.x, cameraPosition.z, getCameraAngle());
    }

    @Override
    public void start() {
        tracker.initialize(hardwareMap, shouldUseWebcam(), shouldShowCameraPreview());
        tracker.start();
    }

    // Make sure you call super.loop() in your derived class.
    @Override
    public void loop() {
        position = tracker.getCurrentOrLastKnownPosition();
    }

    /**
     * Adds the current XYZ and PRH information to the telemetry. Does not call telemetry.update().
     */
    public void addPositionToTelemetry(){
        position.addToTelemetry(telemetry, false);

    }

}
