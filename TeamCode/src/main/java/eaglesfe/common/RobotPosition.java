package eaglesfe.common;

import android.util.Log;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;

import static java.lang.Float.NaN;

public class RobotPosition {

    public static final RobotPosition UNKNOWN = new RobotPosition();

    public RobotPosition(float x, float y, float z, float roll, float pitch, float heading){
        this.x = x;
        this.y = y;
        this.z = z;
        this.roll = roll;
        this.pitch = pitch;
        this.heading = heading;
    }

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

    private RobotPosition(VectorF translation, Orientation rotation){
        this.x = translation.get(0) / VuforiaBase.MM_PER_INCH;
        this.y = translation.get(1) / VuforiaBase.MM_PER_INCH;
        this.z = translation.get(2) / VuforiaBase.MM_PER_INCH;

        this.roll = rotation.firstAngle * -1;
        this.pitch = rotation.secondAngle * -1;
        this.heading = rotation.thirdAngle;
    }

    public boolean isKnown (){
        return this != UNKNOWN;
    }

    public void addToTelemetry(Telemetry telemetry, boolean immediateUpdate){
        boolean isKnown = isKnown();
        String positionFormatter = isKnown ? "X: %-10.1f Y: %-10.1f Z: %-10.1f" : "Unknown";
        String rotationFormatter = isKnown ? "R: %-10.1f P: %-10.1f H: %-10.1f" : "Unknown";
        telemetry.addData("", positionFormatter, x, y, z);
        telemetry.addData("", rotationFormatter, roll, pitch, heading);

        if (immediateUpdate){
            telemetry.update();
        }
    }

    public void addToBirdseye(BirdseyeServer birdseyeServer){
        JSONObject obj = asJSONObject();
        birdseyeServer.broadcast(obj);
    }

    public JSONObject asJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("x", x);
            obj.put("y", y);
            obj.put("z", z);
            obj.put("pitch", pitch);
            obj.put("roll", roll);
            obj.put("heading", heading);
        } catch (JSONException e){
            Log.e("ROBOT_POSITION", e.getMessage());
        }

        return obj;
    }

    /* To help in constructing a mental model, these four properties are relative to the FIELD's coordinate system... */
    public float x;
    public float y;
    public float z;
    public float heading;

    /* ... and these two properties are relative to the ROBOT's coordinate system */
    public float pitch;
    public float roll;
}
