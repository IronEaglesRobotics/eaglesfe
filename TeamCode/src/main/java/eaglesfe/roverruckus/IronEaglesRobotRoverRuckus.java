package eaglesfe.roverruckus;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.FORWARD;
import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.REVERSE;

import eaglesfe.common.Arms;
import eaglesfe.common.MecanumDrive;

public class IronEaglesRobotRoverRuckus {

    private HardwareMap _hardwareMap;
    private MecanumDrive _drive;
    private Arms _Arms;

    public IronEaglesRobotRoverRuckus(HardwareMap hardwareMap){
        this._hardwareMap = hardwareMap;
        initializeHardware();
    }

    public MecanumDrive get_drive() {
        return _drive;
    }

    public Arms get_Arms() {
        return _Arms;
    }
    private void initializeHardware(){
        // Initialize Drive Base
        DcMotor frontLeft = this._hardwareMap.get(DcMotor.class, Constants.frontLeft_Name);
        DcMotor frontRight = this._hardwareMap.get(DcMotor.class, Constants.frontRight_Name);
        DcMotor backLeft = this._hardwareMap.get(DcMotor.class, Constants.backLeft_Name);
        DcMotor backRight = this._hardwareMap.get(DcMotor.class, Constants.backRight_Name);
        DcMotor lift = this._hardwareMap.get(DcMotor.class, Constants.Lift);
        DcMotor armAngle = this._hardwareMap.get(DcMotor.class, Constants.Arm);
        DcMotor extend = this._hardwareMap.get(DcMotor.class, Constants.Extend);
        Servo collectorLeft = this._hardwareMap.get(Servo.class, Constants.CollectLeft);
        Servo collectorRight = this._hardwareMap.get(Servo.class, Constants.CollectRight);
        Servo sensorStick = this._hardwareMap.get(Servo.class, Constants.sensorStick);
        ColorSensor sample = this._hardwareMap.get(ColorSensor.class, Constants.sample);
        BNO055IMU internalGyro = this._hardwareMap.get(BNO055IMU.class, Constants.gyro);
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.loggingEnabled = true;
        parameters.loggingTag     = "IMU";
        internalGyro.initialize(parameters);

        frontLeft.setDirection(FORWARD);
        frontRight.setDirection(FORWARD);
        backLeft.setDirection(REVERSE);
        backRight.setDirection(REVERSE);
        lift.setDirection(FORWARD);
        armAngle.setDirection(FORWARD);
        extend.setDirection(FORWARD);

        _drive = new MecanumDrive(frontLeft, frontRight, backLeft, backRight, internalGyro);
        _Arms = new Arms(lift, armAngle, extend, collectorLeft, collectorRight, sensorStick, sample);
    }

    public class Constants {
        static final String frontLeft_Name = "FrontLeft";
        static final String frontRight_Name = "FrontRight";
        static final String backLeft_Name = "BackLeft";
        static final String backRight_Name = "BackRight";
        static final String Lift = "Lift";
        static final String Arm = "Arm";
        static final String Extend = "Extend";
        static final String CollectLeft = "CollectLeft";
        static final String CollectRight = "CollectRight";
        static final String sensorStick = "sensorStick";
        static final String sample = "sample";
        static final String gyro = "imu";
    }
}