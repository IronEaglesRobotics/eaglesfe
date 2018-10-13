package eaglesfe.flightrecorder;

import android.support.annotation.VisibleForTesting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eaglesfe.common.VisionBasedRobotPosition;

public class KeyFrames {

    List<RobotSnapshot> keyFrames;

    public KeyFrames(OpMode opMode) {
        keyFrames = new ArrayList<>();
    }

    public void addKeyFrame(RobotSnapshot snapshot) {
        keyFrames.add(snapshot);
    }

    @VisibleForTesting
    public void keyFramesToFile(File file) {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(file, keyFrames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void keyFramesToFile(OpMode opMode) {

        File path = opMode.hardwareMap.appContext.getFilesDir();
        File file = new File(path, "my-file-name.txt");
        keyFramesToFile(file);
    }


}
