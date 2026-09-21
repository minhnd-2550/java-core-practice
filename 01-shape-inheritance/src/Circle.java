public class Circle extends Shape {
    public Circle(double radius) {
        // A circle's width and height are both equal to its diameter.
        super(2 * radius, 2 * radius);
    }

    public double getRadius() {
        return getWidth() / 2;
    }

    public double getArea() {
        double radius = getRadius();
        return Math.PI * radius * radius;
    }

    public double getCircumference() {
        return Math.PI * getWidth();
    }

    @Override
    public String toString() {
        return "Circle{radius=" + getRadius()
                + ", width=" + getWidth() + ", height=" + getHeight() + "}";
    }
}
