package eaglesfe.common;

import  com.qualcomm.robotcore.hardware.DcMotor;

public class Arms {

    private DcMotor Lift;
    private DcMotor Collector;

    public Arms(DcMotor Lift, DcMotor Collector) {
        this.Lift = Lift;
        this.Collector = Collector;

        this.Lift.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.Collector.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void updateArms(boolean lu, boolean ld, boolean cu, boolean cd) {

        if (lu && !ld) {
            Lift.setPower(1);
        } else if (!lu && ld) {
            Lift.setPower(-1);
        } else {
            Lift.setPower(0);
        }

        if (cu && !cd) {
            Collector.setPower(1);
        } else if (!cu && cd) {
            Collector.setPower(-1);
        } else {
            Collector.setPower(0);
        }
    }

}

