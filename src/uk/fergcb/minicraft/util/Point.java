package uk.fergcb.minicraft.util;

public class Point {

    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double distSquared(Point b) {
        return Math.pow(x - b.x, 2) + Math.pow(y - b.y, 2);
    }

    public double dist(Point b) {
        return Math.sqrt(distSquared(b));
    }

}
