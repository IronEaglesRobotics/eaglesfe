package eaglesfe.roverruckus.teleop;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import eaglesfe.common.BirdseyeServer;
import eaglesfe.common.RobotPosition;

@TeleOp(name="Birdseye Test", group ="Experimental")
public class BirdseyeTest extends OpMode {

    protected BirdseyeServer birdseye;

    private RobotPosition debugData = new RobotPosition(0,0,0,0,0,0);
    private boolean debugAxisZig = true;
    private String debugAxis = "X";

    @Override
    public void init() {
        birdseye = BirdseyeServer.GetInstance(this.telemetry);
        birdseye.start();
        debugTimer.scheduleAtFixedRate(debugTask, 0, 100);
    }

    @Override
    public void loop() {

    }

    private Timer debugTimer = new Timer();
    private TimerTask debugTask = new TimerTask() {
        @Override
        public void run() {
            switch (debugAxis) {
                case "X":
                    debugData.x = debugAxisZig ? debugData.x + 4 : debugData.x - 4;
                    if (Math.abs(debugData.x) >= 60) {
                        debugAxis = "Y";
                        debugAxisZig = debugData.y < 0;
                    }
                    break;
                case "Y":
                    debugData.y = debugAxisZig ? debugData.y + 4 : debugData.y - 4;
                    if (Math.abs(debugData.y) >= 60) {
                        debugAxis = "X";
                        debugAxisZig = debugData.x < 0;
                    }
                    break;
            }
            debugData.pitch = (debugData.pitch + 10) % 360;
            debugData.roll = (debugData.roll + 10) % 360;
            debugData.heading = (debugData.heading + 10) % 360;
            JSONObject json = debugData.asJSONObject();
            birdseye.broadcast(json);
        }
    };

    @Override
    public void stop() {
        try {
            birdseye.stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
