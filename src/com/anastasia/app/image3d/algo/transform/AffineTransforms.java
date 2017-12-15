package com.anastasia.app.image3d.algo.transform;

public class AffineTransforms {

    public final static int X = 0, Y = 1, Z = 2, ONE = 3;
    public final static int X_AXIS = (1 << X), Y_AXIS = (1 << Y), Z_AXIS = (1 << Z);

    public static double[][] createMatrix() {
        double[][] matrix = new double[4][4];
        matrix[ONE][ONE] = 1;
        return matrix;
    }

    public static AffineTransform equal() {
        return reorder(X, Y, Z);
    }

    public static AffineTransform chain(AffineTransform... transforms) {
        AffineTransform result = equal();
        for (AffineTransform transform : transforms) {
            result = transform.chainAfter(result);
        }

        return result;
    }

    public static AffineTransform rotate(double angle, int type) {
        double[][] matrix = createMatrix();
        for (int i = 0; i < 4; ++i) {
            matrix[i][i] = 1;
        }

        int minBit = 4, maxBit = 0;
        switch (type) {
            case X_AXIS + Y_AXIS:
                minBit = X;
                maxBit = Y;
                break;
            case X_AXIS + Z_AXIS:
                minBit = X;
                maxBit = Z;
                break;
            case Y_AXIS + Z_AXIS:
                minBit = Y;
                maxBit = Z;
                break;
            default:
                return null;
        }

        matrix[minBit][minBit] = Math.cos(angle);
        matrix[minBit][maxBit] = Math.sin(angle);

        matrix[maxBit][minBit] = Math.sin(-angle);
        matrix[maxBit][maxBit] = Math.cos(angle);

        return new AffineTransform(matrix);
    }

    public static AffineTransform reorder(int newX, int newY, int newZ) {
        double[][] matrix = createMatrix();

        matrix[X][newX] = 1;
        matrix[Y][newY] = 1;
        matrix[Z][newZ] = 1;

        return new AffineTransform(matrix);
    }

    public static AffineTransform shift(double... deltas) {
        double[][] matrix = createMatrix();

        for (int i = 0; i < 3; ++i) {
            matrix[i][i] = 1;
            matrix[i][ONE] = deltas[i];
        }

        return new AffineTransform(matrix);
    }

    public static AffineTransform zoom(double... coeffs) {
        double[][] matrix = createMatrix();

        for (int i = 0; i < 3; ++i) {
            matrix[i][i] = coeffs[i];
        }

        return new AffineTransform(matrix);
    }
}
