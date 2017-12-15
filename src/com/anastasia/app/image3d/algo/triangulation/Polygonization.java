package com.anastasia.app.image3d.algo.triangulation;

import com.anastasia.app.image3d.algo.Point3D;
import com.anastasia.app.image3d.algo.Polygon;

import java.util.*;

public class Polygonization {

    public static Polygon[] generate(double radius, int nAlpha, int nBeta, Figure3D figure) {

        List<Polygon> polygons = new ArrayList<>();

        double dAlpha = 2 * Math.PI / Math.max(nAlpha + 1, 1);
        double dBeta = Math.PI / Math.max(nBeta + 1, 1);

        Figure3D.PointGenerator generator = figure.generator();
        Figure3D.TriangulationFilter filter = figure.filter();

        for (double alpha = 0; alpha < 2 * Math.PI; alpha += dAlpha) {
            double nextAlpha = alpha + dAlpha;
            for (double beta = 0; beta < Math.PI; beta += dBeta) {
                double nextBeta = beta + dBeta;

                Point3D a = generator.generate(radius, alpha, beta);
                Point3D b = generator.generate(radius, alpha, nextBeta);
                Point3D c = generator.generate(radius, nextAlpha, nextBeta);
                Point3D d = generator.generate(radius, nextAlpha, beta);

                Polygon polygon = new Polygon(a, b, c, d);
                if (filter == null || filter.accept(polygon)) {
                    polygons.add(polygon);
                }
            }
        }

        return polygons.toArray(new Polygon[0]);
    }
}
