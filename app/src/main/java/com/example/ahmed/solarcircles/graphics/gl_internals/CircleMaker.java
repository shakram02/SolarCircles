package com.example.ahmed.solarcircles.graphics.gl_internals;

public class CircleMaker {
    private static final int COORDINATE_COUNT = 3;

    public static float[] CreateCirclePoints(float cx, float cy, float r, int num_segments) {

        double theta = (2 * 3.1415926) / num_segments;
        double c = Math.cos(theta);
        double s = Math.sin(theta);
        double t;

        float x = r;//we start at angle = 0
        float y = 0;

        float[] points = new float[COORDINATE_COUNT * num_segments];

        for (int i = 0; i < (COORDINATE_COUNT * num_segments); i += COORDINATE_COUNT) {
            // X, Y, Z
            points[i] = (x + cx);
            points[i + 1] = (y + cy);
            points[i + 2] = 0f;

            //apply the rotation matrix
            t = x;
            x = (float) (c * x - s * y);
            y = (float) (s * t + c * y);
        }

        return points;
    }
}
