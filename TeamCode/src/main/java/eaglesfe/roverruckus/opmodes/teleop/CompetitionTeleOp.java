package eaglesfe.roverruckus.opmodes.teleop;
        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

        import org.firstinspires.ftc.robotcore.internal.opengl.models.Geometry;

        import eaglesfe.roverruckus.IronEaglesRobotRoverRuckus;
        import eaglesfe.roverruckus.opmodes.RoverRuckusBirdseyeTeleop;
        import eaglesfe.roverruckus.Robot;

@TeleOp(name="TeleOp", group ="Competition")
public class CompetitionTeleOp extends OpMode {

    Robot robot;


    @Override
    public void init() {
//        super.init();

        start = System.currentTimeMillis();
        robot = new Robot(hardwareMap);
        /* Do hardware initialization stuff here */
    }

    private long start;

    @Override
    public void loop() {
//        super.loop();

        /* Handle gamepad / driver input here */
        /* Use 'this.position' to determine your position on the field */

        double x = Math.pow(gamepad1.left_stick_x, 3);
        double y = -Math.pow(gamepad1.left_stick_y, 3);
        double z = Math.pow(gamepad1.right_stick_x, 3);
        float liftUp = gamepad1.right_trigger;
        float liftDown = gamepad1.left_trigger;
        float extendOut = gamepad2.right_trigger;
        float extendIn = gamepad2.left_trigger;
        float collectorUp = gamepad2.left_stick_y;
        boolean collectorLeft = gamepad2.b;
        boolean collectorRight = gamepad2.x;

        robot.setDriveInput(x, y, z);
        robot.setExtendSpeed(extendOut - extendIn);
        robot.setArmSpeed(collectorUp);
        robot.setLiftSpeed(liftDown - liftUp);
        robot.collect(collectorLeft, collectorRight);
        telemetry.addData("Color:RGB",robot.getArmPosition());
        telemetry.addData("Left:", robot.getCollectorLeftPosition());
        telemetry.addData("Right", robot.getCollectorRightPosition());

//        if (System.currentTimeMillis() - start > 500) {
//            birdseye.broadcast(position.asJSONObject());
//            start = System.currentTimeMillis();
//        }

        telemetry.update();
    }
}