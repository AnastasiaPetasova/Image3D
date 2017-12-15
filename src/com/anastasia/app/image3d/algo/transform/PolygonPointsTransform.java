package com.anastasia.app.image3d.algo.transform;

import com.anastasia.app.image3d.algo.Point3D;
import com.anastasia.app.image3d.algo.Polygon;

public abstract class PolygonPointsTransform implements PolygonsTransform {

    static PolygonPointsTransform withChain(AffineTransform... affineTransforms) {
        AffineTransform transform = AffineTransforms.chain(affineTransforms);
        return with(transform);
    }

    public static PolygonPointsTransform with(PointTransform pointTransform) {
        return new PolygonPointsTransform() {
            @Override
            protected PointTransform collectTransformInfo(Polygon[] polygons) {
                return pointTransform;
            }
        };
    }

    protected abstract PointTransform collectTransformInfo(Polygon[] polygons);

    @Override
    public Polygon[] transform(Polygon[] polygons) {
        PointTransform pointTransform = collectTransformInfo(polygons);

        Polygon[] res = new Polygon[polygons.length];

        for (int i = 0; i < polygons.length; ++i) {
            Polygon polygon = polygons[i];

            Point3D[] resPoints = new Point3D[polygon.size()];
            for (int j = 0; j < resPoints.length; ++j) {
                resPoints[j] = pointTransform.transform(polygon.points[j]);
            }

            res[i] = new Polygon(resPoints);
        }

        return res;
    }
}
