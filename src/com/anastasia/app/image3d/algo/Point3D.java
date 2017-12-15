package com.anastasia.app.image3d.algo;

public class Point3D {

    public double x, y, z;

    public Point3D(double... coords) {
        this.x = coords[0];
        this.y = coords[1];
        this.z = coords[2];
    }
}
