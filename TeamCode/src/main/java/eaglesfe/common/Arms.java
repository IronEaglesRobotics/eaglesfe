package eaglesfe.common;

import  com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Arms {

    private DcMotor Lift;
    private DcMotor Collector;
    private DcMotor Extend;
    private Servo CollectorLeft;
    private Servo CollectorRight;
    boolean leftLast;
    boolean rightLast;
    boolean bothLast;

    public Arms(DcMotor Lift, DcMotor Collector, DcMotor Extend, Servo CollectorLeft, Servo CollectorRight) {
        this.Lift = Lift;
        this.Collector = Collector;
        this.Extend = Extend;
        this.CollectorLeft = CollectorLeft;
        this.CollectorRight = CollectorRight;

        this.Lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.Collector.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.Extend.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.Lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.Collector.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.Extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.CollectorLeft.setDirection(Servo.Direction.REVERSE);
        this.CollectorRight.setDirection(Servo.Direction.FORWARD);

        this.CollectorLeft.scaleRange(0, .75);
        this.CollectorRight.scaleRange(0, .75);

        this.CollectorLeft.setPosition(0);
        this.CollectorRight.setPosition(0);
    }

    public void updateArms(float liftUp, float liftDown, boolean collectorUp, boolean collectorDown, float extendOut,
                           float extendIn, boolean collectorLeft, boolean collectorRight) {

        boolean isLeftOpen = CollectorLeft.getPosition() > 0.5;
        boolean isRightOpen = CollectorRight.getPosition() > 0.5;

        if (collectorLeft && !leftLast) {
            CollectorLeft.setPosition(isLeftOpen ? 0 : 1);
        }

        if (collectorRight && !rightLast) {
            CollectorRight.setPosition(isRightOpen ? 0 : 1);
        }

        Lift.setPower(liftUp - liftDown);
        Extend.setPower(extendOut - extendIn);

        if (collectorUp && !collectorDown) {
            Collector.setPower(1);
        } else if (!collectorUp && collectorDown) {
            Collector.setPower(-1);
        } else {
            Collector.setPower(0);
        }

        this.leftLast = collectorLeft;
        this.rightLast = collectorRight;
    }

    public double getCollectorLeft() {
        return CollectorLeft.getPosition();
    }

    public double getCollectorRight() {
        return CollectorRight.getPosition();
    }
}
