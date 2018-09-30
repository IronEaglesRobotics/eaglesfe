package eaglesfe.common;

import android.graphics.Point;

public class MathHelpers {

    public static double getDistanceBetweenTwoPoints (Point start, Point end) {
        float o = end.y - start.y;
        float a = end.x - start.x;
        return Math.sqrt(Math.pow(o, 2) + Math.pow(a, 2));
    }

    public static double getAngleBetweenTwoPoints(Point start, Point end) {
        float o = end.y - start.y;
        float a = end.x - start.x;

        double inRads = Math.atan2(o, a);
        return  (inRads >= 0 ? inRads : inRads + (2 * Math.PI)) * 180 / Math.PI;
    }
}

