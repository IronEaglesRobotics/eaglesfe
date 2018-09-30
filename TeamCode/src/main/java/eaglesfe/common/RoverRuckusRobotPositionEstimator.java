package eaglesfe.common;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import java.util.ArrayList;
import java.util.List;

public class RoverRuckusRobotPositionEstimator
{
    private static final String VUFORIA_KEY     = "AUmjH6X/////AAABmeSd/rs+aU4giLmf5DG5vUaAfHFLv0/vAnAFxt5vM6cbn1/nI2sdkRSEf6HZLA/is/+VQY5/i6u5fbJ4TugEN8HOxRwvUvkrAeIpgnMYEe3jdD+dPxhE88dB58mlPfVwIPJc2KF4RE7weuRBoZ8KlrEKbNNu20ommdG7S/HXP9Kv/xocj82rgj+iPEaitftALZ6QaGBdfSl3nzVMK8/KgQJNlSbGic/Wf3VI8zcYmMyDslQPK45hZKlHW6ezxdGgJ7VJCax+Of8u/LEwfzqDqBsuS4/moNBJ1mF6reBKe1hIE2ffVTSvKa2t95g7ht3Z4M6yQdsI0ZaJ6AGnl1wTlm8Saoal4zTbm/VCsmZI081h";
    private static final float mmTargetHeight   = 6 * VuforiaBase.MM_PER_INCH;          // the height of the center of the target image above the floor

    private List<VuforiaTrackable> trackables;
    private VuforiaTrackables targetsRoverRuckus;
    private boolean isActive;
    private boolean isInitialized;
    OpenGLMatrix lastLocation;

    private int cameraForwardOffset;
    private int cameraVerticalOffset;
    private int cameraLeftOffset;

    RoverRuckusRobotPositionEstimator() {
        this(0,0,0);
    }

    RoverRuckusRobotPositionEstimator(int cameraForwardOffset, int cameraLeftOffset, int cameraVerticalOffset) {
        this.cameraForwardOffset = cameraForwardOffset;
        this.cameraVerticalOffset = cameraVerticalOffset;
        this.cameraLeftOffset = cameraLeftOffset;
    }

    public void initialize(HardwareMap hardwareMap, VuforiaLocalizer.CameraDirection camera, boolean preview){

        VuforiaLocalizer.Parameters parameters;

        if (preview){
            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        }
        else {
            parameters = new VuforiaLocalizer.Parameters();
        }

        parameters.vuforiaLicenseKey = VUFORIA_KEY ;
        parameters.cameraDirection   = camera;

        //  Instantiate the Vuforia engine
        VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the data sets that for the trackable objects. These particular data
        // sets are stored in the 'assets' part of our application.
        targetsRoverRuckus = vuforia.loadTrackablesFromAsset("RoverRuckus");

        VuforiaTrackable blueRover = targetsRoverRuckus.get(0);
        blueRover.setName("Blue-Rover");
        OpenGLMatrix blueRoverLocationOnField = OpenGLMatrix
                .translation(0, VuforiaBase.MM_FTC_FIELD_WIDTH, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90, 0, 0));
        blueRover.setLocation(blueRoverLocationOnField);

        VuforiaTrackable redFootprint = targetsRoverRuckus.get(1);
        redFootprint.setName("Red-Footprint");
        OpenGLMatrix redFootprintLocationOnField = OpenGLMatrix
                .translation(0, -VuforiaBase.MM_FTC_FIELD_WIDTH, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90, 0, 180));
        redFootprint.setLocation(redFootprintLocationOnField);

        VuforiaTrackable frontCraters = targetsRoverRuckus.get(2);
        frontCraters.setName("Front-Craters");
        OpenGLMatrix frontCratersLocationOnField = OpenGLMatrix
                .translation(-VuforiaBase.MM_FTC_FIELD_WIDTH, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90, 0 , 90));
        frontCraters.setLocation(frontCratersLocationOnField);

        VuforiaTrackable backSpace = targetsRoverRuckus.get(3);
        backSpace.setName("Back-Space");
        OpenGLMatrix backSpaceLocationOnField = OpenGLMatrix
                .translation(VuforiaBase.MM_FTC_FIELD_WIDTH, 0, mmTargetHeight)
                .multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90, 0, -90));
        backSpace.setLocation(backSpaceLocationOnField);

        // For convenience, gather together all the trackable objects in one easily-iterable collection */
        trackables = new ArrayList<>();
        trackables.addAll(targetsRoverRuckus);

        OpenGLMatrix phoneLocationOnRobot = OpenGLMatrix
                .translation(cameraForwardOffset, cameraLeftOffset, cameraVerticalOffset)
                .multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.YZX, AngleUnit.DEGREES,
                        camera == CameraDirection.FRONT ? 90 : -90, 0, 0));

        for (VuforiaTrackable trackable : trackables)
        {
            ((VuforiaTrackableDefaultListener)trackable.getListener()).setPhoneInformation(phoneLocationOnRobot, parameters.cameraDirection);
        }

        isInitialized = true;
    }

    public void start()
    {
        assertInitialized();

        if (!isActive){
            targetsRoverRuckus.activate();
            isActive = true;
        }
    }

    public void stop()
    {
        assertInitialized();

        if (isActive) {
            targetsRoverRuckus.deactivate();
            isActive = false;
        }
    }

    public RobotPosition getCurrentOrLastKnownPosition() throws IllegalStateException {
        RobotPosition currentPosition = getCurrentPosition();
        return currentPosition.isKnown() ? currentPosition : new RobotPosition(lastLocation);
    }

    public RobotPosition getCurrentPosition() throws IllegalStateException {

        assertInitialized();
        assertTrackingStarted();

        // check all the trackable target to see which one (if any) is visible.
        for (VuforiaTrackable trackable : trackables) {
            VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener) trackable.getListener();
            if (listener.isVisible()) {

                // getUpdatedRobotLocation() will return null if no new information is available since
                // the last time that call was made, or if the trackable is not currently visible.
                OpenGLMatrix updatedRobotLocation = listener.getUpdatedRobotLocation();
                if (updatedRobotLocation != null) {
                    lastLocation = updatedRobotLocation;
                    return new RobotPosition(lastLocation);
                }
            }
        }
        return RobotPosition.UNKNOWN;
    }

    private void assertInitialized() throws IllegalStateException {
        if (!isInitialized){
            throw new IllegalStateException("Position estimator has not been initialized. Make sure you call initialize().");
        }
    }

    private void assertTrackingStarted() throws IllegalStateException {
        if (!isInitialized){
            throw new IllegalStateException("Position estimator is not tracking. Make sure you call start().");
        }
    }
}
