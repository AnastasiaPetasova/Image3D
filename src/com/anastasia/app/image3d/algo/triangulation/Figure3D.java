package com.anastasia.app.image3d.algo.triangulation;

import com.anastasia.app.image3d.algo.Point3D;
import com.anastasia.app.image3d.algo.Triangle;

public interface Figure3D {

    interface PointGenerator {

        Point3D generate(double radius, double alpha, double beta);
    }

    interface TriangulationFilter {

        boolean accept(Triangle triangle);
    }

    PointGenerator generator();
    TriangulationFilter filter();
}
