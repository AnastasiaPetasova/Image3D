package com.anastasia.app.image3d.algo.triangulation;

import com.anastasia.app.image3d.algo.Point3D;
import com.anastasia.app.image3d.algo.Polygon;

public interface Figure3D {

    interface PointGenerator {

        Point3D generate(double radius, double alpha, double beta);
    }

    interface TriangulationFilter {

        boolean accept(Polygon polygon);
    }

    PointGenerator generator();
    TriangulationFilter filter();
}
