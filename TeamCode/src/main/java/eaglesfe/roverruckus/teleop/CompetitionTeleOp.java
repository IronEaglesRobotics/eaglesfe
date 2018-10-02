package eaglesfe.roverruckus.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import eaglesfe.common.MecanumDrive;
import eaglesfe.common.PositionAwareTeleOp;

@TeleOp(name="TeleOp", group ="Competition")
public class CompetitionTeleOp extends PositionAwareTeleOp {

    private MecanumDrive _drive;

    @Override
    public void init() {
        super.init();

        _drive = new MecanumDrive(
                this.hardwareMap.get(DcMotor.class, "FrontLeft"),
                this.hardwareMap.get(DcMotor.class, "FrightRight"),
                this.hardwareMap.get(DcMotor.class, "BackLeft"),
                this.hardwareMap.get(DcMotor.class, "BackRight"));

        /* Do hardware initialization stuff here */
    }

    @Override
    public void loop() {
        super.loop();

        /* Handle gamepad / driver input here */
        /* Use 'this.position' to determine your position on the field */
        double x = gamepad1.left_stick_x;
        double y = gamepad1.left_stick_y;
        double z = gamepad1.right_stick_x;
        _drive.updateMotors(x, y, z);
    }
}
