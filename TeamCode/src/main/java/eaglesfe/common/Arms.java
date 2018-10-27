package eaglesfe.common;

import  com.qualcomm.robotcore.hardware.DcMotor;

public class Arms {

    private DcMotor Lift;
    private DcMotor Collector;
    private DcMotor Extend;

    public Arms(DcMotor Lift, DcMotor Collector, DcMotor Extend) {
        this.Lift = Lift;
        this.Collector = Collector;
        this.Extend = Extend;

        this.Lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.Collector.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.Extend.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        this.Lift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.Collector.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.Extend.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void updateArms(float lu, float ld, boolean cu, boolean cd, float eo, float ei) {

        Lift.setPower(lu - ld);
        Extend.setPower(eo - ei);

        if (cu && !cd) {
            Collector.setPower(1);
        } else if (!cu && cd) {
            Collector.setPower(-1);
        } else {
            Collector.setPower(0);
        }








    }

}

