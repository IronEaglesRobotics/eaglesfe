package eaglesfe.flightrecorder;

import eaglesfe.common.VisionBasedRobotPosition;

public class RobotSnapshot {

    private long timestamp;
    private VisionBasedRobotPosition visionBasedRobotPosition;
    private EncoderBasedRobotPosition encoderBasedRobotPosition;
    private SensorBasedRobotPosition sensorBasedRobotPosition;

    public RobotSnapshot(VisionBasedRobotPosition visionBasedRobotPosition, EncoderBasedRobotPosition encoderBasedRobotPosition, SensorBasedRobotPosition sensorBasedRobotPosition) {
        this.visionBasedRobotPosition = visionBasedRobotPosition;
        this.encoderBasedRobotPosition = encoderBasedRobotPosition;
        this.sensorBasedRobotPosition = sensorBasedRobotPosition;
        this.timestamp = System.currentTimeMillis();
    }
}
