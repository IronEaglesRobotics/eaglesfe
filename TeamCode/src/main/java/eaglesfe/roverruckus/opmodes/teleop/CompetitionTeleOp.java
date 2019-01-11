package eaglesfe.roverruckus.opmodes.teleop;
        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

        import eaglesfe.roverruckus.Robot;

@TeleOp(name="TeleOp", group ="Competition")
public class CompetitionTeleOp extends OpMode {

    Robot robot;
    private boolean bPrev;
    private boolean xPrev;
    private boolean collectorLeft;
    private boolean collectorRight;

    @Override
    public void init() {

        start = System.currentTimeMillis();
        robot = new Robot(hardwareMap);
        /* Do hardware initialization stuff here */
    }

    private long start;

    @Override
    public void loop() {
        double x = Math.pow(gamepad1.left_stick_x, 3);
        double y = -Math.pow(gamepad1.left_stick_y, 3);
        double z = Math.pow(gamepad1.right_stick_x, 3);
        float liftUp = gamepad1.right_trigger;
        float liftDown = gamepad1.left_trigger;
        float extendOut = gamepad2.right_trigger;
        float extendIn = gamepad2.left_trigger;
        float collectorUp = gamepad2.left_stick_y;
        if (gamepad2.b && !bPrev) {
            collectorLeft = !collectorLeft;
        }
        if (gamepad2.x && !xPrev) {
            collectorRight = !collectorRight;
        }

        bPrev = gamepad2.b;
        xPrev = gamepad2.x;

        robot.setDriveInput(x, y, z);
        robot.setExtendSpeed(extendOut - extendIn);
        robot.setArmSpeed(collectorUp);
        robot.setLiftSpeed(liftDown - liftUp);
        robot.collect(collectorLeft, collectorRight);
        telemetry.addData("Arm Position",robot.getArmPosition());
        telemetry.addData("Left Collector", robot.getCollectorLeftPosition());
        telemetry.addData("Right Collector", robot.getCollectorRightPosition());

        telemetry.update();
    }
}