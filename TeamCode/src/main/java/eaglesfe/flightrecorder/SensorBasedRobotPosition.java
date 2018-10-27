package eaglesfe.flightrecorder;

import java.util.HashMap;
import java.util.Map;

public class SensorBasedRobotPosition {

    private Map<String, Double> sensorValues = new HashMap<>();

    public Map<String, Double> getSensorValues() {
        return sensorValues;
    }

    public void setSensorValues(Map<String, Double> sensorValues) {
        this.sensorValues = sensorValues;
    }

    public void put(String sensorName, Double sensorValue) {
        sensorValues.put(sensorName, sensorValue);
    }

    @Override
    public String toString() {
        return "SensorBasedRobotPosition{" +
                "sensorValues=" + sensorValues.toString() +
                '}';
    }
}
