package eaglesfe.common;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;

import static java.lang.Float.NaN;

public class RobotPosition {

    public static final RobotPosition UNKNOWN = new RobotPosition();

    protected RobotPosition(){
        this.x = NaN;
        this.y = NaN;
        this.z = NaN;
        this.roll = NaN;
        this.pitch = NaN;
        this.heading = NaN;
    }

    RobotPosition(OpenGLMatrix matrix) {
        this(matrix.getTranslation(), Orientation.getOrientation(matrix, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES));
    }

    RobotPosition(VectorF translation, Orientation rotation){
        this.x = translation.get(0) / VuforiaBase.MM_PER_INCH;
        this.y = translation.get(1) / VuforiaBase.MM_PER_INCH;
        this.z = translation.get(2) / VuforiaBase.MM_PER_INCH;

        this.roll = rotation.firstAngle;
        this.pitch = rotation.secondAngle;
        this.heading = rotation.thirdAngle;
    }

    public boolean isKnown (){
        return this != UNKNOWN;
    }

    public void addToTelemetry(Telemetry telemetry, boolean immediateUpdate){
        boolean isKnown = isKnown();
        String positionFormatter = isKnown ? "{X, Y, Z} = %.1f, %.1f, %.1f" : "Unknown";
        String rotationFormatter = isKnown ? "{R, P, H} = %.0f, %.0f, %.0f" : "Unknown";
        telemetry.addData("Pos (in)", positionFormatter, x, y, z);
        telemetry.addData("Rot (deg)", rotationFormatter, roll, pitch, heading);

        if (immediateUpdate){
            telemetry.update();
        }
    }

    public float x;
    public float y;
    public float z;
    public float pitch;
    public float roll;
    public float heading;
}
