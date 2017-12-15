package com.anastasia.app.image3d.algo.transform;

import com.anastasia.app.image3d.algo.Triangle;

import java.util.Arrays;

public class Transforms {

    /**
     * Перестановка y и z (так как мы хотим смотреть не сверху, а сбоку)
     * + Поворот на один градус (чтобы мы видели и внутренность тоже)
     */
    public static TrianglesTransform ACSONOMETRIC_TRANSFORM = TrianglePointsTransform.withChain(
        AffineTransforms.reorder(AffineTransforms.X, AffineTransforms.Z, AffineTransforms.Y),
        AffineTransforms.rotate(-Math.PI / 180, AffineTransforms.Y_AXIS + AffineTransforms.Z_AXIS)
    );

    public static TrianglesTransform SORT_BY_DEPTH = value -> {
        Arrays.sort(value, (a, b) -> {
            double aZSum = 0, bZSum = 0;
            for (double z : a.zPoints()) aZSum += z;
            for (double z : b.zPoints()) bZSum += z;

            return Double.compare(aZSum, bZSum);
        });

        return value;
    };
}
