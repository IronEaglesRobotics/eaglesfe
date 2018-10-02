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
    }

    public void updateMotors(double x, double y, double z){
        double flPower, frPower, blPower, brPower;

        flPower = x + y + z;
        frPower = x - y - z;
        blPower = x - y + z;
        brPower = x + y - z;

        double max = Math.max(flPower, Math.max(frPower, Math.max(blPower, brPower)));

        flPower /= max;
        frPower /= max;
        blPower /= max;
        brPower /= max;

        frontLeft.setPower(flPower);
        frontRight.setPower(frPower);
        backLeft.setPower(blPower);
        backRight.setPower(brPower);
    }
}
