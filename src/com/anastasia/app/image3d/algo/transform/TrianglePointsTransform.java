package com.anastasia.app.image3d.algo.transform;

import com.anastasia.app.image3d.algo.Point3D;
import com.anastasia.app.image3d.algo.Triangle;

public abstract class TrianglePointsTransform implements TrianglesTransform {

    public static TrianglePointsTransform withChain(AffineTransform... affineTransforms) {
        AffineTransform transform = AffineTransforms.chain(affineTransforms);
        return with(transform);
    }

    public static TrianglePointsTransform with(PointTransform pointTransform) {
        return new TrianglePointsTransform() {
            @Override
            protected PointTransform collectTransformInfo(Triangle[] triangles) {
                return pointTransform;
            }
        };
    }

    protected abstract PointTransform collectTransformInfo(Triangle[] triangles);

    @Override
    public Triangle[] transform(Triangle[] triangles) {
        PointTransform pointTransform = collectTransformInfo(triangles);

        Triangle[] res = new Triangle[triangles.length];

        for (int i = 0; i < triangles.length; ++i) {
            Triangle triangle = triangles[i];

            Point3D[] resPoints = new Point3D[3];
            for (int j = 0; j < 3; ++j) {
                resPoints[j] = pointTransform.transform(triangle.points[j]);
            }

            res[i] = new Triangle(resPoints);
        }

        return res;
    }
}
