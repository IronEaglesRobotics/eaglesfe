package eaglesfe.roverruckus.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.internal.opengl.models.Geometry;

import eaglesfe.flightrecorder.EncoderBasedRobotPosition;
import eaglesfe.flightrecorder.EncoderPositionRecorder;
import eaglesfe.flightrecorder.KeyFrames;
import eaglesfe.flightrecorder.RobotSnapshot;
import eaglesfe.flightrecorder.SensorBasedRobotPosition;
import eaglesfe.flightrecorder.SensorValueRecorder;

import eaglesfe.common.VisionBasedRobotPosition;
import eaglesfe.roverruckus.util.RoverRuckusBirdseyeTracker;

//   |----------------|
// B |       X+       | R
// L |                | E
// U | Y+          Y- | D
// E |       X-       |
//   |________________|

public abstract class RoverRuckusBirdseyeTeleop extends OpMode {

    private EncoderPositionRecorder encoderPositionRecorder;
    private SensorValueRecorder sensorValueRecorder;
    private VisionBasedRobotPosition position = VisionBasedRobotPosition.UNKNOWN;
    private EncoderBasedRobotPosition encoderBasedRobotPosition;
    private SensorBasedRobotPosition sensorBasedRobotPosition;
    private KeyFrames keyFrames = new KeyFrames();
    private boolean isFlightRecorder = false;

    private RoverRuckusBirdseyeTracker tracker;
    protected VisionBasedRobotPosition positionEstimator = VisionBasedRobotPosition.UNKNOWN;


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

    public boolean isFlightRecorder() {
        return isFlightRecorder;
    }

    public void setFlightRecorder(boolean flightRecorder) {
        isFlightRecorder = flightRecorder;
    }

    // Make sure you call super.init() in your derived class.
    @Override
    public void init() {
        //Vuforia position data
        Geometry.Point3 cameraPosition = getCameraPositionOnRobot();
        tracker = new RoverRuckusBirdseyeTracker(cameraPosition.y, cameraPosition.x, cameraPosition.z, getCameraAngle());
        tracker.initialize(this.hardwareMap, shouldUseWebcam(), shouldShowCameraPreview());
        tracker.start();

        //Encoder recording
        encoderPositionRecorder = new EncoderPositionRecorder(this.hardwareMap);
        encoderPositionRecorder.init();

        //Sensor recording, really just gyro
        sensorValueRecorder = new SensorValueRecorder(this.hardwareMap);
        sensorValueRecorder.getSensorValues();
    }

    // Make sure you call super.loop() in your derived class.
    @Override
    public void loop() {
        position = tracker.getCurrentOrLastKnownPosition();
        encoderBasedRobotPosition = encoderPositionRecorder.getEncoderPositions();
        sensorBasedRobotPosition = sensorValueRecorder.getSensorValues();

        /*
        Hit the a button to save a frame. Hit the y button to finalize the program.
         */
        if (isFlightRecorder && gamepad1.a) {
            RobotSnapshot robotSnapshot = new RobotSnapshot(position, encoderBasedRobotPosition, sensorBasedRobotPosition);
            this.telemetry.addData("Writing keyframe: ", robotSnapshot.toString());
            keyFrames.addKeyFrame(robotSnapshot);
        }
        if (isFlightRecorder && gamepad1.y) {
            this.telemetry.addLine("Finalizing flight to file");
            keyFrames.keyFramesToFile(this);
        }
    }

    /**
     * Adds the current XYZ and PRH information to the telemetry. Does not call telemetry.update().
     */
    public void addPositionToTelemetry(){
        position.addToTelemetry(telemetry, false);
    }
}
