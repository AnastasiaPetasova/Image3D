package com.anastasia.app.image3d.algo.triangulation;

import com.anastasia.app.image3d.algo.Point3D;

public interface Figures {

    Figure3D KINDER_SURPRISE = new Figure3D() {
        @Override
        public PointGenerator generator() {
            return (radius, alpha, beta) -> {
                double x = radius * Math.sin(alpha) * Math.cos(beta);
                double y = radius * Math.sin(alpha) * Math.sin(beta);

                double z = radius * Math.cos(alpha);
                if (z > 0) z += radius;

                return new Point3D(x, y, z);
            };
        }

        @Override
        public TriangulationFilter filter() {
            return null;
        }
    };
}
