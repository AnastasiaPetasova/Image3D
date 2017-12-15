package com.anastasia.app.image3d.algo.triangulation;

import com.anastasia.app.image3d.algo.Point3D;
import com.anastasia.app.image3d.algo.Triangle;

public class Rectangle {

    Point3D[] points;

    Rectangle(Point3D... points) {
        this.points = points;
    }

    Triangle[] triangulation() {
        return new Triangle[] {
            new Triangle(points[0], points[1], points[3]),
            new Triangle(points[1], points[2], points[3])
        };
    }
}
