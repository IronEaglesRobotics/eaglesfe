package eaglesfe.common;

import com.qualcomm.robotcore.hardware.DcMotor;

public class MecanumDrive {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    public MecanumDrive(DcMotor frontLeft, DcMotor frontRight, DcMotor backLeft, DcMotor backRight){
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;

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
}
