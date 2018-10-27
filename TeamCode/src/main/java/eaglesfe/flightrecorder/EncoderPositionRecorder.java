package eaglesfe.flightrecorder;

import android.support.annotation.VisibleForTesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EncoderPositionRecorder {

    private HardwareMap hardwareMap;

    public EncoderPositionRecorder(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
    }

    @VisibleForTesting
    public EncoderPositionRecorder() {
    }

    public void init() {
        final List<DcMotor> motors = hardwareMap.getAll(DcMotor.class);
        for (DcMotor motor : motors) {
            DcMotor.RunMode runMode = motor.getMode();
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(runMode);
        }

        getEncoderPositions();
    }

    public EncoderBasedRobotPosition getEncoderPositions() {

        EncoderBasedRobotPosition encoderBasedRobotPosition = new EncoderBasedRobotPosition();
        final List<DcMotor> motors = hardwareMap.getAll(DcMotor.class);
        for (DcMotor motor : motors) {
            String motorName = hardwareMap.getNamesOf(motor).iterator().next();
            int position = motor.getCurrentPosition();
            encoderBasedRobotPosition.put(motorName, position);
        }
        return encoderBasedRobotPosition;
    }
/*
    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(encoderPositions);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map<String, Integer> fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> positions = new HashMap<>();
        try {
            positions = objectMapper.readValue(json, new TypeReference<Map<String,Object>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return positions;
    }

    @Override
    public String toString() {
        return "EncoderPositionRecorder{" +
                "encoderPositions=" + encoderPositions.toString() +
                '}';
    }*/
}
