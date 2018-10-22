package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import eaglesfe.common.VisionBasedRobotPosition;
import eaglesfe.flightrecorder.EncoderBasedRobotPosition;
import eaglesfe.flightrecorder.KeyFrames;
import eaglesfe.flightrecorder.RobotSnapshot;
import eaglesfe.flightrecorder.SensorBasedRobotPosition;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class KeyFramesTest {

    @Before
    public void setUp() throws Exception {


    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void writeKeyFrames() throws InterruptedException {

        KeyFrames keyFrames = new KeyFrames();
        EncoderBasedRobotPosition encoderBasedRobotPosition = new EncoderBasedRobotPosition();
        SensorBasedRobotPosition sensorBasedRobotPosition = new SensorBasedRobotPosition();
        VisionBasedRobotPosition visionBasedRobotPosition = new VisionBasedRobotPosition();
        keyFrames.addKeyFrame(new RobotSnapshot(visionBasedRobotPosition, encoderBasedRobotPosition, sensorBasedRobotPosition));
        System.out.println(keyFrames.toString());

        visionBasedRobotPosition.setAllValues(0,0,0,0,0,0);
        encoderBasedRobotPosition.put("Motor1", 0);
        encoderBasedRobotPosition.put("Motor2", 0);
        encoderBasedRobotPosition.put("Motor3", 0);
        encoderBasedRobotPosition.put("Motor4", 0);
        sensorBasedRobotPosition.put("gyro", 0.0);
        keyFrames.addKeyFrame(new RobotSnapshot(visionBasedRobotPosition, encoderBasedRobotPosition, sensorBasedRobotPosition));
        System.out.println(keyFrames.toString());

        visionBasedRobotPosition.setAllValues(1,2,3,4,5,6);
        encoderBasedRobotPosition.put("Motor1", 7);
        encoderBasedRobotPosition.put("Motor2", 8);
        encoderBasedRobotPosition.put("Motor3", 9);
        encoderBasedRobotPosition.put("Motor4", 10);
        sensorBasedRobotPosition.put("gyro", 11.0);
        keyFrames.addKeyFrame(new RobotSnapshot(visionBasedRobotPosition, encoderBasedRobotPosition, sensorBasedRobotPosition));
        System.out.println(keyFrames.toString());

        Thread.sleep(500);
        EncoderBasedRobotPosition encoderBasedRobotPosition2 = new EncoderBasedRobotPosition();
        SensorBasedRobotPosition sensorBasedRobotPosition2 = new SensorBasedRobotPosition();
        VisionBasedRobotPosition visionBasedRobotPosition2 = new VisionBasedRobotPosition();
        visionBasedRobotPosition2.setAllValues(30,20,0,90,0,0);
        encoderBasedRobotPosition2.put("Motor1", 1000);
        encoderBasedRobotPosition2.put("Motor2", 1000);
        encoderBasedRobotPosition2.put("Motor3", -1000);
        encoderBasedRobotPosition2.put("Motor4", -1000);
        sensorBasedRobotPosition2.put("gyro", 90.0);
        keyFrames.addKeyFrame(new RobotSnapshot(visionBasedRobotPosition2, encoderBasedRobotPosition2, sensorBasedRobotPosition2));

        System.out.println(keyFrames.toString());

        System.out.println("TmpDir: " + System.getProperty("java.io.tmpdir"));
        File outFile = new File(System.getProperty("java.io.tmpdir") + File.separator + "writeKeyFramesTest.json");
        keyFrames.keyFramesToFile(outFile);

        KeyFrames keyFramesFromFile = KeyFrames.keyFramesFileToObject(outFile);
        System.out.println(keyFramesFromFile.toString());

        assertThat(keyFramesFromFile, equalTo(keyFrames));
    }
}
