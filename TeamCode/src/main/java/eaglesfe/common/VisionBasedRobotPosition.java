package eaglesfe.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

import java.util.Map;

import static java.lang.Float.NaN;

public class VisionBasedRobotPosition {

    public static final VisionBasedRobotPosition UNKNOWN = new VisionBasedRobotPosition();

    /* To help in constructing a mental model, these four properties are relative to the FIELD's coordinate system... */
    public float x;
    public float y;
    public float z;
    public float heading;

    /* ... and these two properties are relative to the ROBOT's coordinate system */
    public float pitch;
    public float roll;


    public VisionBasedRobotPosition() {}

    public void setAllValues(float x, float y, float z, float heading, float pitch, float roll) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.heading = heading;
        this.pitch = pitch;
        this.roll = roll;
    }

    public VisionBasedRobotPosition(OpenGLMatrix matrix) {
        this(matrix.getTranslation(), Orientation.getOrientation(matrix, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES));
    }

    private VisionBasedRobotPosition(VectorF translation, Orientation rotation){
        this.x = translation.get(0) / VuforiaBase.MM_PER_INCH;
        this.y = translation.get(1) / VuforiaBase.MM_PER_INCH;
        this.z = translation.get(2) / VuforiaBase.MM_PER_INCH;

        this.roll = rotation.firstAngle * -1;
        this.pitch = rotation.secondAngle * -1;
        this.heading = rotation.thirdAngle;
    }

    @JsonIgnore
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

    @Override
    public String toString() {
        return "VisionBasedRobotPosition{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", heading=" + heading +
                ", pitch=" + pitch +
                ", roll=" + roll +
                '}';
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

}
