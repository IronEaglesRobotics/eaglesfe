package com.eaglesfe.birdseye.roverruckus;

import android.graphics.Point;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.tfod.TfodRoverRuckus.LABEL_GOLD_MINERAL;

public class MineralSample {
    // Intentionally public to obviate the need for a custom TypeAdapter for serialization
    public final List<Point> goldMineralLocations = new ArrayList<>();
    public final List<Point> silverMineralLocations = new ArrayList<>();
    public GoldMineralArrangement goldMineralArrangement;
    public int silverSampleSize = 0;
    public int goldSampleSize = 0;
    public int sampleSize = 0;
    public double angleToGoldMineral = Double.MIN_VALUE;


    MineralSample(List<Recognition> recognitions) {
        for (Recognition recognition : recognitions) {
            int x = (int)((recognition.getLeft() + (recognition.getWidth() / 2)) / recognition.getImageWidth() * 100);
            int y = (int)((recognition.getTop() + (recognition.getHeight() / 2)) / recognition.getImageHeight() * 100);
            if (x >= 10 && y >= 10) {
                sampleSize++;
                Point position = new Point(x, y);
                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                    goldMineralLocations.add(position);
                    goldSampleSize++;
                    angleToGoldMineral = goldSampleSize == 1 ? recognition.estimateAngleToObject(AngleUnit.DEGREES) : Double.MIN_VALUE;
                } else {
                    silverMineralLocations.add(position);
                    silverSampleSize++;
                }
            }
        }

        goldMineralArrangement = getGoldMineralArrangement();
    }

    private GoldMineralArrangement getGoldMineralArrangement() {
        if (goldMineralLocations.size() == 1 && silverMineralLocations.size() == 2) {
            double goldMineralX = goldMineralLocations.get(0).x;
            double silverMineral1X = silverMineralLocations.get(0).x;
            double silverMineral2X = silverMineralLocations.get(1).x;
            if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                    return GoldMineralArrangement.LEFT;
                } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                    return GoldMineralArrangement.RIGHT;
                } else {
                    return GoldMineralArrangement.CENTER;
                }
            }
        }

        return GoldMineralArrangement.UNKNOWN;
    }

    public enum GoldMineralArrangement {
        UNKNOWN, LEFT, CENTER, RIGHT
    }
}