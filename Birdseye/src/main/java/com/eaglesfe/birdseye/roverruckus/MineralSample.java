package com.eaglesfe.birdseye.roverruckus;

import android.graphics.Point;
import android.graphics.Rect;

import com.vuforia.Rectangle;

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
    public Rect boundingBox;

    MineralSample(List<Recognition> recognitions) {
        this(recognitions, 0,0,0,0);
    }

    MineralSample(List<Recognition> recognitions, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.boundingBox = new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
        for (Recognition recognition : recognitions) {
            float totalWidth = recognition.getImageWidth();
            float totalHeight = recognition.getImageHeight();

            float left = (recognition.getLeft() / totalWidth) * 100;
            float right = (recognition.getRight() / totalWidth) * 100;
            float top = (recognition.getTop() / totalHeight) * 100;
            float bottom = (recognition.getBottom() / totalHeight) * 100;
            float width = recognition.getWidth();
            float height = recognition.getHeight();

            Point position = new Point((int)(left + (width / 2)), (int)(top + (height / 2)));

            boolean isWithinPadding = left > paddingLeft && top > paddingTop && right < paddingRight && bottom < paddingBottom;
            boolean isLargerThanMinimumSize = width > 10 && height < 10;

            if (isLargerThanMinimumSize && isWithinPadding) {
                sampleSize++;
                if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                    goldMineralLocations.add(position);
                    goldSampleSize++;
                    angleToGoldMineral = goldSampleSize == 1 ? recognition.estimateAngleToObject(AngleUnit.DEGREES) : Double.MIN_VALUE;
                } else {
                    silverMineralLocations.add(position);
                    silverSampleSize++;
                }

                boundingBox.left = left < boundingBox.left ? (int)left : boundingBox.left;
                boundingBox.right = right > boundingBox.right ? (int)right : boundingBox.right;
                boundingBox.top = top < boundingBox.top ? (int)top : boundingBox.top;
                boundingBox.bottom = bottom > boundingBox.bottom ? (int)bottom : boundingBox.bottom;
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