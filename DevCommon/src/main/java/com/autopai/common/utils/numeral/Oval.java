package com.autopai.common.utils.numeral;

public class Oval {
    private float mA;
    private float mB;

    public Oval(float a, float b) {
        mA = a;
        mB = b;
    }

    public Oval(Point_N p1, Point_N p2) {
        constructByPoint(p1, p2);
    }

    void constructByPoint(Point_N p1, Point_N p2) {
        double squareX1 = Math.pow(p1.x, 2);
        double squareX2 = Math.pow(p2.x, 2);
        double squareY1 = Math.pow(p1.y, 2);
        double squareY2 = Math.pow(p2.y, 2);

        double squareB = (squareX2 * squareY1 - squareX1 * squareY2) / (squareX2 - squareX1);
        double squareA = (squareX2 * squareY1 - squareX1 * squareY2) / (squareY1 - squareY2);

        mA = (float) Math.sqrt(squareA);
        mB = (float) Math.sqrt(squareB);
    }

    public float getY(float x) {
        double squareY = (1 - Math.pow(x, 2) / Math.pow(mA, 2)) * Math.pow(mB, 2);
        return (float)Math.sqrt(squareY);
    }

    public float getX(float y) {
        double squareX = (1 - Math.pow(y, 2) / Math.pow(mB, 2)) * Math.pow(mA, 2);
        return (float)Math.sqrt(squareX);
    }

    public float getA() {
        return mA;
    }

    public float getB() {
        return mB;
    }
}
