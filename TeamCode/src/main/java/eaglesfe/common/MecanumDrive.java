package eaglesfe.common;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MecanumDrive {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private BNO055IMU internalGyro;

    public MecanumDrive(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight, BNO055IMU internalGyro){
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.internalGyro = internalGyro;

        this.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public void updateMotors(double x, double y, double z){

        double flPower, frPower, blPower, brPower;

        flPower = z + x - y;
        frPower = z + x + y;
        blPower = -z + x + y;
        brPower = -z + x - y;

        double max = (Math.abs(z) + Math.abs(y) + Math.abs(x));

        if (max < 1) {
            flPower /= 1;
            frPower /= 1;
            blPower /= 1;
            brPower /= 1;
        } else {
            flPower /= max;
            frPower /= max;
            blPower /= max;
            brPower /= max;
        }

        frontLeft.setPower(flPower);
        frontRight.setPower(frPower);
        backLeft.setPower(blPower);
        backRight.setPower(brPower);

    }

    public void updateDriveTime(double powerx,double powery,double powerz, long millis, long tStart) {

        while (System.currentTimeMillis() - tStart <= millis) {
            updateMotors(powerx, powery, powerz);
        }

        updateMotors(0,0,0);
    }

    public void updateDriveGyro(double powerz, float rStart, float rTarget, OpMode opMode) {
        rTarget = (float) Math.toRadians(rTarget);
        float angleDistance = Math.abs(rTarget - internalGyro.getAngularOrientation().firstAngle);
        while (Math.abs(internalGyro.getAngularOrientation().firstAngle - rStart) < angleDistance) {
            updateMotors(0,0,powerz);
            opMode.telemetry.addData("angle", internalGyro.getAngularOrientation().firstAngle);
            opMode.telemetry.addData("angle", internalGyro.getAngularOrientation().secondAngle);
            opMode.telemetry.addData("angle", internalGyro.getAngularOrientation().thirdAngle);
            opMode.telemetry.update();
        }
        updateMotors(0,0,0);
    }

    public float getCurrentAngle() {
        return internalGyro.getAngularOrientation().firstAngle;
    }
}
