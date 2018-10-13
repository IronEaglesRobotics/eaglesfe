package eaglesfe.flightrecorder;

import java.util.HashMap;
import java.util.Map;

public class SensorBasedRobotPosition {

    private Map<String, Double> sensorValues;

    public Map<String, Double> getSensorValues() {
        return sensorValues;
    }

    public void setSensorValues(Map<String, Double> sensorValues) {
        this.sensorValues = sensorValues;
    }

    public void put(String sensorName, Double sensorValue) {
        sensorValues.put(sensorName, sensorValue);
    }
}
