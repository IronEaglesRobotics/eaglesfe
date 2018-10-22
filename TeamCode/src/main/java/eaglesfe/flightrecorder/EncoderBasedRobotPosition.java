package eaglesfe.flightrecorder;

import java.util.HashMap;
import java.util.Map;

public class EncoderBasedRobotPosition {

    private Map<String, Integer> encoderPositions = new HashMap<>();

    public Map<String, Integer> getEncoderPositions() {
        return encoderPositions;
    }

    public void setEncoderPositions(Map<String, Integer> encoderPositions) {
        this.encoderPositions = encoderPositions;
    }

    public void put(String motorName, Integer encoderValue) {
        encoderPositions.put(motorName, encoderValue);
    }

    @Override
    public String toString() {
        return "EncoderBasedRobotPosition{" +
                "encoderPositions=" + encoderPositions.toString() +
                '}';
    }
}
