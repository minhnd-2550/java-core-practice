public class Shape {
    private final double width;
    private final double height;

    public Shape(double width, double height) {
        if (!Double.isFinite(width) || !Double.isFinite(height)
                || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be finite and greater than 0.");
        }
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Shape{width=" + width + ", height=" + height + "}";
    }
}
