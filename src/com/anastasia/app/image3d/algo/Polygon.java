package com.anastasia.app.image3d.algo;

public class Polygon {

    public Point3D[] points;
    private double[] xPoints, yPoints, zPoints;

    public Polygon(Point3D... points) {
        this.points = points;
    }

    public double[] xPoints() {
        if (xPoints == null) {
            xPoints = new double[size()];
            for (int i = 0; i < xPoints.length; ++i) {
                xPoints[i] = points[i].x;
            }
        }

        return xPoints;
    }

    public double[] yPoints() {
        if (yPoints == null) {
            yPoints = new double[size()];
            for (int i = 0; i < yPoints.length; ++i) {
                yPoints[i] = points[i].y;
            }
        }

        return yPoints;
    }

    public double[] zPoints() {
        if (zPoints == null) {
            zPoints = new double[size()];
            for (int i = 0; i < zPoints.length; ++i) {
                zPoints[i] = points[i].z;
            }
        }

        return zPoints;
    }

    public int size() {
        return points.length;
    }
}
