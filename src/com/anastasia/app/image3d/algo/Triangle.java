package com.anastasia.app.image3d.algo;

public class Triangle {

    public Point3D[] points;
    double[] xPoints, yPoints, zPoints;

    public Triangle(Point3D... points) {
        this.points = points;
    }

    public double[] xPoints() {
        if (xPoints == null) {
            xPoints = new double[3];
            for (int i = 0; i < 3; ++i) {
                xPoints[i] = points[i].x;
            }
        }

        return xPoints;
    }

    public double[] yPoints() {
        if (yPoints == null) {
            yPoints = new double[3];
            for (int i = 0; i < 3; ++i) {
                yPoints[i] = points[i].y;
            }
        }

        return yPoints;
    }

    public double[] zPoints() {
        if (zPoints == null) {
            zPoints = new double[3];
            for (int i = 0; i < 3; ++i) {
                zPoints[i] = points[i].z;
            }
        }

        return zPoints;
    }
}
