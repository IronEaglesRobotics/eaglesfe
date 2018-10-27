package eaglesfe.flightrecorder;

import eaglesfe.common.VisionBasedRobotPosition;

public class RobotSnapshot {

    private long timestamp;
    private VisionBasedRobotPosition visionBasedRobotPosition;
    private EncoderBasedRobotPosition encoderBasedRobotPosition;
    private SensorBasedRobotPosition sensorBasedRobotPosition;

    public RobotSnapshot() {
    }

    public RobotSnapshot(VisionBasedRobotPosition visionBasedRobotPosition, EncoderBasedRobotPosition encoderBasedRobotPosition, SensorBasedRobotPosition sensorBasedRobotPosition) {
        this.visionBasedRobotPosition = visionBasedRobotPosition;
        this.encoderBasedRobotPosition = encoderBasedRobotPosition;
        this.sensorBasedRobotPosition = sensorBasedRobotPosition;
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public VisionBasedRobotPosition getVisionBasedRobotPosition() {
        return visionBasedRobotPosition;
    }

    public EncoderBasedRobotPosition getEncoderBasedRobotPosition() {
        return encoderBasedRobotPosition;
    }

    public SensorBasedRobotPosition getSensorBasedRobotPosition() {
        return sensorBasedRobotPosition;
    }

    @Override
    public String toString() {
        return "RobotSnapshot{" +
                "timestamp=" + timestamp +
                ", visionBasedRobotPosition=" + visionBasedRobotPosition.toString() +
                ", encoderBasedRobotPosition=" + encoderBasedRobotPosition.toString() +
                ", sensorBasedRobotPosition=" + sensorBasedRobotPosition.toString() +
                '}';
    }
}
