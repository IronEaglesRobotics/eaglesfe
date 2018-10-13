package eaglesfe.flightrecorder;

import eaglesfe.common.VisionBasedRobotPosition;

public class KeyFrame {

    private VisionBasedRobotPosition visionBasedRobotPosition;
    private EncoderPositionRecorder encoderPositions;
    private SensorValueRecorder sensorValueRecorder;

    public KeyFrame(VisionBasedRobotPosition visionBasedRobotPosition, EncoderPositionRecorder encoderPositions, SensorValueRecorder sensorValueRecorder) {
        this.visionBasedRobotPosition = visionBasedRobotPosition;
        this.encoderPositions = encoderPositions;
        this.sensorValueRecorder = sensorValueRecorder;
    }

    private void updateKeyFrameValues() {
        encoderPositions.recordEncoderPositions();
        sensorValueRecorder.getSensorValues();
    }


}
