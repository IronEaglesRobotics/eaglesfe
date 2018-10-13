package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import eaglesfe.flightrecorder.EncoderPositionRecorder;

public class JsonPojoTestTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void firstTest() {
        assert(true);
    }

/*    @Test
    public void encodersToJson() {
        EncoderPositionRecorder encoderPositionRecorder = new EncoderPositionRecorder();

        Map<String, Integer> positions = new HashMap<>();
        positions.put("Motor1", 0);
        positions.put("Motor2", 234235);
        encoderPositionRecorder.setEncoderPositions(positions);

        String json = encoderPositionRecorder.toJson();
        System.out.println(json);

        Map<String, Integer> positionsMap = EncoderPositionRecorder.fromJson(json);
        System.out.println(positionsMap.toString());
    }*/

    @Test
    public void writeKeyFrames() {

    }
}