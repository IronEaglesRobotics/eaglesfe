package eaglesfe.flightrecorder;

import android.support.annotation.VisibleForTesting;
import android.text.format.DateFormat;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.rits.cloning.Cloner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import eaglesfe.common.VisionBasedRobotPosition;

import static java.lang.Double.NaN;

public class KeyFrames {

    private List<RobotSnapshot> keyFrames;
    Cloner cloner=new Cloner();

    public KeyFrames() {
        keyFrames = new ArrayList<>();
    }

    public void addKeyFrame(RobotSnapshot snapshot) {
        keyFrames.add(cloner.deepClone(snapshot));
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

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(System.currentTimeMillis());
        String date = DateFormat.format("dd-MM-yyyy-hh-mm-ss", cal).toString();

        File path = opMode.hardwareMap.appContext.getFilesDir();
        File file = new File(path, date + ".json");
        keyFramesToFile(file);
    }

    public static KeyFrames keyFramesFileToObject(File file) {
        final ObjectMapper mapper = new ObjectMapper();
        KeyFrames keyFrames = new KeyFrames();
        try {
            List<RobotSnapshot> frames = mapper.readValue(file, new TypeReference<List<RobotSnapshot>>(){});
            keyFrames.setKeyFrames(frames);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keyFrames;

    }

    public void calculateKeyFrameDifference(int index) {
        if (keyFrames.get(index) != null && keyFrames.get(index-1) != null) {
            RobotSnapshot currentSnapshot = keyFrames.get(index);
            RobotSnapshot previousSnapshot = keyFrames.get(index-1);
            if (currentSnapshot.getVisionBasedRobotPosition().x != NaN && previousSnapshot.getVisionBasedRobotPosition().x != NaN) {
                //Do vision based movement
                System.out.println("Vision");
            } else if (Math.abs(currentSnapshot.getSensorBasedRobotPosition().getSensorValues().get("gyro") - previousSnapshot.getSensorBasedRobotPosition().getSensorValues().get("gyro")) > 10) {
                //If the robot turned more than 10 degrees, assume it's a gyro-based movement
                System.out.println("Sensor");
            } else {
                //Fall back to encoder based movements
                System.out.println("Encoder");
            }
        }
    }

    public List<RobotSnapshot> getKeyFrames() {
        return keyFrames;
    }

    public void setKeyFrames(List<RobotSnapshot> keyFrames) {
        this.keyFrames = keyFrames;
    }

    @Override
    public String toString() {
        return "KeyFrames{" +
                "keyFrames=" + keyFrames.toString() +
                '}';
    }
}
