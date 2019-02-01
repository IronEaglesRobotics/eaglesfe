package eaglesfe.roverruckus.opmodes.teleop;

import com.eaglesfe.birdseye.BirdseyeServer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.io.IOException;

import eaglesfe.roverruckus.Robot;

@TeleOp(name="Presentation Demo", group ="Competition")
public class PresentationDemo extends OpMode {

    BirdseyeServer server;
    Robot robot;

    @Override
    public void init() {
        server  =   new BirdseyeServer(3708, telemetry);
        robot   =   new Robot(hardwareMap);

        robot.setVisionEnabled(true);
        robot.useRearCamera();
        server.start();
    }

    private long nextUpdate = 0;

    @Override
    public void loop() {
        if (this.getRuntime() >= nextUpdate) {
            nextUpdate += 250;
        }
        else {
            return;
        }

        try {
            server.addData("position", robot.getPosition());
            server.beginArray("gamepads");
            server.addData("gamepad1", this.gamepad1);
            server.addData("gamepad2", this.gamepad2);
            server.endArray();
            server.addData("gyro", robot.getGyroHeading180());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        server.stop();
    }
}
