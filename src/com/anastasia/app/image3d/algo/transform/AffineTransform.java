package com.anastasia.app.image3d.algo.transform;

import com.anastasia.app.image3d.algo.Point3D;

public class AffineTransform implements PointTransform {

    private double[][] matrix;

    AffineTransform(double[][] matrix) {
        this.matrix = matrix;
    }

    @Override
    public Point3D transform(Point3D value) {
        double[] vector = { value.x, value.y, value.z, 1 };

        double[] res = new double[vector.length];

        for (int i = 0; i < res.length; ++i) {
            for (int j = 0; j < vector.length; ++j) {
                res[i] += matrix[i][j] * vector[j];
            }
        }

        return new Point3D(res);
    }

    AffineTransform chainAfter(AffineTransform other) {
        double[][] resMatrix = new double[4][4];

        double[][] thisMatrix = this.matrix;
        double[][] otherMatrix = other.matrix;

        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 4; ++j) {
                for (int k = 0; k < 4; ++k) {
                    resMatrix[i][j] += thisMatrix[i][k] * otherMatrix[k][j];
                }
            }
        }

        return new AffineTransform(resMatrix);
    }
}
