package eaglesfe.flightrecorder;

import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorValueRecorder {

    private HardwareMap hardwareMap;

    public SensorValueRecorder(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    public SensorBasedRobotPosition getSensorValues() {

        SensorBasedRobotPosition sensorBasedRobotPosition = new SensorBasedRobotPosition();
        final List<GyroSensor> gyroSensors = hardwareMap.getAll(GyroSensor.class);
        for (GyroSensor gyroSensor : gyroSensors) {
            String sensorName = hardwareMap.getNamesOf(gyroSensor).iterator().next();
            int angle = gyroSensor.getHeading();
            sensorBasedRobotPosition.put(sensorName, (double) angle);
        }

        return sensorBasedRobotPosition;
    }

}
